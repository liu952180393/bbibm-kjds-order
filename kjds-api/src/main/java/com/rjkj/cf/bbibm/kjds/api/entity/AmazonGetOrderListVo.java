package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询亚马逊订单请求参数
 */
@Data
@ApiModel(value = "查询亚马逊订单请求参数")
public class AmazonGetOrderListVo {

    /**
     * 开始时间
     */
    @ApiModelProperty(value="开始时间")
    private String startTime;
    /**
     * 结束时间
     */
    @ApiModelProperty(value="结束时间")
    private String endTime;

}
