package com.rjkj.cf.bbibm.kjds.product.advertisement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *@描述：广告信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-28 15:52:05
 **/
@Data
@TableName("bbibm_advertisement")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "广告信息")
public class Advertisement extends Model<Advertisement> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 图片地址
     */
    @ApiModelProperty(value="图片地址")
    private String image;
    /**
     * 跳转链接
     */
    @ApiModelProperty(value="跳转链接")
    private String url;
    /**
     * 倒计时
     */
    @ApiModelProperty(value="倒计时")
    private Integer days;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 启用状态（1为启用 0为禁用）
     */
    @ApiModelProperty(value="启用状态（1为启用 0为禁用）")
    private String state;
    }
