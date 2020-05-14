package com.rjkj.cf.bbibm.kjds.goods.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.SysFileUtils;
import com.rjkj.cf.bbibm.kjds.goods.entity.User;
import com.rjkj.cf.bbibm.kjds.goods.mapper.UserMapper;
import com.rjkj.cf.bbibm.kjds.goods.service.UserService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user" )
@Api(value = "user", tags = "mybatis-plus测试")
public class UserController {
    private final UserMapper userMapper;
    private final RemoteProductFeignService remoteProductFeignService;
    private final UserService userService;

    @ApiOperation(value = "mybatis-plus测试", notes = "mybatis-plus测试")
    @SysLog("mybatis-plus测试" )
    @GetMapping("testmybatis")
    public R selectAll() {
        List<User> users = userMapper.selectList(null);
        for (User user : users) {
            System.out.println(user);
        }
        return null;
    }

    @ApiOperation(value = "mybatis-plus测试新增", notes = "mybatis-plus测试新增")
    @SysLog("mybatis-plus测试" )
    @GetMapping("testAdd")
    public R testAdd() {
        User user = new User();
        user.setId(6);
        user.setName("nihao");
        user.setAge(60);
        user.setEmail("10010@qq.com");
        userMapper.insert(user);
        return null;
    }

    @ApiOperation(value = "mybatis-plus测试修改", notes = "mybatis-plus测试修改")
    @SysLog("mybatis-plus测试修改" )
    @GetMapping("testUpdate")
    public R testUpdate() {
        User user = userMapper.selectById(6);
        user.setName("妮妮");
        userMapper.updateById(user);
        return null;
    }

    @ApiOperation(value = "mybatis-plus测试删除", notes = "mybatis-plus测试删除")
    @SysLog("mybatis-plus测试删除" )
    @GetMapping("testDel")
    public R testDel() {
        userMapper.deleteById(6);
        return null;
    }

    @ApiOperation(value = "mybatis-plus测试高级查询", notes = "mybatis-plus测试高级查询")
    @SysLog("mybatis-plus测试高级查询" )
    @GetMapping("testSelectH")
    public R testSelectH() {
        List<User> billie = userMapper.selectList(Wrappers.<User>query().lambda()
                .eq(User::getName, "Billie"));
        return R.ok(billie);
    }

    @ApiOperation(value = "mybatis-plus测试分页", notes = "mybatis-plus测试分页")
    @SysLog("mybatis-plus测试分页" )
    @GetMapping("testPage")
    public R testPage() {
        Page<User> objectPage = new Page<>(2,2);
        IPage<User> userIPage = userMapper.selectPage(objectPage, null);
        return R.ok(userIPage);
    }

    @ApiOperation(value = "测试远程调用", notes = "测试远程调用")
    @SysLog("测试远程调用" )
    @GetMapping("testRest")
    public R testRest(String id) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity request = new HttpEntity(null, headers);
        return restTemplate.postForObject("http://kjds-product/product/testRestGet?id="+id, request, R.class);
    }

//    @ApiOperation(value ="测试feign远程调用", notes = "测试feign远程调用")
//    @SysLog("测试feign远程调用" )
//    @GetMapping("testfeign")
//    public R testfeign(String id) {
//        return remoteProductFeignService.testFeign(id,SecurityConstants.FROM);
//    }

    @PostMapping("/testTan")
    @Transactional(rollbackFor = Exception.class)
    public void test() {
        try {
            userService.testTran();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @PostMapping("/testTm")
//    @Transactional(rollbackFor = Exception.class)
//    public void testTm() {
//        try {
//            userService.testTm();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
