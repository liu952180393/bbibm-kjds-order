package com.rjkj.cf.bbibm.kjds.product.ebay.controller;

import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.bbibm.kjds.api.entity.Goods;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.EbayUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.PriceChangeUtils;
import com.rjkj.cf.bbibm.kjds.product.ebay.service.EbayProductService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;


/**
 * @描述：Ebay商城商品上传
 * @项目：跨境电商
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-09 14:55:08
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/ebayProduct")
@Api(value = "ebayProduct", tags = "Ebay商城商品管理")
public class EbayProductController {
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final EbayProductService ebayProductService;

    /**
     * Ebay根据商品id下架商品
     *
     * @return R
     */
    @ApiOperation(value = "根据产品id下架商品", notes = "根据产品id下架商品")
    @SysLog("根据产品id下架商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pids", value = "产品ID,多个ID用 ',' 逗号分隔,注意和店铺ID一一对应", required = true, dataType = "String"),
            @ApiImplicitParam(name = "shopIds", value = "店铺ID,多个ID用 ',' 逗号分隔", required = true, dataType = "String")
    })
    @PostMapping("/endItem")
    public R endItem(Authentication authentication,String pids, String shopIds) {
        RjkjUser user = SecurityUtils.getUser();
        String uid = user.getId();
        if (StringUtils.isEmpty(pids)) {
            throw new RuntimeException("产品id不能为空！");
        }
        if (StringUtils.isEmpty(shopIds)) {
            throw new RuntimeException("店铺id不能为空！");
        }
        String[] pidsArr = pids.split(",");
        String[] shopIdsArr = shopIds.split(",");
        for (int i = 0; i < pidsArr.length; i++) {
            //根据店铺ID和产品ID获取唯一该店铺的唯一一条产品
            String pid = pidsArr[i];
            String sid = shopIdsArr[i];
            R<GoodsProduct> goodsProductById = remoteGoodsFeignService.getGoodsProductById(pid, sid,uid);
            GoodsProduct goodsProduct = goodsProductById.getData();
            //删除非上架成功的商品
            if(goodsProduct.getRackStatus() != 0){
                remoteGoodsFeignService.deleteProduct(pid,uid,goodsProduct.getGid(),goodsProduct.getArea());
                continue;
            }
            String gid = goodsProduct.getGid();
            //获得该产品的ItemId
            String itemId = goodsProduct.getItemId();
            R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid);
            Goods goods = goodsById.getData();
            String token = goods.getMwsToken();
            ebayProductService.endItem(authentication,pid,gid,itemId,token,goods.getUid());
        }
        return R.ok(true,"操作成功");
    }

    /**
     * 根据Ebay商品id上架商品
     *
     * @return R
     */
    @ApiOperation(value = "根据Ebay商品id上架商品", notes = "根据Ebay商品id上架商品")
    @SysLog("根据Ebay商品id上架商品")
    @PostMapping("/relistItem")
    public R relistItem(List<GoodsProduct> goodsProducts) {
        try {
            for (GoodsProduct goodsProduct : goodsProducts) {
                String gid = goodsProduct.getGid();
                String itemId = goodsProduct.getItemId();
                R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid);
                Goods goods = goodsById.getData();
                String token = goods.getMwsToken();
                String callName = "RelistFixedPriceItem";
                //获取URLConnection
//                URLConnection conn = EbayUtils.getUrlConnection(callName);
                Document document = DocumentHelper.createDocument();
                // 创建根节点
                document.addElement("RelistFixedPriceItemRequest", "urn:ebay:apis:eBLBaseComponents");
                // 通过document对象获取根元素的信息
                document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
                document.getRootElement().addElement("ItemID").setText(itemId);
                document.getRootElement().addElement("EndingReason").setText("NotAvailable");
                String endItemResponse = EbayUtils.sendRequest(callName, document.asXML());
                String result = JSONObject.parseObject(endItemResponse).getJSONObject("EndItemResponse").getString("Ack");
                //上架成功
                if ("Success".equals(result)) {
//                    remoteGoodsFeignService.goodsProductCallBack(goodsProduct.getId(), sid, "", itemId, 0);
//                    return R.ok(true, "上架成功");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(true, "上架中");
    }

    /**
     * Ebay根据商品id批量修改所有变体的价格
     *
     * @return R
     */
    @ApiOperation(value = "Ebay根据商品id一键改价", notes = "Ebay根据商品id一键改价")
    @SysLog("Ebay根据商品id一键改价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "产品ID,多个ID用 ',' 逗号分隔,注意和店铺ID一一对应", required = true, dataType = "String"),
            @ApiImplicitParam(name = "shopIds", value = "店铺ID,多个ID用 ',' 逗号分隔", required = true, dataType = "String"),
            @ApiImplicitParam(name = "priceType", value = "金额调整类型（1涨价 2降价）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "changePrice", value = "调整值", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "调整类型（1百分比 2固定值）", required = true, dataType = "String")
    })
    @PostMapping("/updatePrice")
    public R updatePriceBatch(String ids, String shopIds, BigDecimal changePrice, int priceType, int type) {
        if (StringUtils.isEmpty(ids)) {
            throw new RuntimeException("itemId不能为空！");
        }
        if (StringUtils.isEmpty(shopIds)) {
            throw new RuntimeException("店铺Id不能为空！");
        }
        try {
            RjkjUser user = SecurityUtils.getUser();
            String uid = user.getId();
            String[] idsArr = ids.split(",");
            String[] shopIdsArr = shopIds.split(",");
            for (int i = 0; i < idsArr.length; i++) {
                //根据店铺ID和产品ID获取唯一该店铺的唯一一条产品
                String pid = idsArr[i];
                String sid = shopIdsArr[i];
                R<GoodsProduct> goodsProductById = remoteGoodsFeignService.getGoodsProductById(pid, sid,uid);
                GoodsProduct goodsProduct = goodsProductById.getData();
                //根据传入的比例或者固定值计算价格，返回计算好的价格。
                BigDecimal price = PriceChangeUtils.changePrice(BigDecimal.valueOf(goodsProduct.getCostUnitPrice()), changePrice, type, priceType);
                String gid = goodsProduct.getGid();
                //获得该产品的ItemId
                String itemId = goodsProduct.getItemId();
                R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid);
                Goods goods = goodsById.getData();
                String token = goods.getMwsToken();
                //获得该商品的详细信息，包含所有的变体
                String itemDetail = EbayUtils.getItem(itemId, token);
                String variations = "<Variations>";
                //如果该商品信息包含变体，就批量修改所有变体的价格。
                if (itemDetail.contains(variations)) {
                    int strStartIndex = itemDetail.indexOf(variations);
                    int strEndIndex = itemDetail.indexOf("</Variations>");
                    //将变体信息截取出来
                    String resultVar = itemDetail.substring(strStartIndex, strEndIndex).substring(variations.length());
                    //将查询出来的商品信息拼接进修改价格的参数
                    StringBuilder sb = new StringBuilder();
                    sb.append("<ReviseFixedPriceItemRequest xmlns=\"urn:ebay:apis:eBLBaseComponents\">");
                    sb.append("<Item>");
                    sb.append("<Variations>");
                    sb.append(resultVar);
                    sb.append("</Variations>");
                    sb.append("</Item>");
                    sb.append("</ReviseFixedPriceItemRequest>");
                    Document document = DocumentHelper.parseText(sb.toString());
                    String callName = "ReviseFixedPriceItem";
                    //获取URLConnection
//                    URLConnection conn = EbayUtils.getUrlConnection(callName);
                    // 通过document对象获取根元素的信息
                    document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
                    Element item = document.getRootElement().element("Item");
                    item.addElement("ItemID").setText(itemId);
                    Element element = item.element("Variations");
                    //更改所有的变体价格为现在的最新价格。
                    Iterator iterator = element.elementIterator();
                    while (iterator.hasNext()) {
                        Element stu = (Element) iterator.next();
                        Iterator iterator1 = stu.elementIterator();
                        while (iterator1.hasNext()) {
                            Element stuChild = (Element) iterator1.next();
                            if ("StartPrice".equals(stuChild.getName())) {
                                stuChild.setText(String.valueOf(price));
                            }
                        }
                    }
                    String reviseFixedPriceItemResponse = EbayUtils.sendRequest(callName, document.asXML());
                    String result = JSONObject.parseObject(reviseFixedPriceItemResponse).getJSONObject("ReviseFixedPriceItemResponse").getString("Ack");
                    if ("Success".equals(result)) {
                        //回调修改商品列表价格
//                    remoteGoodsFeignService.changePriceByGid(pid, sid, type, priceType, goodsProduct.getArea(), changePrice);
                    }
                    //无变体，则另外拼接参数进行修改价格。
                } else {
                    Document document = DocumentHelper.createDocument();
                    String callName = "ReviseFixedPriceItem";
                    // 创建根节点
                    document.addElement("ReviseFixedPriceItemRequest", "urn:ebay:apis:eBLBaseComponents");
                    // 通过document对象获取根元素的信息
                    document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
                    document.getRootElement().addElement("startPrice").setText(String.valueOf(price));
                    String reviseFixedPriceItemResponse = EbayUtils.sendRequest(callName, document.asXML());
                    String result = JSONObject.parseObject(reviseFixedPriceItemResponse).getJSONObject("ReviseFixedPriceItemResponse").getString("Ack");
                    if ("Success".equals(result)) {
                        //回调修改商品列表价格
//                        remoteGoodsFeignService.changePriceByGid(pid, sid, type, priceType, goodsProduct.getArea(), changePrice);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(false, "系统错误！");
        }
        return R.ok(true, "价格修改中！");
    }

//    /**
//     * Ebay根据商品id修改价格
//     *
//     * @return R
//     */
//    @ApiOperation(value = "Ebay根据商品id修改价格", notes = "Ebay根据商品id修改价格")
//    @SysLog("Ebay根据商品id修改价格")
//    @PostMapping("/updatePrice")
//    public R updatePrice(String itemId, String price) {
//        String token = null;
//        if (StringUtil.isEmpty(itemId)) {
//            throw new RuntimeException("itemId不能为空！");
//        }
//        if (StringUtil.isEmpty(price)) {
//            throw new RuntimeException("价格不能为空！");
//        }
//        try {
//            String callName = "ReviseInventoryStatus";
//            //获取URLConnection
//            URLConnection conn = EbayUtils.getUrlConnection(callName);
//            Document document = DocumentHelper.createDocument();
//            document.addElement("ReviseInventoryStatusRequest", "urn:ebay:apis:eBLBaseComponents");
//            // 通过document对象获取根元素的信息
//            document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
//            Element element = document.getRootElement().addElement("Item").addElement("ItemID");
//            element.setText(itemId);
////            element.setDocument();
//            String reviseInventoryStatusResponse = EbayUtils.sendRequest(conn, document.asXML());
//            String result = JSONObject.parseObject(reviseInventoryStatusResponse).getString("Ack");
//            if ("Success".equals(result)) {
//                return R.ok(true,"修改价格成功！");
//            } else {
//                return R.failed(false,"修改价格失败！");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return R.failed();
//        }
//    }


}
