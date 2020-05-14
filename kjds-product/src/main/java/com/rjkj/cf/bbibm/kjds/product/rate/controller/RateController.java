package com.rjkj.cf.bbibm.kjds.product.rate.controller;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.product.rate.entity.Rate;
import com.rjkj.cf.bbibm.kjds.product.rate.service.RateService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.bouncycastle.jcajce.provider.symmetric.AES;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


/**
 *@描述：汇率信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-24 14:07:34
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/rate" )
@Api(value = "rate", tags = "汇率信息管理")
public class RateController {

    private final RateService rateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param rate 汇率信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getRatePage(Page page, Rate rate) {
        return R.ok(rateService.page(page, Wrappers.query(rate)));
    }


    /**
     * 通过id查询汇率信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(rateService.getById(id));
    }

    /**
     * 新增汇率信息
     * @param rate 汇率信息
     * @return R
     */
    @ApiOperation(value = "新增汇率信息", notes = "新增汇率信息")
    @SysLog("新增汇率信息" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('rate_rate_add')" )
    public R save(@RequestBody Rate rate) {
        rate.setId(IDUtils.getGUUID(""));
        return R.ok(rateService.save(rate));
    }

    /**
     * 修改汇率信息
     * @param rate 汇率信息
     * @return R
     */
    @ApiOperation(value = "修改汇率信息", notes = "修改汇率信息")
    @SysLog("修改汇率信息" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('rate_rate_edit')" )
    public R updateById(@RequestBody Rate rate) {
        return R.ok(rateService.updateById(rate));
    }

    /**
     * 通过id删除汇率信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除汇率信息", notes = "通过id删除汇率信息")
    @SysLog("通过id删除汇率信息" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('rate_rate_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(rateService.removeById(id));
    }


    /**
     * 通过区域查询汇率信息
     * @param area area
     * @return R
     */
    @ApiOperation(value = "通过区域查询汇率信息", notes = "通过区域查询汇率信息")
    @SysLog("通过区域查询汇率信息" )
    @PostMapping(value = "/queryByRateOne")
    @Inner(value = false)
    public BigDecimal queryByRateOne(String area){
        Rate oneBean = rateService.getOne(Wrappers.<Rate>query().lambda().eq(Rate::getName, area));
        if(oneBean!=null){
            return oneBean.getRate();
        }else{
            throw new RuntimeException("该区域下没有汇率信息");
        }
    }

}
