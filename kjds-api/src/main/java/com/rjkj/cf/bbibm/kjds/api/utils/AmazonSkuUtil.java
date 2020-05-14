package com.rjkj.cf.bbibm.kjds.api.utils;

import lombok.Data;

/**
 * @description: 亚马逊存放商品sku实体（用于商品上传时回调）
 */
@Data
public class AmazonSkuUtil {

    //亚马逊商品下的所有sku信息
    public String productAllSku;

}
