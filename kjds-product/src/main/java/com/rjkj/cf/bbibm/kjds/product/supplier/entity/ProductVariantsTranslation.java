package com.rjkj.cf.bbibm.kjds.product.supplier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 *@描述：商品翻译的变体信息
 *@项目：
 *@公司：软江科技
 *@作者：Yihao
 *@时间：2020-01-07 16:45:18
 **/
@Data
@TableName("bbibm_product_variants_translation")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商品翻译的变体信息")
public class ProductVariantsTranslation extends Model<ProductVariantsTranslation> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="")
    private String id;
    /**
     * 父商品SKU
     */
    @ApiModelProperty(value="父商品SKU")
    private String parentSku;
    /**
     * 变体SKU
     */
    @ApiModelProperty(value="变体SKU")
    private String sku;
    /**
     * 变体SIZE
     */
    @ApiModelProperty(value="变体SIZE")
    private String variantSize;
    /**
     * 变体COLOR
     */
    @ApiModelProperty(value="变体COLOR")
    private String variantColor;
    /**
     * 翻译区域
     */
    @ApiModelProperty(value="翻译区域")
    private String area;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    }
