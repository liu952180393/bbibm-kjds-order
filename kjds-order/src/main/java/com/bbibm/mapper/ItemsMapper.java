package com.bbibm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bbibm.entity.Items;

import java.util.ArrayList;

/**
 *@描述：详情表
 *@项目：
 *@公司：软江科技
 *@作者：liu
 *@时间：2020-05-13 14:49:54
 **/
public interface ItemsMapper extends BaseMapper<Items> {

    void insert(ArrayList<Items> items);
}
