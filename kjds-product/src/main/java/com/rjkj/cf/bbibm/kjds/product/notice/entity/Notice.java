//package com.rjkj.cf.bbibm.kjds.product.notice.entity;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.baomidou.mybatisplus.extension.activerecord.Model;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// *@描述：系统消息
// *@项目：
// *@公司：软江科技
// *@作者：crq
// *@时间：2019-10-18 14:47:09
// **/
//@Data
//@TableName("bbibm_notice")
//@EqualsAndHashCode(callSuper = true)
//@ApiModel(value = "系统消息")
//public class Notice extends Model<Notice> {
//private static final long serialVersionUID = 1L;
//
//    /**
//     * id
//     */
//    @TableId(value = "id",type = IdType.INPUT)
//    @ApiModelProperty(value="id")
//    private String id;
//    /**
//     * 内容
//     */
//    @ApiModelProperty(value="内容")
//    private String description;
//    /**
//     * 0为系统消息，1为公告
//     */
//    @ApiModelProperty(value="0为系统消息，1为公告")
//    private Integer type;
//    /**
//     *
//     */
//    @ApiModelProperty(value="用户id")
//    private String userId;
//    /**
//     * 0为未读，1为已读
//     */
//    @ApiModelProperty(value="0为未读，1为已读")
//    private Integer state;
//    /**
//     * 创建时间
//     */
//    @ApiModelProperty(value="创建时间")
//    private LocalDateTime createTime;
//    }
