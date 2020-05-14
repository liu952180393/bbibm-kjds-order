package com.rjkj.cf.bbibm.kjds.goods.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsOrder;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.CommissionDetailsVo;
import com.rjkj.cf.common.security.service.RjkjUser;

import java.util.ArrayList;

/**
 *@描述：店铺申请订单信息
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-17 15:35:49
 **/
public interface GoodsOrderService extends IService<GoodsOrder> {

    ArrayList<CommissionDetailsVo> getCommissionDetails(Page page, RjkjUser user, String type);


    int updateByOrderId(String orderNo);
}
