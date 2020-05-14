package com.rjkj.cf.bbibm.kjds.product.additem.controller;//package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.rabbitmq.client.Channel;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.product.additem.service.AddItemService;
import com.rjkj.cf.bbibm.kjds.product.additem.service.AddItemToMallService;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class DirectReceiver {
    private  final AddItemToMallService addItemToMallService;

    @RabbitHandler
    //监听的队列名称
    @RabbitListener(queues = "upload_product")
    public void process(List<GoodsProduct> goodsProducts,Channel channel, Message message) throws IOException{
        try {
            addItemToMallService.addItemToMall(goodsProducts);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                System.out.println("消息已重复处理失败,拒绝再次接收！");
//                 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列
                channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
            } else {
                System.out.println("消息即将再次返回队列处理！");
//                // requeue为是否重新回到队列，true重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                e.printStackTrace();
            }
        }
    }
}