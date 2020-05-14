package com.rjkj.cf.bbibm.kjds.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/22 17:24
 **/
@ApiModel("我的店铺店铺实体")
@Data
public class GoodsRsp {

    @ApiModelProperty("店铺id")
    private String gid;

    @ApiModelProperty("店铺名称")
    private String gname;

    @ApiModelProperty("店铺图片")
    private String goodsPhoto;


    @ApiModelProperty("已上架数量")
    private int gSelfNum;

    @ApiModelProperty("shopid")
    private String accountSiteId;


}
