package com.bbibm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 *@描述：商品表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-13 14:36:24
 **/
@Data
@TableName("orders")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商品表")
public class Orders extends Model<Orders> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer orderId;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String estimatedShippingFee;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String paymentMethod;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String messageToSeller;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String shippingCarrier;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String currency;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private LocalDateTime createTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private LocalDateTime payTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String daysToShip;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private LocalDateTime shipByDate;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String trackingNo;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String orderStatus;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private LocalDateTime updateTime;
    /**
     * 
     */
    /**
     *
     */
    @ApiModelProperty(value="")
    private ArrayList<recipientAddress> recipientAddress;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String escrowAmount;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private Integer goodsToDeclare;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String totalAmount;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String serviceCode;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String country;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String ordersn;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String buyerUsername;
    /**
     *
     */
    @ApiModelProperty(value="")
    private ArrayList<Items> items;
    }
