package com.rjkj.cf.bbibm.kjds.product.amazon.controller;

import com.rjkj.cf.bbibm.kjds.api.entity.Goods;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.utils.ComputedFreightPriceUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.PriceChangeUtils;
import com.rjkj.cf.bbibm.kjds.product.amazon.service.AmazonService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @描述：亚马逊商品信息管理
 * @项目：
 * @公司：软江科技
 * @作者：crq
 * @时间：2019-10-11 14:55:08
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/amaoznFeedUpload")
@Api(value = "amaoznFeedUpload", tags = "亚马逊商品信息管理")
public class AmaoznFeedUploadController {

    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final AmazonService amazonService;

//
//    /**
//     * 生成xml数据,上传到亚马逊
//     *
//     * @param
//     */
//    @ApiOperation(value = "生成xml数据,上传到亚马逊", notes = "生成xml数据,上传到亚马逊")
//    @SysLog("生成xml数据,上传到亚马逊")
//    @PostMapping("/uploadAmazonFeed")
//    public R uploadAmazonFeed(@RequestBody List<GoodsProduct> goodsProduct) throws Exception {
//
//    	try {
//			FormateAmazonJsonUtil formateAmazonJsonUtil=new FormateAmazonJsonUtil();
//
//			//将数据转换为亚马逊上传数据格式
//			JSONArray array = formateAmazonJsonUtil.formateJonsForAmazon(goodsProduct);
//			SubmitFeedUtil sfc=new SubmitFeedUtil();
//			CreateAmazonXmlUtil cxu=new CreateAmazonXmlUtil();
//
//			/**
//			 * 商品上传
//			 */
//			String product_type="_POST_PRODUCT_DATA_";
//			String createProductXml = cxu.createProductXml(array);
//			String submitProductId = sfc.SubmitFeed("AKIAI4SKJMBRKZLSLLTQ", "ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34",
//					"hyxun", AmazonConstant.MWS_AUTH_TOKEN,
//					AmazonConstant.SELLER_ID, AmazonConstant.MWS_AUTH_TOKEN,product_type,createProductXml);
//
//			TimeUnit.MINUTES.sleep(4);
//
//			System.out.println("上传---------");
//
//			net.sf.json.JSONArray jsonArray1 = AmazonUntil.searchByFeedSubmissionId(submitProductId);
//			JSONObject object1 = JSONObject.parseObject(jsonArray1.get(0).toString());
//			System.out.println(object1.toString());
//
//
//
//			/**
//			 * 关联关系上传
//			 */
//			String createRelationXml = cxu.createRelationXml(array);
//			String relation_type="_POST_PRODUCT_RELATIONSHIP_DATA_";
//			String submitRelationId = sfc.SubmitFeed("AKIAI4SKJMBRKZLSLLTQ", "ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34",
//					"hyxun", AmazonConstant.MWS_AUTH_TOKEN,
//					AmazonConstant.SELLER_ID, AmazonConstant.MWS_AUTH_TOKEN,relation_type,createRelationXml);
//
//			TimeUnit.MINUTES.sleep(3);
//
//			System.out.println("上传---------");
//
//			net.sf.json.JSONArray jsonArray2 = AmazonUntil.searchByFeedSubmissionId(submitRelationId);
//			JSONObject object2 = JSONObject.parseObject(jsonArray2.get(0).toString());
//			System.out.println(object2.toString());
//
//
//
//
//			/**
//			 * 图片上传
//			 */
//			String image_type="_POST_PRODUCT_IMAGE_DATA_";
//			String createImageXml = cxu.createImageXml(array);
//			String submitImageId = sfc.SubmitFeed("AKIAI4SKJMBRKZLSLLTQ", "ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34",
//					"hyxun", AmazonConstant.MWS_AUTH_TOKEN,
//					AmazonConstant.SELLER_ID, AmazonConstant.MWS_AUTH_TOKEN,image_type,createImageXml);
//
//			TimeUnit.MINUTES.sleep(3);
//
//			System.out.println("上传---------");
//
//			net.sf.json.JSONArray jsonArray3 = AmazonUntil.searchByFeedSubmissionId(submitImageId);
//			JSONObject object3 = JSONObject.parseObject(jsonArray3.get(0).toString());
//			System.out.println(object3.toString());
//
//
//			/**
//			 * 价格上传
//			 */
//			String price_type="_POST_PRODUCT_PRICING_DATA_";
//			String createPriceXml = cxu.createPriceXml(array);
//			String submitPriceId = sfc.SubmitFeed("AKIAI4SKJMBRKZLSLLTQ", "ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34",
//					"hyxun", AmazonConstant.MWS_AUTH_TOKEN,
//					AmazonConstant.SELLER_ID, AmazonConstant.MWS_AUTH_TOKEN,price_type,createPriceXml);
//
//			TimeUnit.MINUTES.sleep(3);
//
//			System.out.println("上传---------");
//
//			net.sf.json.JSONArray jsonArray4 = AmazonUntil.searchByFeedSubmissionId(submitPriceId);
//			JSONObject object4 = JSONObject.parseObject(jsonArray4.get(0).toString());
//			System.out.println(object4.toString());
//
//
//
//			/**
//			 * 库存上传
//			 */
//			String quantity_type="_POST_INVENTORY_AVAILABILITY_DATA_";
//			String createQuantityXml = cxu.createQuantityXml(array);
//			String submitQuantityId = sfc.SubmitFeed("AKIAI4SKJMBRKZLSLLTQ", "ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34",
//					"hyxun", AmazonConstant.MWS_AUTH_TOKEN,
//					AmazonConstant.SELLER_ID, AmazonConstant.MWS_AUTH_TOKEN,quantity_type,createQuantityXml);
//
//			TimeUnit.MINUTES.sleep(3);
//
//			System.out.println("上传---------");
//
//			net.sf.json.JSONArray jsonArray5 = AmazonUntil.searchByFeedSubmissionId(submitQuantityId);
//			JSONObject object5 = JSONObject.parseObject(jsonArray5.get(0).toString());
//			System.out.println(object5.toString());
//
//
//
//		}catch (Exception e){
//    		e.printStackTrace();
//		}
//		return R.ok();
//    }
//
//

