package com.rjkj.cf.bbibm.kjds.product.apphome.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformGoodsRsp;
import com.rjkj.cf.bbibm.kjds.product.apphome.entity.AppHomeInfo;

import java.util.List;

/**
 *@描述：app首页展示图标表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-15 10:37:47
 **/
public interface AppHomeInfoService extends IService<AppHomeInfo> {

    List<PlatformGoodsRsp> queryUserShop();

}
