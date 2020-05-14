package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @描述：提现信息表
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-11-11 15:48:25
 **/
@Data
@TableName("bbibm_cash_out_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "提现信息表")
public class CashOutInfo extends Model<CashOutInfo> {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @TableId(value = "ID", type = IdType.INPUT)
    @ApiModelProperty(value = "ID")
    private String id;
    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String uid;
    /**
     * 提现到那个平台 1 微信 2 支付宝
     */
    @ApiModelProperty(value="提现到那个平台 1 微信 2 支付宝")
    private String type;
    /**
     * 状态 状态 1 正在申请 2 申请通过 3 拒绝申请
     */
    @ApiModelProperty(value = "状态 状态 1 正在申请 2 申请通过 3 拒绝申请")
    private String status;
    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间")
    private LocalDateTime applytime;
    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private LocalDateTime checktime;
    /**
     * 支付时间
     */
    @ApiModelProperty(value = "支付时间")
    private LocalDateTime paytime;

    /**
     * 提现的支付宝或微信账号
     */
    @ApiModelProperty(value = "提现的支付宝或微信账号")
    private String userNumber;
    /**
     * 会员真实姓名
     */
    @ApiModelProperty(value = "会员真实姓名")
    private String userName;
    /**
     * 提现金额
     */
    @ApiModelProperty(value = "提现金额")
    private BigDecimal amount;
    /**
     * 手续费
     */
    @ApiModelProperty(value = "手续费")
    private BigDecimal fee;
    /**
     * 备注内容
     */
    @ApiModelProperty(value = "备注内容")
    private String contens;
    /**
     * 提现类型 1城市代理人 2 自招成员
     */
    @ApiModelProperty(value="提现类型 1城市代理人 2 自招成员  3 供应商 4余额提现")
    private String cashOutType;

    /**
     * 申请开始时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "申请开始时间")
    private String applaStartTime;

    /**
     * 申请结束时间
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "申请结束时间")
    private String applaEndTime;
}
