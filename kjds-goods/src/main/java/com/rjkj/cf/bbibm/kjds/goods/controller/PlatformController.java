package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.rjkj.cf.bbibm.kjds.api.entity.PlatformInfoVo;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 *@描述：平台信息
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-16 10:00:35
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/platform" )
@Api(value = "platform", tags = "平台信息管理")
public class PlatformController {

    private final GoodsService platformService;



    /**
     * 通过id查询平台信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/{id}" )
    public R getById(@PathVariable("id" ) String id) {
        return R.ok(platformService.getById(id));
    }
    /**
     *  用户平台信息
     * @return
     */
    @PostMapping(value = "user/info")
    @SysLog("用户平台信息")
    R<List<PlatformInfoVo>> userPlatformInfo(){
        try {
            RjkjUser user = SecurityUtils.getUser();
            List<PlatformInfoVo>  vos=platformService.selectUserPlatformInfo(user);
            return R.ok(vos);
        }catch (Exception  e){
           return  R.failed(e.getMessage());
        }
    }


    /**
     *  用户平台信息
     * @return
     */
    @PostMapping(value = "price/list")
    @ApiOperation(value = "获取平台信息",notes = "获取平台信息")
    @SysLog("获取平台信息")
    R<List<PlatformInfoVo>> priceList(){
        try {
            List<PlatformInfoVo>  vos=platformService.priceList();
            for (PlatformInfoVo vo : vos) {
                if("18".equals(vo.getId())){
                    vo.setImage("http://ymx.bbibm.com/kjds/07584ec9062a4a58bb0b43d92b530a5e.jpg");
                }else if("19".equals(vo.getId())){
                    vo.setImage("http://ymx.bbibm.com/kjds/4acd4f54f9264e5abbd79fb60160c3c0.jpg");
                }else if("20".equals(vo.getId())){
                    vo.setImage("http://ymx.bbibm.com/kjds/6432830b06174b679021b1f4db39a4f8.jpg");
                }

            }
            return R.ok(vos);
        }catch (Exception  e){
            return  R.failed(e.getMessage());
        }
    }


}
