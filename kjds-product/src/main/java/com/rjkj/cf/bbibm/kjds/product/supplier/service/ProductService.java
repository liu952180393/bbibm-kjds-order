package com.rjkj.cf.bbibm.kjds.product.supplier.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductTranscation;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.Product;
import com.rjkj.cf.common.security.service.RjkjUser;
import org.springframework.security.core.Authentication;

import java.util.List;

/**
 *@描述：供应商商品上传
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 14:55:08
 **/
public interface ProductService extends IService<Product> {

    /**
     * 上传供应商商品信息
     * @return
     */
    boolean addProductInfo(Product product);

    /**
     * 修改供应商商品信息
     * @return
     */
    boolean updateProductInfo(Product product);


    /**
     * 删除供应商商品信息
     * @return
     */
   boolean deleteProductInfo(String id);


    /**
     * 查询产品信息分类数据
     * @return
     */
    public IPage<Product> querySupplierProduct(Page page, String ishot, String categroryId);


    /**
     * 查询产品信息分类数据
     * @return
     */
    public IPage<Product> queryByProduct(Page page,String categroryId,String timeSort,String priceSort,String keyWord);


    int saveTranscationData(RjkjUser user, List<ProductTranscation> transcations);

    IPage<Product> getUnreviewedList(Page page, RjkjUser user);


    /**
     * 根据变体SKU查询父商品信息
     */
    Product getProductByVsku(String sku);

    void updateProductStatus(Authentication aToken, String productId, String auditStatus, String errMsg);

    void translationProductInfo(Authentication aToken, String productId);

    /**
     * 测试事务
     */
    void testTran();
}
