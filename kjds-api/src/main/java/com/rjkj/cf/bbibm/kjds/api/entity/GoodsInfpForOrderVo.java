package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 平台基本信息实体类（订单查询需要的参数）
 */
@Data
@ApiModel(value = "平台基本信息实体类（订单查询需要的参数）")
public class GoodsInfpForOrderVo {

    //店铺id
    private String shopId;

    //店铺token
    private String token;

    //店铺区域标识
    private String area;

    //开发者密钥
    private String secretKey;

    //开发者id
    private String awsAccessKeyId;



}
