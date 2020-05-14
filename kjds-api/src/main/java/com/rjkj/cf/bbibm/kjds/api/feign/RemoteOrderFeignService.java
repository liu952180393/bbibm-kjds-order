package com.rjkj.cf.bbibm.kjds.api.feign;

import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @描述：订单内部接口
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/9 15:41
 **/
@FeignClient(contextId = "remoteOrderFeignService", value = Const.ORDER_SERVICE_NAME)
public interface RemoteOrderFeignService {

    /**
     * Ebay同步订单信息
     *
     * @param from
     * @return
     */
    @PostMapping(value = "/ebayorder/synchronizeEbayOrderByTime")
    R ebayGetOrderList(@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * Shopee同步订单信息
     *
     * @param from
     * @return
     */
    @PostMapping(value = "/shopeeorder/synchronizeShopeeOrderByTime")
    R shopeeGetOrderList(@RequestHeader(SecurityConstants.FROM) String from);

    /**
     * amazon同步订单信息
     *
     * @param
     * @param from
     * @return
     */
    @PostMapping(value = "/amazonorder/getAmazonOrderList")
    R getAmazonOrderList(@RequestHeader(SecurityConstants.FROM) String from);
}
