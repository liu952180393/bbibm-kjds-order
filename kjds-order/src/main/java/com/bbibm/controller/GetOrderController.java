package com.bbibm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bbibm.entity.BbibmKjdsGoods;
import com.bbibm.entity.Orders;
import com.bbibm.mapper.BbibmKjdsGoodsMapper;
import com.bbibm.mapper.ItemsMapper;
import com.bbibm.mapper.OrdersMapper;
import com.bbibm.serviceImpl.findAllOrderServiceImpl;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 *@描述：订单同步
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2019-10-17 15:35:49
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/goods" )
@Api(value = "goods", tags = "订单同步")
public class GetOrderController {
    private findAllOrderServiceImpl findAllOrderService;
    private BbibmKjdsGoodsMapper bbibmKjdsGoodsMapper;
    private final RedisTemplate redisTemplate;
    private OrdersMapper ordersMapper;
    private ItemsMapper itemsMapper;

    @ApiOperation(value = "链接远程", notes = "链接远程")
    @SysLog("链接远程" )
    @GetMapping("user")
    public String testRest(String area, String shopId) throws Exception {
            //查找店铺状态为启用的店铺信息
            List<BbibmKjdsGoods> bbibmKjdsGoods = bbibmKjdsGoodsMapper.selectList(Wrappers.<BbibmKjdsGoods>query().lambda().eq(BbibmKjdsGoods::getStatus, "0"));
           //同步订单信息
            for (BbibmKjdsGoods bbibmKjdsGood : bbibmKjdsGoods) {
                shopId = bbibmKjdsGood.getAccountSiteId();
                area = bbibmKjdsGood.getArea();
                String json = findAllOrderService.testRest(area, shopId);
                JSONObject jsonObject = JSONObject.parseObject(json);
                JSONArray orders = jsonObject.getJSONArray("orders");
                for (Object order : orders) {
                    String orderString = JSONObject.toJSONString(order);
                    JSONObject orderJson = JSON.parseObject(orderString);
                    Orders orders2 = JSON.parseObject(order.toString(), Orders.class);
                    //存入订单状态信息redis
                    redisTemplate.opsForValue().set("orderStatus",orders2.getOrderStatus());
                    if(orders2.getOrdersn().equals(null)){
                        //新增
                        Orders orders1 = new Orders();
                        orders1.setCurrency(orders2.getCurrency());
                        orders1.setCreateTime(orders2.getCreateTime());
                        orders1.setCountry(orders2.getCountry());
                        orders1.setBuyerUsername(orders2.getBuyerUsername());
                        orders1.setDaysToShip(orders2.getDaysToShip());
                        orders1.setEscrowAmount(orders2.getEscrowAmount());
                        orders1.setEstimatedShippingFee(orders2.getEstimatedShippingFee());
                        orders1.setGoodsToDeclare(orders2.getGoodsToDeclare());
                        orders1.setItems(orders2.getItems());
                        orders1.setMessageToSeller(orders2.getMessageToSeller());
                        orders1.setOrdersn(orders2.getOrdersn());
                        orders1.setOrderStatus(orders2.getOrderStatus());
                        orders1.setPaymentMethod(orders2.getPaymentMethod());
                        orders1.setPayTime(orders1.getPayTime());
                        orders1.setRecipientAddress(orders2.getRecipientAddress());
                        orders1.setServiceCode(orders2.getServiceCode());
                        orders1.setShipByDate(orders2.getShipByDate());
                        orders1.setShippingCarrier(orders2.getShippingCarrier());
                        orders1.setTotalAmount(orders2.getTotalAmount());
                        orders1.setTrackingNo(orders2.getTrackingNo());
                        orders1.setUpdateTime(orders2.getUpdateTime());
                        ordersMapper.insert(orders1);
                        itemsMapper.insert(orders1.getItems());
                        }else {
                        System.out.println(1);
                    }

                }

            }
//            JSONArray items1 = orderJson.getJSONArray("items");
//            for (Object items : items1) {
//                Items items2 = JSON.parseObject(items.toString(), Items.class);
//                System.out.println(items2);//待
//            }
//            Orders orders1 = JSON.parseObject(orderJson.toJSONString(), Orders.class);
//            System.out.println(orders1);
//            //新增
//            ordersService.save(orders1);待
//        }
        return "成功";
}

}