package com.rjkj.cf.bbibm.kjds.product.additem.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.bbibm.kjds.api.entity.Goods;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteTransactionFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.*;
import com.rjkj.cf.bbibm.kjds.product.additem.service.AddItemService;
import com.rjkj.cf.bbibm.kjds.product.additem.service.AddItemToMallService;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.CreateAmazonXmlUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.FormateAmazonNotEanJsonUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.SubmitFeedUtil;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariantsTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductTranslationService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.rjkj.cf.bbibm.kjds.product.amazon.utils.MallConstant.*;

/**
 * @描述：商品上传
 * @项目：跨境电商
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-11 14:55:08
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/addItem")
@Api(value = "addItem", tags = "商品上传")
public class AdditemController {
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final FormateAmazonNotEanJsonUtil formateAmazonNotEanJsonUtil;
    private final AddItemService itemService;
    private final AddItemToMallService addItemToMallService;
    private final RemoteTransactionFeignService remoteTransactionFeignService;
    private final ProductTranslationService productTranslationService;

    /**
     * 上传商品到各个平台
     *
     * @param goodsProduct 商品信息
     * @return
     */
    @ApiOperation(value = "上传商品到各个平台", notes = "上传商品到各个平台")
    @SysLog("上传商品到各个平台")
    @PostMapping("/addItemToMall")
    public void addItemToMall(@RequestBody List<GoodsProduct> goodsProduct) {
//        try {
//            if (goodsProduct.size() > 0) {
//                goodsProduct.forEach(iteam -> {
//                    //增加运费价格
//                    Double weight = iteam.getWeight();
//                    BigDecimal weightDecimal = BigDecimal.ZERO;
//                    if (weight == null) {
//                        weightDecimal = new BigDecimal(1000);
//                    } else {
//                        weightDecimal = new BigDecimal(iteam.getWeight());
//                    }
//                    BigDecimal bigDecimal = ComputedFreightPriceUtil.changePriceRule(weightDecimal, new BigDecimal(iteam.getCostUnitPrice()), iteam.getArea());
//                    if (bigDecimal != null) {
//                        iteam.setCostUnitPrice(bigDecimal.doubleValue());
//                    }
//                    //将图片设置到上传的商品属性中
//                    String image = iteam.getImage();
//                    List<SysFile> sysFile = SysFileUtils.getSysFile(image);
//                    List<String> listBig = new ArrayList<>();
//                    for (SysFile file : sysFile) {
//                        listBig.add(file.getPath());
//                    }
//                    String area = iteam.getArea();
//                    iteam.setImageList(listBig);
//                    List<ProductTranslation> productTranslationList = productTranslationService.list(Wrappers.<ProductTranslation>query().lambda()
//                            .eq(ProductTranslation::getProductId, iteam.getId())
//                            .eq(ProductTranslation::getArea, area));
//
//                    iteam.setProductHighlights(iteam.getProductHighlights().replaceAll("_msg_", "123msg123"));
//                    if (productTranslationList.size() > 0) {
//                        ProductTranslation productTranslation = productTranslationList.get(0);
//                        //如果非TW和JP地区，则增加是否有中文的判断
//                        if (!"TW".equals(area) && !"JP".equals(area)) {
//                            String productTitle = productTranslation.getProductTitle();
//                            //不为空且不包含中文字
//                            if (StringUtils.isNotBlank(productTitle) && !KjdsUtils.isContainChinese(productTitle)) {
//                                iteam.setProductTitle(productTitle);
//                                //不满足则进行实时翻译
//                            } else if (StringUtils.isBlank(productTitle) || KjdsUtils.isContainChinese(productTitle)) {
//                                for (int i = 0; i < 5; i++) {
//                                    String productTitle1 = null;
//                                    try {
//                                        productTitle1 = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductTitle(), "ZH", area).getData();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (StringUtils.isNotBlank(productTitle1)) {
//                                        if (!KjdsUtils.isContainChinese(productTitle1)) {
//                                            iteam.setProductTitle(productTitle1);
//                                            productTranslation.setProductTitle(productTitle1);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                            String highlights = productTranslation.getHighlights();
//                            if (StringUtils.isNotBlank(highlights) && !KjdsUtils.isContainChinese(highlights)) {
//                                iteam.setProductHighlights(highlights);
//                            } else if (StringUtils.isBlank(highlights) || KjdsUtils.isContainChinese(highlights)) {
//                                for (int i = 0; i < 5; i++) {
//                                    String getProductHighlights = null;
//                                    try {
//                                        getProductHighlights = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductHighlights(), "ZH", area).getData();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (StringUtils.isNotBlank(getProductHighlights)) {
//                                        if (!KjdsUtils.isContainChinese(getProductHighlights)) {
//                                            iteam.setProductHighlights(getProductHighlights);
//                                            productTranslation.setHighlights(getProductHighlights);
//                                            break;
//                                        }
//                                    }
//                                }
//                            }
//                            String keyword = productTranslation.getKeyword();
//                            if (StringUtils.isNotBlank(keyword) && !KjdsUtils.isContainChinese(keyword)) {
//                                iteam.setKeyWord(keyword);
//                            } else if (StringUtils.isBlank(keyword) || KjdsUtils.isContainChinese(keyword)) {
//                                for (int i = 0; i < 5; i++) {
//                                    String getKeyWord = null;
//                                    try {
//                                        getKeyWord = remoteTransactionFeignService.transcationSomeInfo(iteam.getKeyWord(), "ZH", area).getData();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (StringUtils.isNotBlank(getKeyWord) && !KjdsUtils.isContainChinese(getKeyWord)) {
//                                        iteam.setKeyWord(getKeyWord);
//                                        productTranslation.setKeyword(getKeyWord);
//                                        break;
//                                    }
//                                }
//
//                            }
//                            String description = productTranslation.getDescription();
//                            if (StringUtils.isNotBlank(description) && !KjdsUtils.isContainChinese(description)) {
//                                iteam.setDescription(description);
//                            } else {
//                                for (int i = 0; i < 5; i++) {
//                                    String getDescription = null;
//                                    try {
//                                        getDescription = remoteTransactionFeignService.transcationSomeInfo(iteam.getDescription(), "ZH", area).getData();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                    if (StringUtils.isNotBlank(getDescription) && !KjdsUtils.isContainChinese(getDescription)) {
//                                        iteam.setDescription(getDescription);
//                                        productTranslation.setDescription(getDescription);
//                                        break;
//                                    }
//                                }
//
//                            }
//                            //对翻译后的新数据存入翻译表，避免下次重复翻译
//                            productTranslationService.updateById(productTranslation);
//                        } else {
//                            //JP和TW地区处理
//                            List list = translateProductInfo(iteam, productTranslation);
//                            //翻译后的商品信息
//                            GoodsProduct goodsProduct1 = (GoodsProduct) list.get(0);
//                            ProductTranslation productTranslation1 = (ProductTranslation) list.get(1);
//                            iteam.setProductTitle(goodsProduct1.getProductTitle());
//                            iteam.setProductHighlights(goodsProduct1.getProductHighlights());
//                            iteam.setKeyWord(goodsProduct1.getKeyWord());
//                            iteam.setDescription(goodsProduct1.getDescription());
//                            productTranslationService.updateById(productTranslation1);
//                        }
//                    } else {
//                        //翻译表中如不存在翻译信息
//                        ProductTranslation productTranslation = new ProductTranslation();
//                        productTranslation.setId(IDUtils.getGUUID(""));
//                        productTranslation.setProductId(iteam.getId());
//                        productTranslation.setProductSku(iteam.getSku());
//                        productTranslation.setArea(area);
//                        productTranslation.setCreateTime(LocalDateTime.now());
//                        List list = translateProductInfo(iteam, productTranslation);
//                        GoodsProduct goodsProduct1 = (GoodsProduct) list.get(0);
//                        ProductTranslation productTranslation1 = (ProductTranslation) list.get(1);
//                        iteam.setProductTitle(goodsProduct1.getProductTitle());
//                        iteam.setDescription(goodsProduct1.getDescription());
//                        iteam.setProductHighlights(goodsProduct1.getProductHighlights());
//                        iteam.setKeyWord(goodsProduct1.getKeyWord());
//                        productTranslationService.save(productTranslation1);
//                    }
//
//                    //变体信息翻译
//                    for (ProductVariant productVariant : iteam.getProductVariants()) {
//                        ProductVariantsTranslation variantTranInfo = productTranslationService.getVariantTranInfo(productVariant.getSku(), area);
//                        if (StringUtils.isNotBlank(productVariant.getVariantColor())) {
//                            if (variantTranInfo != null) {
//                                productVariant.setVariantColor(variantTranInfo.getVariantColor());
//                            } else {
//                                String stringColor = null;
//                                try {
//                                    stringColor = remoteTransactionFeignService.transcationSomeInfo(productVariant.getVariantColor(), "ZH", area).getData();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                if (StringUtils.isNotBlank(stringColor)) {
//                                    productVariant.setVariantColor(stringColor);
//                                }
//                            }
//                        }
//
//                        if (StringUtils.isNotBlank(productVariant.getVariantSize())) {
//                            if (variantTranInfo != null) {
//                                productVariant.setVariantColor(variantTranInfo.getVariantColor());
//                            } else {
//                                String stringSize = null;
//                                try {
//                                    stringSize = remoteTransactionFeignService.transcationSomeInfo(productVariant.getVariantSize(), "ZH", area).getData();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                if (StringUtils.isNotBlank(stringSize)) {
//                                    productVariant.setVariantSize(stringSize);
//                                }
//                            }
//                        }
//                        //增加运费价格
//                        BigDecimal variantPrice = ComputedFreightPriceUtil.changePriceRule(weightDecimal, new BigDecimal(productVariant.getPrice()), iteam.getArea());
//                        if (variantPrice != null) {
//                            productVariant.setPrice(variantPrice.doubleValue());
//                        }
////                        List<String> listSmall = new ArrayList<>();
////                        List<SysFile> sysFileSmall = SysFileUtils.getSysFile(productVariant.getImage());
////                        for (SysFile file : sysFileSmall) {
////                            listSmall.add(file.getPath());
////                        }
////                        productVariant.setImageList(listSmall);
//                        String description = iteam.getDescription();
//                        String delSpanDesc = description.replaceAll("<span([^>]{0,})>", "")
//                                .replaceAll("</span>", "")
//                                .replaceAll("</ span>", "");
//                        iteam.setDescription(delSpanDesc);
//                    }
//                });
//            }
//            System.out.println("=================================");
//
//            String shopId = null;
////            int a = 1 / 0;
//            String token = null;
//            String sid = null;
//            String marktPlaceId = null;
//            String gid1 = goodsProduct.get(0).getGid();
//            if (StringUtils.isBlank(gid1)) {
//                throw new RuntimeException("gid为空！");
//            }
//            R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid1);
//            Goods goods = goodsById.getData();
//            String type = goods.getPid();
//
//            //18 亚马逊  19 Ebay  20 Shopee
//            if ("18".equals(type)) {
//                token = goods.getMwsToken();
//                shopId = goods.getAccountSiteId();
//                sid = goods.getSid();
//                marktPlaceId = goods.getArea();
//            } else if ("19".equals(type)) {
//                token = goods.getMwsToken();
//                sid = goods.getSid();
//            } else if ("20".equals(type)) {
//                shopId = goods.getAccountSiteId();
//                sid = goods.getSid();
//            }
//            if (AMAZON.equals(type)) {
//                itemService.AmazonUploadInfo(goodsProduct, aToken, token, shopId, sid, goods, marktPlaceId);
//            } else if (EBAY.equals(type)) {
//                for (GoodsProduct product : goodsProduct) {
//                    itemService.additemToEbay(aToken, token, sid, product);
//                }
//            } else if (SHOPEE.equals(type)) {
//                for (GoodsProduct product : goodsProduct) {
//                    itemService.additemToShopee(aToken, shopId, sid, product);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
//        }
        addItemToMallService.addItemToMall(goodsProduct);
    }

    /**
     * 上架已下架的商品
     *
     * @param
     */
    @ApiOperation(value = "上架已下架的商品", notes = "上架已下架的商品")
    @SysLog("上架已下架的商品")
    @PostMapping("/relistItem")
    public void relistItem(@RequestBody List<GoodsProduct> goodsProducts, Authentication authentication) {
        try {
            String shopId = null;
            String token = null;
            String sid = null;
            String marketPlace = null;
            String secretKey = null;
            String awsAccessKeyId = null;
            String gid1 = goodsProducts.get(0).getGid();
            if (null == gid1) {
                throw new RuntimeException("gid为空！");
            }
            R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid1);
            Goods goods = goodsById.getData();
            String type = goods.getPid();
            //18 亚马逊  19 Ebay  20 Shopee
            if ("18".equals(type)) {
                token = goods.getMwsToken();
                shopId = goods.getAccountSiteId();
                sid = goods.getSid();
                marketPlace = goods.getArea();

                //根据区域值获取亚马逊对应的开发者账号信息
                String secretyKeyByArea = KjdsUtils.getSecretyKeyByArea(goods.getArea());
                String[] awsIdandSecrety = secretyKeyByArea.split("_");
                //亚马逊开发者id
                awsAccessKeyId = awsIdandSecrety[0];
                //亚马逊开发者秘钥
                secretKey = awsIdandSecrety[1];

            } else if ("19".equals(type)) {
                token = goods.getMwsToken();
                sid = goods.getSid();
            }

            if (AMAZON.equals(type)) {
                List<AmazonAllProductId> listProductId = new ArrayList<>();//存放当前批次产品的全部id信息
                for (int i = 0; i < goodsProducts.size(); i++) {

                    AmazonAllProductId bean = new AmazonAllProductId();
                    bean.setProductId(goodsProducts.get(i).getId());
                    List<AmazonSkuUtil> listAmazonSku = new ArrayList<>();//存放当前批次产品的全部sku信息
                    if (goodsProducts.get(i).getProductVariants() != null) {
                        goodsProducts.get(i).setStock(goodsProducts.get(i).getStock());//设置上架时库存量

                        AmazonSkuUtil skuOne = new AmazonSkuUtil();//将父产品的sku放入属于该商品的sku集合中
                        skuOne.setProductAllSku(goodsProducts.get(i).getSku());
                        listAmazonSku.add(skuOne);
                        for (int j = 0; j < goodsProducts.get(i).getProductVariants().size(); j++) {
                            goodsProducts.get(i).getProductVariants().get(j).setStock(goodsProducts.get(i).getProductVariants().get(j).getStock());

                            AmazonSkuUtil skuTwo = new AmazonSkuUtil();//将子产品的sku放入属于该商品的sku集合中
                            skuTwo.setProductAllSku(goodsProducts.get(i).getProductVariants().get(j).getSku());
                            listAmazonSku.add(skuTwo);
                        }
                    }
                    bean.setListSku(listAmazonSku);
                    listProductId.add(bean);
                }


                //将数据转换为亚马逊上传数据格式
                JSONArray array = formateAmazonNotEanJsonUtil.formateJonsForAmazon(goodsProducts, marketPlace, shopId);
                SubmitFeedUtil sfc = new SubmitFeedUtil();
                CreateAmazonXmlUtil cxu = new CreateAmazonXmlUtil();
                String createQuantityXml = cxu.createQuantityXml(array);
                String marketplaceId = AmazonUntil.formatteMarketplaceId(marketPlace);
                String quantity_type = "_POST_INVENTORY_AVAILABILITY_DATA_";
                String submitQuantityId = sfc.SubmitFeed(awsAccessKeyId, secretKey,
                        "hyxun", token,
                        shopId, token, quantity_type, createQuantityXml, marketplaceId, goodsProducts, sid, "2");

                TimeUnit.MINUTES.sleep(3);
                System.out.println(LocalDateTime.now() + "------上架商品----------");

                //判断当前文件是否上传完成
                while (true) {
                    net.sf.json.JSONObject objectss2 = (net.sf.json.JSONObject) AmazonUntil.feedList(submitQuantityId, shopId, token, secretKey, awsAccessKeyId, marketplaceId).get(0);
                    if (StringUtils.equals(objectss2.getString("FeedProcessingStatus"), "_DONE_")) {
                        break;
                    }
                    TimeUnit.MINUTES.sleep(3);
                }
                net.sf.json.JSONArray jsonArray2 = AmazonUntil.searchByFeedSubmissionId(submitQuantityId, shopId, token, secretKey, awsAccessKeyId, marketplaceId);
                JSONObject object2 = JSONObject.parseObject(jsonArray2.get(0).toString());


                //上传后执行回调方法
                if (object2.getString("ProcessingReport") != null) {
                    net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(object2.getString("ProcessingReport"));
                    if (jsonObj.has("Result")) {
                        String resultInfo = jsonObj.getString("Result");
                        if ("[".equals(resultInfo.substring(0, 1))) {//返回的一个错误的集合
                            net.sf.json.JSONArray arrayResult = net.sf.json.JSONArray.fromObject(resultInfo);
                            List<String> listErroProductId = new ArrayList<>();//遍历出现错误的商品id
                            for (int i = 0; i < listProductId.size(); i++) {//循环上传的全部商品
                                for (int x = 0; x < arrayResult.size(); x++) {
                                    String someInfo = arrayResult.getString(x);
                                    String errorSku = net.sf.json.JSONObject.fromObject(someInfo).getJSONArray("AdditionalInfo").getString(0);
                                    AmazonSkuUtil bean = new AmazonSkuUtil();
                                    if (errorSku.contains("##")) {
                                        bean.setProductAllSku(errorSku.split("##")[0]);
                                    } else {
                                        bean.setProductAllSku(errorSku);
                                    }
                                    if (listProductId.get(i).getListSku().contains(bean)) {
                                        listErroProductId.add(listProductId.get(i).getProductId());
                                        break;
                                    }
                                }
                            }

                            for (int m = 0; m < listProductId.size(); m++) {//调用回调函数
                                if (listErroProductId.contains(listProductId.get(m).getProductId())) {//包含的为上传有误的数据
                                    for (int x = 0; x < arrayResult.size(); x++) {
                                        String additionalInfo = arrayResult.getJSONObject(x).getJSONArray("AdditionalInfo").getString(0);
                                        AmazonSkuUtil bean222 = new AmazonSkuUtil();
                                        if (additionalInfo.contains("##")) {
                                            bean222.setProductAllSku(additionalInfo.split("##")[0]);
                                        } else {
                                            bean222.setProductAllSku(additionalInfo);
                                        }
                                        if (listProductId.get(m).getListSku().contains(bean222)) {
                                            KjdsUtils.callback(listProductId.get(m).getProductId(), sid, arrayResult.getJSONObject(x).getString("ResultDescription"), "", 5,goodsProducts.get(0).getUid());
                                            break;
                                        }
                                    }
                                } else {
                                    KjdsUtils.callback(listProductId.get(m).getProductId(), sid, "", "XXXXXXXX", 0,goodsProducts.get(0).getUid());
                                }
                            }


                        } else {//返回的一个错的对象
                            net.sf.json.JSONObject objcetResult = net.sf.json.JSONObject.fromObject(resultInfo);
                            net.sf.json.JSONArray additionalInfoArray = objcetResult.getJSONArray("AdditionalInfo");
                            for (int i = 0; i < listProductId.size(); i++) {//循环上传的全部商品
                                List<AmazonSkuUtil> listSku = listProductId.get(i).getListSku();
                                AmazonSkuUtil bean = new AmazonSkuUtil();
                                String stringErrorSku = additionalInfoArray.getString(0);
                                if (stringErrorSku.contains("##")) {
                                    bean.setProductAllSku(stringErrorSku.split("##")[0]);
                                } else {
                                    bean.setProductAllSku(stringErrorSku);
                                }

                                if (listSku.contains(bean)) {//如果该sku包含在这个商品中说明该商品有数据上传异常
                                    KjdsUtils.callback(listProductId.get(i).getProductId(), sid, objcetResult.getString("ResultDescription"), "", 5,goodsProducts.get(0).getUid());
                                } else {
                                    KjdsUtils.callback( listProductId.get(i).getProductId(), sid, "", "XXXXXXXX", 0,goodsProducts.get(0).getUid());
                                }
                            }
                        }
                    } else {//商品全部成功上传
                        for (int i = 0; i < listProductId.size(); i++) {
                            KjdsUtils.callback(listProductId.get(i).getProductId(), sid, "", "XXXXXXXX", 0,goodsProducts.get(0).getUid());
                        }
                    }
                }

            } else if (EBAY.equals(type)) {
                for (GoodsProduct goodsProduct : goodsProducts) {
                    String itemId = goodsProduct.getItemId();
                    String pid = goodsProduct.getId();
                    itemService.relistItemToEbay(authentication, token, itemId, pid, sid,goodsProduct.getUid());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List translateProductInfo(GoodsProduct iteam, ProductTranslation productTranslation) {
        String productTitle = productTranslation.getProductTitle();
        if (StringUtils.isNotBlank(productTitle)) {
            iteam.setProductTitle(productTitle);
        } else if (StringUtils.isBlank(productTitle)) {
            for (int i = 0; i < 5; i++) {
                String productTitle1 = null;
                try {
                    productTitle1 = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductTitle(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(productTitle1)) {
                    iteam.setProductTitle(productTitle1);
                    productTranslation.setProductTitle(productTitle1);
                    break;
                }
            }
        }
        String highlights = productTranslation.getHighlights();
        if (StringUtils.isNotBlank(highlights)) {
            iteam.setProductHighlights(highlights);
        } else {
            for (int i = 0; i < 5; i++) {
                String getProductHighlights = null;
                try {
                    getProductHighlights = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductHighlights(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(getProductHighlights)) {
                    iteam.setProductHighlights(getProductHighlights);
                    productTranslation.setHighlights(getProductHighlights);
                    break;
                }
            }
        }
        String keyword = productTranslation.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            iteam.setKeyWord(keyword);
        } else {
            for (int i = 0; i < 5; i++) {
                String getKeyWord = null;
                try {
                    getKeyWord = remoteTransactionFeignService.transcationSomeInfo(iteam.getKeyWord(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(getKeyWord)) {
                    iteam.setKeyWord(getKeyWord);
                    productTranslation.setKeyword(getKeyWord);
                    break;
                }
            }
        }
        String description = productTranslation.getDescription();
        if (StringUtils.isNotBlank(description)) {
            iteam.setDescription(description);
        } else {
            for (int i = 0; i < 5; i++) {
                String getDescription = null;
                try {
                    getDescription = remoteTransactionFeignService.transcationSomeInfo(iteam.getDescription(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(getDescription)) {
                    iteam.setDescription(getDescription);
                    productTranslation.setDescription(getDescription);
                    break;
                }
            }
        }
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(iteam);
        objects.add(productTranslation);
        return objects;
    }
}