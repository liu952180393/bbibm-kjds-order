package com.bbibm.generator.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbibm.generator.entity.RikjModelTest;
import com.bbibm.generator.service.RikjModelTestService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：用户表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-12 14:31:19
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/rikjmodeltest" )
@Api(value = "rikjmodeltest", tags = "用户表管理")
public class RikjModelTestController {

    private final RikjModelTestService rikjModelTestService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param rikjModelTest 用户表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getRikjModelTestPage(Page page, RikjModelTest rikjModelTest) {
        return R.ok(rikjModelTestService.page(page, Wrappers.query(rikjModelTest)));
    }


    /**
     * 通过id查询用户表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("id" )
    public R getById(@PathVariable("id" ) Integer id) {
        return R.ok(rikjModelTestService.getById(id));
    }

    /**
     * 新增用户表
     * @param rikjModelTest 用户表
     * @return R
     */
    @ApiOperation(value = "新增用户表", notes = "新增用户表")
    @SysLog("新增用户表" )
    @PostMapping
    public R save(@RequestBody RikjModelTest rikjModelTest) {
        return R.ok(rikjModelTestService.save(rikjModelTest));
    }

    /**
     * 修改用户表
     * @param rikjModelTest 用户表
     * @return R
     */
    @ApiOperation(value = "修改用户表", notes = "修改用户表")
    @SysLog("修改用户表" )
    @PutMapping
    public R updateById(@RequestBody RikjModelTest rikjModelTest) {
        return R.ok(rikjModelTestService.updateById(rikjModelTest));
    }

    /**
     * 通过id删除用户表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除用户表", notes = "通过id删除用户表")
    @SysLog("通过id删除用户表" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable Integer id) {
        return R.ok(rikjModelTestService.removeById(id));
    }

}
