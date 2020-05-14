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
 *@描述：用户关联
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-12 17:19:08
 **/
@Data
@TableName("bbibm_kjds_user_join")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户关联")
public class UserJoin extends Model<UserJoin> {
private static final long serialVersionUID = 1L;

    /**
     * 分享人id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="分享人id")
    private String shareId;
    /**
     * 分享码
     */
    @ApiModelProperty(value="分享码")
    private String shareCode;
    /**
     * 关联人id
     */
    @ApiModelProperty(value="关联人id")
    private String recvId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime ctime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
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
     * 设备类型
     */
    @ApiModelProperty(value="设备类型")
    private String decvType;
    /**
     * 设备唯一标识
     */
    @ApiModelProperty(value="设备唯一标识")
    private String decvCode;
    /**
     * 被分享人手机号
     */
    @ApiModelProperty(value="被分享人手机号")
    private String recvPhone;
}
