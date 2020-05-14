package com.rjkj.cf.bbibm.kjds.product.amazonproduct.entity;

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
 *@描述：亚马逊亮点，描述库
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-11-06 15:52:56
 **/
@Data
@TableName("amazon_product_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "亚马逊亮点，描述库")
public class AmazonProductInfo extends Model<AmazonProductInfo> {
private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    @ApiModelProperty(value="")
    private Integer id;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String categoryId;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String title;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String dec;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String url;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String createTime;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String packageDimensions;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String highlights;
    }
