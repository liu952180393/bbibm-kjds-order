package com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.SysFileUtils;
import com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.entity.ProductInfo;
import com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.mapper.ProductInfoMapper;
import com.rjkj.cf.bbibm.kjds.product.alibabaItemInfo.service.ProductInfoService;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.Product;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.ProductVariant;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductService;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.ProductVariantService;
import com.rjkj.cf.common.core.util.R;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @描述：
 * @项目：
 * @公司：软江科技
 * @作者：yihao
 * @时间：2019-10-31 10:39:56
 **/
@Service
@AllArgsConstructor
public class ProductInfoServiceImpl extends ServiceImpl<ProductInfoMapper, ProductInfo> implements ProductInfoService {
    private final ProductInfoMapper productInfoMapper;
    private final ProductService productService;
    private final ProductVariantService productVariantService;
    private final RestTemplate restTemplate;

    @Override
    public void add() {
        Map<String,Object> map=new HashMap<>();
        map.put("catrgoryId","50012028");
        List<String> titleList = productInfoMapper.queryTitle(map);

        for (String title : titleList) {

           List<ProductInfo> productInfos = productInfoMapper.queryProductListByTitle(title);

            List<SysFile> listFile=new ArrayList<>();
            Product pro=new Product();
            String mainId = IDUtils.getGUUID("");
            pro.setId(mainId);

            //根据标题区分放置商品分类信息
            if(title.contains("雨鞋")) {
                pro.setProductClassificationName("女鞋,雨鞋");
                pro.setProductClassificationId("50012047");
            }else if(title.contains("高帮鞋")){
                pro.setProductClassificationName("女鞋,高帮鞋");
                pro.setProductClassificationId("50012825");
            }else if(title.contains("靴子")){
                pro.setProductClassificationName("女鞋,靴子");
                pro.setProductClassificationId("50012028");
            }else if(title.contains("凉鞋")){
                pro.setProductClassificationName("女鞋,凉鞋");
                pro.setProductClassificationId("50012032");
            }else if(title.contains("拖鞋")){
                pro.setProductClassificationName("女鞋,拖鞋");
                pro.setProductClassificationId("50012033");
            }else if(title.contains("帆布鞋")){
                pro.setProductClassificationName("女鞋,帆布鞋");
                pro.setProductClassificationId("50012042");
            }else{
                pro.setProductClassificationName("女鞋,低帮鞋");
                pro.setProductClassificationId("50012027");
            }



            //商品主图片id
            String parentImageId=IDUtils.getGUUID("");
            pro.setImage(parentImageId);
            pro.setAuditStatus("0");
            pro.setRackStatus(0);
            pro.setBrandName("无");
            pro.setManufacturerCode("109909");
            pro.setSupplierName("供应商");
            pro.setSupplierNumber("2000");
            pro.setStock(1000);
            //主商品sku
            Date parentDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdhh");
            String format = sdf.format(parentDate);
            String parentSku="RJ-" + format + "-" + IDUtils.getGUUID("").substring(0, 25);
            pro.setSku(parentSku);
            pro.setIshot("1");
            pro.setOriginArea("中国");

            pro.setProductTitle(productInfos.get(0).getTitle());
            pro.setProductHighlights("产品价格实惠质量好");
            pro.setUrl(productInfos.get(0).getUrl());
            pro.setPlatName("16909");
            pro.setCreateTime(LocalDateTime.now());
            pro.setCostUnitPrice(Double.valueOf(productInfos.get(0).getVariantPrice())*2);


            //如果产品主图不为空时
            if(StringUtils.isNotEmpty(productInfos.get(0).getImage())){
                String[] split = productInfos.get(0).getImage().split(",");
                if(split.length>9){
                    for(int i=0;i<9;i++){
                        //商品主体图片
                        SysFile fiZhu=new SysFile();
                        fiZhu.setId(parentImageId);
                        fiZhu.setPath(split[i]);
                        fiZhu.setCreateTime(LocalDateTime.now());
                        listFile.add(fiZhu);
                    }
                }else{
                    for(int i=0;i<split.length;i++){
                        //商品主体图片
                        SysFile fiZhu=new SysFile();
                        fiZhu.setId(parentImageId);
                        fiZhu.setPath(split[i]);
                        fiZhu.setCreateTime(LocalDateTime.now());
                        listFile.add(fiZhu);
                    }
                }


            }else{


                if(productInfos.size()>9){
                    for(int i=0;i<9;i++){
                        //商品主体图片
                        SysFile fiZhu=new SysFile();
                        fiZhu.setId(parentImageId);
                        fiZhu.setPath(productInfos.get(i).getVariantsImage());
                        fiZhu.setCreateTime(LocalDateTime.now());
                        listFile.add(fiZhu);
                    }
                }else{
                    for(int i=0;i<productInfos.size();i++){
                        //商品主体图片
                        SysFile fiZhu=new SysFile();
                        fiZhu.setId(parentImageId);
                        fiZhu.setPath(productInfos.get(i).getVariantsImage());
                        fiZhu.setCreateTime(LocalDateTime.now());
                        listFile.add(fiZhu);
                    }
                }


            }


//            List<SysFile> sysFile = SysFileUtils.getSysFile(product.getImage());
            pro.setProductImages(JSONObject.toJSONString(listFile));

            //变体信息
            List<ProductVariant> listVar=new ArrayList<>();
            for (ProductInfo productInfo : productInfos) {

                //商品子图片id
                String childImageId=IDUtils.getGUUID("");
                ProductVariant variant=new ProductVariant();
                variant.setId(IDUtils.getGUUID(""));

                variant.setPrice(Double.valueOf(productInfo.getVariantPrice())*2);
                variant.setVariantSize(productInfo.getVariantsSize());
                variant.setVariantColor(productInfo.getVariantsColor());
                variant.setStock(1000);
                variant.setSku("RJ-" + format + "-" + IDUtils.getGUUID("").substring(0, 25));
                variant.setImage(childImageId);
                variant.setParentSku(parentSku);
                variant.setCreateTime(LocalDateTime.now());

                listVar.add(variant);//变体信息设置完成


                //判断商品主图存在时
                if(StringUtils.isNotEmpty(productInfo.getImage())){
                    //将变体自己的图片存放
                    SysFile fis=new SysFile();
                    fis.setId(childImageId);
                    fis.setPath(productInfo.getVariantsImage());
                    fis.setCreateTime(LocalDateTime.now());
                    listFile.add(fis);

                    String[] split = productInfo.getImage().split(",");
                    //变体图片开始
                    if(split.length>8){
                        for(int m=0;m<8;m++){
                            fis=new SysFile();
                            fis.setId(childImageId);
                            fis.setPath(split[m]);
                            fis.setCreateTime(LocalDateTime.now());
                            listFile.add(fis);
                        }
                    }else{
                        for(int m=0;m<split.length;m++){
                            fis=new SysFile();
                            fis.setId(childImageId);
                            fis.setPath(split[m]);
                            fis.setCreateTime(LocalDateTime.now());
                            listFile.add(fis);
                        }
                    }


                }else{

                    //变体图片开始
                    if(productInfos.size()>9){
                        for(int m=0;m<9;m++){
                            SysFile fis=new SysFile();
                            fis.setId(childImageId);
                            fis.setPath(productInfos.get(m).getVariantsImage());
                            fis.setCreateTime(LocalDateTime.now());
                            listFile.add(fis);
                        }
                    }else{
                        for(int m=0;m<productInfos.size();m++){
                            SysFile fis=new SysFile();
                            fis.setId(childImageId);
                            fis.setPath(productInfos.get(m).getVariantsImage());
                            fis.setCreateTime(LocalDateTime.now());
                            listFile.add(fis);
                        }
                    }

                }

            }

            productService.save(pro);
            productVariantService.saveBatch(listVar);
            R r = restTemplate.postForObject("http://rj-admin/sys-file/addSysFiles", listFile, R.class);
            System.out.println(r.getCode());

        }
        System.out.println("完成新增信息");
    }
}
