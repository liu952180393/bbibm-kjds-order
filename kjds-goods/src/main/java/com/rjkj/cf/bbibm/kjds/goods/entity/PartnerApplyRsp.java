package com.rjkj.cf.bbibm.kjds.goods.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/22 10:17
 **/
@ApiModel("app城市合伙人返回实体")
@Data
public class PartnerApplyRsp {

    @ApiModelProperty("订单号")
    private String oid;

    @ApiModelProperty("支付金额")
    private String payAmount;

    @ApiModelProperty("支付加密信息")
    private String payInfo;


    @ApiModelProperty("appid")
    private String appid;
}
