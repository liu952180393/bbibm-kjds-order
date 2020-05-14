package com.rjkj.cf.bbibm.kjds.product.appcategory.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 *@描述：app商品分类属性表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-12 11:05:19
 **/
@Data
@TableName("bbibm_app_category")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "app商品分类属性表")
public class AppCategory extends Model<AppCategory> {
private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.INPUT)
    @ApiModelProperty(value="id")
    private String id;
    /**
     * 类别名称
     */
    @ApiModelProperty(value="类别名称")
    private String name;
    /**
     * 是否有子类，0否
     */
    @ApiModelProperty(value="是否有子类，0否")
    private Integer isParent;
    /**
     * 父ID
     */
    @ApiModelProperty(value="父ID")
    private Integer parentId;
    /**
     * 所属层级
     */
    @ApiModelProperty(value="所属层级")
    private Integer level;
    /**
     * 各层级ID
     */
    @ApiModelProperty(value="各层级ID")
    private String pathid;
    /**
     * 路径名称
     */
    @ApiModelProperty(value="路径名称")
    private String path;
    /**
     * 主ID对应的Shopee商城的id
     */
    @ApiModelProperty(value="主ID对应的Shopee商城的id")
    private String shopee;
    /**
     * 主ID对应的Ebay商城的id
     */
    @ApiModelProperty(value="主ID对应的Ebay商城的id")
    private String ebay;
    /**
     * 主ID对应的Amazon商城的id
     */
    @ApiModelProperty(value="主ID对应的Amazon商城的id")
    private String amazon;

    /**
     * 当前分类的下级分类
     */
    @ApiModelProperty(value="当前分类的下级分类")
    @TableField(exist = false)
    private List<AppCategory> listCateGory;
    }
