package com.rjkj.cf.bbibm.kjds.product.apphome.entity;

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
 *@描述：app首页展示图标表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-15 10:37:47
 **/
@Data
@TableName("bbibm_app_home_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "app首页展示图标表")
public class AppHomeInfo extends Model<AppHomeInfo> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 分类属性id
     */
    @ApiModelProperty(value="分类属性id")
    private String categrteid;
    /**
     * 分类名称
     */
    @ApiModelProperty(value="分类名称")
    private String name;
    /**
     * 图片地址
     */
    @ApiModelProperty(value="图片地址")
    private String image;
    /**
     * 跳转地址
     */
    @ApiModelProperty(value="跳转地址")
    private String url;
    /**
     * 类型（1为商品分类图标，2为城市合伙人图标3为分类图片4为底部功能图标）
     */
    @ApiModelProperty(value="类型（1为商品分类图标，2为城市合伙人图标3为分类图片4为底部功能图标）")
    private String type;
    }
