package com.rjkj.cf.bbibm.kjds.goods.reqvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/8 14:58
 **/
@ApiModel(value = "添加店铺实体")
@Data
public class GoodsReqVo {

    /**
     * 店铺名
     */
    @ApiModelProperty(value="店铺名")
    private String sname;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 联系方式
     */
    @ApiModelProperty(value="联系方式")
    private String phone;

    /**
     * 订单处理时间天数
     */
    @ApiModelProperty(value="订单处理时间天数")
    private Integer oday;

    /**
     * 店铺处理百分比
     */
    @ApiModelProperty(value="店铺处理百分比")
    private Integer percentage;

    /**
     * 开户站点
     */
    @ApiModelProperty(value="开户站点")
    private String accountSiteId;
}
