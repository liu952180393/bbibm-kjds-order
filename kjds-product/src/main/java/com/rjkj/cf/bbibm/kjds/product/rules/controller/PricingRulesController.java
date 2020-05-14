package com.rjkj.cf.bbibm.kjds.product.rules.controller;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.PricingRules;
import com.rjkj.cf.bbibm.kjds.api.utils.ComputedFreightPriceUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.product.rules.service.PricingRulesService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


/**
 *@描述：运费规则表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2020-01-07 15:25:04
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/pricingrules" )
@Api(value = "pricingrules", tags = "运费规则表管理")
public class PricingRulesController {

    private final PricingRulesService pricingRulesService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param pricingRules 运费规则表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getPricingRulesPage(Page page, PricingRules pricingRules) {
        return R.ok(pricingRulesService.page(page, Wrappers.query(pricingRules)));
    }


    /**
     * 通过id查询运费规则表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(pricingRulesService.getById(id));
    }

    /**
     * 新增运费规则表
     * @param pricingRules 运费规则表
     * @return R
     */
    @ApiOperation(value = "新增运费规则表", notes = "新增运费规则表")
    @SysLog("新增运费规则表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('rules_pricingrules_add')" )
    public R save(@RequestBody PricingRules pricingRules) {
                pricingRules.setId(IDUtils.getGUUID(""));
                pricingRules.setBasePrice(new BigDecimal(0));
        return R.ok(pricingRulesService.save(pricingRules));
    }

    /**
     * 修改运费规则表
     * @param pricingRules 运费规则表
     * @return R
     */
    @ApiOperation(value = "修改运费规则表", notes = "修改运费规则表")
    @SysLog("修改运费规则表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('rules_pricingrules_edit')" )
    public R updateById(@RequestBody PricingRules pricingRules) {
        pricingRules.setBasePrice(new BigDecimal(0));
        return R.ok(pricingRulesService.updateById(pricingRules));
    }

    /**
     * 通过id删除运费规则表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除运费规则表", notes = "通过id删除运费规则表")
    @SysLog("通过id删除运费规则表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('rules_pricingrules_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(pricingRulesService.removeById(id));
    }


    /**
     * 根据区域查询运费模板信息
     * @param area area
     * @return R
     */
    @ApiOperation(value = "根据区域查询运费模板信息", notes = "根据区域查询运费模板信息")
    @SysLog("根据区域查询运费模板信息" )
    @PostMapping("/queryPriceInfoOne")
    @Inner(value = false)
    public PricingRules queryPriceInfoOne(String area){
        PricingRules oneBean = pricingRulesService.getOne(Wrappers.<PricingRules>query().lambda().eq(PricingRules::getArea, area));
        if(oneBean!=null){
            return oneBean;
        }else{
            throw new RuntimeException("该区域运费模板信息为空");
        }

    }

}
