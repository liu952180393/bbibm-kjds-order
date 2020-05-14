package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsInfpForOrderVo;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformGoodsRsp;
import com.rjkj.cf.bbibm.kjds.api.entity.ShoppeId;
import com.rjkj.cf.bbibm.kjds.api.feign.RemotePayFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.ShopeeConstant;
import com.rjkj.cf.bbibm.kjds.api.utils.ShopeeUtils;
import com.rjkj.cf.bbibm.kjds.goods.entity.ApplyGoodsReq;
import com.rjkj.cf.bbibm.kjds.goods.entity.Goods;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.ShopApprovalVo;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @描述：商户模块
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-08 12:00:03
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/goods")
@Api(value = "goods", tags = "商户模块管理")
@Slf4j
public class GoodsController {
    private final GoodsService goodsService;
    private final RemotePayFeignService remotePayFeignService;
    private final Environment env;
    private final RedisTemplate redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;


    /**
     * 分页查询
     *
     * @param page  分页对象
     * @param goods 商户模块
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R<IPage<Goods>> getGoodsPage(Page page, Goods goods) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            IPage<Goods> goodsList = goodsService.listGoods(page, goods, user);
            return R.ok(goodsList);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据重定向连接和partnerKey生成授权链接
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据重定向连接和partnerKey生成授权链接", notes = "根据重定向连接和partnerKey生成授权链接")
    @SysLog("根据重定向连接和partnerKey生成授权链接")
    @GetMapping("/calToken")
    public R calToken(String randomStr) {
        try {
            String property = env.getProperty("spring.cloud.nacos.discovery.server-addr");
            String redirectLink = "http://" + property.split(":")[0] + ":8666/goods/goods/authCalback?randomStr=" + randomStr;
            String authKey = ShopeeUtils.calToken(redirectLink, ShopeeConstant.PARTNER_KEY);
            return R.ok("https://partner.shopeemobile.com/api/v1/shop/auth_partner?id=" + ShopeeConstant.PARTNER_ID + "&token=" + authKey + "&redirect=" + redirectLink + "");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("授权失败！");
        }
    }

    /**
     * authCalback
     */
    @ApiOperation(value = "authCalback", notes = "authCalback")
    @GetMapping("/authCalback")
    @Inner(value = false)
    public String authCalback(String randomStr, @RequestParam("shop_id") String shopId) {
        //初始化redis
        HashOperations hashOperations = redisTemplate.opsForHash();
        redisTemplate.expire("authInfo", 1, TimeUnit.HOURS);
        hashOperations.put("authInfo", randomStr, shopId);
        return "<script>window.close()</script>";
    }

