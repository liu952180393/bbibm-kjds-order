package com.rjkj.cf.bbibm.kjds.product.supplier.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariantsTranslation;
import org.springframework.security.core.Authentication;

/**
 *@描述：商品翻译后的信息
 *@项目：
 *@公司：软江科技
 *@作者：Yihao
 *@时间：2020-01-07 11:48:25
 **/
public interface ProductTranslationService extends IService<ProductTranslation> {

    /**
     * 异步翻译商品信息
     * @param aToken
     * @param productId
     */
    void translationProductInfo(Authentication aToken, String productId);

    /**
     * 根据sku和区域获取翻译后的变体信息
     * @param sku
     * @param area
     * @return
     */
    ProductVariantsTranslation getVariantTranInfo(String sku,String area);
}
