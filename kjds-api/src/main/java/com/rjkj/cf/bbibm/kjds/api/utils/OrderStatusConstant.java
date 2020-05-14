package com.rjkj.cf.bbibm.kjds.api.utils;

public class OrderStatusConstant {
    /**
     * 待付款
     */
    public static final String UNPAID_ORDER = "1";
    /**
     * 待发货
     */
    public static final String NOTSHIPPED_ORDER = "2";
    /**
     * 已发货
     */
    public static final String SHIPPED_ORDER = "3";
    /**
     * 已完成
     */
    public static final String COMPLETE_ORDER = "4";
    /**
     * 已取消
     */
    public static final String CANCELLED_ORDER = "5";
    /**
     * 今日新订单
     */
    public static final String TODAY_NEW_ORDER = "6";
    /**
     * 查看全部订单
     */
    public static final String ALL_ORDER = "7";
    /**
     * 发货前退款
     */
    public static final String REFUND_BEFORE = "8";
    /**
     * 待退款
     */
    public static final String PENDING_REFUND = "9";
    /**
     * 已入账
     */
    public static final String ALREADY_ARRIVED = "10";
}
