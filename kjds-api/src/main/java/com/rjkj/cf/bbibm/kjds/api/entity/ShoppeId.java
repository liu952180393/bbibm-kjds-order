package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/16 18:59
 **/
@ApiModel("店铺站点id")
@Data
public class ShoppeId {

    @ApiModelProperty("店铺id")
    private String sid;

    @ApiModelProperty("站点id")
    private String shoppid;
}
