package com.rjkj.cf.bbibm.kjds.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 *@描述：版本更新
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-24 10:45:42
 **/
@Data
@TableName("bbibm_kjds_app_update")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "版本更新")
public class AppUpdate extends Model<AppUpdate> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 版本
     */
    @ApiModelProperty(value="版本")
    private Integer code;
    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;
    /**
     * 下载地址
     */
    @ApiModelProperty(value="下载地址")
    private String url;
    /**
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String content;
    /**
     * 是否强制更新（1强制更新  0不强制更新）
     */
    @ApiModelProperty(value="是否强制更新（1强制更新  0不强制更新）")
    private String isUpdate;
    /**
     * ios或者android（1为android  0为ios）
     */
    @ApiModelProperty(value="ios或者android（1为android  0为ios）")
    private String type;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * app的下载地址用于显示app下载路径
     */
    @ApiModelProperty(value="app的下载地址用于显示app下载路径")
    private String apkUrl;

    }
