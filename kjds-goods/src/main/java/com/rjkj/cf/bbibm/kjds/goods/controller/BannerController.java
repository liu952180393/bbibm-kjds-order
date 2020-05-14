package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.goods.entity.Banner;
import com.rjkj.cf.bbibm.kjds.goods.service.BannerService;
import com.rjkj.cf.common.security.annotation.Inner;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *@描述：轮播图
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-11 19:05:09
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/banner" )
@Api(value = "banner", tags = "轮播图管理")
public class BannerController {

    private final  BannerService bannerService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param banner 轮播图
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getBannerPage(Page page, Banner banner) {
        List<Banner> list = bannerService.list();
        return R.ok(bannerService.page(page, Wrappers.query(banner)));
    }


    /**
     * 通过id查询轮播图
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(bannerService.getById(id));
    }

    /**
     * 新增轮播图
     * @param banner 轮播图
     * @return R
     */
    @ApiOperation(value = "新增轮播图", notes = "新增轮播图")
    @SysLog("新增轮播图" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_banner_add')" )
    public R save(@RequestBody Banner banner) {
        return R.ok(bannerService.save(banner));
    }

    /**
     * 修改轮播图
     * @param banner 轮播图
     * @return R
     */
    @ApiOperation(value = "修改轮播图", notes = "修改轮播图")
    @SysLog("修改轮播图" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_banner_edit')" )
    public R updateById(@RequestBody Banner banner) {
        return R.ok(bannerService.updateById(banner));
    }

    /**
     * 通过id删除轮播图
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除轮播图", notes = "通过id删除轮播图")
    @SysLog("通过id删除轮播图" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_banner_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(bannerService.removeById(id));
    }



    @SysLog("appp主页轮播图列表")
    @Inner(value = false)
    @ApiOperation(value = "轮播图列表",notes = "轮播图列表")
    @PostMapping("list")
    public R<List<Banner>> list(){
        try {
            return R.ok(bannerService.list(Wrappers.<Banner>query().lambda()
                    .orderByDesc(Banner::getSort)));
        }catch (Exception  e){
            return  R.failed(e.getMessage());
        }

    }
}
