package com.rjkj.cf.bbibm.kjds.api.utils;


import com.rjkj.cf.admin.api.entity.UserInfoRsp;
import com.rjkj.cf.bbibm.kjds.api.entity.PricingRules;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteUserFeignService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.core.util.SpringContextHolder;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * 计算商品价格规则类
 */
@UtilityClass
public class ComputedFreightPriceUtil {

    @Setter
    private static RemoteProductFeignService remoteProductFeignService;

    @Setter
    private static RemoteUserFeignService remoteUserFeignService;

    private void init() {
        if (remoteProductFeignService == null) {
            RemoteProductFeignService bean = SpringContextHolder.getApplicationContext().getBean(RemoteProductFeignService.class);
            remoteProductFeignService = bean;
        }
        if(remoteUserFeignService == null){
            RemoteUserFeignService bean= SpringContextHolder.getApplicationContext().getBean(RemoteUserFeignService.class);
            remoteUserFeignService = bean;
        }
    }

    /**
     * @param weight       商品重量  g为单位
     * @param productPrice 商品本身价格
     * @param area         上传到哪个国家的区域值
     * @return
     */
    public BigDecimal changePriceRule(BigDecimal weight, BigDecimal productPrice, String area,String uid) {
        init();
        try {
            PricingRules ruleBean = KjdsUtils.queryPriceInfoOne(area);
//            R<PricingRules> pricingRulesInfo = remoteProductFeignService.queryPriceInfoOne(area);
//            PricingRules ruleBean = pricingRulesInfo.getData();

            //定于一个总价格的句柄（人民币）
            BigDecimal allPrice = null;
            //如果商品的重量小于基础运费的商品重量就使用起价价格
            if (weight.compareTo(ruleBean.getBaseWeight()) == -1) {
                //商品价格加上基础价格
                BigDecimal priceOne = productPrice.add(ruleBean.getBasePrice());
                //上一步加上基础运费
                BigDecimal priceTwo = priceOne.add(ruleBean.getBasicFreight());
                //上一步加上挂号费
                allPrice = priceTwo.add(ruleBean.getRegisterPrice());

            } else {
                //多余的重量
                BigDecimal moreWeight = weight.subtract(ruleBean.getBaseWeight());
                //超过的重量倍数
                BigDecimal moreWeightMultiple = moreWeight.divide(ruleBean.getOverWeight(), 0, BigDecimal.ROUND_UP);
                //超过重量的总价格
                BigDecimal morePrice = moreWeightMultiple.multiply(ruleBean.getOverPrice());
                //超过重量价格加上基础费用
                BigDecimal add = morePrice.add(ruleBean.getBasePrice());
                //上一步加上商品价格
                BigDecimal priceOne = add.add(productPrice);
                //上一步加上挂号费
                BigDecimal priceTwo = priceOne.add(ruleBean.getRegisterPrice());
                //上一步加上基础运费
                allPrice = priceTwo.add(ruleBean.getBasicFreight());
            }

            //人民币对应的汇率价格信息
            BigDecimal rateBigDecimal = KjdsUtils.queryByRateOne(area);
//            BigDecimal rateBigDecimal = remoteProductFeignService.queryByRateOne(area);
            /*//提升倍数后的价格（原运费规则中折扣价格暂时未使用）
            BigDecimal finalPrice = allPrice.multiply(ruleBean.getDiscount());*/


            //根据用户自己定义的涨价倍数计算价格
//            R<UserInfoRsp> userInfo = remoteUserFeignService.getUserInfo();
            UserInfoRsp userInfoById = KjdsUtils.getUserInfoById(uid);
//            BigDecimal discount = userInfo.getData().getUserInfoVo().getDiscount().divide(new BigDecimal(100));
            BigDecimal discount = userInfoById.getUserInfoVo().getDiscount().divide(new BigDecimal(100));
            BigDecimal lastDecimal = discount.add(new BigDecimal(1));

            BigDecimal finalPrice = allPrice.multiply(lastDecimal);

            //计算商品乘以佣金的价格
            BigDecimal commissionPercentage = new BigDecimal(Const.commission_percentage).add(new BigDecimal(1));
            BigDecimal lastAllPrice = finalPrice.multiply(commissionPercentage);

            //换算汇率后的价格
            return lastAllPrice.divide(rateBigDecimal, 2, BigDecimal.ROUND_HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
