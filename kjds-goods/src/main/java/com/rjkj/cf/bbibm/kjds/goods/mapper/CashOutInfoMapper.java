package com.rjkj.cf.bbibm.kjds.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjkj.cf.bbibm.kjds.goods.entity.CashOutInfo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 *@描述：提现信息表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-11-11 15:48:25
 **/
public interface CashOutInfoMapper extends BaseMapper<CashOutInfo> {

    BigDecimal cashOutCount(@Param("uid") String userId,@Param("cashOutType") String cashOutType);
}
