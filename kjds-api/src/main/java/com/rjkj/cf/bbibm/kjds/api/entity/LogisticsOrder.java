package com.rjkj.cf.bbibm.kjds.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @描述：云涂物流申请发货
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2020-01-13 15:22:37
 **/
@Data
@TableName("bbibm_logistics_order")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "云涂物流申请发货")
public class LogisticsOrder extends Model<LogisticsOrder> {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value = "")
    private String id;
    /**
     * 客户订单号,不能重复
     */
    @ApiModelProperty(value = "客户订单号,不能重复")
    private String customerOrderNumber;
    /**
     * 运输方式代码
     */
    @ApiModelProperty(value = "运输方式代码")
    private String shippingMethodCode;
    /**
     * 运单包裹的件数，必须大于0的整数
     */
    @ApiModelProperty(value = "运单包裹的件数，必须大于0的整数")
    private Integer packageCount;
    /**
     * 运单包裹的件数，必须大于0的整数
     */
    @ApiModelProperty(value = "是否退回,包裹无人签收时是否退回，1-退回，0-不退回，默认0")
    private Integer returnOption;
    /**
     * 预估包裹总重量，单位kg,最多3位小数
     */
    @ApiModelProperty(value = "预估包裹总重量，单位kg,最多3位小数")
    private BigDecimal weight;
    /**
     * 收件人所在国家，填写国际通用标准2位简码
     */
    @ApiModelProperty(value = "收件人所在国家，填写国际通用标准2位简码")
    private String countryCode;
    /**
     * 收件人姓
     */
    @ApiModelProperty(value = "收件人姓")
    private String firstName;
    /**
     * 收件人名字
     */
    @ApiModelProperty(value = "收件人名字")
    private String lastName;
    /**
     * 收件人详细地址
     */
    @ApiModelProperty(value = "收件人详细地址")
    private String street;
    /**
     * 收件人所在城市
     */
    @ApiModelProperty(value = "收件人所在城市")
    private String city;
    /**
     * 收件人所在省/州
     */
    @ApiModelProperty(value = "收件人所在省/州")
    private String state;
    /**
     * 收件人邮编
     */
    @ApiModelProperty(value = "收件人邮编")
    private String zip;
    /**
     * 收件人电话
     */
    @ApiModelProperty(value = "收件人电话")
    private String phone;
    /**
     * 包裹申报名称(英文)必填
     */
    @ApiModelProperty(value = "包裹申报名称(英文)必填")
    private String eName;
    /**
     * 包裹申报名称(中文)
     */
    @ApiModelProperty(value = "包裹申报名称(中文)")
    private String cName;
    /**
     * 申报数量,必填
     */
    @ApiModelProperty(value = "申报数量,必填")
    private Integer quantity;
    /**
     * 申报价格(单价),单位USD,必填
     */
    @ApiModelProperty(value = "申报价格(单价),单位USD,必填")
    private BigDecimal unitPrice;
    /**
     * 申报重量(单重)，单位kg
     */
    @ApiModelProperty(value = "申报重量(单重)，单位kg")
    private BigDecimal unitWeight;
    /**
     * 订单备注，用于打印配货单
     */
    @ApiModelProperty(value = "订单备注，用于打印配货单")
    private String remark;
    /**
     * 申报币种，默认：USD
     */
    @ApiModelProperty(value = "申报币种，默认：USD")
    private String currencyCode;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 关联平台订单ID
     */
    @ApiModelProperty(value = "关联平台订单ID")
    private String platformOrderId;
    /**
     * 所属平台id
     */
    @ApiModelProperty(value = "所属平台id")
    private String platformPid;

}
