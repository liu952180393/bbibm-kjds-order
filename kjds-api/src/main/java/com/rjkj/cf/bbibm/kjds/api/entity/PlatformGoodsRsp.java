package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/22 17:21
 **/
@ApiModel("我的店铺列表实体")
@Data
public class PlatformGoodsRsp {

    @ApiModelProperty(value = "平台id")
    private String pid;

    @ApiModelProperty(value = "平台名称")
    private String pname;

    @ApiModelProperty(value = "已上架数量")
    private int pShelfNum;

    @ApiModelProperty(value = "平台图片")
    private String platformPhoto;


    @ApiModelProperty(value = "店铺列表")
    List<GoodsRsp> goodsRspList;

    @ApiModelProperty("获取平台订单状态URL")
    private String orderStatusCount;

    @ApiModelProperty("获取平台各状态下的订单列表")
    private String orderListByStatus;

    @ApiModelProperty("平台根据订单号查询订单详细信息")
    private String orderById;

    @ApiModelProperty("根据产品ID下架")
    private String endItem;

    @ApiModelProperty("根据订单ID发货")
    private String orderToShip;

    @ApiModelProperty("平台代发货")
    private String proxyToShip;

    @ApiModelProperty("对已上架商品进行改价")
    private String updatePrice;


}
