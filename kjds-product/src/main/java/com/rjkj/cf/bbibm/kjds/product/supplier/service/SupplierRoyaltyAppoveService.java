package com.rjkj.cf.bbibm.kjds.product.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.SupplierRoyaltyAppove;

import java.math.BigDecimal;


/**
 *@描述：供应商提成信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-11 11:39:06
 **/
public interface SupplierRoyaltyAppoveService extends IService<SupplierRoyaltyAppove> {


    /**
     * 根据供应商id查询全部提成信息
     * @param userId
     * @return
     */
    public BigDecimal queryAllSupplierCount(String userId);



    /**
     * 根据供应商id查询全部今日提成信息
     * @param userId
     * @return
     */
    public BigDecimal queryTodayAllSupplierCount(String userId);

}
