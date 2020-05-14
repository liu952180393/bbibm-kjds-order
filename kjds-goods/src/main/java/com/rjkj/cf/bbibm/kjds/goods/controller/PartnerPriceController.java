package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerAppRsp;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerPrice;
import com.rjkj.cf.bbibm.kjds.goods.service.PartnerPriceService;
import com.rjkj.cf.common.security.annotation.Inner;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.lettuce.core.protocol.CommandHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


/**
 *@描述：城市合伙人价格表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 13:15:07
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/partnerprice" )
@Api(value = "partnerprice", tags = "城市合伙人价格表管理")
public class PartnerPriceController {

    private final  PartnerPriceService partnerPriceService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param partnerPrice 城市合伙人价格表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getPartnerPricePage(Page page, PartnerPrice partnerPrice) {
        return R.ok(partnerPriceService.page(page, Wrappers.query(partnerPrice)));
    }
    /**
     * app 城市合伙人价格列表
     * @return
     */
    @ApiOperation(value = "app 城市合伙人价格列表", notes = "app 城市合伙人价格列表")
    @PostMapping("/list" )
    public R<List<PartnerPrice>> listAll() {
        return R.ok(partnerPriceService.list());
    }

    /**
     * 通过id查询城市合伙人价格表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(partnerPriceService.getById(id));
    }

    /**
     * 新增城市合伙人价格表
     * @param partnerPrice 城市合伙人价格表
     * @return R
     */
    @ApiOperation(value = "新增城市合伙人价格表", notes = "新增城市合伙人价格表")
    @SysLog("新增城市合伙人价格表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_partnerprice_add')" )
    public R save(@RequestBody PartnerPrice partnerPrice) {
        RjkjUser user = SecurityUtils.getUser();
        partnerPrice.setId(IDUtils.getGUUID(""));
        partnerPrice.setCuid(user.getId());
        partnerPrice.setCtime(LocalDateTime.now());
        return R.ok(partnerPriceService.save(partnerPrice));
    }

    /**
     * 修改城市合伙人价格表
     * @param partnerPrice 城市合伙人价格表
     * @return R
     */
    @ApiOperation(value = "修改城市合伙人价格表", notes = "修改城市合伙人价格表")
    @SysLog("修改城市合伙人价格表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_partnerprice_edit')" )
    public R updateById(@RequestBody PartnerPrice partnerPrice) {
        RjkjUser user = SecurityUtils.getUser();
        partnerPrice.setUuid(user.getId());
        partnerPrice.setUtime(LocalDateTime.now());
        return R.ok(partnerPriceService.updateById(partnerPrice));
    }

    /**
     * 通过id删除城市合伙人价格表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除城市合伙人价格表", notes = "通过id删除城市合伙人价格表")
    @SysLog("通过id删除城市合伙人价格表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_partnerprice_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(partnerPriceService.removeById(id));
    }

//    /**
//     *  城市合伙人价格表
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "APP-城市合伙人价格表", notes = "APP-城市合伙人价格表")
//    @SysLog("APP-城市合伙人价格表" )
//    @DeleteMapping("list" )
//    public R<List<PartnerAppRsp>> list() {
//        try {
//            List<PartnerAppRsp>  rsp=partnerPriceService.listAll();
//            return R.ok(rsp);
//        }catch (Exception e){
//            return R.failed(e.getMessage());
//        }
//
//    }
//

}
