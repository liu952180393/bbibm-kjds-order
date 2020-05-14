package com.rjkj.cf.bbibm.kjds.product.additem.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.JsonArray;
import com.mysql.cj.conf.ConnectionUrl;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.admin.api.vo.UserVO;
import com.rjkj.cf.bbibm.kjds.api.entity.AmazonGetOrderListVo;
import com.rjkj.cf.bbibm.kjds.api.entity.Goods;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteTransactionFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.*;
import com.rjkj.cf.bbibm.kjds.product.amazon.service.MarketplaceWebServiceException;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.CreateAmazonXmlUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.FormateAmazonJsonUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.SubmitFeedUtil;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;
import com.rjkj.cf.bbibm.kjds.product.appcategory.mapper.AppCategoryMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariantsTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductTranslationService;
import com.rjkj.cf.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rjkj.cf.bbibm.kjds.api.utils.ShopeeConstant.ADD_ITEM_URL;
import static com.rjkj.cf.bbibm.kjds.api.utils.ShopeeConstant.PARTNER_ID;
import static com.rjkj.cf.bbibm.kjds.product.amazon.utils.MallConstant.*;

@AllArgsConstructor
@Component
@Slf4j
public class AddItemService {
    private final AppCategoryMapper appCategoryMapper;
    private final FormateAmazonJsonUtil formateAmazonJsonUtil;
    private final AmazonUploadService amazonUploadService;
//    private final ProductTranslationService productTranslationService;
//    private final RemoteTransactionFeignService remoteTransactionFeignService;
//    private final RemoteGoodsFeignService remoteGoodsFeignService;

