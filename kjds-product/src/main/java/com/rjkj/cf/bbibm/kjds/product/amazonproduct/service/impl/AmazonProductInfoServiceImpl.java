package com.rjkj.cf.bbibm.kjds.product.amazonproduct.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.entity.AmazonProductInfo;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.mapper.AmazonProductInfoMapper;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.service.AmazonProductInfoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 *@描述：亚马逊亮点，描述库
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-11-06 15:52:56
 **/
@Service
@AllArgsConstructor
public class AmazonProductInfoServiceImpl extends ServiceImpl<AmazonProductInfoMapper, AmazonProductInfo> implements AmazonProductInfoService {

    private final  AmazonProductInfoMapper amazonProductInfoMapper;

    @Override
    public List<AmazonProductInfo> queryInfoByCategoryId(String categoryId) {
        return amazonProductInfoMapper.queryInfoByCategoryId(categoryId);
    }
}
