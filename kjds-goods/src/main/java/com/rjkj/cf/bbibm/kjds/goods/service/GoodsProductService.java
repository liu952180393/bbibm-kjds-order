package com.rjkj.cf.bbibm.kjds.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsProductRsp;
import com.rjkj.cf.common.security.service.RjkjUser;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;

/**
 *@描述：商户产品表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-08 17:54:19
 **/
public interface GoodsProductService extends IService<GoodsProduct> {

    /**
     * 拉取产品到本地
     * @param user 当前登录用户
     * @return
     */
    int extractProduct(String pids,  RjkjUser user);


    /**
     *  产品列表
     * @param user
     * @param page
     * @param goodsProduct
     * @return
     */
    IPage listGoodsProduct(RjkjUser user, Page page, GoodsProduct goodsProduct);

    /**
     *
     * 一键改价
     * @return
     */
    int changePrice(RjkjUser user, String ids, BigDecimal changePrice, int priceType, int type);

    /**
     * 获取用户平台产品
     * @param user
     * @param pid
     * @param status
     * @return
     */
    IPage<GoodsProductRsp> goodsProductPlatformById(Page page, RjkjUser user, String pid, String gid, String status);

    /**
     * 获取用户平台产品（已下架产品）
     * @param user
     * @param pid
     * @return
     */
    IPage<GoodsProductRsp> goodsProductlowershelf(Page page,RjkjUser user, String pid,String gid);

    /**
     * 待上架产品
     * @param page
     * @param user
     * @return
     */
    IPage<GoodsProductRsp> listWaitShelf(Page page, RjkjUser user,String gid);

    /**
     *  上传店铺
     * @param ids
     * @param gid
     * @return
     */
    int uploadGoodsProduct(Authentication token, RjkjUser user, String ids, String gid);


    /**
     *  app上架到店铺
     * @param ids
     * @param gid
     * @return
     */
    int upperGoodsProduct(Authentication token, RjkjUser user, String ids, String gid);

    /**
     * 删除产品
     * @param id
     * @param user
     * @return
     */
    int deleteProduct(String id, RjkjUser user);

    /**
     * 修改上传后的产品价格
     * @param pid
     * @param gid
     * @param price
     */
    void updatePrice(RjkjUser  user,String pid, String gid, BigDecimal price);

    /**
     * 修改产品列表
     * @param pid
     * @param gid
     * @param errorMsg
     * @param itemId
     * @param rackStatus
     */
    void updateByGid(String uid,String pid, String gid, String errorMsg, String itemId, int rackStatus);

    /**
     * 内部修改价格到店铺
     * @param user
     * @param pid
     * @param gid
     * @param type
     * @param priceType
     * @param area
     * @param changePrice
     * @return
     */
    BigDecimal updatePriceByGid(RjkjUser user, String pid, String gid, int type, int priceType, String area, BigDecimal changePrice);

    /**
     * 单个价格修改
     * @param user
     * @param id
     * @param changePrice
     */
    void changeUnitPrice(RjkjUser user, String id, BigDecimal changePrice);

    int updateBrand(String userId, String itemId, String brand);

    IPage<GoodsProductRsp> recyclingLibraryList(Page page, RjkjUser user);

    boolean removeLibrary(String ids, RjkjUser user);
}
