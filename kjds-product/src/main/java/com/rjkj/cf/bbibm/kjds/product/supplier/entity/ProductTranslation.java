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
 *@描述：商品翻译后的信息
 *@项目：
 *@公司：软江科技
 *@作者：Yihao
 *@时间：2020-01-07 11:48:25
 **/
@Data
@TableName("bbibm_product_translation")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商品翻译后的信息")
public class ProductTranslation extends Model<ProductTranslation> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="")
    private String id;
    /**
     * 商品ID
     */
    @ApiModelProperty(value="商品ID")
    private String productId;
    /**
     * sku
     */
    @ApiModelProperty(value="商品SKU")
    private String productSku;
    /**
     * 商品标题
     */
    @ApiModelProperty(value="商品标题")
    private String productTitle;
    /**
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String description;
    /**
     * 关键词
     */
    @ApiModelProperty(value="关键词")
    private String keyword;
    /**
     * 亮点
     */
    @ApiModelProperty(value="亮点")
    private String highlights;

    /**
     * 品牌
     */
    @ApiModelProperty(value="品牌")
    private String brand;
    /**
     * 语言区域
     */
    @ApiModelProperty(value="语言区域")
    private String area;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    }
