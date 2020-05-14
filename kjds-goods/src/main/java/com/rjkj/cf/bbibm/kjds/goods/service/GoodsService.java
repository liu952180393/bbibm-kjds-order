package com.rjkj.cf.bbibm.kjds.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.api.entity.PaySuccesCallBussEntity;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformGoodsRsp;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformInfoVo;
import com.rjkj.cf.bbibm.kjds.api.entity.ShoppeId;
import com.rjkj.cf.bbibm.kjds.goods.entity.ApplyGoodsReq;
import com.rjkj.cf.bbibm.kjds.goods.entity.Goods;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.ShopApprovalVo;
import com.rjkj.cf.common.security.service.RjkjUser;

import java.math.BigDecimal;
import java.util.List;

/**
 *@描述：商户模块
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-08 12:00:03
 **/
public interface GoodsService extends IService<Goods> {

    /**
     *  添加店铺
     * @param goods
     * @return
     */
    int addGoods(RjkjUser  user,Goods goods);


    /**
     *  修改店铺信息
     * @param user
     * @param goods
     * @return
     */
    int updateGoods(RjkjUser user, Goods goods);


    /**
     *  查询商品列表
     * @param page
     * @param goods
     * @param user
     * @return
     */
    IPage<Goods> listGoods(Page page, Goods goods, RjkjUser user);

    /**
     * 查询用户平台信息
     * @param user
     * @return
     */
    List<PlatformInfoVo> selectUserPlatformInfo(RjkjUser user);


    /**
     * 获取shoppid
     * @param user
     * @return
     */
    List<ShoppeId> getShoppids(RjkjUser user,String pid);

    /**
     *  平台价格获取
     * @return
     */
    List<PlatformInfoVo> priceList();

    /**
     * 申请店铺
     * @param user
     * @param req
     * @return
     */
    String applyGoods(RjkjUser user, ApplyGoodsReq req);

    /**
     * 获取店铺列表
     * @param user
     * @return
     */
    List<PlatformGoodsRsp> goodsList(RjkjUser user);

    /**
     *  支付成功后回调业务
     * @param paySuccesCallBussEntity
     */
    void payBussCallback(PaySuccesCallBussEntity paySuccesCallBussEntity);

    /**
     * 更新Redis中存入的店铺信息
     */
    void updateGoodsToRedis();

    /**
     * 获取待审核的店铺列表
     * @param page
     * @param goods
     * @return
     */
    IPage<Goods> getApprovalGoodsPage(Page page,Goods goods);

    void shopApproval(ShopApprovalVo shopApprovalVo, RjkjUser user);

    String addrechargeBalanceInfo(RjkjUser user, String payType, BigDecimal amount);


    List<PlatformGoodsRsp> getGoodsListByUserId(String userId);

    List<String> getGoodsUserIdListByGoodsId(String shopId, String area);

    void test();

    int updateStatusToReview(String gid);
}
