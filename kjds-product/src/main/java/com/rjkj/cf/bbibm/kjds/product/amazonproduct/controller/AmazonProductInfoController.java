package com.rjkj.cf.bbibm.kjds.product.amazonproduct.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.entity.AmazonProductInfo;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.service.AmazonProductInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：亚马逊亮点，描述库
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-11-06 15:52:56
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/amazonproductinfo" )
@Api(value = "amazonproductinfo", tags = "亚马逊亮点，描述库管理")
public class AmazonProductInfoController {

    private final  AmazonProductInfoService amazonProductInfoService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param amazonProductInfo 亚马逊亮点，描述库
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getAmazonProductInfoPage(Page page, AmazonProductInfo amazonProductInfo) {
        return R.ok(amazonProductInfoService.page(page, Wrappers.query(amazonProductInfo)));
    }


    /**
     * 通过id查询亚马逊亮点，描述库
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(amazonProductInfoService.getById(id));
    }

    /**
     * 新增亚马逊亮点，描述库
     * @param amazonProductInfo 亚马逊亮点，描述库
     * @return R
     */
    @ApiOperation(value = "新增亚马逊亮点，描述库", notes = "新增亚马逊亮点，描述库")
    @SysLog("新增亚马逊亮点，描述库" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('amazonproduct_amazonproductinfo_add')" )
    public R save(@RequestBody AmazonProductInfo amazonProductInfo) {
        return R.ok(amazonProductInfoService.save(amazonProductInfo));
    }

    /**
     * 修改亚马逊亮点，描述库
     * @param amazonProductInfo 亚马逊亮点，描述库
     * @return R
     */
    @ApiOperation(value = "修改亚马逊亮点，描述库", notes = "修改亚马逊亮点，描述库")
    @SysLog("修改亚马逊亮点，描述库" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('amazonproduct_amazonproductinfo_edit')" )
    public R updateById(@RequestBody AmazonProductInfo amazonProductInfo) {
        return R.ok(amazonProductInfoService.updateById(amazonProductInfo));
    }

    /**
     * 通过id删除亚马逊亮点，描述库
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除亚马逊亮点，描述库", notes = "通过id删除亚马逊亮点，描述库")
    @SysLog("通过id删除亚马逊亮点，描述库" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('amazonproduct_amazonproductinfo_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(amazonProductInfoService.removeById(id));
    }

}
