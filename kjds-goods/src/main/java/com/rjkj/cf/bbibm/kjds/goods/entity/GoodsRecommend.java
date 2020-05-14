package com.rjkj.cf.bbibm.kjds.goods.entity;

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
 *@描述：推荐费率
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-11-01 17:07:09
 **/
@Data
@TableName("bbibm_kjds_goods_recommend")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "推荐费率")
public class GoodsRecommend extends Model<GoodsRecommend> {
private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value="主键")
    private String id;
    /**
     * 自动费率名称
     */
    @ApiModelProperty(value="自动费率名称")
    private String name;
    /**
     * 费率值
     */
    @ApiModelProperty(value="费率值")
    private BigDecimal fee;
    /**
     * 费率类别（1城市合伙人，2、自推荐人）
     */
    @ApiModelProperty(value="费率类别（1城市合伙人，2、自推荐人）")
    private Integer feeType;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime ctime;
    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime utime;
    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private String cuid;
    /**
     * 修改人
     */
    @ApiModelProperty(value="修改人")
    private String uuid;
    }
