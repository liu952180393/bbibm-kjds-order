package com.rjkj.cf.bbibm.kjds.product.amazonproduct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.entity.AmazonProductInfo;

import java.util.List;

/**
 *@描述：亚马逊亮点，描述库
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-11-06 15:52:56
 **/
public interface AmazonProductInfoMapper extends BaseMapper<AmazonProductInfo> {


    /**
     * 根据分类id查询数据
     * @param categoryId
     * @return
     */
    public List<AmazonProductInfo> queryInfoByCategoryId(String categoryId);

}
