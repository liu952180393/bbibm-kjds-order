package com.rjkj.cf.bbibm.kjds.product.supplier.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 *@描述：供应商提成信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-11 11:39:06
 **/
@Data
@TableName("bbibm_supplier_royalty_appove")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "供应商提成信息")
public class SupplierRoyaltyAppove extends Model<SupplierRoyaltyAppove> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 供应商id
     */
    @ApiModelProperty(value="供应商id")
    private String supplierId;
    /**
     * 提成价格
     */
    @ApiModelProperty(value="提成价格")
    private BigDecimal price;
    /**
     * 产品id
     */
    @ApiModelProperty(value="产品id")
    private String productId;
    /**
     * 产品名称
     */
    @ApiModelProperty(value="产品名称")
    private String productName;
    /**
     * 拉取的用户
     */
    @ApiModelProperty(value="拉取的用户")
    private String userId;
    /**
     * 拉取用户名称
     */
    @ApiModelProperty(value="拉取用户名称")
    private String userName;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    }
