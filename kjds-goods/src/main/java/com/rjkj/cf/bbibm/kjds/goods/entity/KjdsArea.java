package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 *@描述：城市合伙人区域表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-12 17:59:35
 **/
@Data
@TableName("bbibm_kjds_area")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "城市合伙人区域表")
public class KjdsArea extends Model<KjdsArea> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 
     */
    @ApiModelProperty(value="名称")
    private String name;
    }
