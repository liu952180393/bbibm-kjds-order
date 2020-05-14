package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerJoin;
import com.rjkj.cf.bbibm.kjds.goods.service.PartnerJoinService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：城市合伙人分享
 *@项目：
 *@公司：软江科技
 *@作者：yihao
 *@时间：2019-12-11 11:22:06
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/partnerjoin" )
@Api(value = "partnerjoin", tags = "城市合伙人分享管理")
public class PartnerJoinController {

    private final PartnerJoinService partnerJoinService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param partnerJoin 城市合伙人分享
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getPartnerJoinPage(Page page, PartnerJoin partnerJoin) {
        return R.ok(partnerJoinService.page(page, Wrappers.query(partnerJoin)));
    }


    /**
     * 通过id查询城市合伙人分享
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(partnerJoinService.getById(id));
    }

    /**
     * 新增城市合伙人分享
     * @param partnerJoin 城市合伙人分享
     * @return R
     */
    @ApiOperation(value = "新增城市合伙人分享", notes = "新增城市合伙人分享")
    @SysLog("新增城市合伙人分享" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_partnerjoin_add')" )
    public R save(@RequestBody PartnerJoin partnerJoin) {
        return R.ok(partnerJoinService.save(partnerJoin));
    }

    /**
     * 修改城市合伙人分享
     * @param partnerJoin 城市合伙人分享
     * @return R
     */
    @ApiOperation(value = "修改城市合伙人分享", notes = "修改城市合伙人分享")
    @SysLog("修改城市合伙人分享" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_partnerjoin_edit')" )
    public R updateById(@RequestBody PartnerJoin partnerJoin) {
        return R.ok(partnerJoinService.updateById(partnerJoin));
    }

    /**
     * 通过id删除城市合伙人分享
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除城市合伙人分享", notes = "通过id删除城市合伙人分享")
    @SysLog("通过id删除城市合伙人分享" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_partnerjoin_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(partnerJoinService.removeById(id));
    }
}
