package com.rjkj.cf.bbibm.kjds.api.utils;

public class ShopeeConstant {
    /**
     * 开发者ID
     */
    public static final int PARTNER_ID = 843449;
    /**
     * Shopee开发者秘钥
     */
    public static final String PARTNER_KEY = "28c1d5dda7175ae20da943db03b425af5b25a140bdb2b98858fd17276e3e1462";
    /**
     * 上传商品
     */
    public static final String ADD_ITEM_URL = "https://partner.shopeemobile.com/api/v1/item/add";
    /**
     * 根据分类获取必要属性
     */
    public static final String GET_ATTRIBUTES_URL = "https://partner.shopeemobile.com/api/v1/item/attributes/get";
    /**
     * 修改库存
     */
    public static final String UPDATE_STOCK_URL = "https://partner.shopeemobile.com/api/v1/items/update_stock";
    /**
     * 修改价格
     */
    public static final String UPDATE_PRICE_URL = "https://partner.shopeemobile.com/api/v1/items/update_price";
    /**
     * 删除商品
     */
    public static final String DELETE_ITEM_URL = "https://partner.shopeemobile.com/api/v1/item/delete";
    /**
     * 添加单层变体
     */
    public static final String ADD_VARIATIONS_URL = "https://partner.shopeemobile.com/api/v1/item/add_variations";
    /**
     * 删除变体
     */
    public static final String DELETE_VARIATION_URL = "https://partner.shopeemobile.com/api/v1/item/delete_variation";
    /**
     * 获取商品详细信息
     */
    public static final String GET_ITEMDETAIL_URL = "https://partner.shopeemobile.com/api/v1/item/get";
    /**
     * 根据时间查询订单
     */
    public static final String GET_ORDERSLIST_URL = "https://partner.shopeemobile.com/api/v1/orders/basics";
    /**
     * 根据订单状态查询订单
     */
    public static final String GET_ORDERS_BY_STATUS_URL = "https://partner.shopeemobile.com/api/v1/orders/get";
    /**
     * 获取商店的物流信息
     */
    public static final String GET_LOGISTICS_URL = "https://partner.shopeemobile.com/api/v1/logistics/channel/get";
    /**
     * 新增双层变体
     */
    public static final String INIT_TIERVARIATION_URL = "https://partner.shopeemobile.com/api/v1/item/tier_var/init";
    /**
     * 根据订单号获取订单详情
     */
    public static final String GET_ORDER_DETAILS = "https://partner.shopeemobile.com/api/v1/orders/detail";
    /**
     * 批量修改商品变体价格
     */
    public static final String UPDATE_VARIATION_PRICE_BATCH = "https://partner.shopeemobile.com/api/v1/items/update/vars_price";
    /**
     * 批量修改商品变体价格
     */
    public static final String GET_FORDER_WAYBILL = "https://partner.shopeemobile.com/api/v1/logistics/forder_waybill/get_mass";


    /*****************************时间设置**************************************/
    /**
     * 同步多久以内时间的订单
     */
    public static final int SYNCHRONIZE_ORDER_TIME = 15;
}
