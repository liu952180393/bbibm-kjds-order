package com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 *@描述：
 *@项目：
 *@公司：软江科技
 *@作者：yihao
 *@时间：2019-10-31 10:39:56
 **/
@Data
@TableName("product_info")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "")
public class ProductInfo extends Model<ProductInfo> {
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
    private String title;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String price;
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
    private String variantsColor;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String variantsImage;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String variantsSize;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String variantPrice;
    /**
     * 
     */
    @ApiModelProperty(value="")
    private String variantStock;
    /**
     *
     */
    @ApiModelProperty(value="")
    private String image;
    /**
     *
     */
    @ApiModelProperty(value="")
    private String catrgoryId;
    /**
     *
     */
    @ApiModelProperty(value="")
    private String fee;
    }
