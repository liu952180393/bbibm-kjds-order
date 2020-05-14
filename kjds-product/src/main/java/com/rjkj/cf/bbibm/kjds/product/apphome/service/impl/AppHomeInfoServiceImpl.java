package com.rjkj.cf.bbibm.kjds.product.apphome.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformGoodsRsp;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.product.apphome.entity.AppHomeInfo;
import com.rjkj.cf.bbibm.kjds.product.apphome.mapper.AppHomeInfoMapper;
import com.rjkj.cf.bbibm.kjds.product.apphome.service.AppHomeInfoService;
import com.rjkj.cf.common.core.util.R;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *@描述：app首页展示图标表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-15 10:37:47
 **/
@Service
@AllArgsConstructor
public class AppHomeInfoServiceImpl extends ServiceImpl<AppHomeInfoMapper, AppHomeInfo> implements AppHomeInfoService {
    private final RemoteGoodsFeignService remoteGoodsFeignService;

    @Override
    public List<PlatformGoodsRsp> queryUserShop() {
        //根据当前登录用户获取该用户下有哪些平台
        R<List<PlatformGoodsRsp>> platformInfo = remoteGoodsFeignService.goodsList();
        return addUrl(platformInfo);
    }

    private List<PlatformGoodsRsp> addUrl(R<List<PlatformGoodsRsp>> platformInfo){
        if (platformInfo != null) {
            List<PlatformGoodsRsp> data2 = platformInfo.getData();
            for (PlatformGoodsRsp platformGoodsRsp : data2) {
                String pid = platformGoodsRsp.getPid();
                if ("18".equals(pid)) {
                    platformGoodsRsp.setPlatformPhoto("http://ymx.bbibm.com/kjds/07584ec9062a4a58bb0b43d92b530a5e.jpg");
                    platformGoodsRsp.setOrderStatusCount("/order/amazonorder/getAmazonOrderStatusCount");
                    platformGoodsRsp.setOrderListByStatus("/order/amazonorder/getAmazonOrderListByStatus");
                    platformGoodsRsp.setOrderById("/order/amazonorder/getAmazonOrderById");
                    platformGoodsRsp.setEndItem("/product/amaoznFeedUpload/lowerProductInfo");
                    platformGoodsRsp.setOrderToShip("/order/amazonorder/orderToShip");
                    platformGoodsRsp.setProxyToShip("/order/amazonorder/proxyToShip");
                } else if ("19".equals(pid)) {
                    platformGoodsRsp.setPlatformPhoto("http://ymx.bbibm.com/kjds/4acd4f54f9264e5abbd79fb60160c3c0.jpg");
                    platformGoodsRsp.setOrderStatusCount("/order/ebayorder/getEbayOrderStatusCount");
                    platformGoodsRsp.setOrderListByStatus("/order/ebayorder/getEbayOrderListByStatus");
                    platformGoodsRsp.setOrderById("/order/ebayorder/getEbayOrderById");
                    platformGoodsRsp.setEndItem("/product/ebayProduct/endItem");
                    platformGoodsRsp.setOrderToShip("/order/ebayorder/orderToShip");
                    platformGoodsRsp.setProxyToShip("/order/ebayorder/proxyToShip");
                    platformGoodsRsp.setUpdatePrice("/product/ebayProduct/updatePrice");
                } else if ("20".equals(pid)) {
                    platformGoodsRsp.setPlatformPhoto("http://ymx.bbibm.com/kjds/6432830b06174b679021b1f4db39a4f8.jpg");
                    platformGoodsRsp.setOrderStatusCount("/order/shopeeorder/getShopeeOrderStatusCount");
                    platformGoodsRsp.setOrderListByStatus("/order/shopeeorder/getShopeeOrderListByStatus");
                    platformGoodsRsp.setOrderById("/order/shopeeorder/getShopeeOrderById");
                    platformGoodsRsp.setEndItem("/product/shopeeProduct/endItem");
                    platformGoodsRsp.setOrderToShip("/order/shopeeorder/orderToShip");
                    platformGoodsRsp.setProxyToShip("/order/shopeeorder/proxyToShip");
                    platformGoodsRsp.setUpdatePrice("/product/shopeeProduct/updatePrice");
                }
            }
            return data2;
        }
        return null;
    }
}
