package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.*;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsProductRsp;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsProductService;
import com.rjkj.cf.bbibm.kjds.goods.service.ProductVariantService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * @描述：商户产品表
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-08 17:54:19
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/goodsproduct")
@Api(value = "goodsproduct", tags = "商户产品表管理")
public class GoodsProductController {

    private final GoodsProductService goodsProductService;
    private final ProductVariantService productVariantService;
    private final RemoteProductFeignService remoteProductFeignService;

    /**
     * 分页查询
     *
     * @param page         分页对象
     * @param goodsProduct 商户产品表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getGoodsProductPage(Page page, GoodsProduct goodsProduct) {
        RjkjUser user = SecurityUtils.getUser();
        IPage page1 = goodsProductService.listGoodsProduct(user, page, goodsProduct);

        return R.ok();
    }


    /**
     * 通过id查询商户产品表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    @ApiImplicitParam(name = "id", value = "产品id", required = true, paramType = "String")
    public R getById(@PathVariable("id") String id) {
        return R.ok(goodsProductService.getById(id));
    }

///    /**
//     * 新增商户产品表
//     * @param goodsProduct 商户产品表
//     * @return R
//     */
//    @ApiOperation(value = "新增商户产品表", notes = "新增商户产品表")
//    @SysLog("新增商户产品表" )
//    @PostMapping
//    @PreAuthorize("@pms.hasPermission('goods_goodsproduct_add')" )
//    public R save(@RequestBody GoodsProduct goodsProduct) {
//        return R.ok(goodsProductService.save(goodsProduct));
//    }

    /**
     * 拉取产品到商户产品中
     *
     * @return R
     */
    @ApiOperation(value = "修改商户产品表", notes = "修改商户产品表")
    @SysLog("修改商户产品表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_goodsproduct_edit')")
    public R updateById(@RequestBody GoodsProduct goodsProduct) {
        return R.ok(goodsProductService.updateById(goodsProduct));
    }


