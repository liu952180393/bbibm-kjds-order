package com.rjkj.cf.bbibm.kjds.api.feign;

import com.rjkj.cf.bbibm.kjds.api.entity.*;
import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @描述：商户内部接口
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/9 15:41
 **/
@FeignClient(contextId = "remoteGoodsFeignService", value = Const.GOOD_SERVICE_NAME)
public interface RemoteGoodsFeignService {


    /**
     * 翻译回调接口
     *
     * @param goodsProduct
     * @param from
     * @return
     */
    @PostMapping(value = "/goods/transaction/product/callback")
    R transactionCallBack(@RequestBody GoodsProduct goodsProduct,
                          @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 产品上传 接口对接
     *
     * @param goodsProduct
     * @param from
     * @return
     */
    @PostMapping(value = "/goods/product/variant/upload")
    R productVariantUpload(@RequestBody GoodsProduct goodsProduct,
                           @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 用户平台信息
     *
     * @return
     */
    @PostMapping(value = "/platform/user/info")
    R<List<PlatformInfoVo>> userPlatformInfo();


    /**
     * 根据id获取店铺信息
     *
     * @return
     */
    @PostMapping(value = "/goods/get/{id}")
    R<Goods> getGoodsById(@PathVariable("id") String id);

    /**
     * 根据店铺id或区域获取店铺信息
     *
     * @return
     */
    @PostMapping(value = "/goods/area/getinfo")
    R<Goods> getGoodsByIdAndArea(@RequestParam(value = "id") String id,
                                 @RequestParam(value = "area") String area);


    /**
     * 获取shoppid列表
     *
     * @return
     */
    @PostMapping(value = "/goods/user/shoppid/{pid}")
    R<List<ShoppeId>> userShoppid(@PathVariable("pid") String pid);


    /**
     * 获取产品根据id
     *
     * @return
     */
    @PostMapping(value = "/goodsproduct/get/product/one")
    R<GoodsProduct> getGoodsProductById(@RequestParam(value = "pid", required = false) String pid,
                                        @RequestParam("gid") String gid,
                                        @RequestParam("uid") String uid);


    /**
     * 获取用户店铺列表
     *
     * @return
     */
    @PostMapping(value = "/goods/get/list")
    R<List<Goods>> getGoodsList();


    /**
     * 店铺列表
     *
     * @return
     */
    @PostMapping("goods/app/list")
    R<List<PlatformGoodsRsp>> goodsList();

    /**
     * 根据用户ID获取用户下的店铺列表
     *
     * @return
     */
    @PostMapping("goods/getGoodsUserIdListByGoodsId")
    R<List<String>> getGoodsUserIdListByGoodsId(@RequestParam(value = "shopId") String shopId,
                                                   @RequestParam( value = "area") String area,
                                                   @RequestHeader(SecurityConstants.FROM) String from);
    /**
     * 根据用户ID获取用户下的店铺列表
     *
     * @return
     */
    @PostMapping("goods/getGoodsListByUserId")
    R<List<PlatformGoodsRsp>> getGoodsListByUserId(@RequestParam(value = "userId") String userId,
                                                   @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * 上传成功后回调
     *
     * @param pid        产品id
     * @param gid        店铺id （现在未用）
     * @param errorMsg   错误消息
     * @param itemId     上传成功后的店铺id
     * @param rackStatus (上架状态0 上架成功 5 上架失败)
     * @return
     */
    @PostMapping(value = "/goodsproduct/upload/callback")
    R goodsProductCallBack(@RequestBody CallBackVo callBackVo);


    /**
     * 内部修改价格接口
     *
     * @param pid         产品id
     * @param gid         店铺id
     * @param type        类型（1、百分比 2、固定值）
     * @param priceType   价格类型（1、上调，2、下调）
     * @param area        区域
     * @param changePrice 改变价格
     * @return
     */
    @PostMapping("/goodsproduct/change/price/gid")
    R changePriceByGid(@RequestParam(value = "pid", required = true) String pid,
                       @RequestParam(value = "gid", required = true) String gid,
                       @RequestParam(value = "type", required = true) int type,
                       @RequestParam(value = "priceType", required = true) int priceType,
                       @RequestParam(value = "area", required = false) String area,
                       @RequestParam(value = "changePrice", required = true) BigDecimal changePrice);


    /**
     * 店铺信息查询for订单(在redis中获取数据)
     *
     * @param type
     * @param from
     * @return
     */
    @PostMapping(value = "/goods/queryGoodsInfoByRedis")
    R<List<GoodsInfpForOrderVo>> queryGoodsInfoByRedis(@RequestParam(value = "type") String type,
                                                       @RequestHeader(SecurityConstants.FROM) String from);
    /**
     * 同步订单信息到Redis
     *
     * @param from
     * @return
     */
    @PostMapping(value = "/goods/syncGoodsInfoToRedis")
    R syncGoodsInfoToRedis(@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 支付成功后更改店铺为审核中状态
     *
     * @param from
     * @return
     */
    @PostMapping(value = "/goods/updateStatusToReview")
    R updateStatusToReview(@RequestParam(value = "gid") String gid, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 根据商品信息删除商品
     *
     * @param productId(商品id)
     * @param userId(用户id)
     * @param gid(店铺表的sid)
     * @param area(区域值)
     * @return
     */
    @PostMapping(value = "/goodsproduct/deleteProduct")
    R deleteProduct(@RequestParam(value = "productId") String productId,
                    @RequestParam(value = "userId") String userId,
                    @RequestParam(value = "gid") String gid,
                    @RequestParam(value = "area") String area);



    /**
     * 查询app最新的一条下载地址
     *
     * @param type(1为android 0为ios)
     * @param from
     * @return
     */
    @PostMapping(value = "/appupdate/getNewAppInfo")
    R<AppUpdate> getNewAppInfo(@RequestParam(value = "type") String type,
                               @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 通过订单id修改当前订单状态
     *
     * @param orderNo
     * @param from
     * @return
     */
    @PostMapping(value = "/goodsorder/updateByOrderId")
    R updateByOrderId(@RequestParam(value = "orderNo") String orderNo,
                      @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 根据ID查询支付订单信息
     *
     * @param from
     * @return
     */
    @PostMapping(value = "/goodsorder/getGoodsOrderById")
    R<GoodsOrder> getGoodsOrderById(@RequestParam(value = "oid") String oid,
                                    @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 通过订单id修改当前订单状态(城市合伙人)
     *
     * @param orderNo
     * @param from
     * @return
     */
    @PostMapping(value = "/partner/updateByPartnerState")
    R updateByPartnerState(@RequestParam(value = "orderNo") String orderNo,
                           @RequestHeader(SecurityConstants.FROM) String from);

}
