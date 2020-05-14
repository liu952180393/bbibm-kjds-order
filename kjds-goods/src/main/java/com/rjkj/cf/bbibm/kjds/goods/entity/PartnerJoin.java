package com.rjkj.cf.bbibm.kjds.goods.entity;

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
 *@描述：城市合伙人分享
 *@项目：
 *@公司：软江科技
 *@作者：yihao
 *@时间：2019-12-11 11:22:06
 **/
@Data
@TableName("bbibm_kjds_partner_join")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "城市合伙人分享")
public class PartnerJoin extends Model<PartnerJoin> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="")
    private String id;
    /**
     * 分享人ID
     */
    @ApiModelProperty(value="分享人ID")
    private String shareId;
    /**
     * 被分享人ID
     */
    @ApiModelProperty(value="被分享人ID")
    private String recvId;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime ctime;
    }
