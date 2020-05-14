package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.goods.entity.User;
import com.rjkj.cf.bbibm.kjds.goods.mapper.UserMapper;
import com.rjkj.cf.bbibm.kjds.goods.service.UserService;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final RemoteProductFeignService remoteProductFeignService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void testTran() {
        User user = new User();
        user.setId(13);
        user.setName("张三");
        user.setAge(25);
        user.setEmail("");
        userMapper.insert(user);
        System.out.println(1 / 0);
        User user1 = new User();
        user1.setId(14);
        user1.setName("张三");
        user1.setAge(25);
        user1.setEmail("");
        userMapper.insert(user1);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    @LcnTransaction
//    public void testTm() {
//        User user = new User();
//        user.setId(8);
//        user.setName("张三");
//        user.setAge(25);
//        user.setEmail("");
//        userMapper.insert(user);
//        System.out.println(1/0);
//        remoteProductFeignService.testTran(SecurityConstants.FROM);
//    }
}
