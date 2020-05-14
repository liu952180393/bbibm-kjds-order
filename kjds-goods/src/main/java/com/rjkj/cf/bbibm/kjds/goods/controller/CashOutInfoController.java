package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.goods.entity.CashOutInfo;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.CashOutVo;
import com.rjkj.cf.bbibm.kjds.goods.service.CashOutInfoService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * @描述：提现信息表
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-11-11 15:48:25
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/cashoutinfo")
@Api(value = "cashoutinfo", tags = "提现信息表管理")
public class CashOutInfoController {

    private final CashOutInfoService cashOutInfoService;

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param cashOutInfo 提现信息表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R getCashOutInfoPage(Page page, CashOutInfo cashOutInfo) {
        QueryWrapper<CashOutInfo> query = Wrappers.<CashOutInfo>query();
        //判断提现平台查询
        if(StringUtils.isNotEmpty(cashOutInfo.getType())){
            query.lambda().eq(CashOutInfo::getType,cashOutInfo.getType());
        }
        //判断申请状态查询
        if(StringUtils.isNotEmpty(cashOutInfo.getStatus())){
            query.lambda().eq(CashOutInfo::getStatus,cashOutInfo.getStatus());
        }

        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //申请开始时间和结束时间不为空
        if(StringUtils.isNotEmpty(cashOutInfo.getApplaStartTime()) && StringUtils.isNotEmpty(cashOutInfo.getApplaEndTime())){
            LocalDateTime parseStart = LocalDateTime.parse(cashOutInfo.getApplaStartTime(), df);
            LocalDateTime parseEnd = LocalDateTime.parse(cashOutInfo.getApplaEndTime(), df);
            query.lambda().between(CashOutInfo::getApplytime,parseStart,parseEnd);
        }else if(StringUtils.isNotEmpty(cashOutInfo.getApplaStartTime())){
            LocalDateTime parseStart = LocalDateTime.parse(cashOutInfo.getApplaStartTime(), df);
            query.lambda().gt(CashOutInfo::getApplytime,parseStart);
        }else if(StringUtils.isNotEmpty(cashOutInfo.getApplaEndTime())){
            LocalDateTime parseEnd = LocalDateTime.parse(cashOutInfo.getApplaEndTime(), df);
            query.lambda().lt(CashOutInfo::getApplytime,parseEnd);
        }

        return R.ok(cashOutInfoService.page(page, query));

    }


    /**
     * 通过id查询提现信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}")
    public R getById(@PathVariable("id") String id) {
        return R.ok(cashOutInfoService.getById(id));
    }


    /**
     * 修改提现信息表
     *
     * @param cashOutInfo 提现信息表
     * @return R
     */
    @ApiOperation(value = "修改提现信息表", notes = "修改提现信息表")
    @SysLog("修改提现信息表")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_cashoutinfo_edit')")
    public R updateById(@RequestBody CashOutInfo cashOutInfo) {
        return R.ok(cashOutInfoService.updateById(cashOutInfo));
    }
    /**
     * 通过id删除提现信息表
     *
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除提现信息表", notes = "通过id删除提现信息表")
    @SysLog("通过id删除提现信息表")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('goods_cashoutinfo_del')")
    public R removeById(@PathVariable String id) {
        return R.ok(cashOutInfoService.removeById(id));
    }

    /**
     * 申请提现记录保存
     *
     * @param cashOutVo 申请提现记录保存
     * @return R
     */
    @ApiOperation(value = "申请提现记录保存", notes = "申请提现记录保存")
    @SysLog("申请提现记录保存")
    @PostMapping(value = "saveCashOutInfo")
    public R saveCashOutInfo(CashOutVo cashOutVo) {
        try {
            int result = cashOutInfoService.saveCashOutInfo(cashOutVo);
            if (result==1) {
                return R.ok(true, "申请提现成功，请等待审核！");
            } else if(result == -1){
                return R.failed(false, "申请提现失败，请稍后再试！");
            }else {
                return R.failed(false, "本次提现金额超过可提现金额，请重新输入！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 可提现金额
     *
     * @param cashOutType 可提现金额
     * @return R
     */
    @ApiOperation(value = "可提现金额", notes = "可提现金额")
    @SysLog("可提现金额")
    @ApiImplicitParam(name = "cashOutType", value = "提现类型 1城市代理人 2 自招成员 3 供应商", required = true, dataType = "String")
    @PostMapping(value = "cashOutCount")
    public R cashOutCount(String cashOutType) {
        try {
            return R.ok(cashOutInfoService.cashOutCount(cashOutType));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 申请记录状态更改
     * @return R
     */
    @ApiOperation(value = "申请记录状态更改", notes = "申请记录状态更改")
    @SysLog("申请记录状态更改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "提现记录ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "要更改为的状态 2 申请通过 3 拒绝申请", required = true, dataType = "String"),
            @ApiImplicitParam(name = "remark", value = "备注，注释", required = false, dataType = "String")
    })
    @PostMapping(value = "updateStatus")
    public R cashOutCount(String id, String status,String remark) {
        try {
            Boolean result = cashOutInfoService.updateStatus(id, status, remark);
            if(result){
                return R.ok(true,"操作成功！");
            }
            return R.failed(false,"操作失败！");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 确认用户提交提现订单提现方法
     * @return R
     */
    @ApiOperation(value = "确认用户提交提现订单提现方法", notes = "确认用户提交提现订单提现方法")
    @SysLog("确认用户提交提现订单提现方法")
    @ApiImplicitParam(name = "id", value = "id", required = false, dataType = "String")
    @PostMapping(value = "userApplicationCashInfo")
    public R userApplicationCashInfo(String id){
        if(StringUtils.isEmpty(id)){
            throw new RuntimeException("订单id不能为空！");
        }
        CashOutInfo cashBean = cashOutInfoService.getById(id);
        return cashOutInfoService.userApplicationCash(cashBean);
    }

}
