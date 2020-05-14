package com.rjkj.cf.bbibm.kjds.product.advertisement.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.bbibm.kjds.product.advertisement.entity.Advertisement;
import com.rjkj.cf.bbibm.kjds.product.advertisement.service.AdvertisementService;
import com.rjkj.cf.common.security.annotation.Inner;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


/**
 *@描述：广告信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-28 15:52:05
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/advertisement" )
@Api(value = "advertisement", tags = "广告信息管理")
public class AdvertisementController {

    private final  AdvertisementService advertisementService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param advertisement 广告信息
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getAdvertisementPage(Page page, Advertisement advertisement) {
        return R.ok(advertisementService.page(page, Wrappers.query(advertisement)));
    }


    /**
     * 通过id查询广告信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(advertisementService.getById(id));
    }

    /**
     * 新增广告信息
     * @param advertisement 广告信息
     * @return R
     */
    @ApiOperation(value = "新增广告信息", notes = "新增广告信息")
    @SysLog("新增广告信息" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('advertisement_advertisement_add')" )
    public R save(@RequestBody Advertisement advertisement) {
        advertisement.setId(IDUtils.getGUUID(""));
        return R.ok(advertisementService.save(advertisement));
    }

    /**
     * 修改广告信息
     * @param advertisement 广告信息
     * @return R
     */
    @ApiOperation(value = "修改广告信息", notes = "修改广告信息")
    @SysLog("修改广告信息" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('advertisement_advertisement_edit')" )
    public R updateById(@RequestBody Advertisement advertisement) {
        return R.ok(advertisementService.updateById(advertisement));
    }

    /**
     * 通过id删除广告信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除广告信息", notes = "通过id删除广告信息")
    @SysLog("通过id删除广告信息" )
    @DeleteMapping("/{id}" )
    @PreAuthorize("@pms.hasPermission('advertisement_advertisement_del')" )
    public R removeById(@PathVariable String id) {
        return R.ok(advertisementService.removeById(id));
    }



    /**
     * 查询app启动时广告页数据
     * @param
     * @return R
     */
    @ApiOperation(value = "查询app启动时广告页数据", notes = "查询app启动时广告页数据")
    @SysLog("查询app启动时广告页数据" )
    @PostMapping("/queryAdvertisementInfo")
    @Inner(value = false)
    public R<Advertisement> queryAdvertisementInfo(){
        Advertisement bean=new Advertisement();
        try {
            List<Advertisement> list = advertisementService.list(Wrappers.<Advertisement>query().lambda().orderByDesc(Advertisement::getCreateTime).eq(Advertisement::getState,"1"));
            if(list!=null&&list.size()>0){
                if(list.size()==1){
                    bean=list.get(0);
                }else{
                    Random r=new Random();
                    int i = r.nextInt(list.size());
                    bean=list.get(i);
                }
            }
        }catch (Exception e){
            return R.failed(e.getMessage());
        }
        return R.ok(bean);
    }

}
