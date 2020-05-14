package com.rjkj.cf.bbibm.kjds.goods.reqvo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
/**
 * @描述：提现信息实体
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：Yihao
 * @时间：2019/10/8 14:58
 **/
@ApiModel(value = "提现信息实体")
@Data
public class CashOutVo {
    /**
     * 提现到那个平台 0 微信钱包 1 支付宝
     */
    @ApiModelProperty(value="提现到那个平台 1 微信钱包 2 支付宝")
    private String type;

    /**
     * 提现的支付宝或微信账号
     */
    @ApiModelProperty(value="提现的支付宝或微信账号")
    private String userNumber;
    /**
     * 会员真实姓名
     */
    @ApiModelProperty(value="会员真实姓名")
    private String userName;
    /**
     * 提现金额
     */
    @ApiModelProperty(value="提现金额")
    private BigDecimal amount;
    /**
     * 提现类型 1城市代理人 2 自招成员
     */
    @ApiModelProperty(value="提现类型 1城市代理人 2 自招成员 3供应商 4余额提现")
    private String cashOutType;


}
