package com.rjkj.cf.bbibm.kjds.api.feign;


import com.rjkj.cf.admin.api.entity.SysUser;
import com.rjkj.cf.admin.api.entity.UserInfoRsp;
import com.rjkj.cf.admin.api.entity.UserJoinVo;
import com.rjkj.cf.admin.api.vo.UserVO;
import com.rjkj.cf.bbibm.kjds.api.entity.UserOrder;
import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.core.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @描述：商户内部接口
 * @项目：bbibk-rjkj
 * @公司：软江科技
 * @作者：crq
 * @时间：2019/10/9 15:41
 **/
@FeignClient(contextId = "remoteUserFeignService", value = Const.USER_SERVICE_NAME)
public interface RemoteUserFeignService {


    /**
     * 保存用户产生的订单信息
     * @param userOrder
     * @return
     */
    @PostMapping("/userorder/saveUserOrder")
    R saveUserOrder(@RequestBody UserOrder userOrder);


    /**
     * 获取个人信息数据
     * @return
     */
    @PostMapping(value = "/user/get/my/info")
    R<UserInfoRsp> getUserInfo();

    /**
     * 根据用户ID获取用户信息
     *
     * @return R
     */
    @PostMapping("/user/getUserById")
    R<UserVO> getUserById(@RequestParam(value = "id") String id,
                          @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * 修改用户信息
     *
     * @return R
     */
    @PostMapping("/user/updateById")
    R updateById(@RequestBody SysUser sysUser,
                 @RequestHeader(SecurityConstants.FROM) String from);


    /**
     * 根据手机号查询用户关联信息
     *
     * @return R
     */
    @PostMapping("/userjoin/queryByShareInfo")
    R<UserJoinVo> queryByShareInfo(@RequestParam(value = "phone") String phone);

    /**
     * 更新用戶身份
     *
     * @return
     */
    @PostMapping("/user/updateUserRole")
    R updateUserRole(@RequestParam(value ="userId") String userId,
                     @RequestParam(value ="roleType") String roleType,
                     @RequestParam(value ="type") String type);


}
