package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.goods.service.ProductVariantService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：产品变体
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-10 09:30:46
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/productvariant" )
@Api(value = "productvariant", tags = "产品变体管理")
public class ProductVariantController {

    private final  ProductVariantService productVariantService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param productVariant 产品变体
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProductVariantPage(Page page, ProductVariant productVariant) {
        return R.ok(productVariantService.page(page, Wrappers.query(productVariant)));
    }


    /**
     * 通过id查询产品变体
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(productVariantService.getById(id));
    }

    /**
     * 新增产品变体
     * @param productVariant 产品变体
     * @return R
     */
    @ApiOperation(value = "新增产品变体", notes = "新增产品变体")
    @SysLog("新增产品变体" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_productvariant_add')" )
    public R save(@RequestBody ProductVariant productVariant) {
        return R.ok(productVariantService.save(productVariant));
    }

    /**
     * 修改产品变体
     * @param productVariant 产品变体
     * @return R
     */
    @ApiOperation(value = "修改产品变体", notes = "修改产品变体")
    @SysLog("修改产品变体" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_productvariant_edit')" )
    public R updateById(@RequestBody ProductVariant productVariant) {
        return R.ok(productVariantService.updateById(productVariant));
    }

    /**
     * 通过id删除产品变体
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除产品变体", notes = "通过id删除产品变体")
    @SysLog("通过id删除产品变体" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_productvariant_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(productVariantService.removeById(id));
    }

}
