package com.rjkj.cf.bbibm.kjds.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsProductRsp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @描述：商户产品表
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-08 17:54:19
 **/
public interface GoodsProductMapper extends BaseMapper<GoodsProduct> {

    /**
     * 获取用户平台产品
     *
     * @param page
     * @param id
     * @param pid
     * @param statusList
     * @return
     */
    IPage<GoodsProductRsp> goodsProductPlatformById(Page page, @Param("uid") String id, @Param("pid") String pid,
                                                    @Param("gid") String gid,  @Param("statusList")List statusList);

    /**
     * 获取用户平台产品（已下架产品）
     *
     * @param page
     * @param id
     * @param pid
     * @return
     */
    IPage<GoodsProductRsp> goodsProductlowershelf(Page page, @Param("uid") String id, @Param("pid") String pid,
                                                  @Param("gid") String gid);

    /**
     * 待上架产品
     *
     * @param page
     * @param id
     * @return
     */
    IPage<GoodsProductRsp> listWaitShelf(Page page, @Param("uid") String id, @Param("gid") String gid);

    /**
     * 获取上传产品列表
     *
     * @param id
     * @param gid
     * @param asList
     * @return
     */
    List<com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct> selectUploadGoodsProduct(@Param("uid") String uid,
                                                                                  @Param("gid") String gid,
                                                                                  @Param("ids") List<String> ids,
                                                                                  @Param("area") String area);

    /**
     * 获取单条产品数据包含变体
     *
     * @param id
     * @param pid
     * @param gid
     * @param area
     * @return
     */
    GoodsProduct selectOneGoodProduct(@Param("uid") String uid,
                                      @Param("pid") String pid,
                                      @Param("gid") String gid,
                                      @Param("area") String area);


    void updateBrand(@Param("userId") String userId, @Param("productId") String productId, @Param("brand") String brand);

    void updateIsQueue(@Param("gid") String gid, @Param("productId") String productId, @Param("userId") String uid, @Param("isQueue") Integer isQueue);

    IPage<GoodsProductRsp> recyclingLibraryList(Page page, @Param("uid") String uid);

    void updateRecycleLibrary(@Param("pid") List pid, @Param("uid") String uid, @Param("isRecycleLibrary") Integer isRecycleLibrary);
}
