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
import java.time.LocalDateTime;


/**
 * @描述：用户订单金额表
 * @项目：
 * @公司：软江科技
 * @作者：yh
 * @时间：2019-12-18 17:32:07
 **/
@Data
@TableName("bbibm_kjds_user_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户订单金额表")
public class UserOrder extends Model<UserOrder> {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "订单id")
    private String oid;
    /**
     * 支付用户id
     */
    @ApiModelProperty(value = "支付用户id")
    private String payUid;
    /**
     * 收款用户
     */
    @ApiModelProperty(value = "收款用户")
    private String recvUid;
    /**
     * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @ApiModelProperty(value = "实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分")
    private BigDecimal amount;
    /**
     * 支付类型，1、微信支付，2、支付宝支付
     */
    @ApiModelProperty(value = "支付类型，1、微信支付，2、支付宝支付")
    private String amountType;
    /**
     * 状态：1、未付款，2、已付款
     */
    @ApiModelProperty(value = "状态：1、未付款，2、已付款")
    private String status;
    /**
     * 服务费
     */
    @ApiModelProperty(value = "服务费")
    private String servicePrice;
    /**
     * 付款时间
     */
    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;
    /**
     * 订单创建时间
     */
    @ApiModelProperty(value = "订单创建时间")
    private LocalDateTime ctime;
    /**
     * 订单更新时间
     */
    @ApiModelProperty(value = "订单更新时间")
    private LocalDateTime utime;
    /**
     * 订单类型（1充值，2提现、3平台代发货扣款）
     */
    @ApiModelProperty(value = "订单类型（1充值，2提现、3平台代发货扣款）")
    private String orderType;
    /**
     * 订单描述
     */
    @ApiModelProperty(value = "订单描述")
    private String description;
}
