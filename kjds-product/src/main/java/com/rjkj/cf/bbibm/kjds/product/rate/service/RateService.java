package com.rjkj.cf.bbibm.kjds.product.rate.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.product.rate.entity.Rate;

import java.math.BigDecimal;

/**
 *@描述：汇率信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-24 14:07:34
 **/
public interface RateService extends IService<Rate> {

    BigDecimal getRaetByArea(String area);

}
