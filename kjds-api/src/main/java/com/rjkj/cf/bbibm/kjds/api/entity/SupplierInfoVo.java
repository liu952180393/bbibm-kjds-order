package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @描述：供应商提成信息
 * @项目：rjkj
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/18 10:36
 **/
@ApiModel("供应商提成信息")
@Data
public class SupplierInfoVo {

    @ApiModelProperty("提成类型（1、城市合伙人 2、自推荐提成 3、供应商提成）")
    private int  commissionType;

    @ApiModelProperty("今日提成")
    private BigDecimal commissionNowadays;

    @ApiModelProperty("总提成")
    private BigDecimal commissionTotal;

}
