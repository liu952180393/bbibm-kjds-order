package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.goods.entity.SysDict;
import com.rjkj.cf.bbibm.kjds.goods.service.SysDictService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 *@描述：平台价格
 *@项目：
 *@公司：软江科技
 *@作者：yihao
 *@时间：2019-12-06 14:36:15
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/goodsPrice" )
@Api(value = "goodsPrice", tags = "平台价格管理")
public class SysDictController {

    private final SysDictService sysDictService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysDict 平台价格
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getSysDictPage(Page page, SysDict sysDict) {
        sysDict.setType("platform_type");
        return R.ok(sysDictService.page(page, Wrappers.query(sysDict)));
    }


    /**
     * 通过id查询平台价格
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(sysDictService.getById(id));
    }

    /**
     * 新增平台价格
     * @param sysDict 平台价格
     * @return R
     */
    @ApiOperation(value = "新增平台价格", notes = "新增平台价格")
    @SysLog("新增平台价格" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_goodsprice_add')" )
    public R save(@RequestBody SysDict sysDict) {
        return R.ok(sysDictService.save(sysDict));
    }

    /**
     * 修改平台价格
     * @param sysDict 平台价格
     * @return R
     */
    @ApiOperation(value = "修改平台价格", notes = "修改平台价格")
    @SysLog("修改平台价格" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_goodsprice_edit')" )
    public R updateById(@RequestBody SysDict sysDict) {
        return R.ok(sysDictService.updateById(sysDict));
    }

    /**
     * 通过id删除平台价格
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除平台价格", notes = "通过id删除平台价格")
    @SysLog("通过id删除平台价格" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_goodsprice_del')" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(sysDictService.removeById(id));
    }

}
