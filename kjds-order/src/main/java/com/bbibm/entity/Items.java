package com.bbibm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

        
/**
 *@描述：详情表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-13 14:49:54
 **/
@Data
@TableName("items")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "详情表")
public class Items extends Model<Items> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @ApiModelProperty(value="")
    private Double weight;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String itemName;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String itemSku;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private BigDecimal variationDiscountedPrice;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String variationId;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String variationName;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private Integer isAddOnDeal;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String itemId;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String variationSku;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private BigDecimal variationOriginalPrice;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private Integer isMainItem;
    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer itemsId;
    }
