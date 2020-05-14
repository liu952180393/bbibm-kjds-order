package com.rjkj.cf.bbibm.kjds.product.supplier.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.SupplierRoyaltyAppove;
import com.rjkj.cf.bbibm.kjds.product.supplier.mapper.SupplierRoyaltyAppoveMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.SupplierRoyaltyAppoveService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 *@描述：供应商提成信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-11 11:39:06
 **/
@Service
@AllArgsConstructor
public class SupplierRoyaltyAppoveServiceImpl extends ServiceImpl<SupplierRoyaltyAppoveMapper, SupplierRoyaltyAppove> implements SupplierRoyaltyAppoveService {



    @Override
    public BigDecimal queryAllSupplierCount(String userId) {
        return this.baseMapper.queryAllSupplierCount(userId) ;
    }

    @Override
    public BigDecimal queryTodayAllSupplierCount(String userId) {
        return this.baseMapper.queryTodayAllSupplierCount(userId);
    }
}
