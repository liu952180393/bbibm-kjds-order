package com.rjkj.cf.bbibm.kjds.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/15 15:14
 **/
@ApiModel("app店铺列表")
@Data
public class GoodsListVo {


    /**
     * 店铺id
     */
    @TableId(value = "sid",type = IdType.INPUT)
    @ApiModelProperty(value="店铺id")
    private String sid;
    /**
     * 店铺名
     */
    @ApiModelProperty(value="店铺名")
    private String sname;
}
