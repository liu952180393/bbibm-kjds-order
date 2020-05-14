package com.rjkj.cf.bbibm.kjds.goods.entity;

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
 *@描述：店铺申请订单信息
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-17 15:35:49
 **/
@Data
@TableName("bbibm_kjds_goods_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "店铺申请订单信息")
public class GoodsOrder extends Model<GoodsOrder> {
private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="订单id")
    private String oid;
    /**
     * 用户id
     */
    @ApiModelProperty(value="支付用户")
    private String payUid;


    /**
     * 用户id
     */
    @ApiModelProperty(value="收款用户")
    private String recvUid;


    /**
     * 
     */
    @ApiModelProperty(value="店铺id")
    private String gid;
    /**
     * 地址id
     */
    @ApiModelProperty(value="地址id")
    private Long addrId;

    /**
     * 订单类型
     */
    @ApiModelProperty(value="订单类型（1城市合伙人提成 2、商家推荐提成 3、城市合伙人申请 4、申请店铺 5、其它）")
    private String orderType;
    /**
     * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @ApiModelProperty(value="实付金额。精确到2位小数")
    private BigDecimal amount;
    /**
     * 支付类型，1、货到付款，2、在线支付，3、微信支付，4、支付宝支付
     */
    @ApiModelProperty(value="支付类型，1、货到付款，2、在线支付，3、微信支付，4、支付宝支付")
    private String amountType;
    /**
     * 邮费。精确到2位小数;单位:元。如:200.07，表示:200元7分
     */
    @ApiModelProperty(value="邮费。精确到2位小数;")
    private String postFee;
    /**
     * 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
     */
    @ApiModelProperty(value="状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭")
    private String status;
    /**
     * 物流名称
     */
    @ApiModelProperty(value="物流名称")
    private String shippingName;
    /**
     * 物流单号
     */
    @ApiModelProperty(value="物流单号")
    private String shippingCode;
    /**
     * 退换无忧
     */
    @ApiModelProperty(value="退换无忧")
    private String noAnnoyance;
    /**
     * 服务费
     */
    @ApiModelProperty(value="服务费")
    private String servicePrice;
    /**
     * 返现
     */
    @ApiModelProperty(value="返现")
    private String returnPrice;
    /**
     * 订单总重 单位/克
     */
    @ApiModelProperty(value="订单总重 单位/克")
    private String totalWeight;
    /**
     * 买家是否已经评价
     */
    @ApiModelProperty(value="买家是否已经评价")
    private Integer buyerRate;
    /**
     * 交易关闭时间
     */
    @ApiModelProperty(value="交易关闭时间")
    private LocalDateTime closeTime;
    /**
     * 交易完成时间
     */
    @ApiModelProperty(value="交易完成时间")
    private LocalDateTime endTime;
    /**
     * 付款时间
     */
    @ApiModelProperty(value="付款时间")
    private LocalDateTime payTime;
    /**
     * 发货时间
     */
    @ApiModelProperty(value="发货时间")
    private LocalDateTime consignTime;
    /**
     * 订单创建时间
     */
    @ApiModelProperty(value="订单创建时间")
    private LocalDateTime ctime;
    /**
     * 订单更新时间
     */
    @ApiModelProperty(value="订单更新时间")
    private LocalDateTime utime;




    }
