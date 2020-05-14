package com.rjkj.cf.bbibm.kjds.product.appcategory.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;
import com.rjkj.cf.bbibm.kjds.product.appcategory.service.AppCategoryService;
import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *@描述：app商品分类属性表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-12 11:05:19
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/appcategory" )
@Api(value = "appcategory", tags = "app商品分类属性表管理")
public class AppCategoryController {

    @Autowired
    private AppCategoryService appCategoryService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param appCategory app商品分类属性表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getAppCategoryPage(Page page, AppCategory appCategory) {
        return R.ok(appCategoryService.page(page, Wrappers.query(appCategory)));
    }


    /**
     * 通过id查询app商品分类属性表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(appCategoryService.getById(id));
    }

    /**
     * 新增app商品分类属性表
     * @param appCategory app商品分类属性表
     * @return R
     */
    @ApiOperation(value = "新增app商品分类属性表", notes = "新增app商品分类属性表")
    @SysLog("新增app商品分类属性表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('appcategory_appcategory_add')" )
    public R save(@RequestBody AppCategory appCategory) {
        return R.ok(appCategoryService.save(appCategory));
    }

    /**
     * 修改app商品分类属性表
     * @param appCategory app商品分类属性表
     * @return R
     */
    @ApiOperation(value = "修改app商品分类属性表", notes = "修改app商品分类属性表")
    @SysLog("修改app商品分类属性表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('appcategory_appcategory_edit')" )
    public R updateById(@RequestBody AppCategory appCategory) {
        return R.ok(appCategoryService.updateById(appCategory));
    }

    /**
     * 通过id删除app商品分类属性表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除app商品分类属性表", notes = "通过id删除app商品分类属性表")
    @SysLog("通过id删除app商品分类属性表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('appcategory_appcategory_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(appCategoryService.removeById(id));
    }


    /**
     * 查询全部一级分类数据信息
     * @return R
     */
    @ApiOperation(value = "查询全部一级分类数据信息", notes = "查询全部一级分类数据信息")
    @SysLog("查询全部一级分类数据信息" )
    @PostMapping("/queryOneCategory")
    public R<List<AppCategory>> queryOneCategory() {
        try {
            List<AppCategory> appCategories = appCategoryService.queryOneCategory();
            for(int i=0;i<appCategories.size();i++){
                List<AppCategory> appCategoriesList = appCategoryService.queryParentIdCategory(appCategories.get(i).getId());
                appCategories.get(i).setListCateGory(appCategoriesList);
            }
            return R.ok(appCategories);
        }catch (Exception e){
            return R.failed(e.getMessage());
        }

    }


//    /**
//     * 根据parentid查询数据
//     * @return R
//     */
//    @ApiOperation(value = "根据parentid查询数据", notes = "根据parentid查询数据")
//    @SysLog("根据parentid查询数据" )
//    @PostMapping("/queryParentIdCategory")
//    public R queryParentIdCategory(@RequestParam String id) {
//        try {
//            return R.ok();
//        }catch (Exception e){
//            return R.failed(e.getMessage());
//        }
//
//    }

}
