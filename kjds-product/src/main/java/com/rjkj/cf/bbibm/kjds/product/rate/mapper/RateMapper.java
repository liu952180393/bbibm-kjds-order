package com.rjkj.cf.bbibm.kjds.product.rate.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjkj.cf.bbibm.kjds.product.rate.entity.Rate;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @描述：汇率信息
 * @项目：
 * @公司：软江科技
 * @作者：crq
 * @时间：2019-12-24 14:07:34
 **/
public interface RateMapper extends BaseMapper<Rate> {

    BigDecimal getRaetByArea(@Param("area") String area);
}
