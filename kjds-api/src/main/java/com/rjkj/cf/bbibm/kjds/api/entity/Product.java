package com.rjkj.cf.bbibm.kjds.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.rjkj.cf.admin.api.entity.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 *@描述：供应商商品上传
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 14:55:08
 **/
@Data
@TableName("bbibm_product")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "供应商商品上传")
public class Product extends Model<Product> {
    private static final long serialVersionUID = 1L;

    /**
     * 产品表id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="产品表id")
    private String id;
    /**
     * 分类名称
     */
    @ApiModelProperty(value="分类名称")
    private String productClassificationName;
    /**
     * 分类名称id
     */
    @ApiModelProperty(value="分类名称id")
    private String productClassificationId;
    /**
     * 商品图片
     */
    @ApiModelProperty(value="商品图片")
    private String image;
    /**
     * 审核状态（0通过 1待审核 2过滤 3侵权 4屏蔽 5违规）
     */
    @ApiModelProperty(value="审核状态（0通过 1待审核 2拒绝 3侵权 4屏蔽 5违规）")
    private String auditStatus;
    /**
     * 上下架状态 （0上架 1下架 2重复 3禁止上传）(不使用)
     */
    @ApiModelProperty(value="上下架状态 （0上架 1下架 2重复 3禁止上传）")
    private Integer rackStatus;
    /**
     * 品牌名称
     */
    @ApiModelProperty(value="品牌名称")
    private String brandName;
    /**
     * 厂商编码
     */
    @ApiModelProperty(value="厂商编码")
    private String manufacturerCode;
    /**
     * 产品名称
     */
    @ApiModelProperty(value="产品名称")
    private String productName;
    /**
     * 原产地区
     */
    @ApiModelProperty(value="原产地区")
    private String originArea;
    /**
     * 供应商名
     */
    @ApiModelProperty(value="供应商名")
    private String supplierName;
    /**
     * 供应商货号
     */
    @ApiModelProperty(value="供应商货号")
    private String supplierNumber;
    /**
     * 厂家名称
     */
    @ApiModelProperty(value="厂家名称")
    private String manufacturerName;
    /**
     * 库存
     */
    @ApiModelProperty(value="库存")
    private Integer stock;
    /**
     * sku
     */
    @ApiModelProperty(value="sku")
    private String sku;
    /**
     * 中文简称
     */
    @ApiModelProperty(value="中文简称")
    private String chineseAbbreviation;
    /**
     * 英文简称
     */
    @ApiModelProperty(value="英文简称")
    private String englishAbbreviation;
    /**
     * 长(CM)
     */
    @ApiModelProperty(value="长(CM)")
    private Double length;
    /**
     * 宽(CM)
     */
    @ApiModelProperty(value="宽(CM)")
    private Double wide;
    /**
     * 高(CM)
     */
    @ApiModelProperty(value="高(CM)")
    private Double height;
    /**
     * 重量(g)
     */
    @ApiModelProperty(value="重量(g)")
    private Double weight;
    /**
     * 成本单价（Y）
     */
    @ApiModelProperty(value="成本单价（Y）")
    private Double costUnitPrice;
    /**
     * 产品标题
     */
    @ApiModelProperty(value="产品标题")
    private String productTitle;
    /**
     * 关键词
     */
    @ApiModelProperty(value="关键词")
    private String keyWord;
    /**
     * 产品要点
     */
    @ApiModelProperty(value="产品要点")
    private String productHighlights;
    /**
     * 描述
     */
    @ApiModelProperty(value="描述")
    private String description;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 是否热销产品
     */
    @ApiModelProperty(value="是否热销产品 0为不热销数据，1为热销数据")
    private String ishot;

    /**
     * 平台名称
     */
    @ApiModelProperty(value="平台名称")
    private String platName;

    /**
     * 商品地址
     */
    @ApiModelProperty(value="商品地址")
    private String url;

    /**
     * 图片全部地址url
     */
    @ApiModelProperty(value="图片全部地址url")
    private String productImages;

    /**
     * 反馈信息
     */
    @ApiModelProperty(value="反馈信息")
    private String errMsg;

    /**
     * 供应商用户id
     */
    @ApiModelProperty(value="供应商用户id")
    private String userId;

    /**
     * 变体信息
     */
    @TableField(exist = false)
    @ApiModelProperty(value="变体信息")
    private List<ProductVariant> variantList;

    /**
     * 图片
     */
    @ApiModelProperty(value="图片列表")
    @TableField(exist = false)
    private List<SysFile>  imageList;

    /**
     * 上架件数
     */
    @ApiModelProperty(value="上架件数")
    @TableField(exist = false)
    private Integer pullNumber;

    /**
     * 是否已经翻译 0未翻译 1已翻译
     */
    @ApiModelProperty(value="是否已经翻译 0未翻译 1已翻译")
    private String translated;
}
