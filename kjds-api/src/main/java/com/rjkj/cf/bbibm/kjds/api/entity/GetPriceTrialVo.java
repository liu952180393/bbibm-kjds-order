package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 云涂物流查询价格Vo
 */
@Data
public class GetPriceTrialVo {
    @ApiModelProperty(value="国家简码")
    private String countryCode;
    @ApiModelProperty(value="包裹重量，单位kg,支持3位小数")
    private BigDecimal weight;
    @ApiModelProperty(value="包裹长度,单位cm,不带小数,不填写默认1")
    private Integer length;
    @ApiModelProperty(value="包裹长度,单位cm,不带小数,不填写默认1")
    private Integer width;
    @ApiModelProperty(value="包裹高度,单位cm,不带小数，不填写默认1")
    private Integer height;
    @ApiModelProperty(value="包裹类型，1-包裹，2-文件，3-防水袋，默认1")
    private Integer packageType;
}
