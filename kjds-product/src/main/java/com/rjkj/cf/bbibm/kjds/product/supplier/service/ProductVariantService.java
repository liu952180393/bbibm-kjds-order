package com.rjkj.cf.bbibm.kjds.product.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariant;

import java.util.List;

/**
 *@描述：商品变体信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 16:09:42
 **/
public interface ProductVariantService extends IService<ProductVariant> {

    /**
     * 根据父sku查询变体信息
     * @param parentSku
     * @return
     */
    public List<ProductVariant> queryByParentSku(String parentSku);

    /**
     * 根据变体SKU查询变体
     */
    ProductVariant selectBySku(String sku);
}
