package com.rjkj.cf.bbibm.kjds.goods.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/22 10:02
 **/
@ApiModel("app代理商申请实体")
@Data
public class PartnerApplyReq {

    /**
     *城市合伙人价格id
     */
    @ApiModelProperty(value = "城市合伙人价格id",required = true,dataType = "String")
    private String partnerPriceId;

    /**
     * 姓名
     */
    @ApiModelProperty(value="姓名",required = true,dataType = "String")
    private String name;
    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱",required = true,dataType = "String")
    private String email;
    /**
     * 申请说明
     */
    @ApiModelProperty(value="申请说明",required = true,dataType = "String")
    private String remake;

    /**
     * 省
     */
    @ApiModelProperty(value="省",dataType = "String")
    private String province;
    /**
     * 市
     */
    @ApiModelProperty(value="市",dataType = "String")
    private String city;
    /**
     * 区
     */
    @ApiModelProperty(value="区",dataType = "String")
    private String area;
    /**
     * 县
     */
    @ApiModelProperty(value="县",dataType = "String")
    private String county;

    /**
     * 支付类型
     */
    @ApiModelProperty(value="支付类型（1、微信2、支付宝）",dataType = "int",required = true)
    private Integer payType;

}
