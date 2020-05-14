package com.rjkj.cf.bbibm.kjds.product.appcategory.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;
import com.rjkj.cf.bbibm.kjds.product.appcategory.mapper.AppCategoryMapper;
import com.rjkj.cf.bbibm.kjds.product.appcategory.service.AppCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *@描述：app商品分类属性表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-12 11:05:19
 **/
@Service
@AllArgsConstructor
public class AppCategoryServiceImpl extends ServiceImpl<AppCategoryMapper, AppCategory> implements AppCategoryService {

    private final AppCategoryMapper appCategoryMapper;

    @Override
    public List<AppCategory> queryOneCategory() {
        return appCategoryMapper.queryOneCategory();
    }

    @Override
    public List<AppCategory> queryParentIdCategory(String id) {
        return appCategoryMapper.queryParentIdCategory(id);
    }
}
