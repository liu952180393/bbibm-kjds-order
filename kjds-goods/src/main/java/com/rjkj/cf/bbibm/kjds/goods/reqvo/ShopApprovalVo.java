package com.rjkj.cf.bbibm.kjds.goods.reqvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：店铺审批成功填写信息实体
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：Yihao
 * @时间：2019/10/8 14:58
 **/
@ApiModel(value = "店铺审批成功填写信息实体")
@Data
public class ShopApprovalVo {
 @ApiModelProperty(value="店铺ID")
 private String sid;
 @ApiModelProperty(value="申请人ID")
 private String uid;
 @ApiModelProperty(value="店铺token")
 private String mwsToken;
 @ApiModelProperty(value="申请人地址")
 private String applicantArea;
 @ApiModelProperty(value="平台店铺地址代码")
 private String shopArea;
 @ApiModelProperty(value="平台店铺ID")
 private String shopId;
 @ApiModelProperty(value="开发者秘钥")
 private String secretKey;
 @ApiModelProperty(value="开发者ID")
 private String awsAccessKeyId;
 @ApiModelProperty(value="0 同意  1拒绝")
 private String type;
}