    /**
     * 下架已上架的亚马逊商品
     *
     * @param
     */
    @ApiOperation(value = "下架已上架的亚马逊商品", notes = "下架已上架的亚马逊商品")
    @SysLog("下架已上架的亚马逊商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pids", value = "产品pids(,区分)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "shopIds", value = "店铺shopIds(,区分)", required = true, dataType = "String"),

    })
    @PostMapping("/lowerProductInfo")
    public R lowerProductInfo(String pids, String shopIds, Authentication atoken) {
        RjkjUser user = SecurityUtils.getUser();
        String uid = user.getId();
        try {
            if (StringUtils.isEmpty(pids)) {
                throw new RuntimeException("商品的ids不能为空");
            }
            if (StringUtils.isEmpty(shopIds)) {
                throw new RuntimeException("店铺的ids不能为空");
            }

            //商品的ids
            String[] pidss = pids.split(",");
            //店铺的ids
            String[] shopIdss = shopIds.split(",");
            for (int i = 0; i < pidss.length; i++) {
                List<GoodsProduct> list = new ArrayList<>();
                R<GoodsProduct> goodsProductById = remoteGoodsFeignService.getGoodsProductById(pidss[i], shopIdss[i],uid);

                GoodsProduct bean = goodsProductById.getData();
                //删除上传失败的产品
                if (bean != null) {
                    if (bean.getRackStatus() != 0) {
                        remoteGoodsFeignService.deleteProduct(pidss[i], user.getId(), bean.getGid(), bean.getArea());
                        continue;
                    }
                }
                //商品对应的店铺id（针对平台）
                String gid = goodsProductById.getData().getGid();
                R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid);
                Goods data = goodsById.getData();
                //亚马逊店铺id
                String serrId = data.getAccountSiteId();
                //亚马逊店铺token
                String token = data.getMwsToken();
                //区域标识
                String marketPlace = data.getArea();

                bean.setStock(0);
                if (bean.getProductVariants() != null) {
                    for (int j = 0; j < bean.getProductVariants().size(); j++) {
                        bean.getProductVariants().get(j).setStock(0);
                    }
                }

                list.add(bean);

                amazonService.lowerProductServ(atoken, token, serrId, marketPlace, bean, list, data);

            }

        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }

        return R.ok("", "已提交请求");

    }


    /**
     * 修改已上传产品价格
     *
     * @param
     */
    @ApiOperation(value = "修改已上传产品价格", notes = "修改已上传产品价格")
    @SysLog("修改已上传产品价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pids", value = "产品pids(,区分)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "shopIds", value = "店铺shopIds(,区分)", required = true, dataType = "String"),
            @ApiImplicitParam(name = "priceType", value = "金额调整类型（1涨价 2降价）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "changePrice", value = "调整值", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "调整类型（1百分比 2固定值）", required = true, dataType = "String")

    })
    @PostMapping("/updateProductPriceInfo")
    public R updateProductPriceInfo(String pids, String shopIds, BigDecimal changePrice, int priceType, int type, Authentication atoken) {
        try {
            if (StringUtils.isEmpty(pids)) {
                throw new RuntimeException("商品的id不能为空");
            }
            if (StringUtils.isEmpty(shopIds)) {
                throw new RuntimeException("店铺的id不能为空");
            }
            //商品的ids
            String[] pidss = pids.split(",");
            //店铺的ids
            String[] shopIdss = shopIds.split(",");
            for (int i = 0; i < pidss.length; i++) {
                List<GoodsProduct> goodsProduct = new ArrayList<>();
                RjkjUser user = SecurityUtils.getUser();
                String uid = user.getId();
                R<GoodsProduct> goodsProductById = remoteGoodsFeignService.getGoodsProductById(pidss[i], shopIdss[i], uid);
                GoodsProduct bean = goodsProductById.getData();

                //店铺id（针对app）
                String gid = bean.getGid();
                R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid);
                Goods data = goodsById.getData();
                //亚马逊店铺id
                String serrId = data.getAccountSiteId();
                //亚马逊店铺token
                String token = data.getMwsToken();
                //区域标识
                String marketPlace = data.getArea();

                BigDecimal price = PriceChangeUtils.changePrice(BigDecimal.valueOf(bean.getCostUnitPrice()), changePrice, type, priceType);


                /**
                 * 计算修改后的商品价格
                 */
                double newPrice = ComputedFreightPriceUtil.changePriceRule(new BigDecimal(bean.getWeight()), price, data.getArea(), uid).doubleValue();
                bean.setCostUnitPrice(newPrice);
                if (bean.getProductVariants() != null) {
                    for (int j = 0; j < bean.getProductVariants().size(); j++) {
                        BigDecimal vaciantsPrice = PriceChangeUtils.changePrice(BigDecimal.valueOf(bean.getProductVariants().get(j).getPrice()), changePrice, type, priceType);
                        double lastVarintPrice = ComputedFreightPriceUtil.changePriceRule(new BigDecimal(bean.getWeight()), vaciantsPrice, data.getArea(), uid).doubleValue();
                        bean.getProductVariants().get(j).setPrice(lastVarintPrice);
                    }
                }
                goodsProduct.add(bean);
                amazonService.changeProductPrice(atoken, token, serrId, marketPlace, bean, goodsProduct, data, price);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("", "修改价格异常");
        }
        return R.ok("", "改价中");

    }


}
