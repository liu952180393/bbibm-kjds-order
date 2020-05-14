package com.rjkj.cf.bbibm.kjds.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.bbibm.kjds.api.utils.SysFileUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *@描述：产品变体
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-10 09:30:46
 **/
@Data
@TableName("bbibm_goods_product_variant")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "产品变体")
public class ProductVariant extends Model<ProductVariant> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 尺寸
     */
    @ApiModelProperty(value="尺寸")
    private String variantSize;

    /**
     * 颜色
     */
    @ApiModelProperty(value="颜色")
    private String variantColor;
    /**
     * 库存
     */
    @ApiModelProperty(value="库存")
    private Integer stock;
    /**
     * 子sku
     */
    @ApiModelProperty(value="子sku")
    private String sku;
    /**
     * 图片
     */
    @ApiModelProperty(value="图片")
    private String image;
    /**
     * 父sku（关联父商品信息）
     */
    @ApiModelProperty(value="父sku（关联父商品信息）")
    private String parentSku;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 图片
     */
    @ApiModelProperty(value="图片列表")
    @TableField(exist = false)
    private List<String> imageList;

    public List<String> getImageList() {
        return  SysFileUtils.getSysFile(image).stream()
                .map(SysFile::getPath).collect(Collectors.toList());
    }
    /**
     * 变体价格
     */
    @ApiModelProperty(value="变体价格")
    private Double price;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private String uid;

}
