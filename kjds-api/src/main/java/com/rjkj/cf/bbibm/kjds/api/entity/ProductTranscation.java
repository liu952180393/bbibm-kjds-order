package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/28 18:58
 **/
@ApiModel("产品描述翻译")
@Data
public class ProductTranscation {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("描述")
    private String desc;
}
