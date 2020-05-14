package com.rjkj.cf.bbibm.kjds.product.rate.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.product.rate.entity.Rate;
import com.rjkj.cf.bbibm.kjds.product.rate.mapper.RateMapper;
import com.rjkj.cf.bbibm.kjds.product.rate.service.RateService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 *@描述：汇率信息
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-12-24 14:07:34
 **/
@Service
@AllArgsConstructor
public class RateServiceImpl extends ServiceImpl<RateMapper, Rate> implements RateService {
    private final RateMapper rateMapper;

    @Override
    public BigDecimal getRaetByArea(String area) {
        return rateMapper.getRaetByArea(area);
    }
}
