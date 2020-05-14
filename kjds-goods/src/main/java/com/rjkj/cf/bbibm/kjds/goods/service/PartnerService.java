package com.rjkj.cf.bbibm.kjds.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.goods.entity.Partner;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerApplyReq;
import com.rjkj.cf.bbibm.kjds.goods.entity.PartnerApplyRsp;
import com.rjkj.cf.common.security.service.RjkjUser;

/**
 *@描述：合伙人审核表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-21 11:58:47
 **/
public interface PartnerService extends IService<Partner> {

    /**
     *  城市合伙人申请
     * @param req
     * @param user
     * @return
     */
    String apply(PartnerApplyReq req, RjkjUser user);
}
