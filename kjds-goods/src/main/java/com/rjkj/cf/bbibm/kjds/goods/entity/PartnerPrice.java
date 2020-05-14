package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *@描述：城市合伙人价格表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 13:15:07
 **/
@Data
@TableName("bbibm_kjds_partner_price")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "城市合伙人价格表")
public class PartnerPrice extends Model<PartnerPrice> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 城市合伙人费用名称
     */
    @ApiModelProperty(value="城市合伙人费用名称")
    private String name;
    /**
     * 价格
     */
    @ApiModelProperty(value="价格")
    private BigDecimal price;
    /**
     * 权益
     */
    @ApiModelProperty(value="权益")
    private String equity;
    /**
     * 费率
     */
    @ApiModelProperty(value="费率")
    private BigDecimal fee;
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
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String remake;

    /**
     * 当前合伙人套餐周期有效天数
     */
    @ApiModelProperty(value="当前合伙人套餐周期有效天数")
    private Integer days;

    }
