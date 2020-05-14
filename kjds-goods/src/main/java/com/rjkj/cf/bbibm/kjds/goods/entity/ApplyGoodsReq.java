package com.rjkj.cf.bbibm.kjds.goods.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/17 14:58
 **/
@ApiModel("申请店铺信息")
@Data
public class ApplyGoodsReq {

    @ApiModelProperty(value = "申请店铺类型（1、个人 2、企业",required = true,dataType = "int")
    private int type;


    @ApiModelProperty(value = "正面手持身份证",required = true,dataType = "String")
    private String positiveIdCardUrl;

    @ApiModelProperty(value = "营业执照",required = true,dataType = "String")
    private String businessLicense;

    @ApiModelProperty(value = "反面手持身份证",required = true,dataType = "String")
    private String negativeIdCardUrl;

    @ApiModelProperty(value = "证件类型（1、二代身份证 2、临时身份证）",required = true,dataType = "int")
    private int idcardType;

    @ApiModelProperty(value = "用户国籍id",required = true,dataType = "String")
    private String applyType;

    @ApiModelProperty(value = "平台id",required = true,dataType = "String")
    private String pid;

    @ApiModelProperty(value = "支付类型（1、微信2、支付宝）",required = true,dataType = "int")
    private int payType;

    @ApiModelProperty(value = "店铺名称",required = true,dataType = "String")
    private String gname;


}
