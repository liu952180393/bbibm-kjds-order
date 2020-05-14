package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.goods.entity.KjdsArea;
import com.rjkj.cf.bbibm.kjds.goods.service.KjdsAreaService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *@描述：城市合伙人区域表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-12 17:59:35
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/kjdsarea" )
@Api(value = "kjdsarea", tags = "城市合伙人区域表管理")
public class KjdsAreaController {

    private final KjdsAreaService kjdsAreaService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param kjdsArea 城市合伙人区域表
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getKjdsAreaPage(Page page, KjdsArea kjdsArea) {
        return R.ok(kjdsAreaService.page(page, Wrappers.query(kjdsArea)));
    }


    /**
     * 通过id查询城市合伙人区域表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(kjdsAreaService.getById(id));
    }

    /**
     * 新增城市合伙人区域表
     * @param kjdsArea 城市合伙人区域表
     * @return R
     */
    @ApiOperation(value = "新增城市合伙人区域表", notes = "新增城市合伙人区域表")
    @SysLog("新增城市合伙人区域表" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_kjdsarea_add')" )
    public R save(@RequestBody KjdsArea kjdsArea) {
        return R.ok(kjdsAreaService.save(kjdsArea));
    }

    /**
     * 修改城市合伙人区域表
     * @param kjdsArea 城市合伙人区域表
     * @return R
     */
    @ApiOperation(value = "修改城市合伙人区域表", notes = "修改城市合伙人区域表")
    @SysLog("修改城市合伙人区域表" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_kjdsarea_edit')" )
    public R updateById(@RequestBody KjdsArea kjdsArea) {
        return R.ok(kjdsAreaService.updateById(kjdsArea));
    }

    /**
     * 通过id删除城市合伙人区域表
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除城市合伙人区域表", notes = "通过id删除城市合伙人区域表")
    @SysLog("通过id删除城市合伙人区域表" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_kjdsarea_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(kjdsAreaService.removeById(id));
    }


    /**
     * 查询全部市级信息
     * @return
     */
    @ApiOperation(value = "查询全部市级信息", notes = "查询全部市级信息")
    @PostMapping("/queryKjdsArea")
    public R<List<KjdsArea>> queryKjdsArea() {
        return R.ok(kjdsAreaService.list());
    }


//    /**
//     * 新增城市合伙人区域表
//     * @return R
//     */
//    @ApiOperation(value = "新增城市合伙人区域表", notes = "新增城市合伙人区域表")
//    @SysLog("新增城市合伙人区域表" )
//    @PostMapping("/addInfo")
//    public R addInfo(String name) {
//        String s = name.replaceAll("\\n", "");
//        String s2 = s.replaceAll(" ", "");
//        String[] split = s2.split(",");
//        for (String s1 : split) {
//            KjdsArea bean=new KjdsArea();
//            bean.setId(IDUtils.getGUUID(""));
//            bean.setName(s1);
//            kjdsAreaService.save(bean);
//        }
//        return R.ok();
//    }


}
