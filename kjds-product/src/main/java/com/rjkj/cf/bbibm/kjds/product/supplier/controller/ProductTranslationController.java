package com.rjkj.cf.bbibm.kjds.product.supplier.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductTranslationService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：商品翻译后的信息
 *@项目：
 *@公司：软江科技
 *@作者：Yihao
 *@时间：2020-01-07 11:48:25
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/producttranslation" )
@Api(value = "producttranslation", tags = "商品翻译后的信息管理")
public class ProductTranslationController {

    private final ProductTranslationService productTranslationService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param productTranslation 商品翻译后的信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProductTranslationPage(Page page, ProductTranslation productTranslation) {
        return R.ok(productTranslationService.page(page, Wrappers.query(productTranslation)));
    }


    /**
     * 通过id查询商品翻译后的信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(productTranslationService.getById(id));
    }

    /**
     * 新增商品翻译后的信息
     * @param productTranslation 商品翻译后的信息
     * @return R
     */
    @ApiOperation(value = "新增商品翻译后的信息", notes = "新增商品翻译后的信息")
    @SysLog("新增商品翻译后的信息" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('supplier_producttranslation_add')" )
    public R save(@RequestBody ProductTranslation productTranslation) {
        return R.ok(productTranslationService.save(productTranslation));
    }

    /**
     * 修改商品翻译后的信息
     * @param productTranslation 商品翻译后的信息
     * @return R
     */
    @ApiOperation(value = "修改商品翻译后的信息", notes = "修改商品翻译后的信息")
    @SysLog("修改商品翻译后的信息" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('supplier_producttranslation_edit')" )
    public R updateById(@RequestBody ProductTranslation productTranslation) {
        return R.ok(productTranslationService.updateById(productTranslation));
    }

    /**
     * 通过id删除商品翻译后的信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除商品翻译后的信息", notes = "通过id删除商品翻译后的信息")
    @SysLog("通过id删除商品翻译后的信息" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('supplier_producttranslation_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(productTranslationService.removeById(id));
    }

}
