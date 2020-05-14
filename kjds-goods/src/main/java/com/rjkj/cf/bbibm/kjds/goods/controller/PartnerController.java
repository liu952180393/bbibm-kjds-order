package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.admin.api.feign.RemoteUserService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemotePayFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.bbibm.kjds.api.utils.JpushClientUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.MsgConstant;
import com.rjkj.cf.bbibm.kjds.goods.entity.Partner;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerApplyReq;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerPrice;
import com.rjkj.cf.bbibm.kjds.goods.service.PartnerPriceService;
import com.rjkj.cf.bbibm.kjds.goods.service.PartnerService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


/**
 *@描述：合伙人审核表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 11:58:47
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/partner" )
@Api(value = "partner", tags = "合伙人审核表管理")
public class PartnerController {

    private final  PartnerService partnerService;

    private final RemotePayFeignService remotePayFeignService;

    private final PartnerPriceService partnerPriceService;

    private final RemoteUserService remoteUserService;

    private final RemoteProductFeignService remoteProductFeignService;
    private final RemoteGoodsFeignService remoteGoodsFeignService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param partner 合伙人审核表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getPartnerPage(Page page, Partner partner) {


        LambdaQueryWrapper<Partner> query = Wrappers.<Partner>query().lambda().in(Partner::getOptionStatus, "0", "1", "2");
        //判断审核状态查询
        if(StringUtils.isNotEmpty(partner.getOptionStatus())){
            query.eq(Partner::getOptionStatus,partner.getOptionStatus());
        }


        DateTimeFormatter df=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //申请开始时间和结束时间不为空
        if(StringUtils.isNotEmpty(partner.getApplaStartTime()) && StringUtils.isNotEmpty(partner.getApplaEndTime())){
            LocalDateTime parseStart = LocalDateTime.parse(partner.getApplaStartTime(), df);
            LocalDateTime parseEnd = LocalDateTime.parse(partner.getApplaEndTime(), df);
            query.between(Partner::getCtime,parseStart,parseEnd);
        }else if(StringUtils.isNotEmpty(partner.getApplaStartTime())){
            LocalDateTime parseStart = LocalDateTime.parse(partner.getApplaStartTime(), df);
            query.gt(Partner::getCtime,parseStart);
        }else if(StringUtils.isNotEmpty(partner.getApplaEndTime())){
            LocalDateTime parseEnd = LocalDateTime.parse(partner.getApplaEndTime(), df);
            query.lt(Partner::getCtime,parseEnd);
        }

        IPage pageed = partnerService.page(page, query);

        List<Partner> listRecords = pageed.getRecords();
        for (Partner listRecord : listRecords) {
            PartnerPrice oneBean = partnerPriceService.getOne(Wrappers.<PartnerPrice>query().lambda().eq(PartnerPrice::getId, listRecord.getPartnerPriceId()));
            listRecord.setTypeName(oneBean.getName());
            listRecord.setPrice(oneBean.getPrice());
        }
        return R.ok(pageed);
    }


    /**
     * 通过id查询合伙人审核表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(partnerService.getById(id));
    }


    /**
     * 新增合伙人审核表
     * @param partner 合伙人审核表
     * @return R
     */
    @ApiOperation(value = "新增合伙人审核表", notes = "新增合伙人审核表")
    @SysLog("新增合伙人审核表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_partner_add')" )
    public R save(@RequestBody Partner partner) {
        return R.ok(partnerService.save(partner));
    }


    /**
     * 修改合伙人审核表
     * @param partner 合伙人审核表
     * @return R
     */
    @ApiOperation(value = "修改合伙人审核表", notes = "修改合伙人审核表")
    @SysLog("修改合伙人审核表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_partner_edit')" )
    public R updateById(@RequestBody Partner partner) {
        return R.ok(partnerService.updateById(partner));
    }

    /**
     * 审批合伙人信息
     * @return R
     */
    @ApiOperation(value = "审批合伙人信息", notes = "审批合伙人信息")
    @SysLog("审批合伙人信息" )
    @PostMapping("/approveSupplier")
    @Transactional(rollbackFor = Exception.class)
    public R updateById(String id,String optionStatus,String optionErrorMsg,String partnerPriceId) {
        try {
            PartnerPrice oneBean = partnerPriceService.getOne(Wrappers.<PartnerPrice>query().lambda().eq(PartnerPrice::getId, partnerPriceId));
            Partner partner=partnerService.getOne(Wrappers.<Partner>query().lambda().eq(Partner::getId,id));
            JpushClientUtil jpushClientUtil = new JpushClientUtil(remoteGoodsFeignService, remoteProductFeignService);
            if("2".equals(optionStatus)){
                partner.setOptionErrorMsg(optionErrorMsg);
                //消息通知
                jpushClientUtil.sendOrdinaryMsg(MsgConstant.APPROVESUPPLIER_TITLE,MsgConstant.APPROVESUPPLIER_CONTENS,partner.getCuid());
                jpushClientUtil.saveMsg(partner.getCuid(),"抱歉，您的城市合伙人申请未通过审核。");
            }
            if("1".equals(optionStatus)){
                LocalDateTime beginTime = LocalDateTime.now();
                partner.setBeginTime(beginTime);
                partner.setOptionStatus(optionStatus);
                LocalDateTime endTime = beginTime.plusDays(oneBean.getDays());
                partner.setEndTime(endTime);
                remoteUserService.updateUserRole(partner.getCuid(), Const.PARTNER,"1");
                //消息通知
                jpushClientUtil.sendOrdinaryMsg(MsgConstant.APPROVESUPPLIER_TITLE,MsgConstant.APPROVESUPPLIER_CONTENS,partner.getCuid());
                jpushClientUtil.saveMsg(partner.getCuid(),"恭喜您，您的城市合伙人申请已审核通过。开始日期："+beginTime+"，结束日期："+endTime);
            }
            partnerService.updateById(partner);

            return R.ok(true,"操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed(false,"操作失败！");
        }
    }

    /**
     * 通过id删除合伙人审核表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除合伙人审核表", notes = "通过id删除合伙人审核表")
    @SysLog("通过id删除合伙人审核表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_partner_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(partnerService.removeById(id));
    }



    /**
     * 申请成为城市合伙人
     * @return R
     */
    @ApiOperation(value = "申请成为城市合伙人", notes = "申请成为城市合伙人")
    @SysLog("申请成为城市合伙人" )
    @PostMapping("/apply" )
    public R apply(PartnerApplyReq  req) {
        R appPayCallClientParam=new R();
        try {
            RjkjUser user = SecurityUtils.getUser();
            List<Partner> list = partnerService.list(Wrappers.<Partner>query().lambda().eq(Partner::getCity, req.getCity()));

            //判断当前区域没有申请过城市合伙人
            if(list.size()<1){
                String  oid=partnerService.apply(req,user);
                appPayCallClientParam= remotePayFeignService.getAppPayCallClientParam((req.getPayType() == 1 ? 2 : 1), oid);
                appPayCallClientParam.setMsg("200");
            }else{

                int states=0;
                for (Partner partner : list) {
                    if("0".equals(partner.getOptionStatus()) || "1".equals(partner.getOptionStatus())){
                        if(partner.getUuid().equals(user.getId())){
                            appPayCallClientParam.setMsg("你当前已申请城市合伙人");
                        }else{
                            appPayCallClientParam.setMsg("当前区域已申请城市合伙人");
                        }
                        states=1;
                        break;
                    }

                }

                //为0时可以申请为1时不能申请
                if(states==0){
                    String  oid=partnerService.apply(req,user);
                    appPayCallClientParam= remotePayFeignService.getAppPayCallClientParam((req.getPayType() == 1 ? 2 : 1), oid);
                    appPayCallClientParam.setMsg("200");
                }


            }
            return appPayCallClientParam;
        }catch (Exception e){
           return R.failed(e.getMessage());
        }

    }


    /**
     * 修改城市管理人信息状态为1（支付成功后为审核中状态）
     * @return R
     */
    @ApiOperation(value = "修改城市管理人信息状态为0（支付成功后为审核中状态）", notes = "修改城市管理人信息状态为0（支付成功后为审核中状态）")
    @SysLog("修改城市管理人信息状态为0（支付成功后为审核中状态）" )
    @PostMapping("/updateByPartnerState" )
    @Inner(value = false)
    public void updateByPartnerState(@RequestParam String orderNo){
        try {
            partnerPriceService.updateByPartnerState(orderNo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("发生异常："+e.getMessage());
        }
    }


}
