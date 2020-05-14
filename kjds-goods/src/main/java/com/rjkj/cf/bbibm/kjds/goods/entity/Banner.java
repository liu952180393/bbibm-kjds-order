package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *@描述：轮播图
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-11 19:05:09
 **/
@Data
@TableName("bbibm_banner")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "轮播图")
public class Banner extends Model<Banner> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="")
    private String id;
    /**
     * 提示
     */
    @ApiModelProperty(value="提示")
    private String alt;
    /**
     * 网址
     */
    @ApiModelProperty(value="网址")
    private String href;
    /**
     * 图片地址
     */
    @ApiModelProperty(value="图片地址")
    private String src;
    /**
     * 状态；1启用，2禁用
     */
    @ApiModelProperty(value="状态；1启用，2禁用")
    private Integer status;
    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Integer sort;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @JSONField()
    private LocalDateTime ctime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime utime;
    /**
     * 创建人ID
     */
    @ApiModelProperty(value="创建人ID")
    private String cuid;
    /**
     * 修改人ID
     */
    @ApiModelProperty(value="修改人ID")
    private String uuid;
}
