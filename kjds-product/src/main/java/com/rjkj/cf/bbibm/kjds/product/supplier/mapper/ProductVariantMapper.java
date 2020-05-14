package com.rjkj.cf.bbibm.kjds.product.supplier.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@描述：商品变体信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 16:09:42
 **/
public interface ProductVariantMapper extends BaseMapper<ProductVariant> {

    /**
     * 根据父sku查询变体信息
     * @param parentSku
     * @return
     */
    public List<ProductVariant> queryByParentSku(@Param("parentSku") String parentSku);

}
