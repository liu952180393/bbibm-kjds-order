package com.rjkj.cf.bbibm.kjds.product.pulloutnumer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *@描述：商品上架次数
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-11-01 10:37:18
 **/
@Data
@TableName("bbibm_product_pullout_number")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商品上架次数")
public class ProductPulloutNumber extends Model<ProductPulloutNumber> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 供应商名称
     */
    @ApiModelProperty(value="供应商名称")
    private String supplierName;
    /**
     * 商品id
     */
    @ApiModelProperty(value="商品id")
    private String productId;
    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private String userId;
    /**
     * 上架次数
     */
    @ApiModelProperty(value="上架次数")
    private Integer number;
    }
