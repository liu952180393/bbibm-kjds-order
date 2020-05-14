package com.rjkj.cf.bbibm.kjds.product.apphome.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformGoodsRsp;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;
import com.rjkj.cf.bbibm.kjds.product.appcategory.service.AppCategoryService;
import com.rjkj.cf.bbibm.kjds.product.apphome.entity.AppHomeInfo;
import com.rjkj.cf.bbibm.kjds.product.apphome.service.AppHomeInfoService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @描述：app首页展示图标表
 * @项目：
 * @公司：软江科技
 * @作者：crq
 * @时间：2019-10-15 10:37:47
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/apphomeinfo")
@Api(value = "apphomeinfo", tags = "app首页展示图标表管理")
public class AppHomeInfoController {

    private final AppHomeInfoService appHomeInfoService;

    private final AppCategoryService appCategoryService;

    private final RemoteGoodsFeignService remoteGoodsFeignService;

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param appHomeInfo app首页展示图标表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getAppHomeInfoPage(Page page, AppHomeInfo appHomeInfo) {
        return R.ok(appHomeInfoService.page(page, Wrappers.query(appHomeInfo)));
    }


    /**
     * 通过id查询app首页展示图标表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(appHomeInfoService.getById(id));
    }

    /**
     * 新增app首页展示图标表
     *
     * @param appHomeInfo app首页展示图标表
     * @return R
     */
    @ApiOperation(value = "新增app首页展示图标表", notes = "新增app首页展示图标表")
    @SysLog("新增app首页展示图标表")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('apphome_apphomeinfo_add')")
    public R save(@RequestBody AppHomeInfo appHomeInfo) {
        appHomeInfo.setId(IDUtils.getGUUID(""));
        return R.ok(appHomeInfoService.save(appHomeInfo));
    }

    /**
     * 修改app首页展示图标表
     *
     * @param appHomeInfo app首页展示图标表
     * @return R
     */
    @ApiOperation(value = "修改app首页展示图标表", notes = "修改app首页展示图标表")
    @SysLog("修改app首页展示图标表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('apphome_apphomeinfo_edit')")
    public R updateById(@RequestBody AppHomeInfo appHomeInfo) {
        if("1".equals(appHomeInfo.getType())){//当type为商品分类时
            AppCategory oneBean = appCategoryService.getOne(Wrappers.<AppCategory>query().lambda().eq(AppCategory::getId, appHomeInfo.getCategrteid()));
            appHomeInfo.setName(oneBean.getName());
        }
        return R.ok(appHomeInfoService.updateById(appHomeInfo));
    }

    /**
     * 通过id删除app首页展示图标表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除app首页展示图标表", notes = "通过id删除app首页展示图标表")
    @SysLog("通过id删除app首页展示图标表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('apphome_apphomeinfo_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(appHomeInfoService.removeById(id));
    }


    /**
     * 根据type将app首页数据图标展示出来
     *
     * @return R
     */
    @ApiOperation(value = "根据type将app首页数据图标展示出来", notes = "根据type将app首页数据图标展示出来")
    @SysLog("根据type将app首页数据图标展示出来")
    @PostMapping("/queryByType")
    public R<Map<String, Object>> queryByType() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            String keyName = null;
            for (int i = 0; i < 4; i++) {
                List<AppHomeInfo> list = appHomeInfoService.list(Wrappers.<AppHomeInfo>query().lambda().eq(AppHomeInfo::getType, i));
                if (i == 0) {
                    keyName = "layoutOne";
                } else if (i == 1) {
                    keyName = "layoutTwo";
                } else if (i == 2) {
                    keyName = "layoutThree";
                } else {
                    keyName = "layoutFour";
                }
                map.put(keyName, list);
            }
            List<AppCategory> appCategories = appCategoryService.queryOneCategory();
            map.put("layoutFive", appCategories);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok(map);
    }

    /**
     * 获取用户下拥有的平台信息
     *
     * @return R
     */
    @ApiOperation(value = "获取用户下拥有的平台信息", notes = "获取用户下拥有的平台信息")
    @SysLog("获取用户下拥有的平台信息")
    @PostMapping("/queryUserShop")
    public R<List<PlatformGoodsRsp>> queryUserShop() {
       return R.ok(appHomeInfoService.queryUserShop());
    }
}