    /**
     * 添加到商户产品库中
     *
     * @return R
     */
    @ApiOperation(value = "添加到商户产品库中", notes = "添加到商户产品库中")
    @SysLog("添加到商户产品库中")
    @ApiImplicitParam(name = "pid", value = "产品id", required = true, paramType = "String")
    @PostMapping(value = "extract")
    public R extractProductToGoods(String pid) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            int result = goodsProductService.extractProduct(pid, user);
            if (result==2) {
                return R.failed("添加失败，您还没有店铺，先去申请一个吧");
            }
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
        return R.ok();
    }

    /**
     * 通过id删除商户产品表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商户产品表", notes = "通过id删除商户产品表")
    @SysLog("通过id删除商户产品表")
    @DeleteMapping("/{id}")
    @ApiImplicitParam(name = "id", value = "产品id", required = true, paramType = "String")
    public R removeById(@PathVariable String id) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            goodsProductService.deleteProduct(id, user);
            return R.ok("", "删除成功");
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }


    /**
     * 一键改价
     *
     * @return R
     */
    @ApiOperation(value = "app一键改价", notes = "app一键改价")
    @SysLog("app一键改价")
    @PostMapping("change/price")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "产品ids多个产品id用英文,分割", required = true, paramType = "String"),
            @ApiImplicitParam(name = "changePrice", value = "调整值 ", required = true),
            @ApiImplicitParam(name = "priceType", value = "金额调整类型（1涨价 2降价）", required = true, paramType = "int"),
            @ApiImplicitParam(name = "type", value = "调整类型（1百分比 2固定值）", required = true, paramType = "int")
    })
    public R changePrice(String ids, BigDecimal changePrice, int priceType, int type) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            goodsProductService.extractProduct(ids, user);
            goodsProductService.changePrice(user, ids, changePrice, priceType, type);
            return R.ok("", "改价成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }


    /**
     * 一键改价
     *
     * @return R
     */
    @ApiOperation(value = "app修改产品单价", notes = "app修改产品单价")
    @SysLog("app修改产品单价")
    @PostMapping("change/unit/price")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "产品id", required = true, paramType = "String"),
            @ApiImplicitParam(name = "changePrice", value = "调整比列值 ", required = true)
    })
    public R changeUnitPrice(String id, BigDecimal changePrice) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            goodsProductService.extractProduct(id, user);
            goodsProductService.changeUnitPrice(user, id, changePrice);
            return R.ok("", "改价成功");
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }


    /**
     * 商户平台产品
     *
     * @return R
     */
    @ApiOperation(value = "已上架", notes = "已上架")
    @SysLog("已上架")
    @PostMapping("list/goods/product")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "平台id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "gid", value = "店铺id", required = false, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "需要查询的商品状态（0已上架，1已下架，4上架中，5上架失败，多个状态逗号分隔）", required = false, dataType = "String")
    })
    public R<IPage<GoodsProductRsp>> goodsProductPlatformById(Page page, String pid, String gid,String status) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            IPage<GoodsProductRsp> goodsProductRsps = goodsProductService.goodsProductPlatformById(page, user, pid, gid,status);
            return R.ok(goodsProductRsps, "已上架");
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }


    /**
     * 商户平台产品（已下架产品）
     *
     * @return R
     */
    @ApiOperation(value = "已下架", notes = "已下架")
    @SysLog("已下架")
    @PostMapping("list/goods/lowershelfproduct")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "平台id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "gid", value = "店铺id", required = false, dataType = "String")

    })
    public R<IPage<GoodsProductRsp>> lowershelfproduct(Page page, String pid, String gid) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            IPage<GoodsProductRsp> goodsProductRsps = goodsProductService.goodsProductlowershelf(page, user, pid, gid);
            return R.ok(goodsProductRsps, "已下架");
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 待上架列表
     *
     * @return R
     */
    @ApiOperation(value = "待上架列表", notes = "待上架列表")
    @SysLog("待上架列表")
    @PostMapping("list/wait/shelf")
    @ApiImplicitParam(name = "gid", value = "店铺id", required = false, dataType = "String")
    public R<IPage<GoodsProductRsp>> listWaitShelf(Page page, String gid) {
        try {

            RjkjUser user = SecurityUtils.getUser();
            IPage<GoodsProductRsp> listWaitShelf = goodsProductService.listWaitShelf(page, user, gid);
            return R.ok(listWaitShelf);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 我的产品回收库列表
     *
     * @return R
     */
    @ApiOperation(value = "我的产品回收库列表", notes = "我的产品回收库列表")
    @SysLog("我的产品回收库列表")
    @PostMapping("list/recyclingLibrary")
    public R<IPage<GoodsProductRsp>> recyclingLibrary(Page page) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            return R.ok(goodsProductService.recyclingLibraryList(page,user));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 移除回收库商品，放到待上架列表
     *
     * @return R
     */
    @ApiOperation(value = "移除回收库商品，放到待上架列表", notes = "移除回收库商品，放到待上架列表")
    @SysLog("移除回收库商品，放到待上架列表")
    @PostMapping("list/removeLibrary")
    public R removeLibrary(String ids) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            goodsProductService.removeLibrary(ids,user);
            return R.ok(true,"操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据商品ID删除商品
     *
     * @return R
     */
    @ApiOperation(value = "根据商品ID删除商品", notes = "根据商品ID删除商品")
    @SysLog("根据商品ID删除商品")
    @PostMapping("deleteProduct")
    public R deleteProduct(String productId, String userId, String gid, String area) {
        try {
            List<GoodsProduct> list = goodsProductService.list(Wrappers.<GoodsProduct>query().lambda().eq(GoodsProduct::getId, productId).eq(GoodsProduct::getUid, userId)
                    .eq(GoodsProduct::getGid, gid).eq(GoodsProduct::getArea, area));
            for (GoodsProduct goodsProduct : list) {
                //判断当前商品是否需要删除变体信息
                List<GoodsProduct> list1 = goodsProductService.list(Wrappers.<GoodsProduct>query().lambda().eq(GoodsProduct::getId, goodsProduct.getId())
                        .eq(GoodsProduct::getUid, userId)
                        .notIn(GoodsProduct::getGid,gid)
                        .in(GoodsProduct::getRackStatus, 0,1,4,5,6));
                if (list1.size()<1) {
                    productVariantService.remove(Wrappers.<ProductVariant>query().lambda()
                            .eq(ProductVariant::getParentSku, goodsProduct.getSku())
                            .eq(ProductVariant::getUid, userId));
                }
            }
            return R.ok(goodsProductService.remove(Wrappers.<GoodsProduct>query().lambda().eq(GoodsProduct::getId, productId).eq(GoodsProduct::getUid, userId)
                    .eq(GoodsProduct::getGid, gid).eq(GoodsProduct::getArea, area)));
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * app上传店铺
     *
     * @return R
     */
    @ApiOperation(value = "app上传店铺", notes = "app上传店铺")
    @SysLog("app上传店铺")
    @PostMapping("upload/goods/product")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "产品ids多个产品id用英文,分割", required = true, paramType = "String"),
            @ApiImplicitParam(name = "gid", value = "店铺id ", required = true, paramType = "String")

    })
    public R uploadGoodsProduct(Authentication token, String ids, String gid) {
        try {
            System.out.println("----------------------app上传店铺----------------------");
            RjkjUser user = SecurityUtils.getUser();
            goodsProductService.extractProduct(ids, user);
            remoteProductFeignService.translationProductInfo(ids);
            Arrays.stream(gid.split(",")).forEach(item -> {
                goodsProductService.uploadGoodsProduct(token, user, ids, item);
            });
            return R.ok("", "上架中");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 商品上传失败后重新上传
     *
     * @return R
     */
    @ApiOperation(value = "商品上传失败后重新上传", notes = "商品上传失败后重新上传")
    @SysLog("商品上传失败后重新上传")
    @PostMapping("upload/goods/failAfterUpload")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "jsonArray", value = "店铺id和商品id的json", required = true, paramType = "String"),
    })
    public R failAfterUpload(@RequestBody JSONArray jsonArray,Authentication token) {
        try {
            if(jsonArray.size() == 0){
                throw new RuntimeException("上传的数据不能为空");
            }
            System.out.println("----------------------商品上传失败后重新上传----------------------");
            RjkjUser user = SecurityUtils.getUser();
            for (Object gids : jsonArray) {
                LinkedHashMap gids1 = (LinkedHashMap) gids;
                String gid = (String) gids1.get("gid");
                String ids = (String)gids1.get("ids");
                goodsProductService.uploadGoodsProduct(token, user, ids, gid);
            }
            return R.ok("", "上架中");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("发生错误");
        }
    }


    /**
     * app上架商品到店铺
     *
     * @return R
     */
    @ApiOperation(value = "app上架商品到店铺", notes = "app上架商品到店铺")
    @SysLog("app上架商品到店铺")
    @PostMapping("upper/goods/product")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "产品ids多个产品id用英文,分割", required = true, paramType = "String"),
            @ApiImplicitParam(name = "gid", value = "店铺id ", required = true, paramType = "String")

    })
    public R upperGoodsProduct(Authentication token, String ids, String gid) {
        try {
            System.out.println("----------------------app上架商品到店铺----------------------");
            RjkjUser user = SecurityUtils.getUser();
//            goodsProductService.extractProduct(ids, user);
//            remoteProductFeignService.translationProductInfo(ids);
            Arrays.stream(gid.split(",")).forEach(item -> {
                goodsProductService.upperGoodsProduct(token, user, ids, item);
            });
            return R.ok("", "上架中");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 上传成功后回调
     * //     * @param pid 产品id
     * //     * @param gid 店铺id （现在未用）
     * //     * @param errorMsg 错误消息
     * //     * @param itemId 上传成功后的店铺id
     * //     * @param rackStatus (上架状态0 上架成功 5 上架失败)
     *
     * @return
     */
    @PostMapping(value = "/upload/callback")
    @Inner(value = false)
    R goodsProductCallBack(@RequestBody CallBackVo callBackVo) {
        try {
//            RjkjUser user = SecurityUtils.getUser();
            this.goodsProductService.updateByGid(callBackVo.getUid(), callBackVo.getPid(), callBackVo.getGid(), callBackVo.getErrorMsg(), callBackVo.getItemId(), callBackVo.getRackStatus());
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }

    }



    /**
     * 修改商品价格后回调方法
     //     * @param pid 产品id
     //     * @param gid 店铺id （现在未用）
     //     * @param price 价格
     * @return
     */
    @PostMapping(value = "/updatePriceInfo")
    @Inner(value = false)
    R updatePriceInfo(@RequestBody CallPriceVo callPriceVo){
        try {
            RjkjUser user = SecurityUtils.getUser();
            this.goodsProductService.updatePrice(user,callPriceVo.getPid(),callPriceVo.getGid(),callPriceVo.getPrice());
            return R.ok();
        }catch (Exception e){
            e.printStackTrace();
            return R.failed(e.getMessage());
        }

    }

    /**
     * 获取产品根据id
     *
     * @return
     */
    @PostMapping(value = "/get/product/one")
    @Inner(value = false)
    R<GoodsProduct> getGoodsProductById(@RequestParam(value = "pid", required = false) String pid,
                                        @RequestParam("gid") String gid,@RequestParam("uid") String uid) {
        try {
            List<GoodsProduct> productList = this.goodsProductService.list(Wrappers.<GoodsProduct>query().lambda()
                    .eq(GoodsProduct::getUid, uid)
                    .eq(GoodsProduct::getId, pid)
                    .eq(GoodsProduct::getGid, gid));
            GoodsProduct goodsProduct = productList.get(0);
            goodsProduct.setProductVariants(this.productVariantService.list(Wrappers.<ProductVariant>query().lambda()
                    .eq(ProductVariant::getParentSku, goodsProduct.getSku())
                    .eq(ProductVariant::getUid, uid)));
            return R.ok(goodsProduct);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }


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
                       @RequestParam(value = "changePrice", required = true) BigDecimal changePrice) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            BigDecimal price = this.goodsProductService.updatePriceByGid(user, pid, gid, type, priceType, area, changePrice);
            return R.ok(price);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 修改我的产品库品牌
     *
     * @return
     */
    @ApiOperation(value = "修改我的产品库品牌", notes = "修改我的产品库品牌")
    @SysLog("修改我的产品库品牌")
    @PostMapping("/updateBrand")
    public R updateBrand(String userId, String productId, String brand) {
        try {
            if (StringUtils.isBlank(userId)) {
                throw new RuntimeException("用户ID不能为空");
            }
            if (StringUtils.isBlank(productId)) {
                throw new RuntimeException("商品ID不能为空");
            }
            if (StringUtils.isBlank(brand)) {
                throw new RuntimeException("品牌名称不能为空");
            }
            int i = goodsProductService.updateBrand(userId, productId, brand);
            if (i > 0) {
                return R.ok("","修改成功");
            }else {
                return R.failed("","修改失败");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return R.failed("","修改失败");
        }
    }
}
