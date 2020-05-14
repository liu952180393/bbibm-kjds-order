package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.alibaba.fastjson.annotation.JSONField;
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
 *@描述：MyBatis-Plus测试用户类
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-11 19:05:09
 **/
@Data
@TableName("user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户类")
public class User extends Model<User> {
private static final long serialVersionUID = 1L;


    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="")
    private Integer id;

    @ApiModelProperty(value="名字")
    private String name;

    @ApiModelProperty(value="年龄")
    private Integer age;

    @ApiModelProperty(value="email")
    private String email;
}
