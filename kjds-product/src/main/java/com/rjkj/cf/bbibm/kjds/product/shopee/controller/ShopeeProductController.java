package com.rjkj.cf.bbibm.kjds.product.shopee.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.bbibm.kjds.api.entity.Goods;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.PriceChangeUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.ShopeeConstant;
import com.rjkj.cf.bbibm.kjds.api.utils.ShopeeUtils;
import com.rjkj.cf.bbibm.kjds.product.shopee.service.ShopeeProductService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;


/**
 * @描述：Shopee商城商品管理
 * @项目：跨境电商
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-09 14:55:08
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/shopeeProduct")
@Api(value = "shopeeProduct", tags = "Shopee商城产品管理")
@Slf4j
public class ShopeeProductController {
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final ShopeeProductService shopeeProductService;
    /**
     * Shopee根据商品id下架商品
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据产品id下架商品", notes = "根据产品id下架商品")
    @SysLog("根据产品id下架商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pids", value = "产品ID,多个ID用 ',' 逗号分隔 注意和店铺ID一一对应", required = true, dataType = "String"),
            @ApiImplicitParam(name = "shopIds", value = "店铺ID,多个ID用 ',' 逗号分隔", required = true, dataType = "String")
    })
    @PostMapping("/endItem")
    public R endItem(Authentication authentication,String pids, String shopIds) {
        if (StringUtils.isEmpty(pids)) {
            throw new RuntimeException("产品id不能为空！");
        }
        if (StringUtils.isEmpty(shopIds)) {
            throw new RuntimeException("店铺id不能为空！");
        }
        RjkjUser user = SecurityUtils.getUser();
        String uid = user.getId();
        try {
            String[] pidsArr = pids.split(",");
            String[] shopIdsArr = shopIds.split(",");
            for (int i = 0; i < pidsArr.length; i++) {
                //根据店铺ID和产品ID获取唯一该店铺的唯一一条产品
                String pid = pidsArr[i];
                String sid = shopIdsArr[i];
                R<GoodsProduct> goodsProductById = remoteGoodsFeignService.getGoodsProductById(pid, sid,uid);
                GoodsProduct goodsProduct = goodsProductById.getData();
                if(null == goodsProduct){
                    remoteGoodsFeignService.deleteProduct(pid,uid,goodsProduct.getGid(),goodsProduct.getArea());
                    continue;
                }
                Integer rackStatus = goodsProduct.getRackStatus();
                //删除非上架成功的商品
                if(rackStatus != 0){
                    remoteGoodsFeignService.deleteProduct(pid,uid,goodsProduct.getGid(),goodsProduct.getArea());
                    continue;
                }
                String gid = goodsProduct.getGid();
                //获得该产品的ItemId
                String itemId = goodsProduct.getItemId();
                Goods goods = remoteGoodsFeignService.getGoodsById(gid).getData();
                //获得ShopId
                String shopId = goods.getAccountSiteId();
                //异步执行下架请求
                shopeeProductService.endItem(authentication,pid,gid,shopId,itemId,goods.getUid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(true, "操作成功");
    }

    /**
     * 根据Shopee商品id修改商品的价格
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据Shopee商品id修改商品的价格", notes = "根据Shopee商品id修改商品的价格")
    @SysLog("根据Shopee商品id修改商品的价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pids", value = "产品ID,多个ID用 ',' 逗号分隔,注意和店铺ID一一对应", required = true, dataType = "String"),
            @ApiImplicitParam(name = "shopIds", value = "店铺ID,多个ID用 ',' 逗号分隔", required = true, dataType = "String"),
            @ApiImplicitParam(name = "priceType", value = "金额调整类型（1涨价 2降价）", required = true, dataType = "String"),
            @ApiImplicitParam(name = "changePrice", value = "调整值", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "调整类型（1百分比 2固定值）", required = true, dataType = "String")
    })
    @PostMapping("/updatePrice")
    public R updatePrice(Authentication atoken,String pids, String shopIds, BigDecimal changePrice, int priceType, int type) {
        try {
            if(StringUtils.isBlank(pids)){
                throw new RuntimeException("商品ID不能为空。");
            }
            if(StringUtils.isBlank(shopIds)){
                throw new RuntimeException("店铺ID不能为空。");
            }
            String[] idsArr = pids.split(",");
            String[] shopIdsArr = shopIds.split(",");
            RjkjUser user = SecurityUtils.getUser();
            for (int i = 0; i < idsArr.length; i++) {
                //根据店铺ID和产品ID获取唯一该店铺的唯一一条产品
                String pid = idsArr[i];
                String sid = shopIdsArr[i];
               shopeeProductService.updatePrice(atoken,pid,sid,changePrice,priceType,type,user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(false, e.getMessage());
        }
        return R.ok(true, "价格修改中！");
    }
}
