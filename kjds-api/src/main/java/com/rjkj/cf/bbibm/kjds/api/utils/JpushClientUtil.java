package com.rjkj.cf.bbibm.kjds.api.utils;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.bbibm.kjds.api.entity.Notice;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformGoodsRsp;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.core.util.R;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author yihao
 * @date 2020年1月2日
 * @description: 极光推送工具类
 */
@AllArgsConstructor
public class JpushClientUtil {
	
	private static Logger logger = LoggerFactory.getLogger(JpushClientUtil.class);
 
    private final static String APPKER = "e58980ad503d0b07da521bcb";
 
    private final static String MASTERSECRET = "116e2542862811eb69b2f9d8";
 
    private static JPushClient jPushClient = new JPushClient(MASTERSECRET,APPKER);
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final RemoteProductFeignService remoteProductFeignService;



    /**
     * 发送普通提醒
     * @param alias 别名
     * @return 0推送失败，1推送成功
     */
    public int sendOrdinaryMsg(String title,String content,String alias) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("type",2);
            jsonObject.put("title",title);
            jsonObject.put("content",content);
            jsonObject.put("data",System.currentTimeMillis());
            return sendMsg(alias, jsonObject.toJSONString());
        } catch (APIConnectionException e) {
            logger.error("[极光推送]Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            logger.error("[极光推送]Error response from JPush server. Should review and fix it. ", e);
            logger.info("[极光推送]HTTP Status: " + e.getStatus());
            logger.info("[极光推送]Error Code: " + e.getErrorCode());
            logger.info("[极光推送]Error Message: " + e.getErrorMessage());
        }
        return 0;
    }

    public int saveMsg(String userId,String description){
        try {
            Notice notice = new Notice();
            notice.setUserId(userId);
            notice.setType(0);
            notice.setState(0);
            notice.setDescription(description);
            remoteProductFeignService.saveInfo(notice, SecurityConstants.FROM);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    /**
     * 发送新订单通知
     * @return 0推送失败，1推送成功
     */
    public int sendOrderMsg(String pid,String title, String content, List<String> userIdList) {
        try {
            if(userIdList != null){
                for (String userId : userIdList) {
                    R<List<PlatformGoodsRsp>> goodsListByUserId = remoteGoodsFeignService.getGoodsListByUserId(userId, SecurityConstants.FROM);
                    List<PlatformGoodsRsp> data1 = goodsListByUserId.getData();
                    for (PlatformGoodsRsp platformGoodsRsp : data1) {
                        if (pid.equals(platformGoodsRsp.getPid())) {
                            JSONObject msgContens = new JSONObject();
                            msgContens.put("type", 1);
                            msgContens.put("title", title);
                            msgContens.put("content", content);
                            msgContens.put("data", platformGoodsRsp);
                            return sendMsg(userId, msgContens.toJSONString());
                        }
                    }
                }
                }
        } catch (APIConnectionException e) {
            logger.error("[极光推送]Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            logger.error("[极光推送]Error response from JPush server. Should review and fix it. ", e);
            logger.info("[极光推送]HTTP Status: " + e.getStatus());
            logger.info("[极光推送]Error Code: " + e.getErrorCode());
            logger.info("[极光推送]Error Message: " + e.getErrorMessage());
        }
        return 0;
    }

    private int sendMsg(String alias,String contens) throws APIConnectionException,APIRequestException{
        int result = 0;
        PushPayload pushPayload = PushPayload.newBuilder()
                //指定要推送的平台，all代表当前应用配置了的所有平台，也可以传android等具体平台
                .setPlatform(Platform.all())
                //指定推送的接收对象，all代表所有人，也可以指定已经设置成功的tag或alias或该应应用客户端调用接口获取到的registration id
                .setAudience(Audience.alias(alias))
                .setMessage(Message.newBuilder()
                        .setMsgContent(contens)
                        .build())
                .build();
        PushResult pushResult=jPushClient.sendPush(pushPayload);
        if(pushResult.getResponseCode()==200){
            result=1;
            logger.info("[极光推送]推送用户："+alias+"成功");
        }
        return result;
    }
}