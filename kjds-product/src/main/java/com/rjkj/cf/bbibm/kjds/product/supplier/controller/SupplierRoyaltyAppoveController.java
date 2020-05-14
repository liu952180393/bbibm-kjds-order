package com.rjkj.cf.bbibm.kjds.product.supplier.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.SupplierInfoVo;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.SupplierRoyaltyAppove;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.SupplierRoyaltyAppoveService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


/**
 *@描述：供应商提成信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-11 11:39:06
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/supplierroyaltyappove" )
@Api(value = "supplierroyaltyappove", tags = "供应商提成信息管理")
public class SupplierRoyaltyAppoveController {

    private final SupplierRoyaltyAppoveService supplierRoyaltyAppoveService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param supplierRoyaltyAppove 供应商提成信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getSupplierRoyaltyAppovePage(Page page, SupplierRoyaltyAppove supplierRoyaltyAppove) {
        RjkjUser user = SecurityUtils.getUser();
        return R.ok(supplierRoyaltyAppoveService.page(page, Wrappers.query(supplierRoyaltyAppove).lambda()
                .eq(SupplierRoyaltyAppove::getSupplierId,user.getId())));
    }


    /**
     * 通过id查询供应商提成信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(supplierRoyaltyAppoveService.getById(id));
    }

    /**
     * 新增供应商提成信息
     * @param supplierRoyaltyAppove 供应商提成信息
     * @return R
     */
    @ApiOperation(value = "新增供应商提成信息", notes = "新增供应商提成信息")
    @SysLog("新增供应商提成信息" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('supplier_supplierroyaltyappove_add')" )
    public R save(@RequestBody SupplierRoyaltyAppove supplierRoyaltyAppove) {
        return R.ok(supplierRoyaltyAppoveService.save(supplierRoyaltyAppove));
    }

    /**
     * 修改供应商提成信息
     * @param supplierRoyaltyAppove 供应商提成信息
     * @return R
     */
    @ApiOperation(value = "修改供应商提成信息", notes = "修改供应商提成信息")
    @SysLog("修改供应商提成信息" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('supplier_supplierroyaltyappove_edit')" )
    public R updateById(@RequestBody SupplierRoyaltyAppove supplierRoyaltyAppove) {
        return R.ok(supplierRoyaltyAppoveService.updateById(supplierRoyaltyAppove));
    }

    /**
     * 通过id删除供应商提成信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除供应商提成信息", notes = "通过id删除供应商提成信息")
    @SysLog("通过id删除供应商提成信息" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('supplier_supplierroyaltyappove_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(supplierRoyaltyAppoveService.removeById(id));
    }


    /**
     * 查询供应商的全部提款金额
     * @param userId
     * @return
     */
    @ApiOperation(value = "查询供应商的全部提款金额", notes = "查询供应商的全部提款金额")
    @SysLog("查询供应商的全部提款金额" )
    @PostMapping("/queryAllRoyaltyInfo" )
    @Inner(value = false)
    public R<SupplierInfoVo> queryAllRoyaltyInfo(String userId){

            SupplierInfoVo bean=new SupplierInfoVo();
        try {

            BigDecimal bigDecimalAll = supplierRoyaltyAppoveService.queryAllSupplierCount(userId);
            BigDecimal bigDecimalToday = supplierRoyaltyAppoveService.queryTodayAllSupplierCount(userId);
            bean.setCommissionType(3);
            bean.setCommissionTotal(bigDecimalAll);
            bean.setCommissionNowadays(bigDecimalToday);

        }catch (Exception e){
            e.printStackTrace();
        }
        return R.ok(bean);
    }

}
