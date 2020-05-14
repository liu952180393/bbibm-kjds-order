package com.rjkj.cf.bbibm.kjds.api.utils;

import lombok.Data;

import java.util.List;

/**
 * @description: 亚马逊存放商品id的实体（用于商品上传时回调）
 */
@Data
public class AmazonAllProductId {

    //商品的id
    public String productId;

    //属于该商品的全部sku信息
    public List<AmazonSkuUtil> listSku;

}
