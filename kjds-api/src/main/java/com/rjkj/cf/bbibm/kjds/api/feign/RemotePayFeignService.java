package com.rjkj.cf.bbibm.kjds.api.feign;

import com.rjkj.cf.bbibm.kjds.api.entity.PayGoodsOrder;
import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.common.core.util.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/31 15:21
 **/
@FeignClient(contextId = "remotePayFeignService",value =  Const.PAY_SERVICE_NAME)
public interface RemotePayFeignService {

    /**
     * 支付模块订单 保存
     * 这个是商户本地创建订单的数据状态统一为未支付
     * @return
     */
    @PostMapping("/goods/add/order")
     R goodsAddOrder(@RequestBody PayGoodsOrder payGoodsOrder);

    /**
     * 支付模块内部app唤醒参数
     * @param type （1、支付宝 2、微信）
     * @param oId 订单号
     * @return
     */
    @PostMapping("/goods/call_client/param")
    R  getAppPayCallClientParam(@RequestParam(value = "type",defaultValue ="0") int  type,
                                @RequestParam(value = "oId",defaultValue = "") String oId);


    /**
     * 平台审批同意用户提现接口
     * @param type （1、微信  2，支付宝）
     * @param
     * @return
     */
    @PostMapping("/goods/submitApplicationCash")
    R<Map<String,String>>  submitApplicationCash(@RequestParam(value = "type") String type,
                                  @RequestParam(value = "orderNo") String orderNo,
                                  @RequestParam(value = "aliCode") String aliCode,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "amount") String amount);

}
