package com.rjkj.cf.bbibm.kjds.api.utils;

import java.math.BigDecimal;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/25 14:02
 **/
public class PriceChangeUtils {

    /**
     * 价格变动工具类
     *
     * @param currentPrice 当前价格
     * @param changePrice  改变价格（百分比或固定值）
     * @param type         类型(1、百分比 2、 固定值)
     * @param priceType    价格类型 （1、上调，2、下调）
     * @return
     */
    public static BigDecimal changePrice(BigDecimal currentPrice, BigDecimal changePrice, int type, int priceType) {
        if (type == 1) {
            //百分比
            if (priceType == 1) {
//                    上调 以下同比
                currentPrice = BigDecimal.valueOf(1).add(changePrice.divide(BigDecimal.valueOf(100)))
                        .multiply(currentPrice);
            } else if (priceType == 2) {
//                      下调
                currentPrice = BigDecimal.valueOf(1).subtract(changePrice.divide(BigDecimal.valueOf(100))) .multiply(currentPrice);
            }
        } else if (type == 2) {
            //固定值
            if (priceType == 1) {
                currentPrice = currentPrice.add(changePrice);
            } else if (priceType == 2) {
                currentPrice = currentPrice.subtract(changePrice);
            }
        }
        return currentPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
