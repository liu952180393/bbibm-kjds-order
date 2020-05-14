package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 *@描述：合伙人审核表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 11:58:47
 **/
@Data
@TableName("bbibm_kjds_partner")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "合伙人审核表")
public class Partner extends Model<Partner> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="")
    private String id;
    /**
     * 价格id
     */
    @ApiModelProperty(value="价格id")
    private String partnerPriceId;
    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名")
    private String name;
    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    private String email;
    /**
     * 申请说明
     */
    @ApiModelProperty(value="申请说明")
    private String remake;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime ctime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private String cuid;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime utime;
    /**
     * 修改人
     */
    @ApiModelProperty(value="修改人")
    private String uuid;
    /**
     * 省
     */
    @ApiModelProperty(value="省")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(value="市")
    private String city;
    /**
     * 区
     */
    @ApiModelProperty(value="区")
    private String area;
    /**
     * 县
     */
    @ApiModelProperty(value="县")
    private String county;
    /**
     * 审核人id
     */
    @ApiModelProperty(value="审核人id")
    private String optionUid;
    /**
     * 审核时间
     */
    @ApiModelProperty(value="审核时间")
    private LocalDateTime optionTime;
    /**
     * 审核状态（1、审核成功 2、审核失败）
     */
    @ApiModelProperty(value="审核状态（0、审核中 1、审核成功 2、审核失败 3、未支付）")
    private String optionStatus;
    /**
     * 审核失败原因
     */
    @ApiModelProperty(value="审核失败原因")
    private String optionErrorMsg;

    /**
     * 订单id
     */
    @ApiModelProperty(value="订单id")
    private String orderNo;

    /**
     * 开始时间(城市合伙人)
     */
    @ApiModelProperty(value="开始时间(城市合伙人)")
    private LocalDateTime beginTime;

    /**
     * 结束时间(城市合伙人)
     */
    @ApiModelProperty(value="结束时间(城市合伙人)")
    private LocalDateTime endTime;

    /**
     *合伙人类型名称
     */
    @TableField(exist = false)
    @ApiModelProperty(value="合伙人类型名称")
    private String typeName;

    /**
     * 合伙人支付价格
     */
    @TableField(exist = false)
    @ApiModelProperty(value="合伙人支付价格")
    private BigDecimal price;

    /**
     * 申请开始时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "申请开始时间")
    private String applaStartTime;

    /**
     * 申请结束时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "申请结束时间")
    private String applaEndTime;
    }
