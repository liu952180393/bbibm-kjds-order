package com.rjkj.cf.bbibm.kjds.product.eaninfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.product.eaninfo.entity.Ean;

/**
 *@描述：ean数据表
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-17 14:46:51
 **/
public interface EanService extends IService<Ean> {

    /**
     * 查询一条没有使用过的ean信息
     * @return
     */
    public Ean queryByOneEanInfo();

}
