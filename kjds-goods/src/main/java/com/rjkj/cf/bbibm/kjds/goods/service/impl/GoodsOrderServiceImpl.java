package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsOrder;
import com.rjkj.cf.bbibm.kjds.goods.mapper.GoodsOrderMapper;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.CommissionDetailsVo;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsOrderService;
import com.rjkj.cf.common.security.service.RjkjUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * @描述：店铺申请订单信息
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-17 15:35:49
 **/
@Service
@AllArgsConstructor
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderMapper, GoodsOrder> implements GoodsOrderService {
    private final GoodsOrderMapper goodsOrderMapper;

    @Override
    public ArrayList<CommissionDetailsVo> getCommissionDetails(Page page, RjkjUser user, String type) {
        String id = user.getId();
        IPage iPage = goodsOrderMapper.selectPage(page, Wrappers.<GoodsOrder>query().lambda().eq(GoodsOrder::getOrderType, type)
                .eq(GoodsOrder::getRecvUid, id)
                .eq(GoodsOrder::getStatus, "2"));
        List<GoodsOrder> records = iPage.getRecords();
        ArrayList<CommissionDetailsVo> commissionDetailsVos = new ArrayList<>();
        for (GoodsOrder record : records) {
            CommissionDetailsVo commissionDetailsVo = new CommissionDetailsVo();
            commissionDetailsVo.setAmount(record.getAmount());
            commissionDetailsVo.setCtime(record.getCtime());
            commissionDetailsVo.setUserName(goodsOrderMapper.getUserName(record.getPayUid()));
            commissionDetailsVos.add(commissionDetailsVo);
        }
        return commissionDetailsVos;
//        goodsOrderMapper.getCommissionDetails(page,user.getId(),type);
//        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public int updateByOrderId(String orderNo) {
        GoodsOrder goodBean = goodsOrderMapper.selectById(orderNo);
        //设置订单状态为2已付款
        goodBean.setStatus("2");
        return goodsOrderMapper.updateById(goodBean);
    }
}
