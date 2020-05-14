package com.rjkj.cf.bbibm.kjds.product.appcategory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;
import org.apache.ibatis.annotations.Param;

import javax.validation.constraints.Size;
import java.util.List;

/**
 *@描述：app商品分类属性表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-12 11:05:19
 **/
public interface AppCategoryMapper extends BaseMapper<AppCategory> {

    /**
     * 查询app商品分类一级属性
     * @return
     */
    public List<AppCategory> queryOneCategory();

    /**
     * 根据parentid查询数据信息
     * @return
     */
    public List<AppCategory> queryParentIdCategory(@Param("parentId") String parentId);


    /**
     * 根据分类id查询分类属性信息
     * @param id
     * @return
     */
    public AppCategory queryById(@Param("id")String id);


    /**
     * 根据分类id查询全部下级分类信息
     * @param categroryId
     * @return
     */
    public List<AppCategory> queryByPathId(@Param("categroryId")String categroryId);

}
