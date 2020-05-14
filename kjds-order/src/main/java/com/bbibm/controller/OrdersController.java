package com.bbibm.controller;

import com.bbibm.entity.Orders;
import com.bbibm.service.OrdersService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



/**
 *@描述：商品表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-13 14:36:24
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/orders" )
@Api(value = "orders", tags = "商品表管理")
public class OrdersController {

    private final OrdersService ordersService;

//    /**
//     * 分页查询
//     * @param page 分页对象
//     * @param orders 商品表
//     * @return
//     */
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    @GetMapping("/page" )
//    public R getOrdersPage(Page page, Orders orders) {
//        return R.ok(ordersService.page(page, Wrappers.query(orders)));
//    }


    /**
     * 通过id查询商品表
     * @param orderId id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{orderId}" )
    public R getById(@PathVariable("orderId" ) Integer orderId) {
        return R.ok(ordersService.getById(orderId));
    }

    /**
     * 新增商品表
     * @param orders 商品表
     * @return R
     */
    @ApiOperation(value = "新增商品表", notes = "新增商品表")
    @SysLog("新增商品表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('generator_orders_add')" )
    public R save(@RequestBody Orders orders) {
        return R.ok(ordersService.save(orders));
    }

//    /**
//     * 修改商品表
//     * @param orders 商品表
//     * @return R
//     */
//    @ApiOperation(value = "修改商品表", notes = "修改商品表")
//    @SysLog("修改商品表" )
//    @PutMapping
//    @PreAuthorize("@pms.hasPermission('generator_orders_edit')" )
//    public R updateById(@RequestBody Orders orders) {
//        return R.ok(ordersService.updateById(orders));
//    }
//
//    /**
//     * 通过id删除商品表
//     * @param orderId id
//     * @return R
//     */
//    @ApiOperation(value = "通过id删除商品表", notes = "通过id删除商品表")
//    @SysLog("通过id删除商品表" )
//    @DeleteMapping("/{orderId}" )
//    @PreAuthorize("@pms.hasPermission('generator_orders_del')" )
//    public R removeById(@PathVariable Integer orderId) {
//        return R.ok(ordersService.removeById(orderId));
//    }

}
