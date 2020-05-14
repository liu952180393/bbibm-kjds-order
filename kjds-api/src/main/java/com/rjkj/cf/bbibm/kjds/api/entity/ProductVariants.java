package com.rjkj.cf.bbibm.kjds.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;



/**
 *@描述：商品变体信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 16:09:42
 **/
@Data
@TableName("bbibm_product_variant")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商品变体信息")
public class ProductVariants extends Model<ProductVariants> {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 变体价格
     */
    @ApiModelProperty(value="变体价格")
    private Double price;
    /**
     * 变体size
     */
    @ApiModelProperty(value="变体size")
    private String variantSize;
    /**
     * 变体color
     */
    @ApiModelProperty(value="变体color")
    private String variantColor;
    /**
     * 库存
     */
    @ApiModelProperty(value="库存")
    private Integer stock;
    /**
     * 子sku
     */
    @ApiModelProperty(value="子sku")
    private String sku;
    /**
     * 图片
     */
    @ApiModelProperty(value="图片")
    private String image;
    /**
     * 父sku（关联父商品信息）
     */
    @ApiModelProperty(value="父sku（关联父商品信息）")
    private String parentSku;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
}
