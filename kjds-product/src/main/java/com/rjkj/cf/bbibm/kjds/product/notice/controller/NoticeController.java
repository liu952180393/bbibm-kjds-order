package com.rjkj.cf.bbibm.kjds.product.notice.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.Notice;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.product.notice.service.NoticeService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


/**
 *@描述：系统消息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-18 14:47:09
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/notice" )
@Api(value = "notice", tags = "系统消息管理")
public class NoticeController {

    private final  NoticeService noticeService;

//    /**
//     * 分页查询
//     * @param page 分页对象
//     * @param notice 系统消息
//     * @return
//     */
//    @ApiOperation(value = "分页查询", notes = "分页查询")
//    @GetMapping("/page" )
//    public R getNoticePage(Page page, Notice notice) {
//        return R.ok(noticeService.page(page, Wrappers.query(notice)));
//    }


//    /**
//     * 通过id查询系统消息
//     * @param id id
//     * @return R
//     */
//    @ApiOperation(value = "通过id查询", notes = "通过id查询")
//    @GetMapping("/{id}" )
//    public R getById(@PathVariable("id" ) String id) {
//        return R.ok(noticeService.getById(id));
//    }

    /**
     * 新增消息
     * @param notice 系统消息
     * @return R
     */
    @ApiOperation(value = "新增消息", notes = "新增消息")
    @SysLog("新增系统消息" )
    @PostMapping("/saveInfo")
    @Inner(value = false)
    public R saveInfo(@RequestBody Notice notice) {
        notice.setId(IDUtils.getGUUID(""));
        notice.setCreateTime(LocalDateTime.now());
        return R.ok(noticeService.save(notice));
    }

//    /**
//     * 修改系统消息
//     * @param notice 系统消息
//     * @return R
//     */
//    @ApiOperation(value = "修改系统消息", notes = "修改系统消息")
//    @SysLog("修改系统消息" )
//    @PutMapping
//    @PreAuthorize("@pms.hasPermission('notice_notice_edit')" )
//    public R updateById(@RequestBody Notice notice) {
//        return R.ok(noticeService.updateById(notice));
//    }

    /**
     * 通过id删除系统消息
     * @param  id
     * @return R
     */
    @ApiOperation(value = "通过id删除系统消息", notes = "通过id删除系统消息")
    @SysLog("通过id删除系统消息" )
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @PostMapping("removeById" )
    public R removeById(String id) {
        return R.ok(noticeService.removeById(id),"删除成功");
    }


    /**通过id修改状态
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id修改状态", notes = "通过id修改状态")
    @SysLog("通过id修改状态" )
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "state", value = "状态(0为未读，1为已读)", required = true, dataType = "String")
    })
    @PostMapping("/updateStateById")
    public R updateStateById(String id,String state) {
        try {
            if(StringUtils.isEmpty(id)){
                throw new RuntimeException("id不能为空");
            }
            if(StringUtils.isEmpty(state)){
                throw new RuntimeException("状态不能为空");
            }
            Notice bean=new Notice();
            bean.setId(id);
            bean.setState(Integer.valueOf(state));
            noticeService.updateById(bean);
        }catch (Exception e){
            return R.failed(e.getMessage());
        }
        return R.ok("","已读信息");
    }


    /**通过id查询信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询信息", notes = "通过id查询信息")
    @SysLog("通过id查询信息" )
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @PostMapping("/queryById")
    public R<Notice> queryById(String id) {
        try {
            if(StringUtils.isEmpty(id)){
                throw new RuntimeException("id不能为空");
            }
            Notice bean = noticeService.getById(id);
            return R.ok(bean);
        }catch (Exception e){
            return R.failed(e.getMessage());
        }
    }


    /**
     * 根据用户id分页查询
     * @param page 分页对象
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "根据用户id分页查询", notes = "根据用户id分页查询")
    @PostMapping("/getNoticeByUserIdPage" )
    public R getNoticeByUserIdPage(Page page, String userId) {
        if(StringUtils.isEmpty(userId)){
            throw new RuntimeException("用户id不能为空");
        }
        return R.ok(noticeService.page(page,Wrappers.<Notice>query().lambda().eq(Notice::getUserId,userId)));
    }

}
