package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 *@描述：平台价格
 *@项目：
 *@公司：软江科技
 *@作者：yihao
 *@时间：2019-12-06 14:36:15
 **/
@Data
@TableName("sys_dict")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "平台价格")
public class SysDict extends Model<SysDict> {
private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    @ApiModelProperty(value="编号")
    private Integer id;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String type;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String description;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String remarks;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String system;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String delFlag;
    /**
     * 所属租户
     */
    @ApiModelProperty(value="所属租户",hidden=true)
    private String tenantId;
    }
