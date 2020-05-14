package com.rjkj.cf.bbibm.kjds.product.supplier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductTranscation;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.SysFileUtils;
import com.rjkj.cf.bbibm.kjds.product.appcategory.entity.AppCategory;
import com.rjkj.cf.bbibm.kjds.product.appcategory.mapper.AppCategoryMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.Product;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.product.supplier.mapper.ProductMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.mapper.ProductVariantMapper;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductService;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductTranslationService;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


/**
 * @描述：供应商商品上传
 * @项目：
 * @公司：软江科技
 * @作者：crq
 * @时间：2019-10-09 14:55:08
 **/
@Service
@AllArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {


    private final ProductMapper productMapper;

    private final ProductVariantMapper productVariantMapper;

    private final AppCategoryMapper appCategoryMapper;

    private final RedisTemplate redisTemplate;

    private final ProductTranslationService productTranslationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addProductInfo(Product product) {
        RjkjUser user = SecurityUtils.getUser();
        try {
            if (product == null) {
                throw new RuntimeException("数据信息不能为空");
            }


            AppCategory appCategoryBean = appCategoryMapper.queryById(product.getProductClassificationId());
            product.setId(IDUtils.getGUUID(""));
            product.setAuditStatus("1");
            product.setRackStatus(0);
            product.setIshot("1");
            product.setPlatName("供应商上传");
            product.setUserId(user.getId());
            product.setCreateTime(LocalDateTime.now());
            String isPrivate = product.getIsPrivate();
            if(StringUtils.isNotBlank(isPrivate)){
                if("0".equals(isPrivate) || "1".equals(isPrivate)){
                    product.setIsPrivate(isPrivate);
                }else {
                    throw new RuntimeException("不包含此选项");
                }
            }else {
                throw new RuntimeException("是否私有选项必填！");
            }
            if (appCategoryBean != null) {
                product.setProductClassificationName(appCategoryBean.getPath());
            }

            //将供应商价格提百分之20
            BigDecimal purchasePrice = product.getPurchasePrice();
            double allPrice = purchasePrice.multiply(new BigDecimal(1.2)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            product.setCostUnitPrice(allPrice);

            //将排序后的图片信息重新设置进数据库
            if (product.getProductImageSort() != null && product.getProductImageSort().size() > 0) {
                SysFileUtils.deleteFile(product.getImage());

                List<SysFile> listSortImage = new ArrayList<>();
                for (int p = 0; p < product.getProductImageSort().size(); p++) {
                    SysFile bean = new SysFile();
                    bean.setId(product.getImage());
                    String[] split = product.getProductImageSort().get(p).getImageUrl().split("/");
                    bean.setFileName(split[split.length - 1]);
                    bean.setPath(product.getProductImageSort().get(p).getImageUrl());
                    bean.setPosition(product.getProductImageSort().get(p).getSort());
                    listSortImage.add(bean);
                }
                SysFileUtils.saveBeatch(listSortImage);


            }

            String image1 = product.getImage();
            if(StringUtils.isBlank(image1)){
                throw new RuntimeException("图片为空!");
            }
            List<SysFile> sysFile = SysFileUtils.getSysFile(image1);
            product.setProductImages(JSONObject.toJSONString(sysFile));

//            if (sysFile.size() > 9) {
//                List<SysFile> listSecond = new ArrayList<>();
//                for (int i = 0; i < 9; i++) {
//                    listSecond.add(sysFile.get(i));
//                }
//                product.setProductImages(JSONObject.toJSONString(listSecond));
//            } else {
//                product.setProductImages(JSONObject.toJSONString(sysFile));
//            }


            String parentSku = KjdsUtils.getProductSku();
            product.setSku(parentSku);
            productMapper.insert(product);

            if (product.getVariantList().size() > 0 && product.getVariantList() != null) {
                for (int i = 0; i < product.getVariantList().size(); i++) {
                    ProductVariant productVariant = product.getVariantList().get(i);
                    productVariant.setId(IDUtils.getGUUID(""));
                    productVariant.setParentSku(parentSku);
                    productVariant.setSort(i);
                    //将供应商价格提百分之20
                    BigDecimal variantPrice = productVariant.getVariantPrice();
                    double allVariantPrice = variantPrice.multiply(new BigDecimal(1.2)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    productVariant.setPrice(allVariantPrice);


                    String childSku = KjdsUtils.getProductSku();
                    productVariant.setSku(childSku);
                    productVariant.setCreateTime(LocalDateTime.now());

                    String imagesId = IDUtils.getGUUID("");
                    String image = productVariant.getImage();
                    if (StringUtils.isNotEmpty(image)) {
                        String[] splitImages = image.split(",");
                        List<SysFile> listVarImages = new ArrayList<>();

                        for (int j = 0; j < splitImages.length; j++) {
                            SysFile bean = new SysFile();
                            bean.setId(imagesId);
                            String[] split = splitImages[j].split("/");
                            bean.setFileName(split[split.length - 1]);
                            bean.setPath(splitImages[j]);
                            listVarImages.add(bean);
                        }
                        SysFileUtils.saveBeatch(listVarImages);

                    }
                    productVariant.setImage(imagesId);
                    productVariantMapper.insert(productVariant);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProductInfo(Product product) {
        try {

            if (product == null) {
                throw new RuntimeException("数据信息不能为空");
            }

            product.setPlatName("供应商上传");
            AppCategory appCategoryBean = appCategoryMapper.queryById(product.getProductClassificationId());
            if (appCategoryBean != null) {
                product.setProductClassificationName(appCategoryBean.getPath());
            }


            //将排序后的图片信息重新设置进数据库
            if (product.getProductImageSort() != null && product.getProductImageSort().size() > 0) {
                SysFileUtils.deleteFile(product.getImage());

                List<SysFile> listSortImage = new ArrayList<>();
                for (int p = 0; p < product.getProductImageSort().size(); p++) {
                    SysFile bean = new SysFile();
                    bean.setId(product.getImage());
                    String[] split = product.getProductImageSort().get(p).getImageUrl().split("/");
                    bean.setFileName(split[split.length - 1]);
                    bean.setPath(product.getProductImageSort().get(p).getImageUrl());
                    bean.setPosition(product.getProductImageSort().get(p).getSort());
                    listSortImage.add(bean);
                }
                SysFileUtils.saveBeatch(listSortImage);


            }


            List<SysFile> sysFile = SysFileUtils.getSysFile(product.getImage());
            product.setProductImages(JSONObject.toJSONString(sysFile));

//            if (sysFile.size() > 9) {
//                List<SysFile> listSecond = new ArrayList<>();
//                for (int i = 0; i < 9; i++) {
//                    listSecond.add(sysFile.get(i));
//                }
//                product.setProductImages(JSONObject.toJSONString(listSecond));
//            } else {
//                product.setProductImages(JSONObject.toJSONString(sysFile));
//            }

            //将供应商价格提百分之20
            BigDecimal purchasePrice = product.getPurchasePrice();
            double allPrice = purchasePrice.multiply(new BigDecimal(1.2)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            product.setCostUnitPrice(allPrice);
            product.setCreateTime(LocalDateTime.now());
            productMapper.updateById(product);

            productVariantMapper.delete(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getParentSku, product.getSku()));
            if (product.getVariantList().size() > 0) {
                for (int i = 0; i < product.getVariantList().size(); i++) {

                    ProductVariant productVariant = product.getVariantList().get(i);


                    //查询数据库原始变体数据
                    ProductVariant variant = productVariantMapper.selectOne(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getId, productVariant.getId()));
                    if (variant != null) {
                        //删除数据库中的数据
                        SysFileUtils.deleteFile(variant.getImage());
                    }

                    //将供应商价格提百分之20
                    BigDecimal variantPrice = productVariant.getVariantPrice();
                    double allVariantPrice = variantPrice.multiply(new BigDecimal(1.2)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    productVariant.setPrice(allVariantPrice);
                    String imagesId = IDUtils.getGUUID("");
                    String image = productVariant.getImage();
                    if (StringUtils.isNotEmpty(image)) {
                        String[] splitImages = image.split(",");
                        List<SysFile> listVarImages = new ArrayList<>();

                        for (int j = 0; j < splitImages.length; j++) {
                            SysFile bean = new SysFile();
                            bean.setId(imagesId);
                            String[] split = splitImages[j].split("/");
                            bean.setFileName(split[split.length - 1]);
                            bean.setPath(splitImages[j]);
                            listVarImages.add(bean);
                        }
                        SysFileUtils.saveBeatch(listVarImages);
                    }

                    productVariant.setImage(imagesId);
                    productVariant.setSort(i);
                    productVariant.setSku(KjdsUtils.getProductSku());
                    productVariant.setParentSku(product.getSku());
                    productVariant.setCreateTime(LocalDateTime.now());
//                    productVariantMapper.updateById(productVariant);
                    productVariant.setId(IDUtils.getGUUID(""));
                    productVariantMapper.insert(productVariant);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProductInfo(String id) {
        try {
            Product productBean = productMapper.selectById(id);
            productMapper.deleteById(id);
            SysFileUtils.deleteFile(productBean.getImage());

            List<ProductVariant> productVariantsBean = productVariantMapper.selectList(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getParentSku, productBean.getSku()));
            for (ProductVariant variantBean : productVariantsBean) {
                SysFileUtils.deleteFile(variantBean.getImage());
            }
            productVariantMapper.delete(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getParentSku, productBean.getSku()));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public IPage<Product> querySupplierProduct(Page page, String ishot, String categroryId) {
        IPage<Product> Page;

        RjkjUser user = SecurityUtils.getUser();
        //初始化redis
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        HashOperations hashOperations = redisTemplate.opsForHash();
        Iterator iterator = hashOperations.keys(user.getId()).iterator();
        List<String> listGoodsId = new ArrayList<>();
        while (iterator.hasNext()) {
            String next = iterator.next().toString();
            listGoodsId.add(next);
        }


        //当前数据为热门数据时
        if (StringUtils.equals("1", ishot)) {
            LambdaQueryWrapper<Product> query = Wrappers.<Product>query().lambda()
                    .eq(Product::getIshot, ishot)
                    .eq(Product::getAuditStatus, "0")
                    .and(wrapper -> wrapper.eq(Product::getIsPrivate,"0")
                            .or().eq(Product::getIsPrivate,"1")
                            .eq(Product::getUserId,user.getId()));

            if (listGoodsId.size() > 0) {//当用户添加了产品后
                query.notIn(Product::getId, listGoodsId);
            }
            Page = productMapper.selectPage(page, query);
        } else {
            ArrayList<String> list = new ArrayList<String>();
            List<AppCategory> appCategories = appCategoryMapper.queryByPathId(categroryId);
            for (int i = 0; i < appCategories.size(); i++) {
                list.add(appCategories.get(i).getId());
            }
            LambdaQueryWrapper<Product> query = Wrappers.<Product>query().lambda()
                    .eq(Product::getAuditStatus, "0")
                    .in(Product::getProductClassificationId, list)
                    .and(wrapper -> wrapper.eq(Product::getIsPrivate,"0")
                    .or().eq(Product::getIsPrivate,"1")
                    .eq(Product::getUserId,user.getId()));
            if (listGoodsId.size() > 0) {//当用户添加了产品后
                query.notIn(Product::getId, listGoodsId);
            }
            Page = productMapper.selectPage(page, query);
        }
        return Page;
    }

    @Override
    public IPage<Product> queryByProduct(Page page, String categroryId, String timeSort, String priceSort, String keyWord) {
        IPage<Product> Page = null;
        try {
            RjkjUser user = SecurityUtils.getUser();
            //初始化redis
            redisTemplate.setKeySerializer(new StringRedisSerializer());
            HashOperations hashOperations = redisTemplate.opsForHash();
            Iterator iterator = hashOperations.keys(user.getId()).iterator();
            List<String> listGoodsId = new ArrayList<>();
            while (iterator.hasNext()) {
                String next = iterator.next().toString();
                listGoodsId.add(next);
            }


            //分类id为空时查询全部商品信息
            LambdaQueryWrapper<Product> query = null;


            if (StringUtils.isBlank(categroryId)) {//判断分类id为空时
                if (StringUtils.isNotEmpty(keyWord)) {//判断关键字不为空时
                    query = Wrappers.<Product>query().lambda().eq(Product::getAuditStatus, "0").like(Product::getKeyWord, keyWord);
                } else {
                    query = Wrappers.<Product>query().lambda().eq(Product::getAuditStatus, "0");
                }
                if (listGoodsId != null && listGoodsId.size() > 0) {//当用户添加了产品后
                    query.notIn(Product::getId, listGoodsId);
                }
                //根据两个字段排序都需要排序时
                if (StringUtils.isNoneBlank(timeSort) && StringUtils.isNoneBlank(priceSort)) {
                    if ("asc".equals(timeSort) && "asc".equals(priceSort)) {
                        query.orderBy(true, true, Product::getCreateTime);
                        query.orderBy(true, true, Product::getCostUnitPrice);
                    } else if ("asc".equals(timeSort) && "desc".equals(priceSort)) {
                        query.orderBy(true, true, Product::getCreateTime);
                        query.orderBy(true, false, Product::getCostUnitPrice);
                    } else if ("desc".equals(timeSort) && "asc".equals(priceSort)) {
                        query.orderBy(true, false, Product::getCreateTime);
                        query.orderBy(true, true, Product::getCostUnitPrice);
                    }
                    //根据时间排序
                } else if (StringUtils.isNoneBlank(timeSort)) {
                    if ("asc".equals(timeSort)) {//升序
                        query.orderBy(true, true, Product::getCreateTime);
                    } else {//降序
                        query.orderBy(true, false, Product::getCreateTime);
                    }
                    //根据价格排序
                } else if (StringUtils.isNoneBlank(priceSort)) {
                    if ("asc".equals(priceSort)) {//升序
                        query.orderBy(true, true, Product::getCostUnitPrice);
                    } else {//降序
                        query.orderBy(true, false, Product::getCostUnitPrice);
                    }
                }

                //设置用户商品数据是否公开查询问题
                query.and(wrapper -> wrapper.eq(Product::getIsPrivate,"0")
                        .or().eq(Product::getIsPrivate,"1")
                        .eq(Product::getUserId,user.getId()));

                Page = productMapper.selectPage(page, query);
            } else {

                List<String> list = new ArrayList<>();
                AppCategory appCategory = appCategoryMapper.queryById(categroryId);
                Integer level = appCategory.getLevel();
                //判断当前分类层级为第一级时
                if (level == 1) {
                    List<AppCategory> appCategories = appCategoryMapper.queryByPathId(categroryId);
                    for (int i = 0; i < appCategories.size(); i++) {
                        list.add(appCategories.get(i).getId());
                    }
                } else {
                    list.add(appCategory.getId());
                }
                //判断关键字不为空的时候
                if (StringUtils.isNotEmpty(keyWord)) {
                    query = Wrappers.<Product>query().lambda().eq(Product::getAuditStatus, "0").in(Product::getProductClassificationId, list).like(Product::getKeyWord, keyWord);
                } else {
                    query = Wrappers.<Product>query().lambda().eq(Product::getAuditStatus, "0").in(Product::getProductClassificationId, list);
                }

                if (listGoodsId != null && listGoodsId.size() > 0) {//当用户添加了产品后
                    query.notIn(Product::getId, listGoodsId);
                }
                //根据两个字段排序都需要排序时
                if (StringUtils.isNoneBlank(timeSort) && StringUtils.isNoneBlank(priceSort)) {
                    if ("asc".equals(timeSort) && "asc".equals(priceSort)) {
                        query.orderBy(true, true, Product::getCreateTime);
                        query.orderBy(true, true, Product::getCostUnitPrice);
                    } else if ("asc".equals(timeSort) && "desc".equals(priceSort)) {
                        query.orderBy(true, true, Product::getCreateTime);
                        query.orderBy(true, false, Product::getCostUnitPrice);
                    } else if ("desc".equals(timeSort) && "asc".equals(priceSort)) {
                        query.orderBy(true, false, Product::getCreateTime);
                        query.orderBy(true, true, Product::getCostUnitPrice);
                    }
                    //根据时间排序
                } else if (StringUtils.isNoneBlank(timeSort)) {
                    if ("asc".equals(timeSort)) {//升序
                        query.orderBy(true, true, Product::getCreateTime);
                    } else {//降序
                        query.orderBy(true, false, Product::getCreateTime);
                    }
                    //根据价格排序
                } else if (StringUtils.isNoneBlank(priceSort)) {
                    if ("asc".equals(priceSort)) {//升序
                        query.orderBy(true, true, Product::getCostUnitPrice);
                    } else {//降序
                        query.orderBy(true, false, Product::getCostUnitPrice);
                    }
                }

                //设置用户商品数据是否公开查询问题
                query.and(wrapper -> wrapper.eq(Product::getIsPrivate,"0")
                        .or().eq(Product::getIsPrivate,"1")
                        .eq(Product::getUserId,user.getId()));

                Page = productMapper.selectPage(page, query);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Page;
    }

    @Override
    public int saveTranscationData(RjkjUser user, List<ProductTranscation> transcations) {
        return 0;
    }

    @Override
    public IPage<Product> getUnreviewedList(Page page, RjkjUser user) {
        return productMapper.selectPage(page, Wrappers.<Product>query().lambda().eq(Product::getUserId, user.getId())
                .eq(Product::getAuditStatus, "1"));
    }


    @Override
    public Product getProductByVsku(String sku) {
        ProductVariant productVariant = productVariantMapper.selectOne(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getSku, sku));
        return this.baseMapper.selectOne(Wrappers.<Product>query().lambda().eq(Product::getSku, productVariant.getParentSku()));
    }


    @Override
    public void updateProductStatus(Authentication aToken, String productId, String auditStatus, String errMsg) {
        try {
            String[] productIds = productId.split(",");
            Product product = new Product();
            for (String id : productIds) {
                if ("0".equals(auditStatus)) {
                    product.setAuditStatus(auditStatus);
                    productTranslationService.translationProductInfo(aToken, id);
                    product.setTranslated("1");
                }
                if ("2".equals(auditStatus)) {
                    product.setErrMsg(errMsg);
                    product.setAuditStatus(auditStatus);
                }
                product.setId(id);
                this.baseMapper.updateById(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    @Async
    public void translationProductInfo(Authentication aToken, String productId) {
        String[] split = productId.split(",");
        ArrayList<String> productIds = new ArrayList<>(Arrays.asList(split));
        List<Product> products = this.baseMapper.selectBatchIds(productIds);
        for (Product product : products) {
            if ("0".equals(product.getTranslated())) {
                productTranslationService.translationProductInfo(aToken, productId);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LcnTransaction
    public void testTran() {
        Product product = new Product();
        product.setId("666666666");
        product.setProductClassificationName("1111111111111");
        product.setProductClassificationId("111111");
        product.setClassificationOne("11111");
        product.setImage("11111");
        product.setAuditStatus("1111111");
        product.setRackStatus(0);
        product.setBrandName("111111");
        product.setManufacturerCode("11111");
        productMapper.insert(product);
    }

}
