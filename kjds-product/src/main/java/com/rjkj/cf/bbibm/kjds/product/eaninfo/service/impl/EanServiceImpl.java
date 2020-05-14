package com.rjkj.cf.bbibm.kjds.product.eaninfo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.entity.Ean;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.mapper.EanMapper;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.service.EanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 *@描述：ean数据表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-17 14:46:51
 **/
@Service
public class EanServiceImpl extends ServiceImpl<EanMapper, Ean> implements EanService {

    @Resource
    private EanMapper eanMapper;

    @Override
    public Ean queryByOneEanInfo() {
        return eanMapper.queryByOneEanInfo();
    }
}
