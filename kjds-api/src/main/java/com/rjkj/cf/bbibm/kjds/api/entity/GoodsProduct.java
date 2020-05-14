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
 * @描述：商户产品表
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-08 17:54:19
 **/
@Data
@TableName("bbibm_goods_product")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户产品表")
public class GoodsProduct extends Model<GoodsProduct> {
    private static final long serialVersionUID = 1L;

    /**
     * 产品表id
     */
    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "产品表id")
    private String id;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String productClassificationName;
    /**
     * 分类名称id
     */
    @ApiModelProperty(value = "分类名称id")
    private String productClassificationId;
    /**
     * 上下架状态 （0上架 1下架 2重复 3禁止上传）
     */
    @ApiModelProperty(value = "上下架状态 （0上架 1下架 2重复 3禁止上传  4上架中 5上架失败 6未上架）")
    private Integer rackStatus;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value = "品牌名称")
    private String brandName;
    /**
     * 厂商编码
     */
    @ApiModelProperty(value = "厂商编码")
    private String manufacturerCode;
    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称")
    private String productName;
    /**
     * 原产地区
     */
    @ApiModelProperty(value = "原产地区")
    private String originArea;
    /**
     * 供应商名
     */
    @ApiModelProperty(value = "供应商名")
    private String supplierName;
    /**
     * 供应商货号
     */
    @ApiModelProperty(value = "供应商货号")
    private String supplierNumber;
    /**
     * 厂家名称
     */
    @ApiModelProperty(value = "厂家名称")
    private String manufacturerName;
    /**
     * sku
     */
    @ApiModelProperty(value = "sku")
    private String sku;
    /**
     * 中文简称
     */
    @ApiModelProperty(value = "中文简称")
    private String chineseAbbreviation;
    /**
     * 英文简称
     */
    @ApiModelProperty(value = "英文简称")
    private String englishAbbreviation;
    /**
     * 长(CM)
     */
    @ApiModelProperty(value = "长(CM)")
    private Double length;
    /**
     * 宽(CM)
     */
    @ApiModelProperty(value = "宽(CM)")
    private Double wide;
    /**
     * 高(CM)
     */
    @ApiModelProperty(value = "高(CM)")
    private Double height;
    /**
     * 重量(kg)
     */
    @ApiModelProperty(value = "重量(kg)")
    private Double weight;
    /**
     * 成本单价（Y）
     */
    @ApiModelProperty(value = "成本单价（Y）")
    private Double costUnitPrice;
    /**
     * 产品标题
     */
    @ApiModelProperty(value = "产品标题")
    private String productTitle;
    /**
     * 关键词
     */
    @ApiModelProperty(value = "关键词")
    private String keyWord;
    /**
     * 产品要点
     */
    @ApiModelProperty(value = "产品要点")
    private String productHighlights;
    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String description;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 商户店铺id
     */
    @ApiModelProperty(value = "商户店铺id")
    private String gid;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存")
    private Integer stock;
    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "变体列表")
    @TableField(exist = false)
    private List<ProductVariant> productVariants;


    /**
     * 图片
     */
    @ApiModelProperty(value = "图片列表")
    @TableField(exist = false)
    private List<String> imageList;

    /**
     * 商品全部图片信息
     */
    @ApiModelProperty(value = "商品全部图片信息")
    private String productImages;

//    public List<String> getImageList() {
//       return  SysFileUtils.getSysFile(image).stream()
//                .map(SysFile::getPath).collect(Collectors.toList());
//    }

    /**
     * 图片
     */
    @ApiModelProperty(value = "用户id")
    private String uid;

    /**
     * 错误消息
     */
    @ApiModelProperty(value = "错误消息")
    private String errorMsg;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品id")
    private String itemId;

    /**
     * 区域
     */
    @ApiModelProperty(value = "区域")
    private String area;
    /**
     * 是否在回收库（0否，1是）
     */
    @ApiModelProperty(value = "是否在回收库（0否，1是）")
    private Integer recycleLibrary;
    /**
     * 是否在队列（0否，1是）
     */
    @ApiModelProperty(value = "是否在队列（0否，1是）")
    private Integer queue;
    /**
     * 上传到店铺时间
     */
    @ApiModelProperty(value = "上传到店铺时间")
    private LocalDateTime uploadTime;

}
