package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.api.entity.PayGoodsOrder;
import com.rjkj.cf.bbibm.kjds.api.feign.RemotePayFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsOrder;
import com.rjkj.cf.bbibm.kjds.goods.entity.Partner;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerApplyReq;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerPrice;
import com.rjkj.cf.bbibm.kjds.goods.mapper.PartnerMapper;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsOrderService;
import com.rjkj.cf.bbibm.kjds.goods.service.PartnerPriceService;
import com.rjkj.cf.bbibm.kjds.goods.service.PartnerService;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.sequence.sequence.Sequence;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


/**
 *@描述：合伙人审核表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 11:58:47
 **/
@Service
@AllArgsConstructor
public class PartnerServiceImpl extends ServiceImpl<PartnerMapper, Partner> implements PartnerService {

    private final Sequence  paySequence;
    private final PartnerPriceService partnerPriceService;
    private final GoodsOrderService  goodsOrderService;
    private final RemotePayFeignService remotePayFeignService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String apply(PartnerApplyReq req, RjkjUser user) {
        if(StringUtils.isEmpty(req.getPartnerPriceId())){
            throw new RuntimeException("城市合伙人价格不能为空");
        }
        if(StringUtils.isEmpty(req.getName())){
            throw new RuntimeException("名字不能为空");
        }
        if(StringUtils.isEmpty(req.getEmail())){
            throw new RuntimeException("邮箱不能为空");
        }
        if(StringUtils.isEmpty(req.getRemake())){
            throw new RuntimeException("申请说明不能为空");
        }
        Optional.ofNullable(req.getPayType()).orElseThrow(()->new RuntimeException("支付类型不能为空"));

        String orderNo = paySequence.nextNo();

        Partner  partner=new Partner();
        partner.setId(IDUtils.getGUUID(UUID.randomUUID().toString()));
        partner.setArea(req.getArea());
        partner.setCity(req.getCity());
        partner.setCounty(req.getCounty());
        partner.setCtime(LocalDateTime.now());
        partner.setCuid(user.getId());
        partner.setEmail(req.getEmail());
        partner.setName(req.getName());
        partner.setOrderNo(orderNo);
        partner.setProvince(req.getProvince());
        partner.setPartnerPriceId(req.getPartnerPriceId());
        partner.setUtime(LocalDateTime.now());
        partner.setUuid(user.getId());
        partner.setRemake(req.getRemake());
        partner.setOptionStatus("3");
        this.baseMapper.insert(partner);
        PartnerPrice byId = partnerPriceService.getById(req.getPartnerPriceId());
        GoodsOrder  goodsOrder=new GoodsOrder();
        goodsOrder.setOid(orderNo);
        goodsOrder.setPayUid(user.getId());
        goodsOrder.setUtime(LocalDateTime.now());
        goodsOrder.setAmount(byId.getPrice().abs());
        goodsOrder.setAmountType(String.valueOf(req.getPayType()));
        goodsOrder.setBuyerRate(0);
        goodsOrder.setCtime(LocalDateTime.now());
        goodsOrder.setOrderType("3");
        goodsOrder.setStatus("1");
        this.goodsOrderService.save(goodsOrder);
/*        PartnerApplyRsp  rsp=new PartnerApplyRsp();
        rsp.setAppid("");
        rsp.setOid(goodsOrder.getOid());
        rsp.setPayAmount(byId.getPrice().setScale(2,BigDecimal.ROUND_HALF_UP).toPlainString());
        rsp.setPayInfo("");*/




        PayGoodsOrder payGoodsOrder=new PayGoodsOrder();
        payGoodsOrder.setPayOrderId(goodsOrder.getOid());
        payGoodsOrder.setGoodsName(user.getUsername()+"-城市合伙人申请");
        payGoodsOrder.setAmount(goodsOrder.getAmount());
        payGoodsOrder.setCallUrl("http://kjds-goods/partner/apply/pay/callback");
        remotePayFeignService.goodsAddOrder(payGoodsOrder);
        return goodsOrder.getOid();
    }
}
