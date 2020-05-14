package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.rjkj.cf.bbibm.kjds.goods.entity.Partner;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerAppRsp;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerPrice;
import com.rjkj.cf.bbibm.kjds.goods.mapper.PartnerMapper;
import com.rjkj.cf.bbibm.kjds.goods.mapper.PartnerPriceMapper;
import com.rjkj.cf.bbibm.kjds.goods.service.PartnerPriceService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *@描述：城市合伙人价格表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 13:15:07
 **/
@Service
@AllArgsConstructor
public class PartnerPriceServiceImpl extends ServiceImpl<PartnerPriceMapper, PartnerPrice> implements PartnerPriceService {

    private final PartnerPriceMapper  partnerPriceMapper;
    private final PartnerMapper partnerMapper;

    @Override
    public List<PartnerAppRsp> listAll() {
//        return partnerPriceMapper.listAll();
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void updateByPartnerState(String orderNo) {
        Partner partnerBean = partnerMapper.selectOne(Wrappers.<Partner>query().lambda().eq(Partner::getOrderNo, orderNo));
        partnerBean.setOptionStatus("0");
        partnerMapper.updateById(partnerBean);
    }
}
