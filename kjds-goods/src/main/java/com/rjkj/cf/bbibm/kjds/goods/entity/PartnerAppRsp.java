package com.rjkj.cf.bbibm.kjds.goods.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/21 13:22
 **/
@ApiModel("城市合伙人价格列表")
@Data
public class PartnerAppRsp {

    @ApiModelProperty(value="id")
    private String id;


    /**
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String remake;

    /**
     * 价格
     */
    @ApiModelProperty(value="价格")
    private BigDecimal price;


    /**
     * 城市合伙人费用名称
     */
    @ApiModelProperty(value="城市合伙人费用名称")
    private String name;
}
