package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 统计订单状态数量实体类
 */
@Data
@ApiModel(value = "统计订单状态数量")
public class OrderStatusCountVo {

    /**
     * 待付款
     */
    @ApiModelProperty(value = "未付款")
    private String unpaid;

    /**
     * 待发货
     */
    @ApiModelProperty(value = "待发货")
    private String notShipped;

    /**
     * 已发货
     */
    @ApiModelProperty(value = "已发货")
    private String shipped;

    /**
     * 已完成
     */
    @ApiModelProperty(value = "已完成")
    private String complete;

    /**
     * 已取消
     */
    @ApiModelProperty(value = "已取消")
    private String cancelled;

    /**
     * 今日新订单
     */
    @ApiModelProperty(value = "今日新订单")
    private String newOrder;

    /**
     * 发货前退款
     */
    @ApiModelProperty(value = "发货前退款")
    private String refundBefore;

    /**
     * 待退款
     */
    @ApiModelProperty(value = "待退款")
    private String pendingRefund;

    /**
     * 已入账
     */
    @ApiModelProperty(value = "已入账")
    private String arrival;


}
