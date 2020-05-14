package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.tx.annotation.TxTransaction;
import com.rjkj.cf.admin.api.entity.CommissionInfoVo;
import com.rjkj.cf.admin.api.entity.SysUser;
import com.rjkj.cf.admin.api.entity.UserInfoRsp;
import com.rjkj.cf.admin.api.feign.RemoteUserService;
import com.rjkj.cf.bbibm.kjds.api.entity.UserOrder;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemotePayFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteUserFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.JpushClientUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.MsgConstant;
import com.rjkj.cf.bbibm.kjds.goods.entity.CashOutInfo;
import com.rjkj.cf.bbibm.kjds.goods.mapper.CashOutInfoMapper;
import com.rjkj.cf.bbibm.kjds.goods.reqvo.CashOutVo;
import com.rjkj.cf.bbibm.kjds.goods.service.CashOutInfoService;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import com.rjkj.cf.common.sequence.sequence.Sequence;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 * @描述：提现信息表
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-11-11 15:48:25
 **/
@Service
@AllArgsConstructor
public class CashOutInfoServiceImpl extends ServiceImpl<CashOutInfoMapper, CashOutInfo> implements CashOutInfoService {
    private final CashOutInfoMapper cashOutInfoMapper;
    private final RemoteUserFeignService remoteUserService;
    private final RemotePayFeignService remotePayFeignService;
    private final Sequence paySequence;
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final RemoteProductFeignService remoteProductFeignService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCashOutInfo(CashOutVo cashOutVo) {
        try {
            UserInfoRsp data = remoteUserService.getUserInfo().getData();
            String userId = SecurityUtils.getUser().getId();
            if (!"4".equals(cashOutVo.getCashOutType())) {
                //当申请提现的总收入
                BigDecimal totalAmount = BigDecimal.ZERO;
                List<CommissionInfoVo> commissionInfoVos = data.getCommissionInfoVos();
                for (CommissionInfoVo commissionInfoVo : commissionInfoVos) {
                    if (cashOutVo.getCashOutType().equals(String.valueOf(commissionInfoVo.getCommissionType()))) {
                        totalAmount = new BigDecimal(commissionInfoVo.getCommissionTotal());
                    }
                }
                //已提现的金额
                BigDecimal cashOutCount = cashOutInfoMapper.cashOutCount(userId, cashOutVo.getCashOutType());
                //可以提现的金额
                BigDecimal balance = totalAmount.subtract(cashOutCount);
                //申请提现的金额
                BigDecimal cashOutAmount = cashOutVo.getAmount();
                if (cashOutAmount.compareTo(BigDecimal.ZERO) < 1) {
                    throw new RuntimeException("提现金额不能小于0!");
                }

                if (cashOutAmount.compareTo(balance) == 1) {
                    return -2;
                }
            }
            BigDecimal totalMoeny = BigDecimal.ZERO;
            if ("4".equals(cashOutVo.getCashOutType())) {
                totalMoeny = new BigDecimal(data.getUserInfoVo().getTotalMoeny());
                if (cashOutVo.getAmount().compareTo(totalMoeny) == 1) {
                    return -2;
                }
                if (totalMoeny.compareTo(BigDecimal.ZERO) < 1) {
                    throw new RuntimeException("提现金额不能小于0!");
                }
            }
            //保存提现信息表
            CashOutInfo cashOutInfo = new CashOutInfo();
            cashOutInfo.setId(IDUtils.getGUUID(""));
            cashOutInfo.setUid(userId);
            cashOutInfo.setType(cashOutVo.getType());
            cashOutInfo.setStatus("1");
            cashOutInfo.setAmount(cashOutVo.getAmount());
            cashOutInfo.setApplytime(LocalDateTime.now());
            cashOutInfo.setUserNumber(cashOutVo.getUserNumber());
            cashOutInfo.setUserName(cashOutVo.getUserName());
            cashOutInfo.setCashOutType(cashOutVo.getCashOutType());
            int insert = cashOutInfoMapper.insert(cashOutInfo);
            if (insert > 0) {
                if ("4".equals(cashOutVo.getCashOutType())) {
                    BigDecimal amount = cashOutVo.getAmount();
                    SysUser sysUser = new SysUser();
                    sysUser.setUserId(userId);
                    sysUser.setTotalMoeny(totalMoeny.subtract(amount));
                    remoteUserService.updateById(sysUser, SecurityConstants.FROM);

                    //如果是余额提现，在增加一条用户ORDER
                    UserOrder userOrder = new UserOrder();
                    userOrder.setOid(IDUtils.getGUUID(""));
                    userOrder.setPayUid(userId);
                    userOrder.setRecvUid(userId);
                    userOrder.setAmount(amount);
                    userOrder.setAmountType("3");
                    userOrder.setStatus("2");
                    userOrder.setPayTime(LocalDateTime.now());
                    userOrder.setCtime(LocalDateTime.now());
                    userOrder.setOrderType("2");
                    userOrder.setDescription("申请提现");
                    remoteUserService.saveUserOrder(userOrder);

                    //消息通知
                    JpushClientUtil jpushClientUtil = new JpushClientUtil(remoteGoodsFeignService, remoteProductFeignService);
                    jpushClientUtil.sendOrdinaryMsg(MsgConstant.CASHOUT_TITLE, MsgConstant.CASHOUT_CONTENS, userId);
                    jpushClientUtil.saveMsg(userId, MsgConstant.CASHOUT_CONTENS);
                }
                return 1;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    @TxTransaction
    public BigDecimal cashOutCount(String cashOutType) {
        UserInfoRsp data = remoteUserService.getUserInfo().getData();
        if (data != null) {
            String userId = SecurityUtils.getUser().getId();
            //当申请提现的总收入
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<CommissionInfoVo> commissionInfoVos = data.getCommissionInfoVos();
            for (CommissionInfoVo commissionInfoVo : commissionInfoVos) {
                if (cashOutType.equals(String.valueOf(commissionInfoVo.getCommissionType()))) {
                    totalAmount = new BigDecimal(commissionInfoVo.getCommissionTotal());
                }
            }
            //已提现的金额
            BigDecimal cashOutCount = cashOutInfoMapper.cashOutCount(userId, cashOutType);
            //可以提现的金额
            return totalAmount.subtract(cashOutCount);
        } else {
            return new BigDecimal(0);
        }

    }

    @Override
    public Boolean updateStatus(String id, String status, String remark) {
        try {
            if (StringUtils.isBlank(id)) {
                throw new RuntimeException("id不能为空！");
            }
            if ("2".equals(status) || "3".equals(status)) {
                CashOutInfo cashOutInfo = new CashOutInfo();
                cashOutInfo.setId(id);
                cashOutInfo.setContens(remark);
                cashOutInfo.setChecktime(LocalDateTime.now());
                cashOutInfo.setStatus(status);
                this.cashOutInfoMapper.updateById(cashOutInfo);
                //消息通知
                CashOutInfo cashOutInfo1 = cashOutInfoMapper.selectById(id);
                String uid = cashOutInfo1.getUid();
                JpushClientUtil jpushClientUtil = new JpushClientUtil(remoteGoodsFeignService, remoteProductFeignService);
                switch (status) {
                    case "2":
                        jpushClientUtil.sendOrdinaryMsg(MsgConstant.CASHOUT_TITLE, MsgConstant.CASHOUT_SUCCESS_CONTENS, uid);
                        jpushClientUtil.saveMsg(uid, MsgConstant.CASHOUT_SUCCESS_CONTENS);
                        break;
                    case "3":
                        jpushClientUtil.sendOrdinaryMsg(MsgConstant.CASHOUT_TITLE, MsgConstant.CASHOUT_FAIL_CONTENS, uid);
                        jpushClientUtil.saveMsg(uid, MsgConstant.CASHOUT_FAIL_CONTENS+"原因："+remark);
                        break;
                    default:
                }
                return true;
            } else {
                throw new RuntimeException("不能更改为此状态！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public R userApplicationCash(CashOutInfo bean) {
        try {
            RjkjUser user = SecurityUtils.getUser();
            String orderNo = paySequence.nextNo();
            Map<String, String> data = remotePayFeignService.submitApplicationCash(bean.getType(), orderNo, bean.getUserNumber(), bean.getUserName(), bean.getAmount().toString()).getData();
            String code = data.get("code");

            //成功同意用户申请的提现请求后
            if ("SUCCESS".equals(code)) {

                UserOrder userOrderBean = new UserOrder();
                userOrderBean.setOid(IDUtils.getGUUID(""));
                userOrderBean.setPayUid(user.getId());
                userOrderBean.setRecvUid(bean.getUid());
                userOrderBean.setAmount(bean.getAmount());
                userOrderBean.setAmountType(bean.getType());
                userOrderBean.setStatus("2");
                userOrderBean.setPayTime(LocalDateTime.now());
                userOrderBean.setCtime(LocalDateTime.now());
                userOrderBean.setOrderType("2");
                userOrderBean.setDescription("平台同意提现申请");

                remoteUserService.saveUserOrder(userOrderBean);

                bean.setStatus("2");//申请通过
                cashOutInfoMapper.updateById(bean);

                return R.ok("", "提现成功");
            } else {
                return R.failed("", "提现失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }


}
