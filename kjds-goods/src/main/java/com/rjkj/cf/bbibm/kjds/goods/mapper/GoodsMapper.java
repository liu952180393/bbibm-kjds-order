package com.rjkj.cf.bbibm.kjds.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformGoodsRsp;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformInfoVo;
import com.rjkj.cf.bbibm.kjds.api.entity.ShoppeId;
import com.rjkj.cf.bbibm.kjds.goods.entity.Goods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *@描述：商户模块
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-08 12:00:03
 **/
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 查询用户平台信息
     * @param id
     * @return
     */
    List<PlatformInfoVo> selectUserPlatformInfo(@Param("id")String id);


    /**
     * 获取shoppid
     * @param id
     * @return
     */
    List<ShoppeId> selectShoppidList(@Param("id") String id,@Param("pid")String pid);

    /**
     * 获取平台信息
     * @return
     */
    List<PlatformInfoVo> priceList();

    /**
     * 获取平台和店铺列表
     * @param id
     * @return
     */
    List<PlatformGoodsRsp> platformList(@Param("uid") String id);

    /**
     * 根据id获取平台信息
     * @param pid
     * @return
     */
    PlatformInfoVo selectPidInfo(@Param("pid") String pid);

    /**
     * 获取分享人信息
     * @param payUid
     * @return
     */
    String getShareUid(@Param("uid") String payUid);
    /**
     * 获取待审核店铺列表
     */
    IPage<Goods> getApprovalGoodsPage(Page page,@Param("pid")String pid,@Param("status")String status,@Param("phone")String phone);
}
