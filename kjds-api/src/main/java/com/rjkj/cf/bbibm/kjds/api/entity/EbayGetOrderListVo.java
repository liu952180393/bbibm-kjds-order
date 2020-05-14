package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 获取订单列表请求实体类
 */
@Data
@ApiModel(value = "获取订单列表请求实体")
public class EbayGetOrderListVo {
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
    /**
     * 用户Token
     */
    @ApiModelProperty(value="用户Token")
    private String token;
}
