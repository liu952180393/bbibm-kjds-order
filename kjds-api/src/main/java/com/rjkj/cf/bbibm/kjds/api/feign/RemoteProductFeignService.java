package com.rjkj.cf.bbibm.kjds.api.feign;

import com.rjkj.cf.bbibm.kjds.api.entity.*;
import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * @描述：商户内部接口
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/9 15:41
 **/
@FeignClient(contextId = "remoteProductFeignService", value = Const.PRODUCT_SERVICE_NAME)
public interface RemoteProductFeignService {


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
     * 商品信息拉取 接口对接
     *
     * @param id
     * @param from
     * @return
     */
    @PostMapping(value = "/product/queryByVartionInfo")
    R productVariantUpload(@RequestParam("id") String id,
                           @RequestHeader(SecurityConstants.FROM) String from);

    /**
     *上架已下架的商品
     * @param goodsProducts
     */
    @PostMapping(value = "/addItem/relistItem")
    void relistItem(@RequestBody List<GoodsProduct> goodsProducts);


    /**
     * 商品上传接口对接
     *
     * @param goodsProduct 商品信息列表
     * @return
     */
    @PostMapping(value = "/addItem/addItemToMall")
    R addItem(@RequestBody List<GoodsProduct> goodsProduct);


    /**
     * 根据商品ids获取商品信息
     *
     * @param
     * @return
     */
    @PostMapping(value = "/product/queryByProductIds")
    R<List<Product>> queryByProductIds(@RequestParam("ids") String ids);


    /**
     * 新增爬取商品信息
     *
     * @param tesss
     * @return
     */
    @PostMapping(value = "/product/insertIntoProduct")
    R insertIntoProduct(@RequestBody String tesss);


    /**
     * 根据商品ids删除redis记录
     *
     * @param ids
     * @return
     */
    @PostMapping(value = "/product/deleteRedisGoodsId")
    R deleteRedisGoodsId(@RequestBody List<String> ids);


    /**queryAllRoyaltyInfo
     * 查询供应商的全部提款金额
     * @param userId
     * @return
     */
    @PostMapping(value = "/supplierroyaltyappove/queryAllRoyaltyInfo")
    R<SupplierInfoVo> queryAllRoyaltyInfo(@RequestParam(value = "userId") String userId);


    /**
     * 根据sku获取商品信息
     * @return
     */
    @PostMapping(value = "/product/getProductBySku")
    R<Product> getProductBySku(@RequestParam(value = "sku") String sku);

    /**
     * 根据sku获取商品信息
     * @return
     */
    @PostMapping(value = "/product/getProductByVsku")
    R<Product> getProductByVsku(@RequestParam(value = "sku") String sku);

    /**
     * 根据变体SKU查询变体商品价格
     * @return
     */
    @PostMapping(value = "/product/getVProducPricetByVsku")
    R<BigDecimal> getVProducPricetByVsku(@RequestParam(value = "sku") String sku);

    /**
     * 保存系统消息
     *
     * @param notice
     * @param from
     * @return
     */
    @PostMapping(value = "/notice/saveInfo")
    R saveInfo(@RequestBody Notice notice,
                           @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * 根据区域查询运费模板信息
     * @param area
     * @return
     */
    @PostMapping(value = "/pricingrules/queryPriceInfoOne")
    PricingRules queryPriceInfoOne(@RequestParam(value = "area") String area);

    /**
     * 通过区域查询汇率信息
     * @param area
     * @return
     */
    @PostMapping(value = "/rate/queryByRateOne")
    BigDecimal queryByRateOne(@RequestParam(value = "area") String area);

    /**
     * 根据商品ID翻译商品信息，存入数据库
     * @param productId
     * @return
     */
    @PostMapping(value = "/product/translationProductInfo")
    R translationProductInfo(@RequestParam(value = "productId") String productId);


    /**
     * 测试feign
     * @param s
     * @param id 商品的id
     * @return
     */
    @PostMapping(value = "/product/testRestGet")
    R testFeign(String s, @RequestParam(value = "id") String id);

    /**
     * 测试TM
     *
     * @param from
     * @return
     */
    @PostMapping(value = "/product/testTran")
    R testTran(@RequestHeader(SecurityConstants.FROM) String from);
}
