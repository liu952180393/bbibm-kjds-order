package com.rjkj.cf.bbibm.kjds.product.appcategory.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;

import java.util.List;

/**
 *@描述：app商品分类属性表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-12 11:05:19
 **/
public interface AppCategoryService extends IService<AppCategory> {

    /**
     * 查询app商品分类一级属性
     * @return
     */
    public List<AppCategory> queryOneCategory();

    /**
     * 根据parentid查询数据信息
     * @return
     */
    public List<AppCategory> queryParentIdCategory(String id);

}
