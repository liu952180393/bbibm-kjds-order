package com.rjkj.cf.bbibm.kjds.product.supplier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *@描述：供应商发货记录
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-13 14:08:23
 **/
@Data
@TableName("bbibm_supplier_product")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "供应商发货记录")
public class SupplierProduct extends Model<SupplierProduct> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="发货记录表ID")
    private String id;
    /**
     * 商品ID
     */
    @ApiModelProperty(value="商品ID")
    private String productId;
    /**
     * 供应商ID
     */
    @ApiModelProperty(value="供应商ID")
    private String supplierId;
    /**
     * 商品主图
     */
    @ApiModelProperty(value="商品主图")
    private String image;
    /**
     * 商品SKU
     */
    @ApiModelProperty(value="商品SKU")
    private String productSku;
    /**
     * 商品名称
     */
    @ApiModelProperty(value="商品名称")
    private String productName;
    /**
     * 变体颜色
     */
    @ApiModelProperty(value="变体颜色")
    private String variantColor;
    /**
     * 变体Size
     */
    @ApiModelProperty(value="变体Size")
    private String variantSize;
    /**
     * 数量
     */
    @ApiModelProperty(value="数量")
    private Integer amount;
    /**
     * 1待发货，2已发货，3已完成，4已取消
     */
    @ApiModelProperty(value="1待发货，2已发货，3已完成，4已取消")
    private String status;
    }
