package com.rjkj.cf.bbibm.kjds.goods.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/17 16:17
 **/
@ApiModel("申请店铺返回数据")
@Data
public class ApplyGoodsRsp {

    @ApiModelProperty("订单号")
    private String oid;

    @ApiModelProperty("支付金额")
    private String payAmount;

    @ApiModelProperty("加密数据")
    private String bb;
}
