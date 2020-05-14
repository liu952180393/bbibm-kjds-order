package com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.entity.ProductInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *@描述：
 *@项目：
 *@公司：软江科技
 *@作者：yihao
 *@时间：2019-10-31 10:39:56
 **/
public interface ProductInfoMapper extends BaseMapper<ProductInfo> {

    List<ProductInfo> queryProductListByTitle(@Param("title")String title);

    List<String> queryTitle(Map<String,Object> map);
}
