package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.entity.PlatformInfoVo;
import com.rjkj.cf.bbibm.kjds.api.entity.Product;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteProductFeignService;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteTransactionFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.PriceChangeUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.SysFileUtils;
import com.rjkj.cf.bbibm.kjds.goods.entity.Goods;
import com.rjkj.cf.bbibm.kjds.goods.entity.GoodsProductRsp;
import com.rjkj.cf.bbibm.kjds.goods.mapper.GoodsMapper;
import com.rjkj.cf.bbibm.kjds.goods.mapper.GoodsProductMapper;
import com.rjkj.cf.bbibm.kjds.goods.service.GoodsProductService;
import com.rjkj.cf.bbibm.kjds.goods.service.ProductVariantService;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.security.service.RjkjUser;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @描述：商户产品表
 * @项目：
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-10-08 17:54:19
 **/
@Service
@AllArgsConstructor
public class GoodsProductServiceImpl extends ServiceImpl<GoodsProductMapper, GoodsProduct> implements GoodsProductService {

    private final RemoteProductFeignService remoteProductFeignService;
    private final RemoteGoodsFeignService remoteGoodsFeignService;
    private final GoodsProductMapper goodsProductMapper;
    private final ProductVariantService productVariantService;
    private final RemoteTransactionFeignService remoteTransactionFeignService;
    private final GoodsMapper goodsMapper;
    private final RestTemplate restTemplate;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int extractProduct(String pids, RjkjUser user) {
        if (StringUtils.isEmpty(pids)) {
            throw new RuntimeException("产品id不能为空");
        }
        try {
            String[] pidArray = pids.split(",");
            List<GoodsProduct> goodsProducts = this.baseMapper.selectList(Wrappers.<GoodsProduct>query().lambda().eq(GoodsProduct::getUid, user.getId())
                    .in(GoodsProduct::getId, Arrays.asList(pidArray)));
            List<com.rjkj.cf.bbibm.kjds.api.entity.Goods> goodsList = remoteGoodsFeignService.getGoodsList().getData();
            if (goodsList == null || goodsList.size() < 1) {
                return 2;
            }
            R<List<Product>> listR = remoteProductFeignService.queryByProductIds(pids);
            if (listR.getCode() != 0) {
                throw new RuntimeException("拉取失败：" + listR.getMsg());
            }
            List<Product> data = listR.getData();
            List<GoodsProduct> collect = data.stream()
                    .filter(product -> checkGoodsProductExisit(product.getId(), goodsProducts))
                    .map(x -> {
                        GoodsProduct goodsProduct = new GoodsProduct();
                        goodsProduct.setId(x.getId());
                        goodsProduct.setBrandName(x.getBrandName());
                        goodsProduct.setChineseAbbreviation(x.getChineseAbbreviation());
                        goodsProduct.setCostUnitPrice(x.getCostUnitPrice());
                        goodsProduct.setCreateTime(LocalDateTime.now());
                        goodsProduct.setDescription(x.getDescription());
                        goodsProduct.setEnglishAbbreviation(x.getEnglishAbbreviation());
                        goodsProduct.setHeight(x.getHeight());
                        goodsProduct.setImage(x.getImage());
                        goodsProduct.setKeyWord(x.getKeyWord());
                        goodsProduct.setLength(x.getLength());
                        goodsProduct.setManufacturerCode(x.getManufacturerCode());
                        goodsProduct.setOriginArea(x.getOriginArea());
                        goodsProduct.setManufacturerName(x.getManufacturerName());
                        goodsProduct.setProductClassificationId(x.getProductClassificationId());
                        goodsProduct.setProductClassificationName(x.getProductClassificationName());
                        goodsProduct.setProductHighlights(x.getProductHighlights());
                        goodsProduct.setProductName(x.getProductName());
                        goodsProduct.setProductTitle(x.getProductTitle());
                        goodsProduct.setProductVariants(x.getVariantList());
                        goodsProduct.setRackStatus(6);
                        goodsProduct.setUid(user.getId());
                        String goodsSkuByBaseSku = KjdsUtils.getGoodsSkuByBaseSku(x.getSku());
                        System.out.println(goodsSkuByBaseSku);
                        goodsProduct.setSku(goodsSkuByBaseSku);
                        goodsProduct.setStock(x.getStock());
                        goodsProduct.setSupplierName(x.getSupplierName());
                        goodsProduct.setSupplierNumber(x.getSupplierNumber());
                        goodsProduct.setWeight(x.getWeight());
                        goodsProduct.setWide(x.getWide());
                        goodsProduct.setProductImages(x.getProductImages());
                        this.goodsProductMapper.insert(goodsProduct);
                        if (x.getVariantList() != null) {
                            List<ProductVariant> productVariants = x.getVariantList().stream()
                                    .map(y -> {
                                        ProductVariant productVariant = new ProductVariant();
                                        productVariant.setId(y.getId());
                                        productVariant.setCreateTime(LocalDateTime.now());
                                        productVariant.setImage(y.getImage());
                                        productVariant.setParentSku(goodsSkuByBaseSku);
                                        productVariant.setSku(KjdsUtils.getGoodsSkuByBaseSku(y.getSku()));
                                        productVariant.setStock(y.getStock());
                                        productVariant.setVariantColor(y.getVariantColor());
                                        productVariant.setPrice(y.getPrice());
                                        productVariant.setVariantSize(y.getVariantSize());
                                        productVariant.setUid(user.getId());
                                        return productVariant;
                                    }).collect(Collectors.toList());
                            goodsProduct.setProductVariants(productVariants);
                            this.productVariantService.saveBatch(productVariants);
                        }
                        return goodsProduct;
                    })
                    .collect(Collectors.toList());
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public IPage listGoodsProduct(RjkjUser user, Page page, GoodsProduct goodsProduct) {
        if (1 == 1) {
            return this.page(page, Wrappers.query(goodsProduct));
        } else {
//           return this.page(page,Wrappers.query(goodsProduct).lambda().eq(user.get))
        }
        return null;
    }


    private boolean checkGoodsProductExisit(String pid, List<GoodsProduct> goodsProducts) {
        for (GoodsProduct goodsProduct : goodsProducts) {
            if (pid.equals(goodsProduct.getId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changePrice(RjkjUser user, String ids, BigDecimal changePrice, int priceType, int type) {
        if (StringUtils.isEmpty(ids)) {
            throw new RuntimeException("产品id不能为空");
        }
        Optional.ofNullable(changePrice).orElseThrow(() -> new RuntimeException("改变价格不能为空"));
        if (priceType == 0) {
            throw new RuntimeException("价格类型不能为空");
        }
        if (type == 0) {
            throw new RuntimeException("价格改变类型不能为空");
        }
        this.baseMapper.selectUploadGoodsProduct(user.getId(), "", Arrays.asList(ids.split(",")), "")
                .stream().forEach(item -> {
            BigDecimal price = BigDecimal.valueOf(item.getCostUnitPrice());
            price = PriceChangeUtils.changePrice(price, changePrice, type, priceType);
            item.setCostUnitPrice(price.doubleValue());
            this.baseMapper.update(item, Wrappers.<GoodsProduct>query().lambda().eq(GoodsProduct::getUid, user.getId())
                    .eq(GoodsProduct::getId, item.getId())
                    .eq(GoodsProduct::getGid, ""));
            List<ProductVariant> productVariants = item.getProductVariants().stream()
                    .map(item1 -> {
                        BigDecimal itemPrice = BigDecimal.valueOf(item1.getPrice());
                        itemPrice = PriceChangeUtils.changePrice(itemPrice, changePrice, type, priceType);
                        item1.setPrice(itemPrice.doubleValue());
                        return item1;
                    }).collect(Collectors.toList());
            if (productVariants.size() > 0) {
                this.productVariantService.updateBatchById(productVariants);
            }
        });
        return 0;
    }


    @Override
    public IPage<GoodsProductRsp> goodsProductPlatformById(Page page, RjkjUser user, String pid, String gid, String status) {
        List<String> statusList = Arrays.asList(status.split(","));
        return goodsProductMapper.goodsProductPlatformById(page, user.getId(), pid, gid,statusList);
    }

    @Override
    public IPage<GoodsProductRsp> goodsProductlowershelf(Page page, RjkjUser user, String pid, String gid) {
        return goodsProductMapper.goodsProductlowershelf(page, user.getId(), pid, gid);

    }

    @Override
    public IPage<GoodsProductRsp> listWaitShelf(Page page, RjkjUser user, String gid) {
        return this.goodsProductMapper.listWaitShelf(page, user.getId(), gid);
    }

    @Override
//    @Transactional(rollbackFor = Exception.class)
    public int uploadGoodsProduct(Authentication token, RjkjUser user, String ids, String gid) {
        if (StringUtils.isEmpty(ids)) {
            throw new RuntimeException("产品id不能为空");
        }
        if (StringUtils.isEmpty(gid)) {
            throw new RuntimeException("店铺id不能为空");
        }

        //将商品id转换为list的方式
        List<String> idList = new ArrayList<String>(Arrays.asList(ids.split(",")));

        //查询当前上架的产品包含上传失败的产品信息
        List<GoodsProduct> goodsProductsError = this.baseMapper.selectList(Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getUid, user.getId())
                .eq(GoodsProduct::getRackStatus, 5)
                .in(GoodsProduct::getId, idList));

        //如果上传商品包含上架失败的产品时删除该数据
        if (goodsProductsError != null && goodsProductsError.size() > 0) {
            this.baseMapper.delete(Wrappers.<GoodsProduct>query().lambda()
                    .eq(GoodsProduct::getGid, gid)
                    .eq(GoodsProduct::getUid, user.getId())
                    .eq(GoodsProduct::getRackStatus, 5)
                    .in(GoodsProduct::getId, idList));
        }


        //根据用户和店铺id传店铺信息
        Goods goods = this.goodsMapper.selectOne(Wrappers.<Goods>query().lambda()
                .eq(Goods::getUid, user.getId())
                .eq(Goods::getSid, gid));
        //获取平台信息
        List<PlatformInfoVo> platformInfoVos = goodsMapper.priceList();
//            Optional.ofNullable(goods).orElseThrow(()->new RuntimeException("店铺不存在"));


        //根据对应的用户和店铺信息查询上传通过的产品信息
        List<GoodsProduct> goodsProducts1 = this.baseMapper.selectList(Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getUid, user.getId())
                .eq(GoodsProduct::getRackStatus, 0)
//                .or().eq(GoodsProduct::getRackStatus,4)
                .in(GoodsProduct::getId, idList));
        //对比遍历出需要上传的产品信息
        if (goodsProducts1 != null) {
            for (GoodsProduct goodsProduct : goodsProducts1) {
                for (int i = 0; i < idList.size(); i++) {
                    if (goodsProduct.getId().equals(idList.get(i))) {
                        idList.remove(i);
                        break;
                    }
                }
            }
        }
        if (idList.size() <= 0) {
//            throw new RuntimeException("商品已上传或上传中");
            return 0;
        }
        List<com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct> goodsProducts = this.baseMapper.selectUploadGoodsProduct(user.getId(), gid, idList, goods.getArea());
        if (goodsProducts.size() <= 0) {
//            throw new RuntimeException("商品已上传或上传中");
            return 0;
        }
        List<GoodsProduct> afterGoodsProdcut = new ArrayList<>();
        List<GoodsProduct> collect = new ArrayList<>();
        for (GoodsProduct goodsP : goodsProducts) {
            if (StringUtils.isEmpty(goodsP.getGid()) && StringUtils.isEmpty(goodsP.getArea())) {
                goodsP.setGid(gid);
                goodsP.setArea(goods.getArea());
                GoodsProduct goodsProduct = this.baseMapper.selectOne(Wrappers.<GoodsProduct>query().lambda()
                        .eq(GoodsProduct::getUid, goodsP.getUid())
                        .eq(GoodsProduct::getGid, gid)
                        .eq(GoodsProduct::getId, goodsP.getId())
                        .eq(GoodsProduct::getArea, goodsP.getArea()));

                if (goodsProduct == null) {
                    goodsP.setCreateTime(LocalDateTime.now());
                    collect.add(goodsP);
                    try {
                        List<SysFile> sysFile = SysFileUtils.getSysFile(goodsP.getImage());//设置产品图片
                        if (sysFile.size() > 0) {
                            goodsP.setProductImages(JSONObject.toJSONString(sysFile));
                        }
                        goodsP.setRackStatus(4);
                        goodsP.setUploadTime(LocalDateTime.now());
                        this.baseMapper.insert(goodsP);
                        ArrayList<String> pid = new ArrayList<>();
                        pid.add(goodsP.getId());
                        goodsProductMapper.updateRecycleLibrary(pid,goodsP.getUid(),1);
                    } catch (Exception e) {
                        collect.remove(goodsP);
                    }

                } else {
                    goodsProduct.setProductVariants(goodsP.getProductVariants());
                    addData(goodsProduct, platformInfoVos, collect, afterGoodsProdcut, goods);
                    goodsP.setRackStatus(4);
                    goodsP.setUploadTime(LocalDateTime.now());
                    goodsP.setQueue(1);
                    this.baseMapper.update(goodsP, Wrappers.<GoodsProduct>query().lambda()
                            .eq(GoodsProduct::getId, goodsP.getId())
                            .eq(GoodsProduct::getUid, goodsP.getUid())
                            .eq(GoodsProduct::getGid, gid)
                            .eq(GoodsProduct::getArea, goodsP.getArea()));
                    ArrayList<String> pid = new ArrayList<>();
                    pid.add(goodsP.getId());
                    goodsProductMapper.updateRecycleLibrary(pid,goodsP.getUid(),1);
                }
            } else {
                addData(goodsP, platformInfoVos, collect, afterGoodsProdcut, goods);
                goodsP.setRackStatus(4);
                goodsP.setUploadTime(LocalDateTime.now());
                this.baseMapper.updateById(goodsP);
                ArrayList<String> pid = new ArrayList<>();
                pid.add(goodsP.getId());
                goodsProductMapper.updateRecycleLibrary(pid,goodsP.getUid(),1);
            }
        }
        if (afterGoodsProdcut.size() > 0) {
            Iterator<GoodsProduct> iterator = afterGoodsProdcut.iterator();
            while (iterator.hasNext()) {
                GoodsProduct next = iterator.next();
                if (next.getQueue() == 1) {
                    iterator.remove();
                } else {
                    next.setQueue(1);
                    this.baseMapper.updateIsQueue(next.getGid(), next.getId(), user.getId(), 1);
                }
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) token.getDetails();
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
                    HttpEntity request = new HttpEntity(afterGoodsProdcut, headers);
                    restTemplate.postForObject("http://kjds-product/addItem/relistItem", request, R.class);
//                    remoteProductFeignService.relistItem(afterGoodsProdcut);
                }
            }).start();

        }
        if (collect.size() > 0) {
            Iterator<GoodsProduct> iterator = collect.iterator();
            while (iterator.hasNext()) {
                GoodsProduct next = iterator.next();
                if (next.getQueue() == 1) {
                    iterator.remove();
                } else {
                    next.setQueue(1);
                    this.baseMapper.updateIsQueue(next.getGid(), next.getId(), user.getId(), 1);
                }
            }
        }
        if (collect.size() > 0) {
//            OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) token.getDetails();
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
//            // 设置类型 "application/json;charset=UTF-8"
//            HttpEntity request = new HttpEntity(collect, headers);
//            restTemplate.postForObject("http://kjds-product/addItem/addItemToMall", request, R.class);
            rabbitTemplate.convertAndSend("upload_product", collect);
        }
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int upperGoodsProduct(Authentication token, RjkjUser user, String ids, String gid) {
        if (StringUtils.isEmpty(ids)) {
            throw new RuntimeException("产品id不能为空");
        }
        if (StringUtils.isEmpty(gid)) {
            throw new RuntimeException("店铺id不能为空");
        }
        //根据用户和店铺id查询店铺信息
        Goods goods = this.goodsMapper.selectOne(Wrappers.<Goods>query().lambda()
                .eq(Goods::getUid, user.getId())
                .eq(Goods::getSid, gid));
        //获取平台信息
        List<PlatformInfoVo> platformInfoVos = goodsMapper.priceList();
//            Optional.ofNullable(goods).orElseThrow(()->new RuntimeException("店铺不存在"));
        //将商品id转换为list的方式
        List<String> idList = new ArrayList<String>(Arrays.asList(ids.split(",")));

        //根据对应的用户和店铺信息查询上传通过的产品信息
        List<GoodsProduct> goodsProducts1 = this.baseMapper.selectList(Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getUid, user.getId())
                .eq(GoodsProduct::getRackStatus, 0)
//                .or().eq(GoodsProduct::getRackStatus,4)
                .in(GoodsProduct::getId, idList));
        //对比遍历出需要上传的产品信息
        if (goodsProducts1 != null) {
            for (GoodsProduct goodsProduct : goodsProducts1) {
                for (int i = 0; i < idList.size(); i++) {
                    if (goodsProduct.getId().equals(idList.get(i))) {
                        idList.remove(i);
                        break;
                    }
                }
            }
        }
        if (idList.size() <= 0) {
//            throw new RuntimeException("商品已上传或上传中");
            return 0;
        }
        List<com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct> goodsProducts = this.baseMapper.selectUploadGoodsProduct(user.getId(), gid, idList, goods.getArea());
        if (goodsProducts.size() <= 0) {
//            throw new RuntimeException("商品已上传或上传中");
            return 0;
        }
        List<GoodsProduct> afterGoodsProdcut = new ArrayList<>();
        List<GoodsProduct> collect = new ArrayList<>();
        goodsProducts.stream()
                .forEach(item -> {

                    if (StringUtils.isEmpty(item.getGid()) && StringUtils.isEmpty(item.getArea())) {
                        item.setGid(gid);
                        item.setArea(goods.getArea());
                        GoodsProduct goodsProduct = this.baseMapper.selectOne(Wrappers.<GoodsProduct>query().lambda()
                                .eq(GoodsProduct::getUid, item.getUid())
                                .eq(GoodsProduct::getGid, gid)
                                .eq(GoodsProduct::getId, item.getId())
                                .eq(GoodsProduct::getArea, item.getArea()));

                        if (goodsProduct == null) {
                            collect.add(item);
                            try {
                                List<SysFile> sysFile = SysFileUtils.getSysFile(item.getImage());//设置产品图片
                                if (sysFile.size() > 0) {
                                    item.setProductImages(JSONObject.toJSONString(sysFile));
                                }
                                item.setRackStatus(4);
                                this.baseMapper.insert(item);
                            } catch (Exception e) {
                                collect.remove(item);
                            }

                        } else {
                            goodsProduct.setProductVariants(item.getProductVariants());
                            addData(goodsProduct, platformInfoVos, collect, afterGoodsProdcut, goods);
                            item.setRackStatus(4);
                            this.baseMapper.update(item, Wrappers.<GoodsProduct>query().lambda()
                                    .eq(GoodsProduct::getId, item.getId())
                                    .eq(GoodsProduct::getUid, item.getUid())
                                    .eq(GoodsProduct::getGid, gid)
                                    .eq(GoodsProduct::getArea, item.getArea()));
                        }
                    } else {
                        addData(item, platformInfoVos, collect, afterGoodsProdcut, goods);
                        item.setRackStatus(4);
                        this.baseMapper.updateById(item);
                    }

                });
        if (afterGoodsProdcut.size() > 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) token.getDetails();
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
                    HttpEntity request = new HttpEntity(afterGoodsProdcut, headers);
                    restTemplate.postForObject("http://kjds-product/addItem/relistItem", request, R.class);
//                    remoteProductFeignService.relistItem(afterGoodsProdcut);
                }
            }).start();
        }
        if (collect.size() > 0) {
            rabbitTemplate.convertAndSend("upload_product", collect);
        }
