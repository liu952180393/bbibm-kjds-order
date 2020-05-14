package com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.controller;

import com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.service.ProductInfoService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 *@描述：
 *@项目：
 *@公司：软江科技
 *@作者：yihao
 *@时间：2019-10-31 10:39:56
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/productinfo" )
@Api(value = "productinfo", tags = "管理")
public class ProductInfoController {

    private final  ProductInfoService productInfoService;


    @PostMapping("/page" )
    public void getProductInfoPage() {
        productInfoService.add();
    }


}
