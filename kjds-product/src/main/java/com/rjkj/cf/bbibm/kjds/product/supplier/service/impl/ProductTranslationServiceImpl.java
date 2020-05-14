package com.rjkj.cf.bbibm.kjds.product.supplier.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.Product;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariantsTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.mapper.ProductMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.mapper.ProductTranslationMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.mapper.ProductVariantMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductTranslationService;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductVariantsTranslationService;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @描述：商品翻译后的信息
 * @项目：
 * @公司：软江科技
 * @作者：Yihao
 * @时间：2020-01-07 11:48:25
 **/
@Service
@AllArgsConstructor
public class ProductTranslationServiceImpl extends ServiceImpl<ProductTranslationMapper, ProductTranslation> implements ProductTranslationService {
    private final ProductMapper productMapper;
    private final ProductVariantMapper productVariantMapper;
    private final ProductVariantsTranslationService productVariantsTranslationService;
    private final ProductTranslationMapper productTranslationMapper;


    @Async
    @Override
    public void translationProductInfo(Authentication aToken, String productId) {
        try {
            Product productInfo = productMapper.selectById(productId);
            if (productInfo == null) {
                throw new RuntimeException("该商品信息不存在");
            }
            //预定义需要翻译的字段
            StringBuilder field = new StringBuilder();
            field.append("productTitle")
                    .append(",")
                    .append("keyWord")
                    .append(",")
                    .append("productHighlights")
                    .append(",")
                    .append("description");
//                    .append(",")
//                    .append("brandName")
            //获取需要存入的翻译的国家列表信息，
            String areaData = (String) KjdsUtils.getDictValueByType(aToken, "country_abbreviation").getData();
            String[] split = areaData.split(",");
            productTranslationMapper.delete(Wrappers.<ProductTranslation>query().lambda().eq(ProductTranslation::getProductId, productId));
            productVariantsTranslationService.remove(Wrappers.<ProductVariantsTranslation>query().lambda().eq(ProductVariantsTranslation::getParentSku, productInfo.getSku()));
            for (String area : split) {
                com.rjkj.cf.bbibm.kjds.api.entity.Product product1 = new com.rjkj.cf.bbibm.kjds.api.entity.Product();
                BeanUtils.copyProperties(product1, productInfo);
                String productHighlights = product1.getProductHighlights();
                String highlights = productHighlights.replaceAll("_msg_", "123msg123");
                product1.setProductHighlights(highlights);
                com.rjkj.cf.bbibm.kjds.api.entity.Product translationProductInfo = KjdsUtils.translationProudcts(aToken, product1, field.toString(), area);
                //保存翻译后的商品信息
                ProductTranslation productTranslation = new ProductTranslation();
                productTranslation.setId(IDUtils.getGUUID(""));
                productTranslation.setProductId(productId);
                productTranslation.setProductSku(translationProductInfo.getSku());
                productTranslation.setProductTitle(translationProductInfo.getProductTitle());
                productTranslation.setDescription(translationProductInfo.getDescription());
//                if(StringUtils.isBlank(translationProductInfo.getDescription())){
//                    productTranslation.setDescription("This is the default description, welcome to buy, thank you");
//                }
                productTranslation.setKeyword(translationProductInfo.getKeyWord());
                productTranslation.setHighlights(translationProductInfo.getProductHighlights());
                productTranslation.setBrand(translationProductInfo.getBrandName());
                productTranslation.setArea(area);
                productTranslation.setCreateTime(LocalDateTime.now());
                productTranslationMapper.insert(productTranslation);

                //保存翻译后的变体信息
                List<ProductVariant> listVariant = productVariantMapper.selectList(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getParentSku, productInfo.getSku()));
                if(listVariant.size()>0){
                    for (ProductVariant productVariant : listVariant) {
                        ProductVariantsTranslation productVariantsTranslation = new ProductVariantsTranslation();
                        productVariantsTranslation.setId(IDUtils.getGUUID(""));
                        productVariantsTranslation.setParentSku(productInfo.getSku());
                        if (StringUtils.isNotEmpty(productVariant.getVariantColor())) {
                            String stringColor = (String) KjdsUtils.transcationSomeInfo(aToken, productVariant.getVariantColor(), "ZH", area).getData();
                            productVariantsTranslation.setVariantColor(stringColor);
                        }
                        if (StringUtils.isNotEmpty(productVariant.getVariantSize())) {
                            String stringSize = (String) KjdsUtils.transcationSomeInfo(aToken, productVariant.getVariantSize(), "ZH", area).getData();
                            productVariantsTranslation.setVariantSize(stringSize);
                        }
                        productVariantsTranslation.setArea(area);
                        productVariantsTranslation.setSku(productVariant.getSku());
                        productVariantsTranslation.setCreateTime(LocalDateTime.now());
                        productVariantsTranslationService.save(productVariantsTranslation);
                    }
                }
            }
            productInfo.setTranslated("1");
            productInfo.setId(productId);
            productMapper.updateById(productInfo);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductVariantsTranslation getVariantTranInfo(String sku, String area) {
        List<ProductVariantsTranslation> list = productVariantsTranslationService.list(Wrappers.<ProductVariantsTranslation>query().lambda()
                .eq(ProductVariantsTranslation::getSku, sku)
                .eq(ProductVariantsTranslation::getArea, area));
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
