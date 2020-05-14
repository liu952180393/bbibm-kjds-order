package com.rjkj.cf.bbibm.kjds.product.additem.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.api.utils.ComputedFreightPriceUtil;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.SysFileUtils;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariantsTranslation;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductTranslationService;
import com.rjkj.cf.common.core.util.R;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.rjkj.cf.bbibm.kjds.product.amazon.utils.MallConstant.*;
@AllArgsConstructor
@Component
@Slf4j
public class AddItemToMallService {
    private final AddItemService addItemService;
    private final ProductTranslationService productTranslationService;
    public void addItemToMall(List<GoodsProduct> goodsProduct){
        try {
            if (goodsProduct.size() > 0) {
                goodsProduct.forEach(iteam -> {
                    //增加运费价格
                    Double weight = iteam.getWeight();
                    BigDecimal weightDecimal = BigDecimal.ZERO;
                    if (weight == null) {
                        weightDecimal = new BigDecimal(1000);
                    } else {
                        weightDecimal = new BigDecimal(iteam.getWeight());
                    }
                    BigDecimal bigDecimal = ComputedFreightPriceUtil.changePriceRule(weightDecimal, new BigDecimal(iteam.getCostUnitPrice()), iteam.getArea(),iteam.getUid());
                    if (bigDecimal != null) {
                        iteam.setCostUnitPrice(bigDecimal.doubleValue());
                    }
                    //将图片设置到上传的商品属性中
                    String image = iteam.getImage();
                    List<SysFile> sysFile = SysFileUtils.getSysFile(image);
                    List<String> listBig = new ArrayList<>();
                    for (SysFile file : sysFile) {
                        listBig.add(file.getPath());
                    }
                    String area = iteam.getArea();
                    iteam.setImageList(listBig);
                    List<ProductTranslation> productTranslationList = productTranslationService.list(Wrappers.<ProductTranslation>query().lambda()
                            .eq(ProductTranslation::getProductId, iteam.getId())
                            .eq(ProductTranslation::getArea, area));

                    iteam.setProductHighlights(iteam.getProductHighlights().replaceAll("_msg_", "123msg123"));
                    if (productTranslationList.size() > 0) {
                        ProductTranslation productTranslation = productTranslationList.get(0);
                        //如果非TW和JP地区，则增加是否有中文的判断
                        if (!"TW".equals(area) && !"JP".equals(area)) {
                            String productTitle = productTranslation.getProductTitle();
                            //不为空且不包含中文字
                            if (StringUtils.isNotBlank(productTitle) && !KjdsUtils.isContainChinese(productTitle)) {
                                iteam.setProductTitle(productTitle);
                                //不满足则进行实时翻译
                            } else if (StringUtils.isBlank(productTitle) || KjdsUtils.isContainChinese(productTitle)) {
                                for (int i = 0; i < 5; i++) {
                                    String productTitle1 = null;
                                    try {
                                        productTitle1= KjdsUtils.transcationSomeInfo(iteam.getProductTitle(), "ZH", area).getData();
//                                        productTitle1 = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductTitle(), "ZH", area).getData();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (StringUtils.isNotBlank(productTitle1)) {
                                        if (!KjdsUtils.isContainChinese(productTitle1)) {
                                            iteam.setProductTitle(productTitle1);
                                            productTranslation.setProductTitle(productTitle1);
                                            break;
                                        }
                                    }
                                }
                            }
                            String highlights = productTranslation.getHighlights();
                            if (StringUtils.isNotBlank(highlights) && !KjdsUtils.isContainChinese(highlights)) {
                                iteam.setProductHighlights(highlights);
                            } else if (StringUtils.isBlank(highlights) || KjdsUtils.isContainChinese(highlights)) {
                                for (int i = 0; i < 5; i++) {
                                    String getProductHighlights = null;
                                    try {
                                        getProductHighlights= KjdsUtils.transcationSomeInfo(iteam.getProductHighlights(), "ZH", area).getData();
//                                        getProductHighlights = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductHighlights(), "ZH", area).getData();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (StringUtils.isNotBlank(getProductHighlights)) {
                                        if (!KjdsUtils.isContainChinese(getProductHighlights)) {
                                            iteam.setProductHighlights(getProductHighlights);
                                            productTranslation.setHighlights(getProductHighlights);
                                            break;
                                        }
                                    }
                                }
                            }
                            String keyword = productTranslation.getKeyword();
                            if (StringUtils.isNotBlank(keyword) && !KjdsUtils.isContainChinese(keyword)) {
                                iteam.setKeyWord(keyword);
                            } else if (StringUtils.isBlank(keyword) || KjdsUtils.isContainChinese(keyword)) {
                                for (int i = 0; i < 5; i++) {
                                    String getKeyWord = null;
                                    try {
                                        getKeyWord= KjdsUtils.transcationSomeInfo(iteam.getKeyWord(), "ZH", area).getData();
//                                        getKeyWord = remoteTransactionFeignService.transcationSomeInfo(iteam.getKeyWord(), "ZH", area).getData();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (StringUtils.isNotBlank(getKeyWord) && !KjdsUtils.isContainChinese(getKeyWord)) {
                                        iteam.setKeyWord(getKeyWord);
                                        productTranslation.setKeyword(getKeyWord);
                                        break;
                                    }
                                }

                            }
                            String description = productTranslation.getDescription();
                            if (StringUtils.isNotBlank(description) && !KjdsUtils.isContainChinese(description)) {
                                iteam.setDescription(description);
                            } else {
                                for (int i = 0; i < 5; i++) {
                                    String getDescription = null;
                                    try {
                                        getDescription= KjdsUtils.transcationSomeInfo(iteam.getDescription(), "ZH", area).getData();
//                                        getDescription = remoteTransactionFeignService.transcationSomeInfo(iteam.getDescription(), "ZH", area).getData();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (StringUtils.isNotBlank(getDescription) && !KjdsUtils.isContainChinese(getDescription)) {
                                        iteam.setDescription(getDescription);
                                        productTranslation.setDescription(getDescription);
                                        break;
                                    }
                                }

                            }
                            //对翻译后的新数据存入翻译表，避免下次重复翻译
                            productTranslationService.updateById(productTranslation);
                        } else {
                            //JP和TW地区处理
                            List list = translateProductInfo(iteam, productTranslation);
                            //翻译后的商品信息
                            GoodsProduct goodsProduct1 = (GoodsProduct) list.get(0);
                            ProductTranslation productTranslation1 = (ProductTranslation) list.get(1);
                            iteam.setProductTitle(goodsProduct1.getProductTitle());
                            iteam.setProductHighlights(goodsProduct1.getProductHighlights());
                            iteam.setKeyWord(goodsProduct1.getKeyWord());
                            iteam.setDescription(goodsProduct1.getDescription());
                            productTranslationService.updateById(productTranslation1);
                        }
                    } else {
                        //翻译表中如不存在翻译信息
                        ProductTranslation productTranslation = new ProductTranslation();
                        productTranslation.setId(IDUtils.getGUUID(""));
                        productTranslation.setProductId(iteam.getId());
                        productTranslation.setProductSku(iteam.getSku());
                        productTranslation.setArea(area);
                        productTranslation.setCreateTime(LocalDateTime.now());
                        List list = translateProductInfo(iteam, productTranslation);
                        GoodsProduct goodsProduct1 = (GoodsProduct) list.get(0);
                        ProductTranslation productTranslation1 = (ProductTranslation) list.get(1);
                        iteam.setProductTitle(goodsProduct1.getProductTitle());
                        iteam.setDescription(goodsProduct1.getDescription());
                        iteam.setProductHighlights(goodsProduct1.getProductHighlights());
                        iteam.setKeyWord(goodsProduct1.getKeyWord());
                        productTranslationService.save(productTranslation1);
                    }

                    //变体信息翻译
                    for (ProductVariant productVariant : iteam.getProductVariants()) {
                        ProductVariantsTranslation variantTranInfo = productTranslationService.getVariantTranInfo(productVariant.getSku(), area);
                        if (StringUtils.isNotBlank(productVariant.getVariantColor())) {
                            if (variantTranInfo != null) {
                                productVariant.setVariantColor(variantTranInfo.getVariantColor());
                            } else {
                                String stringColor = null;
                                try {
                                    stringColor= KjdsUtils.transcationSomeInfo(productVariant.getVariantColor(), "ZH", area).getData();
//                                    stringColor = remoteTransactionFeignService.transcationSomeInfo(productVariant.getVariantColor(), "ZH", area).getData();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (StringUtils.isNotBlank(stringColor)) {
                                    productVariant.setVariantColor(stringColor);
                                }
                            }
                        }

                        if (StringUtils.isNotBlank(productVariant.getVariantSize())) {
                            if (variantTranInfo != null) {
                                productVariant.setVariantColor(variantTranInfo.getVariantColor());
                            } else {
                                String stringSize = null;
                                try {
                                    stringSize= KjdsUtils.transcationSomeInfo(productVariant.getVariantSize(), "ZH", area).getData();
//                                    stringSize = remoteTransactionFeignService.transcationSomeInfo(productVariant.getVariantSize(), "ZH", area).getData();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (StringUtils.isNotBlank(stringSize)) {
                                    productVariant.setVariantSize(stringSize);
                                }
                            }
                        }
                        //增加运费价格
                        BigDecimal variantPrice = ComputedFreightPriceUtil.changePriceRule(weightDecimal, new BigDecimal(productVariant.getPrice()), iteam.getArea(),iteam.getUid());
                        if (variantPrice != null) {
                            productVariant.setPrice(variantPrice.doubleValue());
                        }
//                        List<String> listSmall = new ArrayList<>();
//                        List<SysFile> sysFileSmall = SysFileUtils.getSysFile(productVariant.getImage());
//                        for (SysFile file : sysFileSmall) {
//                            listSmall.add(file.getPath());
//                        }
//                        productVariant.setImageList(listSmall);
                        String description = iteam.getDescription();
                        String delSpanDesc = description.replaceAll("<span([^>]{0,})>", "")
                                .replaceAll("</span>", "")
                                .replaceAll("</ span>", "");
                        iteam.setDescription(delSpanDesc);
                    }
                });
            }
            System.out.println("=================================");

            String shopId = null;
//            int a = 1 / 0;
            String token = null;
            String sid = null;
            String marktPlaceId = null;
            String gid1 = goodsProduct.get(0).getGid();
            if (StringUtils.isBlank(gid1)) {
                throw new RuntimeException("gid为空！");
            }
            R<LinkedHashMap> goodsById = KjdsUtils.getGoodsById(gid1);
            LinkedHashMap data = goodsById.getData();
//            R<Goods> goodsById = remoteGoodsFeignService.getGoodsById(gid1);
//            Goods goods = goodsById.getData();
//            String type = goods.getPid();1
            String type = (String)data.get("pid");
            String area = (String)data.get("area");
            //18 亚马逊  19 Ebay  20 Shopee
            if ("18".equals(type)) {
                token = (String)data.get("mwsToken");
                shopId = (String)data.get("accountSiteId");
                sid = (String)data.get("sid");
                marktPlaceId = (String)data.get("area");
//                shopId = goods.getAccountSiteId();
//                token = goods.getMwsToken();
//                sid = goods.getSid();
//                marktPlaceId = goods.getArea();
            } else if ("19".equals(type)) {
                token = (String)data.get("mwsToken");
                sid = (String)data.get("sid");
//                token = goods.getMwsToken();
//                sid = goods.getSid();
            } else if ("20".equals(type)) {
                shopId = (String)data.get("accountSiteId");
                sid = (String)data.get("sid");
//                shopId = goods.getAccountSiteId();
//                sid = goods.getSid();
            }
            if (AMAZON.equals(type)) {
                addItemService.AmazonUploadInfo(goodsProduct, token, shopId, sid, area, marktPlaceId);
            } else if (EBAY.equals(type)) {
                for (GoodsProduct product : goodsProduct) {
                    addItemService.additemToEbay(token, sid, product);
                }
            } else if (SHOPEE.equals(type)) {
                for (GoodsProduct product : goodsProduct) {
                    addItemService.additemToShopee(shopId, sid, product);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }
    public List translateProductInfo(GoodsProduct iteam, ProductTranslation productTranslation) {
        String productTitle = productTranslation.getProductTitle();
        if (StringUtils.isNotBlank(productTitle)) {
            iteam.setProductTitle(productTitle);
        } else if (StringUtils.isBlank(productTitle)) {
            for (int i = 0; i < 5; i++) {
                String productTitle1 = null;
                try {
                    productTitle1= KjdsUtils.transcationSomeInfo(iteam.getProductTitle(), "ZH", iteam.getArea()).getData();
//                    productTitle1 = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductTitle(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(productTitle1)) {
                    iteam.setProductTitle(productTitle1);
                    productTranslation.setProductTitle(productTitle1);
                    break;
                }
            }
        }
        String highlights = productTranslation.getHighlights();
        if (StringUtils.isNotBlank(highlights)) {
            iteam.setProductHighlights(highlights);
        } else {
            for (int i = 0; i < 5; i++) {
                String getProductHighlights = null;
                try {
                    getProductHighlights= KjdsUtils.transcationSomeInfo(iteam.getProductHighlights(), "ZH", iteam.getArea()).getData();
//                    getProductHighlights = remoteTransactionFeignService.transcationSomeInfo(iteam.getProductHighlights(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(getProductHighlights)) {
                    iteam.setProductHighlights(getProductHighlights);
                    productTranslation.setHighlights(getProductHighlights);
                    break;
                }
            }
        }
        String keyword = productTranslation.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            iteam.setKeyWord(keyword);
        } else {
            for (int i = 0; i < 5; i++) {
                String getKeyWord = null;
                try {
                    getKeyWord= KjdsUtils.transcationSomeInfo(iteam.getKeyWord(), "ZH", iteam.getArea()).getData();
//                    getKeyWord = remoteTransactionFeignService.transcationSomeInfo(iteam.getKeyWord(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(getKeyWord)) {
                    iteam.setKeyWord(getKeyWord);
                    productTranslation.setKeyword(getKeyWord);
                    break;
                }
            }
        }
        String description = productTranslation.getDescription();
        if (StringUtils.isNotBlank(description)) {
            iteam.setDescription(description);
        } else {
            for (int i = 0; i < 5; i++) {
                String getDescription = null;
                try {
                    getDescription= KjdsUtils.transcationSomeInfo(iteam.getDescription(), "ZH", iteam.getArea()).getData();
//                    getDescription = remoteTransactionFeignService.transcationSomeInfo(iteam.getDescription(), "ZH", iteam.getArea()).getData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (StringUtils.isNotBlank(getDescription)) {
                    iteam.setDescription(getDescription);
                    productTranslation.setDescription(getDescription);
                    break;
                }
            }
        }
        ArrayList<Object> objects = new ArrayList<>();
        objects.add(iteam);
        objects.add(productTranslation);
        return objects;
    }
}
