package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/16 10:04
 **/
@ApiModel("用户平台信息")
@Data
public class PlatformInfoVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("平台名称")
    private String name;


    @ApiModelProperty("平台类型（亚马逊商城1  ,  Ebay商城 2 ,  Shopee商城  3）")
    private int type;

    @ApiModelProperty("平台ID")
    private String shopId;

    @ApiModelProperty("店铺图片")
    private String image;

    @ApiModelProperty("获取平台订单状态URL")
    private String orderStatusCount;

    @ApiModelProperty("获取平台各状态下的订单列表")
    private String orderListByStatus;

    @ApiModelProperty("平台根据订单号查询订单详细信息")
    private String orderById;

    @ApiModelProperty("价格")
    private String price;

}
