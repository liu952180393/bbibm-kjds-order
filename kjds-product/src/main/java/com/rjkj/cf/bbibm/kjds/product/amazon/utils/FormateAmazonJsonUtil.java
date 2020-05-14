package com.rjkj.cf.bbibm.kjds.product.amazon.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.api.utils.HtmlRepliceUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;
import com.rjkj.cf.bbibm.kjds.product.appcategory.mapper.AppCategoryMapper;
import com.rjkj.cf.bbibm.kjds.product.appcategory.service.AppCategoryService;
import com.rjkj.cf.bbibm.kjds.product.appcategory.service.impl.AppCategoryServiceImpl;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.entity.Ean;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.mapper.EanMapper;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.service.EanService;
import com.rjkj.cf.bbibm.kjds.product.rate.service.RateService;
import com.sun.tracing.dtrace.ArgsAttributes;
import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 格式化app json数据为亚马逊商品上传json数据
 *
 * @author EDZ
 */
@Configuration
@AllArgsConstructor
public class FormateAmazonJsonUtil {

    private final AppCategoryMapper appCategoryMapper;
    private final EanMapper eanMapper;
    private final EanService eanService;


    /**
     * 解析app端存入的json数据为亚马逊上传数据
     *
     * @return
     */
    public JSONArray formateJonsForAmazon(List<GoodsProduct> goodsProducts, String marketPlaceId, String shopId) {


        try {
            JSONArray array = new JSONArray();

            for (int h = 0; h < goodsProducts.size(); h++) {
                GoodsProduct goodsProduct = goodsProducts.get(h);

                String productClassificationId = goodsProduct.getProductClassificationId();
                AppCategory bean = appCategoryMapper.queryById(productClassificationId);

                String typed = null;
                try {
                    typed = bean.getAmazon();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    throw new NullPointerException("商品分类异常");
                }
                //存放亚马逊json格式数据
                JSONObject obj = new JSONObject();
                //上传数据类型
                obj.put("type", typed);
                //产品标题
//                if (StringUtils.isNoneBlank(goodsProduct.getProductTitle())) {
                    obj.put("title", goodsProduct.getProductTitle());
//                } else {
//                    obj.put("title", "This is a product of good quality and low price");
//                }
                //产品描述
                int length = goodsProduct.getDescription().length();
                if (length < 1900) {
                    obj.put("description", goodsProduct.getDescription());
                } else {
                    obj.put("description", goodsProduct.getDescription().substring(0, 1900));
                }
                //产品品牌
                if (StringUtils.isNoneBlank(goodsProduct.getBrandName())) {
                    if (goodsProduct.getBrandName().contains("168pro")) {
                        obj.put("brand", "Generic");
                    } else {
                        obj.put("brand", goodsProduct.getBrandName());
                    }
                } else {
                    obj.put("brand", "Generic");
                }
                //亚马逊上传产品后的亮点
                if (StringUtils.isNoneBlank(goodsProduct.getProductHighlights())) {
                    obj.put("bulletPoint", goodsProduct.getProductHighlights());
                } else {
                    obj.put("bulletPoint", "This product is cheap and affordable");
                }
                //制造商
                if (StringUtils.isNoneBlank(goodsProduct.getManufacturerCode())) {
                    obj.put("manufacturer", goodsProduct.getManufacturerCode());
                } else {
                    obj.put("manufacturer", "number" + (int) ((Math.random() * 9 + 1) * 100000));
                }
                //关键词
                if (StringUtils.isNoneBlank(goodsProduct.getKeyWord())) {
                    obj.put("searchTerms", goodsProduct.getKeyWord());
                } else {
                    obj.put("searchTerms", "Cheapness");
                }

                //存放亚马逊父体和变体的数据
                JSONArray list = new JSONArray();

                //判断变体信息不为空时
                if (goodsProduct.getProductVariants().size() > 0) {

                    //当为双变体时
                    String variantColor = goodsProduct.getProductVariants().get(0).getVariantColor();
                    String variantSize = goodsProduct.getProductVariants().get(0).getVariantSize();
                    //如果为双变体时
                    if (StringUtils.isNoneBlank(variantColor) && StringUtils.isNoneBlank(variantSize)) {

                        //判断JSON数据中变体的详细信息是否存在
                        JSONObject varObj = new JSONObject();//定义存放商品的map对象
                        //父类
                        varObj.put("par", "parent");
                        varObj.put("sku", KjdsUtils.getUploadSkuByGoodsSku(goodsProduct.getSku(), marketPlaceId, shopId, goodsProduct.getCreateTime()));
                        Ean eanOne = eanMapper.queryByOneEanInfo();
                        try {
                            String ean = eanOne.getEan();
                            varObj.put("ean", ean);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            throw new NullPointerException("EAN为空");
                        }
                        eanOne.setType("1");
                        eanService.updateById(eanOne);
                        list.add(varObj);

                        for (int j = 0; j < goodsProduct.getProductVariants().size(); j++) {
                            ProductVariant productVariant = goodsProduct.getProductVariants().get(j);
                            varObj = new JSONObject();
                            //子类（变体）
                            varObj.put("par", "child");
                            varObj.put("sku", KjdsUtils.getUploadSkuByGoodsSku(productVariant.getSku(), marketPlaceId, shopId, goodsProduct.getCreateTime()));
                            Ean eanTwo = eanMapper.queryByOneEanInfo();
                            try {
                                String ean = eanOne.getEan();
                                varObj.put("ean", ean);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                                throw new NullPointerException("EAN为空");
                            }
                            eanTwo.setType("1");
                            eanService.updateById(eanTwo);

                            varObj.put("size", productVariant.getVariantSize());
                            varObj.put("color", productVariant.getVariantColor());
                            varObj.put("price", productVariant.getPrice());
                            varObj.put("quantity", productVariant.getStock());

                            List<String> imageList = productVariant.getImageList();
                            JSONArray imagesArray = new JSONArray();
                            imagesArray.addAll(imageList);
                            varObj.put("images", imagesArray);

                            list.add(varObj);
                        }

                        obj.put("data", list);
                        obj.put("size", "");
                        obj.put("color", "");
                    } else {
                        //判断为单变体时

                        //判断变体的类型是color还是size
                        String type = "";
                        JSONObject varObj = new JSONObject();//定义存放商品的map对象
                        //定义是否为父类（parent）
                        varObj.put("par", "parent");
                        //亚马逊sku（自己定义唯一性）
                        varObj.put("sku", KjdsUtils.getUploadSkuByGoodsSku(goodsProduct.getSku(), marketPlaceId, shopId, goodsProduct.getCreateTime()));
                        //亚马逊上传时需要的ean
                        Ean eanOne = eanMapper.queryByOneEanInfo();
                        try {
                            String ean = eanOne.getEan();
                            varObj.put("ean", ean);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                            throw new NullPointerException("EAN为空");
                        }
                        eanOne.setType("1");
                        eanService.updateById(eanOne);
                        list.add(varObj);

                        for (int i = 0; i < goodsProduct.getProductVariants().size(); i++) {
                            ProductVariant productVariant = goodsProduct.getProductVariants().get(i);
                            varObj = new JSONObject();
                            //定义是否为子类（child）
                            varObj.put("par", "child");
                            varObj.put("sku", KjdsUtils.getUploadSkuByGoodsSku(productVariant.getSku(), marketPlaceId, shopId, goodsProduct.getCreateTime()));
                            Ean eanTwo = eanMapper.queryByOneEanInfo();
                            String ean = null;
                            try {
                                ean = eanTwo.getEan();
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new NullPointerException("EAN为空");
                            }

                            varObj.put("ean", ean);
                            eanTwo.setType("1");
                            eanService.updateById(eanTwo);
                            //type值为color或者size
                            if (StringUtils.isNoneBlank(productVariant.getVariantSize())) {
                                type = "size";
                                varObj.put(type, productVariant.getVariantSize());
                            } else if (StringUtils.isNoneBlank(productVariant.getVariantColor())) {
                                type = "color";
                                varObj.put(type, productVariant.getVariantColor());
                            }

                            varObj.put("price", productVariant.getPrice());
                            //库存
                            varObj.put("quantity", productVariant.getStock());
                            //图片信息
                            List<String> imageList = productVariant.getImageList();
                            JSONArray imagesArray = new JSONArray();
                            imagesArray.addAll(imageList);
                            varObj.put("images", imagesArray);

                            list.add(varObj);
                        }

                        obj.put("data", list);
                        obj.put(type, "");

                    }

                } else {
                    //当商品不存在变体数据的时候

                    JSONObject varObj = new JSONObject();//定义存放商品的map对象
                    //定义为独立的商品
                    varObj.put("par", "single");
                    //亚马逊sku（自己定义唯一性）
                    varObj.put("sku", KjdsUtils.getUploadSkuByGoodsSku(goodsProduct.getSku(), marketPlaceId, shopId, goodsProduct.getCreateTime()));
                    //亚马逊上传时需要的ean
                    Ean eanOne = eanMapper.queryByOneEanInfo();
                    try {
                        String ean = eanOne.getEan();
                        varObj.put("ean", ean);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        throw new NullPointerException("EAN为空");
                    }
                    eanOne.setType("1");
                    eanService.updateById(eanOne);

                    //价格
                    varObj.put("price", goodsProduct.getCostUnitPrice());
                    //库存
                    varObj.put("quantity", goodsProduct.getStock());
                    //图片信息
                    List<String> imageList = goodsProduct.getImageList();
                    JSONArray imagesArray = new JSONArray();
                    imagesArray.addAll(imageList);
                    varObj.put("images", imagesArray);

                    list.add(varObj);

                    obj.put("data", list);

                }
                array.add(obj);
            }

            System.err.println(array.toString());
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }
}