//        remoteTransactionFeignService.transcationProudcts(collect,new StringBuilder()
//                .append("productTitle").append(",")
//                .append("productHighlights").append(",")
//                .append("keyWord").append(",")
//                .append("description").toString(), LANG.ZH,LANG.EN);
        return 0;
    }

    public void addData(GoodsProduct item, List<PlatformInfoVo> platformInfoVos, List<GoodsProduct> collect, List<GoodsProduct> afterGoodsProdcut, Goods goods) {
        if (item.getRackStatus() == 1) {
            platformInfoVos.forEach(platformItem -> {
                if (goods.getPid().equals(platformItem.getId())) {
                    if (platformItem.getType() == 3) {
                        collect.add(item);
                    } else {
                        afterGoodsProdcut.add(item);
                    }
                }
            });
        } else {
            collect.add(item);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProduct(String id, RjkjUser user) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("id不能为空");
        }
//        List<GoodsProduct> goodsProducts = this.baseMapper.selectList(Wrappers.<GoodsProduct>query().lambda()
//                .eq(GoodsProduct::getUid, user.getId())
//                .in(GoodsProduct::getId, Arrays.asList(id.split(",")))
//                .in(GoodsProduct::getRackStatus, 0));
        List<GoodsProduct> goodsProducts = null;
//        if (goodsProducts.size() > 0) {
//            throw new RuntimeException("上架的商品无法删除");
//        }
        goodsProducts = this.baseMapper.selectList(Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getUid, user.getId())
                .in(GoodsProduct::getId, Arrays.asList(id.split(",")))
                .in(GoodsProduct::getRackStatus, 6));
        goodsProducts.stream().forEach(item -> {
            List<GoodsProduct> goodsProducts1 = this.baseMapper.selectList(Wrappers.<GoodsProduct>query().lambda()
                    .eq(GoodsProduct::getUid, user.getId())
                    .eq(GoodsProduct::getId, item.getId())
                    .notIn(GoodsProduct::getRackStatus, 6));
            if (goodsProducts1.size() == 0) {
                productVariantService.remove(Wrappers.<ProductVariant>query().lambda()
                        .eq(ProductVariant::getParentSku, item.getSku())
                        .eq(ProductVariant::getUid, user.getId()));
            }
            this.baseMapper.delete(Wrappers.<GoodsProduct>query(item));
        });
        remoteProductFeignService.deleteRedisGoodsId(Arrays.asList(id.split(",")));
        return 0;
    }

    @Override
    public void updatePrice(RjkjUser user, String pid, String gid, BigDecimal price) {
        GoodsProduct goodsProduct = this.baseMapper.selectOne(Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getId, pid)
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getUid, user.getId()));
        goodsProduct.setCostUnitPrice(price.doubleValue());


        //执行修改价格方法对应到每一个店铺的每一个人所属商品
        this.baseMapper.update(goodsProduct, Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getId, pid)
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getUid, user.getId()));

    }

    @Override
    public void updateByGid(String uid, String pid, String gid, String errorMsg, String itemId, int rackStatus) {
        GoodsProduct goodsProduct = this.baseMapper.selectOne(Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getId, pid)
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getUid, uid));
        if (StringUtils.isNotBlank(errorMsg)) {
//            R res = remoteTransactionFeignService.transcationProudcts(errorMsg);
//            Object data = res.getData();
            String errorData = null;
            for (int i = 0; i < 5; i++) {
                try {
                    errorData = remoteTransactionFeignService.transcationSomeInfo(errorMsg, "EN", "ZH").getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(errorData)) {
                    break;
                }
            }

            if (StringUtils.isNotBlank(errorData)) {
                goodsProduct.setErrorMsg(errorData);
                System.out.println("翻译后的错误信息: " + errorData);
            } else {
                goodsProduct.setErrorMsg(errorMsg);
                System.out.println("未翻译的错误信息: " + errorMsg);
            }
        }
        goodsProduct.setRackStatus(rackStatus);
        goodsProduct.setItemId(itemId);
        goodsProduct.setQueue(0);
        this.baseMapper.update(goodsProduct, Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getId, pid)
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getUid, uid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BigDecimal updatePriceByGid(RjkjUser user, String pid, String gid, int type, int priceType, String area, BigDecimal changePrice) {
        if (StringUtils.isEmpty(pid)) {
            throw new RuntimeException("产品id不能为空");
        }
        if (StringUtils.isEmpty(gid)) {
            throw new RuntimeException("店铺id不能为空");
        }
        Optional.ofNullable(changePrice).orElseThrow(() -> new RuntimeException("改变价格不能为空"));
        if (type == 0) {
            throw new RuntimeException("type类型不能为空");
        }
        if (priceType == 0) {
            throw new RuntimeException("上下调整类型不能为空");
        }
        LambdaQueryWrapper<GoodsProduct> eq = Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getUid, user.getId())
                .eq(GoodsProduct::getGid, gid)
                .eq(GoodsProduct::getId, pid);
        if (StringUtils.isNotBlank(area)) {
            eq.eq(GoodsProduct::getArea, area);
        }
        GoodsProduct goodsProduct = this.baseMapper.selectOne(eq);
        Optional.ofNullable(goodsProduct).orElseThrow(() -> new RuntimeException("产品不存在"));
        List<ProductVariant> list = productVariantService.list(Wrappers.<ProductVariant>query().lambda()
                .eq(ProductVariant::getUid, user.getId())
                .eq(ProductVariant::getParentSku, goodsProduct.getSku()));
        BigDecimal changeValue = PriceChangeUtils.changePrice(BigDecimal.valueOf(goodsProduct.getCostUnitPrice()), changePrice, type, priceType);
        goodsProduct.setCostUnitPrice(changeValue.doubleValue());
        list.stream().forEach(item -> {
            BigDecimal changePrice1 = PriceChangeUtils.changePrice(BigDecimal.valueOf(item.getPrice()), changePrice, type, priceType);
            item.setPrice(changePrice1.doubleValue());
        });
        this.baseMapper.updateById(goodsProduct);
        productVariantService.updateBatchById(list);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeUnitPrice(RjkjUser user, String id, BigDecimal changePrice) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("产品id不能为空");
        }
        Optional.ofNullable(changePrice).orElseThrow(() -> new RuntimeException("产品改变值不能为空"));
        GoodsProduct goodsProduct = this.baseMapper.selectOne(Wrappers.<GoodsProduct>query().lambda().eq(GoodsProduct::getUid, user.getId())
                .eq(GoodsProduct::getId, id)
                .eq(GoodsProduct::getGid, ""));
        Optional.ofNullable(goodsProduct).orElseThrow(() -> new RuntimeException("请先添加产品"));
        goodsProduct.setCostUnitPrice(changePrice.multiply(BigDecimal.valueOf(goodsProduct.getCostUnitPrice())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        List<ProductVariant> list = productVariantService.list(Wrappers.<ProductVariant>query().lambda()
                .eq(ProductVariant::getParentSku, goodsProduct.getSku())
                .eq(ProductVariant::getUid, user.getId()));
        list.stream().forEach(item -> {
            item.setPrice(changePrice.multiply(BigDecimal.valueOf(item.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            ;
            productVariantService.update(item, Wrappers.<ProductVariant>query().lambda()
                    .eq(ProductVariant::getId, item.getId())
                    .eq(ProductVariant::getUid, item.getUid()));
        });
        this.baseMapper.update(goodsProduct, Wrappers.<GoodsProduct>query().lambda()
                .eq(GoodsProduct::getId, goodsProduct.getId())
                .eq(GoodsProduct::getGid, goodsProduct.getGid())
                .eq(GoodsProduct::getUid, goodsProduct.getUid()));
    }

    @Override
    public int updateBrand(String userId, String productId, String brand) {
        goodsProductMapper.updateBrand(userId, productId, brand);
        return 1;
    }

    @Override
    public IPage<GoodsProductRsp> recyclingLibraryList(Page page, RjkjUser user) {
        return goodsProductMapper.recyclingLibraryList(page, user.getId());
    }

    @Override
    public boolean removeLibrary(String ids, RjkjUser user) {
        try {
            List<String> idsList = Arrays.asList(ids.split(","));
            goodsProductMapper.updateRecycleLibrary(idsList,user.getId(),0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
