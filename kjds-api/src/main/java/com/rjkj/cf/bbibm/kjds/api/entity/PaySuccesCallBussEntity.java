package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/11/1 16:19
 **/
@ApiModel("支付成功后回调业务类处理")
@Data
public class PaySuccesCallBussEntity {

    @ApiModelProperty("订单号")
    private String oId;

    @ApiModelProperty("支付状态")
    private String payStatus;

    @ApiModelProperty("支付宝交易单号")
    private String tradeNo;

    @ApiModelProperty("支付金额")
    private String payAmount;

}
