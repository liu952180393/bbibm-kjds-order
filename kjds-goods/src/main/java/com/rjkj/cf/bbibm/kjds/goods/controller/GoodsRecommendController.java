package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsRecommend;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsRecommendService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 *@描述：推荐费率
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-11-01 17:07:09
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/goodsrecommend" )
@Api(value = "goodsrecommend", tags = "推荐费率管理")
public class GoodsRecommendController {

    private final  GoodsRecommendService goodsRecommendService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param goodsRecommend 推荐费率
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getGoodsRecommendPage(Page page, GoodsRecommend goodsRecommend) {
        return R.ok(goodsRecommendService.page(page, Wrappers.query(goodsRecommend)));
    }


    /**
     * 通过id查询推荐费率
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(goodsRecommendService.getById(id));
    }

    /**
     * 新增推荐费率
     * @param goodsRecommend 推荐费率
     * @return R
     */
    @ApiOperation(value = "新增推荐费率", notes = "新增推荐费率")
    @SysLog("新增推荐费率" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('goods_goodsrecommend_add')" )
    public R save(@RequestBody GoodsRecommend goodsRecommend) {
        return R.ok(goodsRecommendService.save(goodsRecommend));
    }

    /**
     * 修改推荐费率
     * @param goodsRecommend 推荐费率
     * @return R
     */
    @ApiOperation(value = "修改推荐费率", notes = "修改推荐费率")
    @SysLog("修改推荐费率" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('goods_goodsrecommend_edit')" )
    public R updateById(@RequestBody GoodsRecommend goodsRecommend) {
        return R.ok(goodsRecommendService.updateById(goodsRecommend));
    }

    /**
     * 通过id删除推荐费率
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除推荐费率", notes = "通过id删除推荐费率")
    @SysLog("通过id删除推荐费率" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('goods_goodsrecommend_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(goodsRecommendService.removeById(id));
    }

}
