package com.rjkj.cf.bbibm.kjds.product.rate.entity;

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
 *@描述：汇率信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-24 14:07:34
 **/
@Data
@TableName("bbibm_rate")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "汇率信息")
public class Rate extends Model<Rate> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 国家简称
     */
    @ApiModelProperty(value="国家简称")
    private String name;
    /**
     * 汇率（对人民币）
     */
    @ApiModelProperty(value="汇率（对人民币）")
    private BigDecimal rate;
    }
