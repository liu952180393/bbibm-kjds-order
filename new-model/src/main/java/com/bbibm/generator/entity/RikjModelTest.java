package com.bbibm.generator.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *@描述：用户表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-12 14:31:19
 **/
@Data
@TableName("rikj_model_test")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户表")
public class RikjModelTest extends Model<RikjModelTest> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId(type = IdType.INPUT )
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String username;
    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    private String password;
    /**
     * 性别
     */
    @ApiModelProperty(value="性别")
    private Integer sex;
    /**
     * 电话
     */
    @ApiModelProperty(value="电话")
    private String phone;
    }
