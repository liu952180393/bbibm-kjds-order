package com.rjkj.cf.bbibm.kjds.product.eaninfo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.entity.Ean;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.service.EanService;
import com.rjkj.cf.common.security.annotation.Inner;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：ean数据表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-17 14:46:51
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/ean" )
@Api(value = "ean", tags = "ean数据表管理")
public class EanController {

    private final  EanService eanService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param ean ean数据表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getEanPage(Page page, Ean ean) {
        return R.ok(eanService.page(page, Wrappers.query(ean)));
    }


    /**
     * 通过id查询ean数据表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(eanService.getById(id));
    }

    /**
     * 新增ean数据表
     * @param ean ean数据表
     * @return R
     */
    @ApiOperation(value = "新增ean数据表", notes = "新增ean数据表")
    @SysLog("新增ean数据表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('eaninfo_ean_add')" )
    public R save(@RequestBody Ean ean) {
        String eans = ean.getEan();
        String[] split = eans.split(",");
        for (String s : split) {
            Ean eanBean=new Ean();
            eanBean.setId(IDUtils.getGUUID(""));
            eanBean.setEan(s);
            eanBean.setType("0");
            eanService.save(eanBean);
        }
        return R.ok("","新增成功");
    }

    /**
     * 修改ean数据表
     * @param ean ean数据表
     * @return R
     */
    @ApiOperation(value = "修改ean数据表", notes = "修改ean数据表")
    @SysLog("修改ean数据表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('eaninfo_ean_edit')" )
    public R updateById(@RequestBody Ean ean) {
        return R.ok(eanService.updateById(ean));
    }

    /**
     * 通过id删除ean数据表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除ean数据表", notes = "通过id删除ean数据表")
    @SysLog("通过id删除ean数据表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('eaninfo_ean_del')" )
    public R removeById(@PathVariable String id) {
        String[] split = id.split(",");
        for (String s : split) {
            eanService.removeById(s);
        }
        return R.ok("删除成功");
    }

    /**
     * 新增ean信息
     * @param ids
     * @return R
     */
    @ApiOperation(value = "新增ean信息", notes = "新增ean信息")
    @SysLog("新增ean信息" )
    @PostMapping("/addEanBeatchInfo")
    @Inner(value = false)
    public R addEanBeatchInfo(String ids){
        try {
            String[] split = ids.split(",");
            for(int i=0;i<split.length;i++){
                Ean bean=new Ean();
                bean.setId(IDUtils.getGUUID(""));
                bean.setType("0");
                bean.setEan(split[i]);
                eanService.save(bean);
            }
            return R.ok();
        }catch (Exception e){
            return R.failed();
        }
    }

}
