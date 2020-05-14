package com.bbibm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;


/**
 *@描述：商铺表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-14 10:36:48
 **/
@Data
@TableName("bbibm_kjds_goods")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商铺表")
public class BbibmKjdsGoods extends Model<BbibmKjdsGoods> {
private static final long serialVersionUID = 1L;

    /**
     * 店铺id
     */
    @TableId
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
     * 店铺状态(0启用 1禁用 2、审核中 3、审核失败 4、未付款)
     */
    @ApiModelProperty(value="店铺状态(0启用 1禁用 2、审核中 3、审核失败 4、未付款)")
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
     * 开户站点(商店ID)
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
    private String cardPositiveUrl;
    /**
     * 身份证反面
     */
    @ApiModelProperty(value="身份证反面")
    private String cardSideUrl;
    /**
     * 身份证号
     */
    @ApiModelProperty(value="身份证号")
    private String cardNum;
    /**
     * 身份证有效开始时间
     */
    @ApiModelProperty(value="身份证有效开始时间")
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
    private String bankUrl;
    /**
     * 银行卡号
     */
    @ApiModelProperty(value="银行卡号")
    private String bankNum;
    /**
     * 身份证名
     */
    @ApiModelProperty(value="身份证名")
    private String cardName;
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
    @ApiModelProperty(value="平台id")
    private Integer pid;
    /**
     * 店铺头像
     */
    @ApiModelProperty(value="店铺头像")
    private String goodsPhoto;
    /**
     * 区域
     */
    @ApiModelProperty(value="区域")
    private String area;
    /**
     * 证件类型（1、二代身份证 2、临时身份证）
     */
    @ApiModelProperty(value="证件类型（1、二代身份证 2、临时身份证）")
    private Integer idcardType;
    /**
     * 账户类型（1、个人 2、企业）
     */
    @ApiModelProperty(value="账户类型（1、个人 2、企业）")
    private Integer type;
    /**
     * 国籍id
     */
    @ApiModelProperty(value="国籍id")
    private String applyType;
    /**
     * 审核人
     */
    @ApiModelProperty(value="审核人")
    private String operUid;
    /**
     * 审核失败消息
     */
    @ApiModelProperty(value="审核失败消息")
    private String errorMsg;
    /**
     * 亚马逊开发者密钥
     */
    @ApiModelProperty(value="亚马逊开发者密钥")
    private String secretKey;
    /**
     * 亚马逊开发者id
     */
    @ApiModelProperty(value="亚马逊开发者id")
    private String awsAccessKeyId;
    }
