package com.rjkj.cf.bbibm.kjds.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsOrder;
import org.apache.ibatis.annotations.Param;

/**
 * @描述：店铺申请订单信息
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-17 15:35:49
 **/
public interface GoodsOrderMapper extends BaseMapper<GoodsOrder> {

    String getUserName(@Param("id") String id);
}
