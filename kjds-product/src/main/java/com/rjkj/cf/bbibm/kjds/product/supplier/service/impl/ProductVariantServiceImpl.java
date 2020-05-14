package com.rjkj.cf.bbibm.kjds.product.supplier.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.product.supplier.mapper.ProductVariantMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductVariantService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *@描述：商品变体信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 16:09:42
 **/
@Service
@AllArgsConstructor
public class ProductVariantServiceImpl extends ServiceImpl<ProductVariantMapper, ProductVariant> implements ProductVariantService {

    private final ProductVariantMapper productVariantMapper;

    @Override
    public List<ProductVariant> queryByParentSku(String parentSku) {
        return productVariantMapper.queryByParentSku(parentSku);
    }

    @Override
    public ProductVariant selectBySku(String sku) {
        return this.baseMapper.selectOne(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getSku,sku));
    }
}
