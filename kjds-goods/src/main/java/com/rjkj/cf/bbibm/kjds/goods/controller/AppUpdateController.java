package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.AppUpdate;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.goods.service.AppUpdateService;
import com.rjkj.cf.common.security.annotation.Inner;
import io.swagger.annotations.ApiImplicitParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 *@描述：版本更新
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-24 10:45:42
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/appupdate" )
@Api(value = "appupdate", tags = "版本更新管理")
public class AppUpdateController {

    private final  AppUpdateService appUpdateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param appUpdate 版本更新
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getAppUpdatePage(Page page, AppUpdate appUpdate) {
        return R.ok(appUpdateService.page(page, Wrappers.query(appUpdate)));
    }


    /**
     * 通过id查询版本更新
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(appUpdateService.getById(id));
    }

    /**
     * 新增版本更新
     * @param appUpdate 版本更新
     * @return R
     */
    @ApiOperation(value = "新增版本更新", notes = "新增版本更新")
    @SysLog("新增版本更新" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_appupdate_add')" )
    public R save(@RequestBody AppUpdate appUpdate) {
        appUpdate.setCreateTime(LocalDateTime.now());
        appUpdate.setApkUrl(appUpdate.getUrl());
        return R.ok(appUpdateService.save(appUpdate));
    }

    /**
     * 修改版本更新
     * @param appUpdate 版本更新
     * @return R
     */
    @ApiOperation(value = "修改版本更新", notes = "修改版本更新")
    @SysLog("修改版本更新" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_appupdate_edit')" )
    public R updateById(@RequestBody AppUpdate appUpdate) {
        appUpdate.setApkUrl(appUpdate.getUrl());
        return R.ok(appUpdateService.updateById(appUpdate));
    }

    /**
     * 通过id删除版本更新
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除版本更新", notes = "通过id删除版本更新")
    @SysLog("通过id删除版本更新" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_appupdate_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(appUpdateService.removeById(id));
    }



    /**
     * 根据type获取最新的一条数据
     * @param type
     * @return R
     */
    @ApiOperation(value = "根据type获取最新的一条数据", notes = "根据type获取最新的一条数据")
    @SysLog("根据type获取最新的一条数据" )
    @PostMapping("/getNewAppInfo")
    @ApiImplicitParam(name ="type",value = "ios或者android（1为android  0为ios）",required = true,dataType = "String")
    @Inner(value = false)
    public R<AppUpdate> getNewAppInfo(@RequestParam(value = "type") String type){

        try {
            if(StringUtils.isEmpty(type)){
                throw new RuntimeException("type类型不能为空");
            }

            List<AppUpdate> list = appUpdateService.list(Wrappers.<AppUpdate>query().lambda().eq(AppUpdate::getType, type).orderByDesc(AppUpdate::getCreateTime));
            AppUpdate appUpdate=new AppUpdate();
            if(list!=null&&list.size()>0){
                appUpdate=list.get(0);
            }

            return R.ok(appUpdate);

        }catch (Exception e){
            return R.failed(e.getMessage());
        }


    }

}
