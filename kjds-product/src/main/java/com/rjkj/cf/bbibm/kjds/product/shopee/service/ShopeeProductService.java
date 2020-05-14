package com.rjkj.cf.bbibm.kjds.product.shopee.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.bbibm.kjds.api.entity.Goods;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.ComputedFreightPriceUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.PriceChangeUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.ShopeeUtils;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;

@AllArgsConstructor
@Component
public class ShopeeProductService {
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    @Async
    public void endItem(Authentication authentication,String pid,String gid,String shopId,String itemId,String uid) {
        //发送请求，返回执行的结果
        String result = ShopeeUtils.endItem(shopId, itemId);
        String itemIdResult = JSONObject.parseObject(result).getString("item_id");
        if (itemIdResult != null) {
            System.out.println("==========Shopee下架============");
            KjdsUtils.callback(pid, gid, "", itemId, 1,uid);
        }
    }
    @Async
    public void updatePrice(Authentication atoken, String pid, String sid, BigDecimal changePrice, int priceType, int type,String uid) {
//        R<GoodsProduct> goodsProductById = remoteGoodsFeignService.getGoodsProductById(pid, sid);
        R<LinkedHashMap> goodsProductById1 = KjdsUtils.getGoodsProductById(pid, sid,uid);
        LinkedHashMap goodsProductData = goodsProductById1.getData();
//        GoodsProduct goodsProduct = goodsProductById.getData();
//        if(goodsProduct == null){
//           throw new RuntimeException("商品信息为空");
//        }
        //根据传入的比例或者固定值计算价格，返回计算好的价格。
        BigDecimal costUnitPrice = new BigDecimal((Double) goodsProductData.get("costUnitPrice"));
        BigDecimal price = PriceChangeUtils.changePrice(costUnitPrice, changePrice, type, priceType);
//        Double weight = goodsProduct.getWeight();
        Double weight1 = (Double) goodsProductData.get("weight");
        String area = (String) goodsProductData.get("area");
        BigDecimal newPrice = ComputedFreightPriceUtil.changePriceRule(new BigDecimal(weight1 == null ? 200 : weight1), price, area, uid);
//        String gid = goodsProduct.getGid();
        String gid1 = (String) goodsProductData.get("gid");
        //获得该产品的ItemId
//        String itemId = goodsProduct.getItemId();
        String itemId1 = (String) goodsProductData.get("itemId");
        R<LinkedHashMap> goodsById = KjdsUtils.getGoodsById(gid1);
        LinkedHashMap goodsByIdMap = goodsById.getData();
        String shopId = (String)goodsByIdMap.get("accountSiteId");
        //查询该商品详细信息，查询是否有变体等信息，有变体则一起修改为相同价格。
        JSONObject itemDetail = ShopeeUtils.getItemDetail(shopId, itemId1);
        JSONArray variationsArray = itemDetail.getJSONObject("item").getJSONArray("variations");
        ArrayList<Long> variationsId = new ArrayList<>();
        if (variationsArray.size() > 0) {
            JSONArray variationArray = new JSONArray();
            for (int j = 0; j < variationsArray.size(); j++) {
                JSONObject variation = JSONObject.parseObject(variationsArray.get(j).toString());
                variationsId.add(variation.getLongValue("variation_id"));
            }
            for (Long variations : variationsId) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("variation_id", variations);
                jsonObject.put("price", newPrice);
                jsonObject.put("item_id", Long.valueOf(itemId1));
                variationArray.add(jsonObject);
            }
            String result = ShopeeUtils.updateVariationPriceBatch(shopId, variationArray);
            JSONArray resultArr = JSONObject.parseObject(result).getJSONObject("batch_result").getJSONArray("failures");
            if (resultArr.size() == 0) {
                //回调修改商品列表价格
                KjdsUtils.callPriceBack(atoken,pid,sid,price);
            }
        } else {
            String result = ShopeeUtils.updateItemPrice(shopId, itemId1, String.valueOf(newPrice));
            JSONObject item = JSONObject.parseObject(result).getJSONObject("item");
            if (item != null) {
                //回调修改商品列表价格
                KjdsUtils.callPriceBack(atoken,pid,gid1,price);
            }
        }
    }
}
