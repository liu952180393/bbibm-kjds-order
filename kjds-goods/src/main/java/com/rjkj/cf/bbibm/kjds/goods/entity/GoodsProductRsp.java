package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.rjkj.cf.admin.api.entity.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/18 13:46
 **/
@ApiModel("商户平台产品列表")
@Data
public class GoodsProductRsp {

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("产品名称")
    private String productTitle;

    @ApiModelProperty("库存")
    private String stock;

    @ApiModelProperty("产品成本价")
    private String costUnitPrice;

    @ApiModelProperty("产品图片")
    private String image;

    @ApiModelProperty("店铺id")
    private String gid;

    @ApiModelProperty(value="品牌名称")
    private String brandName;

    @ApiModelProperty(value="产地")
    private String originArea;

    @ApiModelProperty(value="供应商名称")
    private String supplierName;

    @ApiModelProperty(value="厂商编码")
    private String manufacturerCode;

    @ApiModelProperty(value="关键词")
    private String keyWord;

    @ApiModelProperty(value="产品亮点")
    private String productHighlights;
    @ApiModelProperty(value="厂家名称")
    private String manufacturerName;

    @ApiModelProperty(value="供应商货号")
    private String supplierNumber;

    @ApiModelProperty(value="描述")
    private String description;

    @ApiModelProperty("店铺名称")
    private String gname;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("商品图片全部信息")
    private String productImages;

    @ApiModelProperty("图片列表")
    private List<SysFile> images;

    @ApiModelProperty("商品状态 0 已上架 4 上架中  5上架失败")
    private String status;

    @ApiModelProperty("上架失败错误消息")
    private String errMsg;

//    public List<SysFile> getImages() {
//        return SysFileUtils.getSysFile(image);
//    }
}
