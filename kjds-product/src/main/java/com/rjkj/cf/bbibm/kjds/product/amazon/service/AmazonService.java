package com.rjkj.cf.bbibm.kjds.product.amazon.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.bbibm.kjds.api.entity.Goods;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.AmazonConstant;
import com.rjkj.cf.bbibm.kjds.api.utils.AmazonUntil;
import com.rjkj.cf.bbibm.kjds.api.utils.JpushClientUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.CreateAmazonXmlUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.FormateAmazonJsonUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.FormateAmazonNotEanJsonUtil;
import com.rjkj.cf.bbibm.kjds.product.amazon.utils.SubmitFeedUtil;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.dom4j.DocumentException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sun.security.util.SecurityConstants;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@AllArgsConstructor
public class AmazonService {

    private final FormateAmazonNotEanJsonUtil formateAmazonNotEanJsonUtil;
    private final RemoteProductFeignService remoteProductFeignService;
    private final RemoteGoodsFeignService remoteGoodsFeignService;

    @Async
    public void lowerProductServ(Authentication atoken, String token, String serrId, String marketPlace, GoodsProduct bean, List<GoodsProduct> list, Goods goods) throws Exception {

        SubmitFeedUtil sfc=new SubmitFeedUtil();


        //根据区域值获取亚马逊对应的开发者账号信息
        String secretyKeyByArea = KjdsUtils.getSecretyKeyByArea(goods.getArea());
        String[] awsIdandSecrety = secretyKeyByArea.split("_");
        //亚马逊开发者id
        String awsId=awsIdandSecrety[0];
        //亚马逊开发者秘钥
        String secrety=awsIdandSecrety[1];

        //将数据转换为亚马逊上传数据格式
        JSONArray array = formateAmazonNotEanJsonUtil.formateJonsForAmazon(list,marketPlace,serrId);
        CreateAmazonXmlUtil cxu=new CreateAmazonXmlUtil();
        String createQuantityXml = cxu.createQuantityXml(array);
        String marketplaceId = AmazonUntil.formatteMarketplaceId(marketPlace);

        String quantityType="_POST_INVENTORY_AVAILABILITY_DATA_";
//        String submitQuantityId = sfc.SubmitFeed(awsId, secrety,
//                "hyxun", token,
//                serrId, token,quantityType,createQuantityXml,marketplaceId,list,atoken,"","2");
        String submitImageId = null;
        for (int i = 0; i < 5; i++) {
            try {
                submitImageId = sfc.SubmitFeed(awsId, secrety,
                        "hyxun", token,
                        serrId, token, quantityType, createQuantityXml, marketplaceId, list,  "", "2");

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
        System.out.println(LocalDateTime.now()+"-------下架商品（修改库存）----------");
        //判断当前文件是否上传完成
        for (int i = 0; i < 5 ; i++) {
            net.sf.json.JSONArray jsonArray = null;
            try {
                jsonArray = AmazonUntil.feedList(submitImageId, serrId, token, secrety, awsId, marketplaceId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(jsonArray!=null && jsonArray.size()>0){
                net.sf.json.JSONObject  objectss2= (net.sf.json.JSONObject)jsonArray.get(0);
                if(StringUtils.equals(objectss2.getString("FeedProcessingStatus"),"_DONE_")){
                    break;
                }
                TimeUnit.MINUTES.sleep(3);
            }
            TimeUnit.MINUTES.sleep(3);
        }

        net.sf.json.JSONArray jsonArray2 = AmazonUntil.searchByFeedSubmissionId(submitImageId, serrId, token, secrety, awsId, marketplaceId);
        JSONObject object2 = JSONObject.parseObject(jsonArray2.get(0).toString());
        System.out.println(object2.toString());

        if(object2.getString("ProcessingReport")!=null){
            net.sf.json.JSONObject jsonObj= net.sf.json.JSONObject.fromObject(object2.getString("ProcessingReport"));
            if(jsonObj.has("Result")){//下架失败
                KjdsUtils.callback(bean.getId(), bean.getGid(), jsonObj.getString("Result"), "", 0,bean.getUid());
            }else{//下架成功
                KjdsUtils.callback(bean.getId(), bean.getGid(), "", "XXXXXXXX", 1,bean.getUid());
            }
        }
    }


    /**
     * 修改亚马逊商品
     * @param atoken
     * @param token
     * @param serrId
     * @param marketPlace
     * @param bean
     * @param list
     * @param goods
     */
    @Async
    public void changeProductPrice(Authentication atoken, String token, String serrId, String marketPlace, GoodsProduct bean, List<GoodsProduct> list, Goods goods, BigDecimal changePrice) throws Exception {


        SubmitFeedUtil sfc = new SubmitFeedUtil();

        //根据区域值获取亚马逊对应的开发者账号信息
        String secretyKeyByArea = KjdsUtils.getSecretyKeyByArea(goods.getArea());
        String[] awsIdandSecrety = secretyKeyByArea.split("_");
        //亚马逊开发者id
        String awsId=awsIdandSecrety[0];
        //亚马逊开发者秘钥
        String secrety=awsIdandSecrety[1];

        //将数据转换为亚马逊上传数据格式
        JSONArray array = formateAmazonNotEanJsonUtil.formateJonsForAmazon(list,marketPlace,serrId);
        CreateAmazonXmlUtil cxu = new CreateAmazonXmlUtil();
        String createPriceXml = cxu.createPriceXml(array,marketPlace);

        String marketplaceId = AmazonUntil.formatteMarketplaceId(marketPlace);

        String price_type = "_POST_PRODUCT_PRICING_DATA_";
        String submitPriceId = sfc.SubmitFeed(awsId, secrety,
                "hyxun", token,
                serrId, token, price_type, createPriceXml, marketplaceId,list,"","2");

        TimeUnit.MINUTES.sleep(3);
        System.out.println(LocalDateTime.now()+"------上传价格----------");


        //判断当前文件是否上传完成
        while (true) {
            net.sf.json.JSONArray jsonArray = AmazonUntil.feedList(submitPriceId, serrId, token, secrety, awsId, marketplaceId);

            net.sf.json.JSONObject objectss4 = null;
            if(jsonArray!=null && jsonArray.size()>0){
                objectss4=(net.sf.json.JSONObject)jsonArray.get(0);
                if (StringUtils.equals(objectss4.getString("FeedProcessingStatus"), "_DONE_")) {
                    break;
                }
                TimeUnit.MINUTES.sleep(3);
            }
            TimeUnit.MINUTES.sleep(3);
        }

        net.sf.json.JSONArray jsonArray4 = AmazonUntil.searchByFeedSubmissionId(submitPriceId, serrId, token, secrety, awsId, marketplaceId);
        JSONObject object2 = JSONObject.parseObject(jsonArray4.get(0).toString());
        System.out.println(object2.toString());


        if (object2.getString("ProcessingReport") != null) {
            net.sf.json.JSONObject jsonObj = net.sf.json.JSONObject.fromObject(object2.getString("ProcessingReport"));
            JpushClientUtil jpushClientUtil = new JpushClientUtil(remoteGoodsFeignService, remoteProductFeignService);
            if (jsonObj.has("Result")) {
                jpushClientUtil.sendOrdinaryMsg("修改商品价格","修改商品价格成功",bean.getId());

                KjdsUtils.callPriceBack(atoken,bean.getId(),bean.getGid(),changePrice);
            } else {
                jpushClientUtil.sendOrdinaryMsg("修改商品价格","修改商品价格失败",bean.getId());

                KjdsUtils.callPriceBack(atoken,bean.getId(),bean.getGid(),changePrice);
            }
        }


    }


}
