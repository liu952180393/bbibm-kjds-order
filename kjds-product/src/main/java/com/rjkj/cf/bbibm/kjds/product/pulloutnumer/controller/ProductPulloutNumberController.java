package com.rjkj.cf.bbibm.kjds.product.pulloutnumer.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.product.pulloutnumer.entity.ProductPulloutNumber;
import com.rjkj.cf.bbibm.kjds.product.pulloutnumer.service.ProductPulloutNumberService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：商品上架次数
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-11-01 10:37:18
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/productpulloutnumber" )
@Api(value = "productpulloutnumber", tags = "商品上架次数管理")
public class ProductPulloutNumberController {

    private final  ProductPulloutNumberService productPulloutNumberService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param productPulloutNumber 商品上架次数
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProductPulloutNumberPage(Page page, ProductPulloutNumber productPulloutNumber) {
        return R.ok(productPulloutNumberService.page(page, Wrappers.query(productPulloutNumber)));
    }


    /**
     * 通过id查询商品上架次数
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(productPulloutNumberService.getById(id));
    }

    /**
     * 新增商品上架次数
     * @param productPulloutNumber 商品上架次数
     * @return R
     */
    @ApiOperation(value = "新增商品上架次数", notes = "新增商品上架次数")
    @SysLog("新增商品上架次数" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('pulloutnumer_productpulloutnumber_add')" )
    public R save(@RequestBody ProductPulloutNumber productPulloutNumber) {
        return R.ok(productPulloutNumberService.save(productPulloutNumber));
    }

    /**
     * 修改商品上架次数
     * @param productPulloutNumber 商品上架次数
     * @return R
     */
    @ApiOperation(value = "修改商品上架次数", notes = "修改商品上架次数")
    @SysLog("修改商品上架次数" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('pulloutnumer_productpulloutnumber_edit')" )
    public R updateById(@RequestBody ProductPulloutNumber productPulloutNumber) {
        return R.ok(productPulloutNumberService.updateById(productPulloutNumber));
    }

    /**
     * 通过id删除商品上架次数
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商品上架次数", notes = "通过id删除商品上架次数")
    @SysLog("通过id删除商品上架次数" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('pulloutnumer_productpulloutnumber_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(productPulloutNumberService.removeById(id));
    }

}
