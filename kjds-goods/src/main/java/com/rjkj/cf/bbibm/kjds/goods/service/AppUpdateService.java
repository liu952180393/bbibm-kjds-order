package com.rjkj.cf.bbibm.kjds.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.api.entity.AppUpdate;

/**
 *@描述：版本更新
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-24 10:45:42
 **/
public interface AppUpdateService extends IService<AppUpdate> {

    @Override
    boolean save(AppUpdate entity);

    @Override
    boolean updateById(AppUpdate entity);
}
