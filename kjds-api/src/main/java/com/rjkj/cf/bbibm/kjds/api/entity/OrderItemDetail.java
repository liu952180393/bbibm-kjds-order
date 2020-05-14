package com.rjkj.cf.bbibm.kjds.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：订单商品详情
 * @项目：
 * @公司：软江科技
 * @作者：YIHao
 * @时间：2019-10-09 11:28:29
 **/
@Data
@TableName("bbibm_ebay_order_item_detail")
@ApiModel(value = "Ebay订单商品详情")
public class OrderItemDetail {
    private static final long serialVersionUID = 1L;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String productName;

    /**
     * 商品变体SKU
     */
    @ApiModelProperty(value = "商品变体SKU")
    private String variationSku;
    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private String amount;

    /**
     * 商品主图
     */
    @ApiModelProperty(value = "商品主图")
    private String image;

    /**
     * 供应商用户ID
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "供应商用户ID")
    private String supplierUserId;

}
