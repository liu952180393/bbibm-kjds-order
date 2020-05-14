package com.rjkj.cf.bbibm.kjds.product.supplier.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariantsTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductVariantsTranslationService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



/**
 *@描述：商品翻译的变体信息
 *@项目：
 *@公司：软江科技
 *@作者：Yihao
 *@时间：2020-01-07 16:45:18
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/productvariantstranslation" )
@Api(value = "productvariantstranslation", tags = "商品翻译的变体信息管理")
public class ProductVariantsTranslationController {

    private final ProductVariantsTranslationService productVariantsTranslationService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param productVariantsTranslation 商品翻译的变体信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProductVariantsTranslationPage(Page page, ProductVariantsTranslation productVariantsTranslation) {
        return R.ok(productVariantsTranslationService.page(page, Wrappers.query(productVariantsTranslation)));
    }


    /**
     * 通过id查询商品翻译的变体信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(productVariantsTranslationService.getById(id));
    }

    /**
     * 新增商品翻译的变体信息
     * @param productVariantsTranslation 商品翻译的变体信息
     * @return R
     */
    @ApiOperation(value = "新增商品翻译的变体信息", notes = "新增商品翻译的变体信息")
    @SysLog("新增商品翻译的变体信息" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('supplier_productvariantstranslation_add')" )
    public R save(@RequestBody ProductVariantsTranslation productVariantsTranslation) {
        return R.ok(productVariantsTranslationService.save(productVariantsTranslation));
    }

    /**
     * 修改商品翻译的变体信息
     * @param productVariantsTranslation 商品翻译的变体信息
     * @return R
     */
    @ApiOperation(value = "修改商品翻译的变体信息", notes = "修改商品翻译的变体信息")
    @SysLog("修改商品翻译的变体信息" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('supplier_productvariantstranslation_edit')" )
    public R updateById(@RequestBody ProductVariantsTranslation productVariantsTranslation) {
        return R.ok(productVariantsTranslationService.updateById(productVariantsTranslation));
    }

    /**
     * 通过id删除商品翻译的变体信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商品翻译的变体信息", notes = "通过id删除商品翻译的变体信息")
    @SysLog("通过id删除商品翻译的变体信息" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('supplier_productvariantstranslation_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(productVariantsTranslationService.removeById(id));
    }

}
