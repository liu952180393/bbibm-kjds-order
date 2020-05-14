package com.rjkj.cf.bbibm.kjds.product.supplier.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@描述：主商品图片排序信息实体
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 14:55:08
 **/
@Data
@ApiModel(value = "主商品图片排序信息实体")
public class ProductImageSort {

    /**
     * 商品图片url
     */
    @ApiModelProperty(value="商品图片url")
    private String imageUrl;

    /**
     * 商品图片位置
     */
    @ApiModelProperty(value="商品图片位置")
    private Integer sort;

}
