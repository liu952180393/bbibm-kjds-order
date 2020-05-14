package com.rjkj.cf.bbibm.kjds.product.additem.service;

import com.alibaba.fastjson.JSONArray;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.utils.AmazonUntil;
import com.rjkj.cf.bbibm.kjds.product.amazon.service.MarketplaceWebServiceException;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.CreateAmazonXmlUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.SubmitFeedUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@Component
@Slf4j
public class AmazonUploadService {
//    @Async
    public void uploadStock(List<GoodsProduct> goodsProduct, String token, String shopId, String sid, JSONArray array, SubmitFeedUtil sfc, CreateAmazonXmlUtil cxu, String marketplaceId, String awsId, String secrety) throws Exception {
        String quantityType = "_POST_INVENTORY_AVAILABILITY_DATA_";
        String createQuantityXml = cxu.createQuantityXml(array);
        String submitQuantityId = null;
        for (int i = 0; i < 5; i++) {
            try {
                submitQuantityId = sfc.SubmitFeed(awsId, secrety,
                        "hyxun", token,
                        shopId, token, quantityType, createQuantityXml, marketplaceId, goodsProduct, sid, "1");
            } catch (MarketplaceWebServiceException e) {
                System.out.println("发生了:"+e.getMessage());
            }
            if (submitQuantityId != null){
                break;
            }
            TimeUnit.MINUTES.sleep(1);
            if(i==4){
                throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
            }
        }
        TimeUnit.MINUTES.sleep(3);
        System.out.println(LocalDateTime.now() + "  " + shopId + "------上传库存---------");

        //判断当前文件是否上传完成
        for (int i = 0; i < 5; i++) {
            net.sf.json.JSONArray jsonArray =null;
            for (int j = 0; j < 5; j++) {
                try {
                    jsonArray = AmazonUntil.feedList(submitQuantityId, shopId, token, secrety, awsId, marketplaceId);
                } catch (Exception e) {
                    System.out.println("发生了:"+e.getMessage());
                }
                if (jsonArray != null){
                    break;
                }
                TimeUnit.MINUTES.sleep(1);
                if(i==4){
                    throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
                }
            }
            net.sf.json.JSONObject objectss5 = null;
            if (jsonArray != null && jsonArray.size() > 0) {
                objectss5 = (net.sf.json.JSONObject) jsonArray.get(0);
                if (StringUtils.equals(objectss5.getString("FeedProcessingStatus"), "_DONE_")) {
                    System.out.println(LocalDateTime.now() + "  " + shopId + "  上传库存成功");
                    break;
                }
            }
            if (i <= 3) {
                TimeUnit.MINUTES.sleep(3);
            } else {
                TimeUnit.MINUTES.sleep(10);
            }
            System.out.println(LocalDateTime.now() + "  " + shopId + "判断上传库存：第" + i + "次");
        }
//        net.sf.json.JSONArray jsonArray5 = AmazonUntil.searchByFeedSubmissionId(submitQuantityId, shopId, token, secrety, awsId, marketplaceId);
//        JSONObject object5 = JSONObject.parseObject(jsonArray5.get(0).toString());
//        System.out.println(object5.toString());
    }
//    @Async
    public void uploadPrice(List<GoodsProduct> goodsProduct,String token, String shopId, String sid, String marktPlaceId, JSONArray array, SubmitFeedUtil sfc, CreateAmazonXmlUtil cxu, String marketplaceId, String awsId, String secrety) throws Exception {
        String priceType = "_POST_PRODUCT_PRICING_DATA_";
        String createPriceXml = cxu.createPriceXml(array, marktPlaceId);
        String submitPriceId = null;
        for (int i = 0; i < 5; i++) {
            try {
                submitPriceId = sfc.SubmitFeed(awsId, secrety,
                        "hyxun", token,
                        shopId, token, priceType, createPriceXml, marketplaceId, goodsProduct,sid, "1");
            } catch (MarketplaceWebServiceException e) {
                System.out.println("发生了:"+e.getMessage());
            }
            if (submitPriceId != null){
                break;
            }
            TimeUnit.MINUTES.sleep(1);
            if(i==4){
                throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
            }
        }
        TimeUnit.MINUTES.sleep(3);
        System.out.println(LocalDateTime.now() + "  " + shopId + "--------上传价格---------");

        //判断当前文件是否上传完成
        for (int i = 0; i < 5; i++) {
            net.sf.json.JSONArray jsonArray =null;
            for (int j = 0; j < 5; j++) {
                try {
                    jsonArray = AmazonUntil.feedList(submitPriceId, shopId, token, secrety, awsId, marketplaceId);
                } catch (Exception e) {
                    System.out.println("发生了:"+e.getMessage());
                }
                if (jsonArray != null){
                    break;
                }
                TimeUnit.MINUTES.sleep(1);
                if(i==4){
                    throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
                }
            }
            net.sf.json.JSONObject objectss4 = null;
            if (jsonArray != null && jsonArray.size() > 0) {
                objectss4 = (net.sf.json.JSONObject) jsonArray.get(0);
                if (StringUtils.equals(objectss4.getString("FeedProcessingStatus"), "_DONE_")) {
                    System.out.println(LocalDateTime.now() + "  " + shopId + "  上传价格成功");
                    break;
                }
            }
            if (i <= 3) {
                TimeUnit.MINUTES.sleep(3);
            } else {
                TimeUnit.MINUTES.sleep(10);
            }
            System.out.println(LocalDateTime.now() + "  " + shopId + "判断上传价格：第" + i + "次");
        }
//        net.sf.json.JSONArray jsonArray4 = AmazonUntil.searchByFeedSubmissionId(submitPriceId, shopId, token, secrety, awsId, marketplaceId);
//        JSONObject object4 = JSONObject.parseObject(jsonArray4.get(0).toString());
//        System.out.println(object4.toString());
    }
//    @Async
    public void uploadImage(List<GoodsProduct> goodsProduct, String token, String shopId, String sid, JSONArray array, SubmitFeedUtil sfc, CreateAmazonXmlUtil cxu, String marketplaceId, String awsId, String secrety) throws Exception {
        String imageType = "_POST_PRODUCT_IMAGE_DATA_";
        String createImageXml = cxu.createImageXml(array);
        String submitImageId = null;
        for (int i = 0; i < 5; i++) {
            try {
                submitImageId = sfc.SubmitFeed(awsId, secrety,
                        "hyxun", token,
                        shopId, token, imageType, createImageXml, marketplaceId, goodsProduct, sid, "1");

            } catch (MarketplaceWebServiceException e) {
                System.out.println("发生了:"+e.getMessage());
            }
            if (submitImageId != null){
                break;
            }
            TimeUnit.MINUTES.sleep(1);
            if(i==4){
                throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
            }
        }
        TimeUnit.MINUTES.sleep(3);
        System.out.println(LocalDateTime.now() + "  " + shopId + "-------上传图片---------");

        //判断当前文件是否上传完成
        for (int i = 0; i < 5; i++) {
            net.sf.json.JSONArray jsonArray = null;
            for (int j = 0; j < 5; j++) {
                try {
                    jsonArray = AmazonUntil.feedList(submitImageId, shopId, token, secrety, awsId, marketplaceId);
                } catch (Exception e) {
                    System.out.println("发生了:"+e.getMessage());
                }
                if (jsonArray != null){
                    break;
                }
                TimeUnit.MINUTES.sleep(1);
                if(i==4){
                    throw new NoHttpResponseException("mws-eu.amazonservices.com:443 failed to respond");
                }
            }

            net.sf.json.JSONObject objectss3 = null;
            if (jsonArray != null && jsonArray.size() > 0) {
                objectss3 = (net.sf.json.JSONObject) jsonArray.get(0);
                if (StringUtils.equals(objectss3.getString("FeedProcessingStatus"), "_DONE_")) {
                    System.out.println(LocalDateTime.now() + "  " + shopId + "  上传图片成功");
                    break;
                }
            }
            if (i <= 3) {
                TimeUnit.MINUTES.sleep(3);
            } else {
                TimeUnit.MINUTES.sleep(10);
            }
            System.out.println(LocalDateTime.now() + "  " + shopId + "判断上传图片：第" + i + "次");
        }
//        net.sf.json.JSONArray jsonArray3 = AmazonUntil.searchByFeedSubmissionId(submitImageId, shopId, token, secrety, awsId, marketplaceId);
//        JSONObject object3 = JSONObject.parseObject(jsonArray3.get(0).toString());
//        System.out.println(object3.toString());
    }
}
