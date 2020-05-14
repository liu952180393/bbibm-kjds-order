package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 *@描述：商户模块
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-08 12:00:03
 **/
@Data
@TableName("bbibm_kjds_goods")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户模块")
public class Goods extends Model<Goods> {
private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    @TableId(value = "sid",type = IdType.INPUT)
    @ApiModelProperty(value="店铺id")
    private String sid;
    /**
     * 店铺名
     */
    @ApiModelProperty(value="店铺名")
    private String sname;
    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String email;
    /**
     * 联系方式
     */
    @ApiModelProperty(value="联系方式")
    private String phone;
    /**
     * 营业只执照类型
     */
    @ApiModelProperty(value="营业只执照类型")
    private String blicenseType;
    /**
     * 部门id
     */
    @ApiModelProperty(value="部门id")
    private String deptId;
    /**
     * 公司id
     */
    @ApiModelProperty(value="公司id")
    private String cid;
    /**
     * 店铺状态(0禁用1启用)
     */
    @ApiModelProperty(value="店铺状态(0启用 1禁用 2、审核中 3、审核失败 4、未付款)")
//    @TableLogic
    private String status;
    /**
     * 店铺启用类型（0,启动账户1sku加密，sku追加国家简称）
     */
    @ApiModelProperty(value="店铺启用类型（0,启动账户1sku加密，sku追加国家简称）")
    private String btype;
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
    @ApiModelProperty(value="开户站点(商店ID)")
    private String accountSiteId;
    /**
     * 开户人
     */
    @ApiModelProperty(value="开户人")
    private String uid;
    /**
     * 身份证正面
     */
    @ApiModelProperty(value="身份证正面")
//    @TableField(exist = false)
    private String cardPositiveUrl;
    /**
     * 身份证反面
     */
    @ApiModelProperty(value="身份证反面")
//    @TableField(exist = false)
    private String cardSideUrl;
    /**
     * 身份证号
     */
    @ApiModelProperty(value="身份证号")
    private String cardNum;

    /**
     * 身份证名
     */
    @ApiModelProperty(value="身份证名")
    private String cardName;
    /**
     * 身份证有效开始时间
     */
    @ApiModelProperty(value="身份证有效开始时间")
    @TableField(exist = false)
    private LocalDateTime cardBtime;
    /**
     * 身份证有效结束时间
     */
    @ApiModelProperty(value="身份证有效结束时间")
    private LocalDateTime cardEtime;
    /**
     * 身份证证件地址
     */
    @ApiModelProperty(value="身份证证件地址")
    private String cardAddr;
    /**
     * 营业执照
     */
    @ApiModelProperty(value="营业执照")
    private String blicenseUrl;
    /**
     * 营业执照统一社会编码
     */
    @ApiModelProperty(value="营业执照统一社会编码")
    private String blicenseCode;
    /**
     * 营业执照法人
     */
    @ApiModelProperty(value="营业执照法人")
    private String blicenseName;
    /**
     * 营业执照地址
     */
    @ApiModelProperty(value="营业执照地址")
    private String blicenseAddr;
    /**
     * 银行卡图片
     */
    @ApiModelProperty(value="银行卡图片")
    @TableField(exist = false)
    private String bankUrl;
    /**
     * 银行卡号
     */
    @ApiModelProperty(value="银行卡号")
    private String bankNum;

    /**
     * vps用户
     */
    @ApiModelProperty(value="vps用户")
    private String vpsName;
    /**
     * vps地址
     */
    @ApiModelProperty(value="vps地址")
    private String vpsAddr;
    /**
     * vps密码
     */
    @ApiModelProperty(value="vps密码")
    private String vpsPwd;
    /**
     * 卖家编号
     */
    @ApiModelProperty(value="卖家编号")
    private String merchantId;
    /**
     * MWS授权token
     */
    @ApiModelProperty(value="MWS授权token")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String mwsToken;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime ctime;
    /**
     * 操作人
     */
    @ApiModelProperty(value="操作人")
    private String cuid;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime utime;
    /**
     * 修改人
     */
    @ApiModelProperty(value="修改人")
    private String uuid;

    /**
     * 平台id
     */
    @ApiModelProperty(value="平台ID")
    private String pid;

    @ApiModelProperty(value="区域")
    private String area;

    @ApiModelProperty(value = "申请店铺类型（1、个人 2、企业",required = true,dataType = "int")
    private int type;

    @ApiModelProperty(value = "证件类型（1、二代身份证 2、临时身份证）",required = true,dataType = "int")
    private int idcardType;

    @ApiModelProperty(value = "用户国籍id",required = true,dataType = "String")
    private String applyType;

    @ApiModelProperty(value = "亚马逊开发者密钥",required = true,dataType = "String")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String secretKey;

    @ApiModelProperty(value = "亚马逊开发者id",required = true,dataType = "String")
    @TableField(strategy = FieldStrategy.IGNORED)
    private String awsAccessKeyId;

    @ApiModelProperty(value = "店铺LOGO",required = true,dataType = "店铺LOGO")
    private String goodsPhoto;

    }
