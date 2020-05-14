package com.rjkj.cf.bbibm.kjds.product.eaninfo.entity;

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
 *@描述：ean数据表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-17 14:46:51
 **/
@Data
@TableName("bbibm_ean")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ean数据表")
public class Ean extends Model<Ean> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * ean值
     */
    @ApiModelProperty(value="ean值")
    private String ean;
    /**
     * 为1时ean用过，为0时ean没用过
     */
    @ApiModelProperty(value="为1时ean用过，为0时ean没用过")
    private String type;
    }