    /**
     * getShopId
     */
    @ApiOperation(value = "getShopId", notes = "getShopId")
    @GetMapping("/getShopId")
    @CrossOrigin(allowCredentials = "true", allowedHeaders = "*", methods = {RequestMethod.GET,
            RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS,
            RequestMethod.HEAD, RequestMethod.PUT, RequestMethod.PATCH}, origins = "*")
    public R getShopId(String randomStr) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        boolean exist = hashOperations.hasKey("authInfo", randomStr);
        //判断Redis是否存在该条订单
        if (exist) {
            return R.ok(hashOperations.get("authInfo", randomStr).toString());
        }
        return R.failed("");
    }

    @PostMapping("/testTransaction")
    public void test() {
        try {
            goodsService.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取各状态下的店铺列表
     *
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "获取各状态下的店铺列表", notes = "获取各状态下的店铺列表")
    @GetMapping("/approvalpage")
    public R<IPage<Goods>> getApprovalGoodsPage(Page page, Goods goods) {
        try {
            return R.ok(goodsService.getApprovalGoodsPage(page, goods));
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }

    }

    /**
     * 通过id查询商户模块
     *
     * @param sid id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @ApiImplicitParam(name = "sid", value = "商户id", required = true, paramType = "String")
    @GetMapping("/{sid}")
    public R<Goods> getById(@PathVariable("sid") String sid) {
        try {
            return R.ok(goodsService.getById(sid));
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 新增商户模块
     *
     * @param goods 商户模块
     * @return R
     */
    @ApiOperation(value = "新增商户模块", notes = "新增商户模块")
    @SysLog("新增商户模块")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_goods_add')")
    public R save(@RequestBody Goods goods) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            goodsService.addGoods(user, goods);
            return R.ok();
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 更新同步店铺信息到redis
     *
     * @return R
     */
    @ApiOperation(value = "更新同步店铺信息到redis", notes = "更新店铺信息到redis")
    @SysLog("更新店铺信息到redis")
    @PostMapping("/syncGoodsInfoToRedis")
    @Inner(value = false)
    public R syncGoodsInfoToRedis() {
        try {
            goodsService.updateGoodsToRedis();
            return R.ok(true, "同步成功！");
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 修改商户模块
     *
     * @param goods 商户模块
     * @return R
     */
    @ApiOperation(value = "修改商户模块", notes = "修改商户模块")
    @SysLog("修改商户模块")
    @PutMapping
//    @PreAuthorize("@pms.hasPermission('goods_goods_edit')" )
    public R updateById(@RequestBody Goods goods) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            goodsService.updateGoods(user, goods);
            return R.ok();
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 通过id删除商户模块
     *
     * @param sid id
     * @return R
     */
    @ApiOperation(value = "通过id删除商户模块", notes = "通过id删除商户模块")
    @SysLog("通过id删除商户模块")
    @DeleteMapping("/{sid}")
    @ApiImplicitParam(name = "sid", value = "商户id", required = true, paramType = "String")
//    @PreAuthorize("@pms.hasPermission('goods_goods_del')" )
    public R removeById(@PathVariable String sid) {
        try {
            return R.ok(goodsService.removeById(sid));
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 通过id删除商户模块
     *
     * @return R
     */
    @ApiOperation(value = "app店铺列表", notes = "app店铺列表")
    @SysLog("app店铺列表")
    @PostMapping("/app/list")
    public R<List<PlatformGoodsRsp>> goodsList() {
        try {
            RjkjUser user = SecurityUtils.getUser();
            List<PlatformGoodsRsp> goodsRsps = goodsService.goodsList(user);
            return R.ok(goodsRsps);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据用户ID获取app店铺列表
     *
     * @return R
     */
    @ApiOperation(value = "根据userId获取app店铺列表", notes = "根据userId获取app店铺列表")
    @SysLog("根据userId获取app店铺列表")
    @PostMapping("/getGoodsListByUserId")
    @Inner(value = false)
    public R<List<PlatformGoodsRsp>> getGoodsListByUserId(@RequestParam(value = "userId") String userId) {
        try {
            return R.ok(goodsService.getGoodsListByUserId(userId));
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据ShopID和地址获取用户列表
     *
     * @return R
     */
    @ApiOperation(value = "根据ShopID和地址获取用户列表", notes = "根据ShopID和地址获取用户列表")
    @SysLog("根据ShopID和地址获取用户列表")
    @PostMapping("/getGoodsUserIdListByGoodsId")
    @Inner(value = false)
    public R<List<String>> getGoodsUserIdListByGoodsId(@RequestParam(value = "shopId") String shopId,
                                                       @RequestParam(value = "area") String area) {
        try {
            return R.ok(goodsService.getGoodsUserIdListByGoodsId(shopId, area));
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 获取shoppid
     *
     * @return R
     */
    @ApiOperation(value = "获取shoppid", notes = "获取shoppid")
    @SysLog("获取shoppid")
    @PostMapping(value = "user/shoppid/{pid}")
    R<List<ShoppeId>> userShoppid(@PathVariable("pid") String pid) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            List<ShoppeId> shopppeIds = goodsService.getShoppids(user, pid);
            return R.ok(shopppeIds);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 申请店铺
     *
     * @return R
     */
    @ApiOperation(value = "申请店铺", notes = "申请店铺")
    @SysLog("申请店铺")
    @PostMapping(value = "apply")
    R applyGoods(ApplyGoodsReq req) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            String oid = goodsService.applyGoods(user, req);
            R appPayCallClientParam = remotePayFeignService.getAppPayCallClientParam((req.getPayType() == 1 ? 2 : 1), oid);
            return appPayCallClientParam;
        } catch (Exception e) {
            log.info("error======================" + e.getMessage());
            return R.failed(e.getMessage());
        }
    }

    /**
     * 用户余额充值
     *
     * @return R
     */
    @ApiOperation(value = "余额充值", notes = "余额充值")
    @SysLog("余额充值")
    @PostMapping(value = "rechargeBalance")
    R rechargeBalance(String payType, BigDecimal amount) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            String oid = goodsService.addrechargeBalanceInfo(user, payType, amount);
            return remotePayFeignService.getAppPayCallClientParam((Integer.valueOf(payType) == 1 ? 2 : 1), oid);
        } catch (Exception e) {
            log.info("error======================" + e.getMessage());
            return R.failed(e.getMessage());
        }
    }


    /**
     * 支付成功后更改店铺为审核中状态
     *
     * @return R
     */
    @ApiOperation(value = "支付成功后更改店铺为审核中状态", notes = "支付成功后更改店铺为审核中状态")
    @SysLog("支付成功后更改店铺为审核中状态")
    @PostMapping(value = "updateStatusToReview")
    @Inner(value = false)
    R updateStatusToReview(@RequestParam(value = "gid") String gid) {
        try {
            return R.ok(goodsService.updateStatusToReview(gid));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("发生异常：" + e.getMessage());
        }
    }

    /**
     * 店铺同意审批
     *
     * @return R
     */
    @ApiOperation(value = "店铺同意审批", notes = "店铺同意审批")
    @SysLog("店铺同意审批")
    @PostMapping(value = "shopApproval")
    @Inner(value = false)
    R shopApproval(ShopApprovalVo shopApprovalVo) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            goodsService.shopApproval(shopApprovalVo, user);
            return R.ok(true, "操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
    }

    /**
     * 获取用户店铺列表
     *
     * @return
     */
    @PostMapping(value = "/get/list")
    @SysLog("获取用户店铺列表")
    R<List<com.rjkj.cf.bbibm.kjds.api.entity.Goods>> getGoodsList() {
        try {
            RjkjUser user = SecurityUtils.getUser();
            List<com.rjkj.cf.bbibm.kjds.api.entity.Goods> collect = goodsService.list(Wrappers.<Goods>query().lambda().eq(Goods::getUid, user.getId()))
                    .stream()
                    .map(item -> {
                        com.rjkj.cf.bbibm.kjds.api.entity.Goods goods = new com.rjkj.cf.bbibm.kjds.api.entity.Goods();
                        goods.setSid(item.getSid());
                        goods.setUid(item.getUid());
                        goods.setMwsToken(item.getMwsToken());
                        goods.setAccountSiteId(item.getAccountSiteId());
                        goods.setPid(item.getPid());
                        goods.setSid(item.getSid());
                        goods.setSname(item.getSname());
                        return goods;
                    }).collect(Collectors.toList());
            return R.ok(collect);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据id获取店铺信息
     *
     * @return
     */
    @PostMapping(value = "/get/{id}")
    @ApiOperation("根据id获取店铺信息")
    @SysLog("根据id获取店铺信息")
    @ApiImplicitParam(name = "id", value = "店铺id", required = true, paramType = "String")
    @Inner(value = false)
    R<com.rjkj.cf.bbibm.kjds.api.entity.Goods> getGoodsById(@PathVariable("id") String id) {
        try {
            Goods byId = this.goodsService.getById(id);
            com.rjkj.cf.bbibm.kjds.api.entity.Goods goods = new com.rjkj.cf.bbibm.kjds.api.entity.Goods();
            goods.setSid(byId.getSid());
            goods.setSname(byId.getSname());
            goods.setPid(byId.getPid());
            goods.setAccountSiteId(byId.getAccountSiteId());
            goods.setVpsAddr(byId.getVpsAddr());
            goods.setMwsToken(byId.getMwsToken());
            goods.setUid(byId.getUid());
            goods.setBankNum(byId.getBankNum());
            goods.setArea(byId.getArea());
            goods.setSecretKey(byId.getSecretKey());
            goods.setAwsAccessKeyId(byId.getAwsAccessKeyId());
            return R.ok(goods);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }

    }

    @GetMapping("/share/register")
    @SysLog("分享界面显示")
    @ApiOperation(value = "分享界面显示", notes = "分享界面显示")
    @Inner(value = false)
    public ModelAndView shareRegister(String code) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    /**
     * 根据店铺id或区域获取店铺信息
     *
     * @return
     */
    @PostMapping(value = "/area/getinfo")
    @SysLog("根据店铺id或区域获取店铺信息")
    public R<Goods> getGoodsByIdAndArea(@RequestParam(value = "id") String id,
                                        @RequestParam(value = "area") String area) {
        try {
            Goods one = this.goodsService.getOne(Wrappers.<Goods>query().lambda()
                    .eq(Goods::getAccountSiteId, id)
                    .eq(Goods::getArea, area));
            Goods goods = new Goods();
            goods.setArea(one.getArea());
            goods.setSid(one.getSid());
            goods.setMwsToken(one.getMwsToken());
            goods.setAccountSiteId(one.getAccountSiteId());
            goods.setSecretKey(one.getSecretKey());
            goods.setAwsAccessKeyId(one.getAwsAccessKeyId());
            return R.ok(goods);
        } catch (Exception e) {
            return R.failed(e.getMessage());
        }
    }


    /**
     * 根据redis中的坐标信息取订单查询的商城信息
     *
     * @return
     */
    @PostMapping("/queryGoodsInfoByRedis")
    @Inner(value = false)
    public R<List<GoodsInfpForOrderVo>> queryGoodsInfoByRedis(@RequestParam(value = "type") String type) {
        List<GoodsInfpForOrderVo> list = new ArrayList<GoodsInfpForOrderVo>();
        try {
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            HashOperations hashOperations = redisTemplate.opsForHash();
            Iterator iterator = hashOperations.keys(type).iterator();
            while (iterator.hasNext()) {
                GoodsInfpForOrderVo goodsVo = new GoodsInfpForOrderVo();
                String goodsInfo = iterator.next().toString();
                String[] goods = goodsInfo.split("_msg_");
                goodsVo.setShopId(goods[0]);
                goodsVo.setToken(goods[1]);
                goodsVo.setArea(goods[2]);
                list.add(goodsVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return R.ok(list);
    }
}
