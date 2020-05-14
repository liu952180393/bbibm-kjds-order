package com.rjkj.cf.bbibm.kjds.api.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 *@描述：运费规则表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2020-01-07 15:25:04
 **/
@Data
@TableName("bbibm_pricing_rules")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "运费规则表")
public class PricingRules extends Model<PricingRules> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 区域
     */
    @ApiModelProperty(value="区域")
    private String area;
    /**
     * 基础价格
     */
    @ApiModelProperty(value="基础价格")
    private BigDecimal basePrice;
    /**
     * 基础重量
     */
    @ApiModelProperty(value="基础重量")
    private BigDecimal baseWeight;
    /**
     * 超过重量
     */
    @ApiModelProperty(value="超过重量")
    private BigDecimal overWeight;
    /**
     * 超过重量的价格
     */
    @ApiModelProperty(value="超过重量的价格")
    private BigDecimal overPrice;
    /**
     * 挂号费
     */
    @ApiModelProperty(value="挂号费")
    private BigDecimal registerPrice;
    /**
     * 折扣
     */
    @ApiModelProperty(value="折扣")
    private BigDecimal discount;
    /**
     * 基础运费
     */
    @ApiModelProperty(value="基础运费")
    private BigDecimal basicFreight;
    }
