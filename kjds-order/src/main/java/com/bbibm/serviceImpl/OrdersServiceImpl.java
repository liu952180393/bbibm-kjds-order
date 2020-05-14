package com.bbibm.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbibm.entity.Orders;
import com.bbibm.mapper.OrdersMapper;
import com.bbibm.service.OrdersService;
import org.springframework.stereotype.Service;


/**
 *@描述：商品表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-13 14:36:24
 **/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

}
