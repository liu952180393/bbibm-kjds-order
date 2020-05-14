package com.rjkj.cf.bbibm.kjds.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.goods.entity.CashOutInfo;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.CashOutVo;
import com.rjkj.cf.common.core.util.R;

import java.math.BigDecimal;

/**
 *@描述：提现信息表
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-11-11 15:48:25
 **/
public interface CashOutInfoService extends IService<CashOutInfo> {

    /**
     * 申请提现记录保存
     * @param cashOutVo
     * @return
     */
    int saveCashOutInfo(CashOutVo cashOutVo);
    /**
     * 可提现金额
     * @param cashOutType
     * @return
     */
    BigDecimal cashOutCount(String cashOutType);

    /**
     * 申请记录状态更改
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(String id, String status,String remark);


    /**
     * 确认用户提交提现订单提现方法
     * @return R
     */
    R userApplicationCash(CashOutInfo bean);
}
