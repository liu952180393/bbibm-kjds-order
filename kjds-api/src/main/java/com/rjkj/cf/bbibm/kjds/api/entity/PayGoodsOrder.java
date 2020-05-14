package com.rjkj.cf.bbibm.kjds.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/31 15:25
 **/

@ApiModel("支付商家订单信息")
@Data
public class PayGoodsOrder {


    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 金额,单位分
     */
    private BigDecimal amount;

    /**
     * 支付订单号
     * 备注：这个订单号是商户本地生成的单号
     * 传递订单号的时候传递这个字段
     */
    private String payOrderId;

    /**
     * 回调业务逻辑处理地址
     */
    private String  callUrl;

}