    /**
     * 上传商品到Ebay
     *
     * @param sid
     * @param product
     */
    @Async
    public void additemToEbay(String token, String sid, GoodsProduct product) {
        try {
            System.out.println("Ebay异步进来了" + product.getProductTitle());
            Thread.sleep(5000);
            String productTitle = product.getProductTitle();
            if (productTitle.length() > 80) {
                product.setProductTitle(productTitle.substring(0, 80));
            }
            Boolean checkToken = EbayUtils.checkToken(token);
            if (!checkToken) {
                System.out.println("网络错误或者该Ebay账户Token已失效！请重新授权。");
                KjdsUtils.callback(product.getId(), sid, "网络错误或者该Ebay账户Token已失效！请重新授权。", "", 5, product.getUid());
                return;
            }
//            BigDecimal raetByArea = rateService.getRaetByArea(product.getArea());
            JSONObject prams = KjdsUtils.classToJson(product);
            System.out.println(prams.toString());
            Element rootElement;
            String callName = "AddFixedPriceItem";
            //获取URLConnection
//        URLConnection conn = EbayUtils.getUrlConnection(callName);
            // 创建document对象
            Document document = DocumentHelper.createDocument();
            // 2、创建根节点
            rootElement = document.addElement("AddFixedPriceItemRequest", "urn:ebay:apis:eBLBaseComponents");

            rootElement.addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);

            Element item = rootElement.addElement("Item");
            String area = product.getArea();
            item.addElement("Country").setText(area);
            String currency = KjdsUtils.translateCurrency(area);
            if (StringUtils.isBlank(currency)) {
                currency = "USD";
            }
            item.addElement("Currency").setText(currency);

            //新旧程度，默认为1000全新
            item.addElement("ConditionID").setText("1000");

            //当不存在单层变体和双层变体的时候，设置商品价格，有变体则以变体价格为准
            if (StringUtils.isBlank(prams.getString("variations")) && StringUtils.isBlank(prams.getString("variation"))) {
//                item.addElement("StartPrice").setText(String.valueOf(new BigDecimal(prams.getString("price")).divide(raetByArea, 1, RoundingMode.HALF_UP)));
                item.addElement("StartPrice").setText(prams.getString("price"));
            }

            item.addElement("DispatchTimeMax").setText("3");
            //上市时间，GTC为上市直到取消
            item.addElement("ListingDuration").setText("GTC");

            item.addElement("ListingType").setText("FixedPriceItem");

            item.addElement("PaymentMethods").setText("PayPal");

            item.addElement("PayPalEmailAddress").setText("megaonlinemerchant@gmail.com");

            //发货地邮政编码
            item.addElement("PostalCode").setText("999077");
            //根据主商品ID获取Ebay商城的ID,用作上传
//                    AppCategory appCategory = appCategoryMapper.queryById(product.getProductClassificationId());
//                    String ebayCategoryId = appCategory.getEbay();
            String ebayCategoryId = "95125";
            if (StringUtils.isBlank(ebayCategoryId)) {
                ebayCategoryId = "95125";
            }
            item.addElement("PrimaryCategory").addElement("CategoryID").setText(ebayCategoryId);

            item.addElement("Title").setText(prams.getString("name"));

            item.addElement("Description").setText(prams.getString("description"));

            //商品主图
            Element pictureDetails = item.addElement("PictureDetails");
            JSONArray imagesUrl = prams.getJSONArray("images_url");
            for (int i = 0; i < imagesUrl.size(); i++) {
                pictureDetails.addElement("PictureURL").setText(imagesUrl.get(i).toString());
            }
            //退货政策
            Element returnPolicy = item.addElement("ReturnPolicy");
            returnPolicy.addElement("ReturnsAcceptedOption").setText("ReturnsAccepted");
            returnPolicy.addElement("RefundOption").setText("MoneyBack");
            returnPolicy.addElement("ReturnsWithinOption").setText("Days_30");
            returnPolicy.addElement("ShippingCostPaidByOption").setText("Buyer");
            //运输政策
            Element shippingDetails = item.addElement("ShippingDetails");
            shippingDetails.addElement("ShippingType").setText("Flat");
            Element shippingServiceOptions = shippingDetails.addElement("ShippingServiceOptions");
            shippingServiceOptions.addElement("ShippingServicePriority").setText("1");
            shippingServiceOptions.addElement("ShippingService").setText("USPSMedia");
            shippingServiceOptions.addElement("ShippingServiceCost").setText("2.50");
            //商品的属性，显示在商品展示图之下，描述之上的属性栏
            Element itemSpecifics = item.addElement("ItemSpecifics");
            //根据分类查询必要属性
            ArrayList<String> categorySpecifics = EbayUtils.getCategorySpecifics(ebayCategoryId, token);
            if (categorySpecifics != null && categorySpecifics.size() > 0) {
                for (String categorySpecific : categorySpecifics) {
                    Element nameValueList = itemSpecifics.addElement("NameValueList");
                    nameValueList.addElement("Name").setText(categorySpecific);
                    nameValueList.addElement("Value").setText("default");
                }
            }
            JSONArray variationArr = prams.getJSONArray("variation");
            //如果单变体存在,并且多变体不存在
            if (StringUtils.isNotEmpty(prams.getString("variations")) && variationArr.isEmpty()) {
                JSONArray arrVariations = prams.getJSONArray("variations");
                //把所有的单变体的name存入List
                ArrayList<String> variationValue = new ArrayList<>();
                for (int i = 0; i < arrVariations.size(); i++) {
                    JSONObject jsonVariations = (JSONObject) arrVariations.get(i);
                    variationValue.add(jsonVariations.getString("name"));
                }
                //变量列表，先定义，Variation里面引用。
                Element variations = item.addElement("Variations");
                Element variationSpecificsSet = variations.addElement("VariationSpecificsSet");
                JSONObject jsonVariation1 = (JSONObject) arrVariations.get(0);
                Element nameVariationsList = variationSpecificsSet.addElement("NameValueList");
                //获得变体的名称
                nameVariationsList.addElement("Name").setText(jsonVariation1.getString("type"));
                //遍历List,取出所有的name，拼接NameValueList数据
                for (int j = 0; j < variationValue.size(); j++) {
                    nameVariationsList.addElement("Value").setText(variationValue.get(j));
                }
                //拼接Variation数据
                for (int i = 0; i < arrVariations.size(); i++) {
                    JSONObject jsonVariation = (JSONObject) arrVariations.get(i);
                    Element variation = variations.addElement("Variation");
                    variation.addElement("SKU").setText(jsonVariation.getString("variation_sku"));
//                    variation.addElement("StartPrice").setText(String.valueOf(new BigDecimal(jsonVariation.getString("price")).divide(raetByArea, 1, RoundingMode.HALF_UP)));
                    variation.addElement("StartPrice").setText(jsonVariation.getString("price"));
                    variation.addElement("Quantity").setText(jsonVariation.getString("stock"));
                    Element variationSpecifics = variation.addElement("VariationSpecifics");
                    Element variationValueList = variationSpecifics.addElement("NameValueList");
                    variationValueList.addElement("Name").setText(jsonVariation.getString("type"));
                    variationValueList.addElement("Value").setText(jsonVariation.getString("name"));
                }
            }
            //多变体存在
            if (variationArr != null && variationArr.size() > 0) {
                //多变量列表，先定义，Variation里面引用。
                Element variations = item.addElement("Variations");
                Element variationSpecificsSet = variations.addElement("VariationSpecificsSet");
                JSONArray arrVariation = prams.getJSONArray("tier_variation");
                ArrayList<String> optionsList = new ArrayList<>();
                ArrayList<List> opList = new ArrayList<>();
                if (!arrVariation.isEmpty()) {
                    for (int i = 0; i < arrVariation.size(); i++) {
                        JSONObject jsonVariation = (JSONObject) arrVariation.get(i);
                        JSONArray options = jsonVariation.getJSONArray("options");
                        Element nameVariationsList = variationSpecificsSet.addElement("NameValueList");
                        nameVariationsList.addElement("Name").setText(jsonVariation.getString("name"));
                        //变体值列表
                        optionsList.add(jsonVariation.getString("name"));
                        for (int j = 0; j < options.size(); j++) {
                            nameVariationsList.addElement("Value").setText(options.get(j).toString());
                            optionsList.add(options.get(j).toString());
                        }
                        opList.add(new ArrayList<>(optionsList));
                        optionsList.clear();
                    }
                }
                //变体组合,判断变体是否存在
                if (StringUtils.isNotBlank(prams.getString("variation"))) {
                    JSONArray arrayVariation = prams.getJSONArray("variation");
                    if (arrayVariation.size() > 0) {
                        for (int i = 0; i < arrayVariation.size(); i++) {
                            JSONObject jsonVariation = (JSONObject) arrayVariation.get(i);
                            Element variation = variations.addElement("Variation");
                            //单个变体的数据
                            variation.addElement("SKU").setText(jsonVariation.getString("variation_sku"));

                            variation.addElement("StartPrice").setText(jsonVariation.getString("price"));
//                            variation.addElement("StartPrice").setText(String.valueOf(new BigDecimal(jsonVariation.getString("price")).divide(raetByArea, 1, RoundingMode.HALF_UP)));
                            variation.addElement("Quantity").setText(jsonVariation.getString("stock"));
                            //索引
                            JSONArray tierIndex = jsonVariation.getJSONArray("tier_index");
                            Element variationSpecifics = variation.addElement("VariationSpecifics");
                            //取出模板中的下标对应组合
                            for (int j = 0; j < tierIndex.size(); j++) {
                                Element variationValueList = variationSpecifics.addElement("NameValueList");
                                variationValueList.addElement("Name").setText((opList.get(j).get(0).toString()));
                                //将模板中的数组下标和遍历的下标作一个对应。
                                variationValueList.addElement("Value").setText(opList.get(j).get((Integer) tierIndex.get(j) + 1).toString());
                            }
                        }
                    }
                }
            }
            System.out.println(rootElement.asXML());
            JSONObject result = JSONObject.parseObject(EbayUtils.sendRequest(callName, "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + rootElement.asXML())).getJSONObject("AddFixedPriceItemResponse");
            System.out.println(result.toString());
            String itemId = result.getString("ItemID");
            if (StringUtils.isNotEmpty(itemId)) {
                System.out.println("=============Ebay上传成功===============");
                KjdsUtils.callback(product.getId(), sid, "", itemId, 0, product.getUid());
            } else {
                System.out.println("=============Ebay上传失败===============");
                String errors = result.getString("Errors");
                String errMsg = "";
                if (errors.startsWith("[")) {
                    JSONObject errors1 = (JSONObject) result.getJSONArray("Errors").get(0);
                    errMsg = errors1.getString("ShortMessage");
                } else {
                    errMsg = result.getJSONObject("Errors").getString("ShortMessage");
                }
                KjdsUtils.callback(product.getId(), sid, errMsg, "", 5, product.getUid());
            }
        } catch (Exception e) {
            e.printStackTrace();
            KjdsUtils.callback(product.getId(), sid, "服务器连接Ebay失败，请稍后重试。", "", 5, product.getUid());
        }
    }

    /**
     * 上传商品到Shopee
     *
     * @param shopId
     * @param sid
     * @param product
     */
    @Async
    public void additemToShopee(String shopId, String sid, GoodsProduct product) {
        try {
            System.out.println("Shopee进来了：" + product.getProductTitle());
            String productTitle = product.getProductTitle();
            //判断商品标题是否不足10字符，不足则复制本身
            if (productTitle.length() < 10) {
                productTitle = productTitle + productTitle;
                product.setProductTitle(productTitle);
            }
            //判断商品标题是否超过60，超过则截取
            if (productTitle.length() > 60) {
                product.setProductTitle(productTitle.substring(0, 60));
            }
//            BigDecimal raetByArea = rateService.getRaetByArea(product.getArea());
            JSONObject prams = KjdsUtils.classToJson(product);
            System.out.println(prams.toString());
            JSONObject item = new JSONObject();
            item.put("name", prams.getString("name"));
            String description = prams.getString("description");
            System.out.println(description);
            description =  description.replaceAll(" ", "nbsp")
                    .replaceAll(",", "com1ma")
                    .replaceAll("，", "com1ma")
                    .replaceAll("。", "full1stop")
                    .replaceAll("/.", "full1x");
            description = description.replaceAll("<[.[^>]]*>", "").replaceAll("[^0-9a-zA-Z\u4e00-\u9fa5]+", "")
                    .replaceAll("nbsp", " ")
                    .replaceAll("com1ma", ",")
                    .replaceAll("full1stop", "。")
                    .replaceAll("full1x", ".")
            .replaceAll("br.","  ");
            item.put("description", description);
            //根据主商品ID获取Shopee商城的ID,用作上传
            AppCategory appCategory = appCategoryMapper.queryById(product.getProductClassificationId());
            //shopee分类ID
            String shopeeCategoryId = appCategory.getShopee();
            if (StringUtils.isEmpty(shopeeCategoryId)) {
                shopeeCategoryId = "9302";
            }
            item.put("category_id", Long.valueOf(shopeeCategoryId));
            item.put("item_sku", prams.getString("item_sku"));
            //如果变体不存在就上传主商品的价格和库存
            if (StringUtils.isBlank(prams.getString("variations"))) {
                item.put("price", prams.getInteger("price"));
                item.put("stock", prams.getInteger("stock"));
            }
            //拼接商品主图信息
            JSONArray images = prams.getJSONArray("images_url");
            JSONArray arrImage = new JSONArray();
            for (int i = 0; i < images.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", images.get(i).toString());
                arrImage.add(jsonObject);
            }
            List<Object> arrayList = null;
            item.put("images", arrImage);
            if (arrImage.size() > 9) {
                arrayList = arrImage.subList(0, 9);
                item.put("images", arrayList);
            }

            //拼接单层变体信息
            if (StringUtils.isNotBlank(prams.getString("variations"))) {
                JSONArray arrVariations = new JSONArray();
                JSONArray variations = prams.getJSONArray("variations");
                for (int i = 0; i < variations.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject2 = JSONObject.parseObject(variations.get(i).toString());
                    jsonObject.put("name", jsonObject2.getString("name"));
                    jsonObject.put("price", jsonObject2.getInteger("price"));
                    jsonObject.put("stock", jsonObject2.getInteger("stock"));
                    jsonObject.put("variation_sku", jsonObject2.getString("variation_sku"));
                    arrVariations.add(jsonObject);
                }
                item.put("variations", arrVariations);
            }
            //拼接根据分类ID获取的必填属性
            item.put("attributes", ShopeeUtils.getAttributes(shopId, shopeeCategoryId,product.getBrandName()));
            //拼接物流信息
            item.put("logistics", ShopeeUtils.getLogistics(shopId));
            //重量
//                    Double weight = product.getWeight();
//                    if (weight < 0.1) {
//                        weight = 0.2;
//                    }
            item.put("weight", 0.2);
            //开发者ID
            item.put("partner_id", PARTNER_ID);
            //目标商店ID
            item.put("shopid", Long.valueOf(shopId));
            //当前的时间戳
            item.put("timestamp", ShopeeUtils.getTimestamp());
            System.out.println(item.toString());
            //执行请求,返回执行结果
            JSONObject jsonObject = JSONObject.parseObject(ShopeeUtils.getUrlConnection(ADD_ITEM_URL, item.toString()));
            System.out.println(jsonObject.toString());
            //有itemId则商品上传成功
            String itemId = jsonObject.getString("item_id");
            if (StringUtils.isNotBlank(itemId)) {
                JSONArray tierVariation = prams.getJSONArray("tier_variation");
                JSONArray twoVariation = prams.getJSONArray("variation");
                //判断商品是否还有没有上传的双层变体，有就继续上传双层变体
                if (null != tierVariation && tierVariation.size() > 0) {
                    if (null != twoVariation && twoVariation.size() > 0) {
                        //新增双层变体
                        System.out.println(ShopeeUtils.initTierVariation(jsonObject.getLong("item_id"), shopId, prams));
                    }
                }
                System.out.println("=============Shopee上传成功===============");
                KjdsUtils.callback(product.getId(), sid, "", itemId, 0, product.getUid());
            } else {
                System.out.println("=============Shopee上传失败===============");
                String msg = jsonObject.getString("msg");
                if (StringUtils.isBlank(msg)) {
                    msg = "上传失败";
                }
                KjdsUtils.callback(product.getId(), sid, msg, "", 5, product.getUid());
            }
            System.out.println("=============Shopee上传结束===============");
        } catch (Exception e) {
            e.printStackTrace();
            KjdsUtils.callback(product.getId(), sid, "连接Shopee失败，请稍后重试。", "", 5, product.getUid());
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 上传亚马逊方法
     *
     * @param goodsProduct
     * @param token
     * @param shopId
     * @param sid
     * @param marktPlaceId
     * @throws Exception
     */
    @Async
    public void AmazonUploadInfo(List<GoodsProduct> goodsProduct, String token, String shopId, String sid, String area, String marktPlaceId) throws Exception {

        try {

            List<AmazonAllProductId> listProductId = new ArrayList<>();//存放当前批次产品的全部id信息
            for (int q = 0; q < goodsProduct.size(); q++) {
                AmazonAllProductId bean = new AmazonAllProductId();
                bean.setProductId(goodsProduct.get(q).getId());

                List<AmazonSkuUtil> listAmazonSku = new ArrayList<>();//存放当前批次产品的全部sku信息
                AmazonSkuUtil skuOne = new AmazonSkuUtil();//将父产品的sku放入属于该商品的sku集合中
                skuOne.setProductAllSku(goodsProduct.get(q).getSku());
                listAmazonSku.add(skuOne);

                //判断变体信息不为空时
                if (goodsProduct.get(q).getProductVariants() != null) {
                    for (int w = 0; w < goodsProduct.get(q).getProductVariants().size(); w++) {
                        AmazonSkuUtil skuTwo = new AmazonSkuUtil();//将子产品的sku放入属于该商品的sku集合中
                        skuTwo.setProductAllSku(goodsProduct.get(q).getProductVariants().get(w).getSku());
                        listAmazonSku.add(skuTwo);
                    }
                }
                bean.setListSku(listAmazonSku);
                listProductId.add(bean);
            }


            //将数据转换为亚马逊上传数据格式
            JSONArray array = formateAmazonJsonUtil.formateJonsForAmazon(goodsProduct, marktPlaceId, shopId);
            SubmitFeedUtil sfc = new SubmitFeedUtil();
            CreateAmazonXmlUtil cxu = new CreateAmazonXmlUtil();

            String marketplaceId = AmazonUntil.formatteMarketplaceId(marktPlaceId);


            //根据区域值获取亚马逊对应的开发者账号信息
            String secretyKeyByArea = KjdsUtils.getSecretyKeyByArea(area);
            String[] awsIdandSecrety = secretyKeyByArea.split("_");
            //亚马逊开发者id
            String awsId = awsIdandSecrety[0];
            //亚马逊开发者秘钥
            String secrety = awsIdandSecrety[1];


            HashMap<String, String> parameters = AmazonUntil.mustByParams();
            parameters.put("AWSAccessKeyId", AmazonUntil.urlEncode(awsId));
            parameters.put("SellerId", AmazonUntil.urlEncode(shopId));
            parameters.put("MWSAuthToken", AmazonUntil.urlEncode(token));
            parameters.put("MarketplaceId.Id.1", AmazonUntil.urlEncode(AmazonUntil.formatteMarketplaceId(marktPlaceId)));

            AmazonGetOrderListVo amazonGetOrderListVo = new AmazonGetOrderListVo();
            amazonGetOrderListVo.setStartTime(DateUtils.getBeforeTime(30));
            amazonGetOrderListVo.setEndTime(DateUtils.getBeforeTime(0));
            net.sf.json.JSONArray orders = AmazonUntil.listOrderPost(AmazonUntil.formatteMarketplaceId(marktPlaceId), parameters, secrety, amazonGetOrderListVo);

            //判断亚马逊请求参数是否异常
            if (orders != null && orders.size() > 0) {
                JSONObject jsonObject = JSONObject.parseObject(orders.get(0).toString());
                if (jsonObject.containsKey("errortyped")) {
                    System.out.println("上传请求参数异常");
                    for (int p = 0; p < goodsProduct.size(); p++) {
                        KjdsUtils.callback(listProductId.get(p).getProductId(), sid, jsonObject.getString("message"), "", 5, goodsProduct.get(0).getUid());
                    }
                    return;
                }
            }


            /**
             * 商品上传
             */
            String productType = "_POST_PRODUCT_DATA_";
            String createProductXml = cxu.createProductXml(array, area);
            String submitProductId = null;
            System.out.println(LocalDateTime.now() + "  " + shopId + "-------上传商品---------");
            for (int i = 0; i < 5; i++) {
                try {
                    submitProductId = sfc.SubmitFeed(awsId, secrety,
                            "hyxun", token,
                            shopId, token, productType, createProductXml, marketplaceId, goodsProduct, sid, "1");
                } catch (MarketplaceWebServiceException e) {
                    System.out.println("发生了:" + e.getMessage());
                }
                if (submitProductId != null) {
                    break;
                }
                TimeUnit.MINUTES.sleep(1);
                if (i == 4) {
                    throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
                }
            }
            TimeUnit.MINUTES.sleep(3);

            //判断当前文件是否上传完成
            for (int i = 0; i < 5; i++) {
                net.sf.json.JSONArray jsonArray = null;
                for (int j = 0; j < 5; j++) {
                    try {
                        jsonArray = AmazonUntil.feedList(submitProductId, shopId, token, secrety, awsId, marketplaceId);
                    } catch (Exception e) {
                        System.out.println("发生了:" + e.getMessage());
                    }
                    if (jsonArray != null) {
                        break;
                    }
                    TimeUnit.MINUTES.sleep(1);
                    if (i == 4) {
                        throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
                    }
                }
                net.sf.json.JSONObject objectss1 = null;
                if (jsonArray != null && jsonArray.size() > 0) {
                    objectss1 = (net.sf.json.JSONObject) jsonArray.get(0);
                    if (StringUtils.equals(objectss1.getString("FeedProcessingStatus"), "_DONE_")) {
                        System.out.println(LocalDateTime.now() + "  " + shopId + "  商品主体上传成功");
                        break;
                    }
                }
                if (i <= 3) {
                    TimeUnit.MINUTES.sleep(3);
                } else {
                    TimeUnit.MINUTES.sleep(10);
                }
                System.out.println(LocalDateTime.now() + "  " + shopId + "  判断商品主体是否上传：第" + i + "次");
            }
            net.sf.json.JSONArray jsonArray1 = null;
            JSONObject object1 = null;
            for (int i = 0; i < 5; i++) {
                try {
                    jsonArray1 = AmazonUntil.searchByFeedSubmissionId(submitProductId, shopId, token, secrety, awsId, marketplaceId);
                    if (jsonArray1 != null) {
                        object1 = JSONObject.parseObject(jsonArray1.get(0).toString());
                        System.out.println(object1.toString());
                        break;
                    }
                    if (i <= 3) {
                        TimeUnit.MINUTES.sleep(3);
                    } else {
                        TimeUnit.MINUTES.sleep(10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /**
             * 关联关系上传
             */
            String relationType = "_POST_PRODUCT_RELATIONSHIP_DATA_";
            String createRelationXml = cxu.createRelationXml(array);
            //判断当前批次产品存在变体数据的时候才进该方法
            if (StringUtils.isNotEmpty(createRelationXml)) {
                System.out.println(LocalDateTime.now() + "  " + shopId + "-------上传关联关系---------");
                String submitRelationId = null;
                for (int i = 0; i < 5; i++) {
                    try {
                        submitRelationId = sfc.SubmitFeed(awsId, secrety,
                                "hyxun", token,
                                shopId, token, relationType, createRelationXml, marketplaceId, goodsProduct, sid, "1");
                    } catch (MarketplaceWebServiceException e) {
                        System.out.println("发生了:" + e.getMessage());
                    }
                    if (submitProductId != null) {
                        break;
                    }
                    TimeUnit.MINUTES.sleep(1);
                    if (i == 4) {
                        throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
                    }
                }
                TimeUnit.MINUTES.sleep(3);

                //判断当前文件是否上传完成
                for (int i = 0; i < 5; i++) {
                    net.sf.json.JSONArray jsonArray = null;
                    for (int j = 0; j < 5; j++) {
                        try {
                            jsonArray = AmazonUntil.feedList(submitRelationId, shopId, token, secrety, awsId, marketplaceId);
                        } catch (Exception e) {
                            System.out.println("发生了:" + e.getMessage());
                        }
                        if (jsonArray != null) {
                            break;
                        }
                        TimeUnit.MINUTES.sleep(1);
                        if (i == 4) {
                            throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
                        }
                    }
                    net.sf.json.JSONObject objectss2 = null;
                    if (jsonArray != null && jsonArray.size() > 0) {
                        objectss2 = (net.sf.json.JSONObject) jsonArray.get(0);
                        if (StringUtils.equals(objectss2.getString("FeedProcessingStatus"), "_DONE_")) {
                            System.out.println(LocalDateTime.now() + "  " + shopId + "  上传关联关系成功");
                            break;
                        }
                    }
                    if (i <= 3) {
                        TimeUnit.MINUTES.sleep(3);
                    } else {
                        TimeUnit.MINUTES.sleep(10);
                    }
                    System.out.println(LocalDateTime.now() + "  " + shopId + "  判断上传关联关系：第" + i + "次");
                }
//                net.sf.json.JSONArray jsonArray2 = AmazonUntil.searchByFeedSubmissionId(submitRelationId, shopId, token, secrety, awsId, marketplaceId);
//                JSONObject object2 = JSONObject.parseObject(jsonArray2.get(0).toString());
//                System.out.println(object2.toString());
            }

            /**
             * 图片上传
             */
            amazonUploadService.uploadImage(goodsProduct, token, shopId, sid, array, sfc, cxu, marketplaceId, awsId, secrety);
            /**
             * 价格上传
             */
            amazonUploadService.uploadPrice(goodsProduct, token, shopId, sid, marktPlaceId, array, sfc, cxu, marketplaceId, awsId, secrety);
            /**
             * 库存上传
             */
            amazonUploadService.uploadStock(goodsProduct, token, shopId, sid, array, sfc, cxu, marketplaceId, awsId, secrety);
            System.out.println("上传已提交，准备开始回调");
//            TimeUnit.MINUTES.sleep(3);
            //上传后执行回调方法
            System.out.println("上传完毕，开始回调");
            if (object1.getString("ProcessingReport") != null) {
                net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(object1.getString("ProcessingReport"));
                if (jsonObj.has("Result")) {
                    String resultInfo = jsonObj.getString("Result");
                    if ("[".equals(resultInfo.substring(0, 1))) {//返回的一个错误的集合
                        net.sf.json.JSONArray arrayResult = net.sf.json.JSONArray.fromObject(resultInfo);
                        List<String> listErroProductId = new ArrayList<>();//遍历出现错误的商品id
                        for (int i = 0; i < listProductId.size(); i++) {//循环上传的全部商品
                            for (int x = 0; x < arrayResult.size(); x++) {
                                String someInfo = arrayResult.getString(x);
                                net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(someInfo);
                                if (jsonObject.has("AdditionalInfo")) {
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
                        }

                        for (int m = 0; m < listProductId.size(); m++) {//调用回调函数
                            if (listErroProductId.contains(listProductId.get(m).getProductId())) {//包含的为上传有误的数据
                                for (int x = 0; x < arrayResult.size(); x++) {
                                    net.sf.json.JSONObject jsonObject = arrayResult.getJSONObject(x);
                                    if (jsonObject.has("AdditionalInfo")) {
                                        String additionalInfo = arrayResult.getJSONObject(x).getJSONArray("AdditionalInfo").getString(0);
                                        AmazonSkuUtil bean222 = new AmazonSkuUtil();
                                        if (additionalInfo.contains("##")) {
                                            bean222.setProductAllSku(additionalInfo.split("##")[0]);
                                        } else {
                                            bean222.setProductAllSku(additionalInfo);
                                        }
                                        if (listProductId.get(m).getListSku().contains(bean222)) {
                                            KjdsUtils.callback(listProductId.get(m).getProductId(), sid, arrayResult.getJSONObject(x).getString("ResultDescription"), "", 5, goodsProduct.get(0).getUid());
                                            break;
                                        }
                                    }
                                }
                            } else {
                                KjdsUtils.callback(listProductId.get(m).getProductId(), sid, "", "XXXXXXXX", 0, goodsProduct.get(0).getUid());
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
                                KjdsUtils.callback(listProductId.get(i).getProductId(), sid, objcetResult.getString("ResultDescription"), "", 5, goodsProduct.get(0).getUid());
                            } else {
                                KjdsUtils.callback(listProductId.get(i).getProductId(), sid, "", "XXXXXXXX", 0, goodsProduct.get(0).getUid());
                            }
                        }
                    }
                } else {//商品全部成功上传
                    for (int i = 0; i < listProductId.size(); i++) {
                        KjdsUtils.callback(listProductId.get(i).getProductId(), sid, "", "XXXXXXXX", 0, goodsProduct.get(0).getUid());
                    }
                }
            }

        } catch (Exception e) {
            if (e instanceof ConnectException) {
                System.out.println("请求亚马逊失败");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口异常 443异常,请重新请求。", "", 5, goodsProduct.get(0).getUid());
                }
            } else if ("EAN为空".equals(e.getMessage())) {
                System.out.println("EAN为空");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "EAN码为空，请联系客服解决。", "", 5, goodsProduct.get(0).getUid());
                }
            } else if ("商品分类异常".equals(e.getMessage())) {
                System.out.println("商品分类异常");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "商品分类异常，商品分类和APP分类不对应，请反馈到客服。", "", 5, goodsProduct.get(0).getUid());
                }
            } else if (e instanceof RuntimeException) {
                System.out.println("数据异常");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "系统运行时异常", "", 5, goodsProduct.get(0).getUid());
                }
            } else if (e instanceof SocketTimeoutException) {
                System.out.println("请求亚马逊失败");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口异常 443异常,请重新请求。", "", 5, goodsProduct.get(0).getUid());
                }
            } else if (e instanceof SocketException) {
                System.out.println("请求亚马逊连接重置");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊连接重置,请重新请求。", "", 5, goodsProduct.get(0).getUid());
                }
            } else if (e instanceof NoHttpResponseException) {
                System.out.println("请求亚马逊数据失败");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口异常 443异常,请重新请求。", "", 5, goodsProduct.get(0).getUid());
                }
            } else if (e instanceof SSLHandshakeException) {
                System.out.println("握手期间远程主机关闭连接");
                for (int u = 0; u < goodsProduct.size(); u++) {
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口异常:握手期间远程主机关闭连接,请重新请求。", "", 5, goodsProduct.get(0).getUid());
                }
            } else {
                for (int u = 0; u < goodsProduct.size(); u++) {
                    System.out.println("系统异常");
                    KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "未知异常:" + e.getMessage(), "", 5, goodsProduct.get(0).getUid());
                }
            }
            e.printStackTrace();
        }
    }

    @Async
    public void relistItemToEbay(Authentication auth, String token, String itemId, String pid, String sid, String uid) {
        try {
            System.out.println("========Ebay Async上架============");
            String callName = "RelistFixedPriceItem";
            //获取URLConnection
//        URLConnection conn = EbayUtils.getUrlConnection(callName);
            Document document = DocumentHelper.createDocument();
            // 创建根节点
            document.addElement("RelistFixedPriceItemRequest", "urn:ebay:apis:eBLBaseComponents");
            // 通过document对象获取根元素的信息
            document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
            if (StringUtils.isBlank(itemId)) {
                KjdsUtils.callback(pid, sid, "商品ID为空，不能上架，请重新上传商品。", "", 5, uid);
                return;
            }
            document.getRootElement().addElement("Item").addElement("ItemID").setText(itemId);
            System.out.println(document.asXML());
            String endItemResponse = EbayUtils.sendRequest(callName, document.asXML());
            System.out.println(endItemResponse);
            JSONObject relistFixedPriceItemResponse = JSONObject.parseObject(endItemResponse).getJSONObject("RelistFixedPriceItemResponse");
            //上架成功
            if ("Success".equals(relistFixedPriceItemResponse.getString("Ack"))) {
                String newItemId = relistFixedPriceItemResponse.getString("ItemID");
                System.out.println("==============Ebay上架成功=================");
                //                        remoteGoodsFeignService.goodsProductCallBack(goodsProduct.getId(), sid, "", newItemId, 0);
                //回调修改状态
                KjdsUtils.callback(pid, sid, "", newItemId, 0, uid);
            } else {
                System.out.println("=============Ebay上传失败===============");
                String errMsg = relistFixedPriceItemResponse.getJSONObject("Errors").getString("ShortMessage");
                KjdsUtils.callback(pid, sid, errMsg, "", 5, uid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("上架失败");
        }
    }

}
