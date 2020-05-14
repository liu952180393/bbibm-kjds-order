package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsOrder;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.CommissionDetailsVo;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsOrderService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;


/**
 *@描述：店铺申请订单信息
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-17 15:35:49
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/goodsorder" )
@Api(value = "goodsorder", tags = "店铺申请订单信息管理")
public class GoodsOrderController {

    private final  GoodsOrderService goodsOrderService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param goodsOrder 店铺申请订单信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getGoodsOrderPage(Page page, GoodsOrder goodsOrder) {
        return R.ok(goodsOrderService.page(page, Wrappers.query(goodsOrder)));
    }


    /**
     * 通过id查询店铺申请订单信息
     * @param oid id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{oid}" )
    public R getById(@PathVariable("oid" ) String oid) {
        return R.ok(goodsOrderService.getById(oid));
    }

    /**
     * 通过id查询店铺申请订单信息
     * @param oid id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @PostMapping("/getGoodsOrderById" )
    @Inner(value = false)
    public R getGoodsOrderById(@RequestParam String oid) {
        return R.ok(goodsOrderService.getById(oid));
    }

    /**
     * 新增店铺申请订单信息
     * @param goodsOrder 店铺申请订单信息
     * @return R
     */
    @ApiOperation(value = "新增店铺申请订单信息", notes = "新增店铺申请订单信息")
    @SysLog("新增店铺申请订单信息" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_goodsorder_add')" )
    public R save(@RequestBody GoodsOrder goodsOrder) {
        return R.ok(goodsOrderService.save(goodsOrder));
    }

    /**
     * 修改店铺申请订单信息
     * @param goodsOrder 店铺申请订单信息
     * @return R
     */
    @ApiOperation(value = "修改店铺申请订单信息", notes = "修改店铺申请订单信息")
    @SysLog("修改店铺申请订单信息" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_goodsorder_edit')" )
    public R updateById(@RequestBody GoodsOrder goodsOrder) {
        return R.ok(goodsOrderService.updateById(goodsOrder));
    }

    /**
     * 通过id删除店铺申请订单信息
     * @param oid id
     * @return R
     */
    @ApiOperation(value = "通过id删除店铺申请订单信息", notes = "通过id删除店铺申请订单信息")
    @SysLog("通过id删除店铺申请订单信息" )
    @DeleteMapping("/{oid}" )
    @PreAuthorize("@pms.hasPermission('goods_goodsorder_del')" )
    public R removeById(@PathVariable String oid) {
        return R.ok(goodsOrderService.removeById(oid));
    }

    /**
     * 通过订单id修改当前订单状态为已付款
     * @param orderNo orderNo
     * @return R
     */
    @ApiOperation(value = "通过订单id修改当前订单状态为已付款", notes = "通过订单id修改当前订单状态为已付款")
    @SysLog("通过订单id修改当前订单状态为已付款" )
    @PostMapping("/updateByOrderId")
    @Inner(value = false)
    public void updateByOrderId(@RequestParam String orderNo){
        try {
            goodsOrderService.updateByOrderId(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("发生异常："+e.getMessage());
        }
    }

    /**
     * 获取当前用户提成明细
     * @param type type
     * @return R
     */
    @ApiImplicitParam(name = "type", value = "类型（2、商家推荐提成 5、城市合伙人提成）", required = true, dataType = "String")
    @SysLog("获取当前用户提成明细" )
    @PostMapping("/getCommissionDetails")
    public R<ArrayList<CommissionDetailsVo>> getCommissionDetails(Page page, String type){
        RjkjUser user = SecurityUtils.getUser();
        return R.ok(goodsOrderService.getCommissionDetails(page,user,type));
    }
}
