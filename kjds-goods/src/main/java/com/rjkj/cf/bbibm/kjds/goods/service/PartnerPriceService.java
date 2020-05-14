package com.rjkj.cf.bbibm.kjds.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerAppRsp;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerPrice;

import java.util.List;

/**
 *@描述：城市合伙人价格表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 13:15:07
 **/
public interface PartnerPriceService extends IService<PartnerPrice> {

    /**
     * 获取城市合伙人价格列表
     * @return
     */
    List<PartnerAppRsp> listAll();

    void updateByPartnerState(String orderNo);
}
