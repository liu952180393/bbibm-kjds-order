package com.rjkj.cf.bbibm.kjds.api.utils;

/**
 * @description: 亚马逊请求默认参数
 */
public class AmazonConstant {

    /**************************公用参数***************************/
    //开发者密钥
//    public  static final String SECRET_KEY = "ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34";
    //开发者id
//    public static final  String AWS_ACCESS_KEY_ID = "AKIAI4SKJMBRKZLSLLTQ";
    //商家token
//    public static final String MWS_AUTH_TOKEN = "amzn.mws.50b66e91-1241-55ef-35e7-23256025ca1e";
    //别人店铺
//    public static final String MWS_AUTH_TOKEN = "amzn.mws.56af5b71-a0ef-1d49-e464-3909bad5ba91";
    //商家id
//    public static final String SELLER_ID = "A2IUQHSA9UZAA4";
    //别人的id
//    public static final String SELLER_ID = "A1YCDQ1PL30G6D";
    //地区
//    public static final String US = "ATVPDKIKX0DER";
    //签名方式
    public static final String ALGORITHM = "HmacSHA256";
    //时间转换格式
    public static final String TIME_FORMAT_STR = "yyyy'-'MM'-'dd'T'HH':'mm':'ss'Z'";
    //编码格式
    public static final String CHARACTER_ENCODING = "UTF-8";
    //版本
    public static final String SIGNATURE_VERSION ="2";





    /*********************上传数据参数***************************/

    //post上传请求地址
//    public static final String ADD_POST_URL = "https://mws.amazonservices.com/";
    //订单api版本
    public static final String ADD_VERSION = "2009-01-01";
    //请求的Amazon方法
    public static final String SUBMIT_FEED_ACTION = "SubmitFeed";





    /************************查询订单参数************************/
    //post订单请求地址
//    public static final String ORDER_POST_URL = "https://mws.amazonservices.com/Orders/2013-09-01";
    //订单api版本
    public static final String ORDER_VERSION = "2013-09-01";
    //请求的Amazon方法ListOrders
    public static final String ORDER_ACTION = "ListOrders";
    //订单中的所有商品均已发货
    public static final String ORDER_SHIPPED ="Shipped";
    //请求的Amazon方法GetOrder
    public static final String GET_ORDER="GetOrder";
    //请求的Amazon方法ListOrderItems
    public static final String ORDER_ITEM_ACTION = "ListOrderItems";


    /************************查询商品参数************************/
    //post订单请求地址
//    public static final String ITEM_POST_URL = "https://mws.amazonservices.com/Products/2011-10-01";
    //请求的Amazon方法OrderIteam
    public static final String ITEM_ACTION = "GetMatchingProductForId";
    //商品api版本
    public static final String ITEM_VERSION = "2011-10-01";







    /*****************************亚马逊区域标识******************************/
    //巴西地区
    public static final String COUNTRY_CODE_BR = "A2Q3Y263D00KWC";
    //加拿大地区
    public static final String COUNTRY_CODE_CA = "A2EUQ1WTGCTBG2";
    //墨西哥地区
    public static final String COUNTRY_CODE_MX = "A1AM78C64UM0Y8";
    //美国地区
    public static final String COUNTRY_CODE_US = "ATVPDKIKX0DER";
    //阿拉伯联合酋长国（UAE）地区
    public static final String COUNTRY_CODE_AE = "A2VIGQ35RCS4UG";
    //德国地区
    public static final String COUNTRY_CODE_DE = "A1PA6795UKMFR9";
    //西班牙地区
    public static final String COUNTRY_CODE_ES = "A1RKKUPIHCS9HS";
    //法国地区
    public static final String COUNTRY_CODE_FR = "A13V1IB3VIYZZH";
    //英国地区
    public static final String COUNTRY_CODE_GB = "A1F83G8C2ARO7P";
    //印度地区
    public static final String COUNTRY_CODE_IN = "A21TJRUUN4KGV";
    //意大利地区
    public static final String COUNTRY_CODE_IT = "APJ6JRA9NG5V4";
    //土耳其地区
    public static final String COUNTRY_CODE_TR = "A33AVAJ2PDY3EV";
    //新加坡地区
    public static final String COUNTRY_CODE_SG = "A19VAU5U5O7RUS";
    //澳大利亚地区
    public static final String COUNTRY_CODE_AU = "A39IBJ37TRP1C6";
    //日本地区
    public static final String COUNTRY_CODE_JP = "A1VC38T7YXB528";




}
