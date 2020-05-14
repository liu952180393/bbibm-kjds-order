package com.rjkj.cf.bbibm.kjds.product.supplier.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.bbibm.kjds.api.feign.RemoteTransactionFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.HtmlRepliceUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.SysFileUtils;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.entity.AmazonProductInfo;
import com.rjkj.cf.bbibm.kjds.product.amazonproduct.service.AmazonProductInfoService;
import com.rjkj.cf.bbibm.kjds.product.pulloutnumer.entity.ProductPulloutNumber;
import com.rjkj.cf.bbibm.kjds.product.pulloutnumer.service.ProductPulloutNumberService;
import com.rjkj.cf.bbibm.kjds.product.supplier.entity.*;
import com.rjkj.cf.bbibm.kjds.product.supplier.service.*;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.common.log.annotation.SysLog;
import com.rjkj.cf.common.security.service.RjkjUser;
import com.rjkj.cf.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


/**
 *@描述：供应商商品上传
 *@项目：
 *@公司：软江科技
 *@作者：crq
 *@时间：2019-10-09 14:55:08
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/product" )
@Api(value = "product", tags = "供应商商品上传管理")
public class ProductController {

    private final  ProductService productService;

    private final ProductVariantService productVariantService;

    private final RedisTemplate redisTemplate;

    private final RestTemplate  restTemplate;

    private final RemoteTransactionFeignService remoteTransactionFeignService;

    private final ProductPulloutNumberService productPulloutNumberService;

    private final AmazonProductInfoService amazonProductInfoService;

    private final SupplierRoyaltyAppoveService supplierRoyaltyAppoveService;

    private final ProductTranslationService productTranslationService;

    private final ProductVariantsTranslationService productVariantsTranslationService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param product 供应商商品上传
     * @return
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page" )
    public R getProductPage(Page page, Product product) {
        RjkjUser user = SecurityUtils.getUser();
        LambdaQueryWrapper<Product> query = Wrappers.<Product>query().lambda().eq(Product::getUserId, user.getId());
//        LambdaQueryWrapper<Product> query = Wrappers.<Product>query().lambda();
        if(StringUtils.isNotEmpty(product.getAuditStatus())){
            query.eq(Product::getAuditStatus,product.getAuditStatus());
        }
        if(StringUtils.isNotEmpty(product.getProductTitle())){
            query.like(Product::getProductTitle,product.getProductTitle());
        }
        if(StringUtils.isNotEmpty(product.getKeyWord())){
            query.like(Product::getKeyWord,product.getKeyWord());
        }
        if(StringUtils.isNotEmpty(product.getManufacturerCode())){
            query.like(Product::getManufacturerCode,product.getManufacturerCode());
        }
        if(StringUtils.isNotEmpty(product.getSupplierNumber())){
            query.like(Product::getSupplierNumber,product.getSupplierNumber());
        }
        return R.ok(productService.page(page,query));
    }



    /**
     * 平台查询供应商商品列表
     * @param page 分页对象
     * @return
     */
    @ApiOperation(value = "平台查询供应商商品列表", notes = "平台查询供应商商品列表")
    @PostMapping("/getUnreviewedList" )
    public R getUnreviewedList(Page page,Product product) {

        LambdaQueryWrapper<Product> query = Wrappers.<Product>query().lambda().orderByDesc(Product::getCreateTime);
        if(StringUtils.isNotEmpty(product.getAuditStatus())){
            query.eq(Product::getAuditStatus,product.getAuditStatus());
        }
        if(StringUtils.isNotEmpty(product.getProductTitle())){
            query.like(Product::getProductTitle,product.getProductTitle());
        }
        if(StringUtils.isNotEmpty(product.getKeyWord())){
            query.like(Product::getKeyWord,product.getKeyWord());
        }
        if(StringUtils.isNotEmpty(product.getManufacturerCode())){
            query.like(Product::getManufacturerCode,product.getManufacturerCode());
        }
        if(StringUtils.isNotEmpty(product.getSupplierNumber())){
            query.like(Product::getSupplierNumber,product.getSupplierNumber());
        }
        return R.ok(productService.page(page,query));
    }

    /**
     * 新增供应商商品上传
     * @param product 供应商商品上传
     * @return R
     */
    @ApiOperation(value = "新增供应商商品上传", notes = "新增供应商商品上传")
    @SysLog("新增供应商商品上传" )
    @PostMapping("/addSupplierProduct")
    public R addSupplierProduct(@RequestBody Product product) {
        return R.ok(productService.addProductInfo(product));
    }



    /**
     * 修改供应商商品上传
     * @param product 修改供应商商品上传
     * @return R
     */
    @ApiOperation(value = "修改供应商商品上传", notes = "修改供应商商品上传")
    @SysLog("修改供应商商品上传" )
    @PostMapping("/updateSupplierProduct")
    public R updateSupplierProduct(@RequestBody Product product) {
        return R.ok(productService.updateProductInfo(product));
    }

    /**
     * 平台审批商品信息
     * @return R
     */
    @ApiOperation(value = "平台审批商品信息（商品审批）", notes = "平台审批商品信息")
    @SysLog("平台审批商品信息" )
    @PostMapping("/approveSupplierProduct")
    public R approveSupplierProduct(Authentication aToken,String productId, String auditStatus, String errMsg) {
        try {
            if(StringUtils.isBlank(productId)){
                throw new RuntimeException("待操作的商品ID不能为空。");
            }
            if(StringUtils.isBlank(auditStatus)){
                throw new RuntimeException("需要更改的商品状态为空。");
            }
            productService.updateProductStatus(aToken,productId, auditStatus, errMsg);
            return R.ok(true,"操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("操作失败！");
        }
    }

    /**
     * 根据商品ID翻译商品信息，存入数据库
     * @return R
     */
    @ApiOperation(value = "根据商品ID翻译商品信息，存入数据库", notes = "根据商品ID翻译商品信息，存入数据库")
    @SysLog("根据商品ID翻译商品信息，存入数据库" )
    @PostMapping("/translationProductInfo")
    public R translationProductInfo(Authentication aToken,String productId) {
        try {
            if(StringUtils.isBlank(productId)){
                throw new RuntimeException("待操作的商品ID不能为空。");
            }
            productService.translationProductInfo(aToken,productId);
            return R.ok(true,"操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("操作失败！");
        }
    }

    @PostMapping("/translationTest")
    public R translationTest(Authentication aToken) {
        try {
            List<Product> list = productService.list();
            int i=0;
            int j=0;
//            ArrayList<String> objects = new ArrayList<>();
//            for (Product product : list) {
//                List<ProductTranslation> list1 = productTranslationService.list(Wrappers.<ProductTranslation>query().lambda().eq(ProductTranslation::getProductSku, product.getSku()));
//                if(list1.size()<11){
//                    productTranslationService.remove(Wrappers.<ProductTranslation>query().lambda().eq(ProductTranslation::getProductSku, product.getSku()));
//                    objects.add(product.getId());
//                 }
//                j=++j;
//                System.out.println("执行了： "+j+"次");
//            }
//            System.out.println(objects.size());
//            for (String object : objects) {
//                i=++i;
//                System.out.println("执行了： "+i+"次");
//                productService.translationProductInfo(aToken,object);
//                Thread.sleep(3000);
//            }
            for (Product product : list) {
                i=++i;
                System.out.println("执行了： "+i+"次");
                productService.translationProductInfo(aToken,product.getId());
                Thread.sleep(3000);
            }
            return R.ok(true,"操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("操作失败！");
        }
    }

    @PostMapping("/checkTranslation")
    public R checkTranslation(Authentication aToken) {
        try {
            List<ProductTranslation> list = productTranslationService.list();
            for (ProductTranslation productTranslation : list) {
                String area = productTranslation.getArea();
                    String productTitle = productTranslation.getProductTitle();
                    if(StringUtils.isBlank(productTitle)){
                        Product one = productService.getOne(Wrappers.<Product>query().lambda().eq(Product::getSku, productTranslation.getProductSku()));
                        String transcationSomeInfo = remoteTransactionFeignService.transcationSomeInfo(one.getProductTitle(), "ZH", area).getData();
                        if(StringUtils.isNotBlank(transcationSomeInfo)){
                            productTranslation.setProductTitle(transcationSomeInfo);
                        }
                    }






            }
            return R.ok(true,"操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("操作失败！");
        }
    }

    /**
     * 通过id删除供应商商品上传
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id删除供应商商品上传", notes = "通过id删除供应商商品上传")
    @SysLog("通过id删除供应商商品上传" )
    @DeleteMapping("/{id}" )
    public R removeById(@PathVariable String id) {
        return R.ok(productService.deleteProductInfo(id));
    }



    /**
     * 根据商品id查询商品的变体信息
     * @param id id
     * @return R
     */
    @ApiOperation(value = "根据商品id查询商品的变体信息", notes = "根据商品id查询商品的变体信息")
    @SysLog("根据商品id查询商品的变体信息" )
    @PostMapping("/queryByVartionInfo")
    public R<Product> queryByVartionInfo(@RequestParam String id){
        try {
            Product pro = productService.getOne(Wrappers.<Product>query().lambda().eq(Product::getId,id));

            if(pro!=null){
                List<SysFile> sysFile = SysFileUtils.getSysFile(pro.getImage());
                pro.setImageList(sysFile);
                List<ProductVariant> productVariants = productVariantService.queryByParentSku(pro.getSku());
                if(productVariants!=null){
                    for(int i=0;i<productVariants.size();i++){
                        productVariants.get(i).setImageList(SysFileUtils.getSysFile(productVariants.get(i).getImage()));
                    }
                }
                pro.setVariantList(productVariants);
            }
            return R.ok(pro);
        }catch (Exception e){
             return R.failed(e.getMessage());
        }

    }



    /**
     * (首页)根据分类查询供应商商品数据
     * @param
     * @return R
     */
    @ApiOperation(value = "(首页)根据分类查询供应商商品数据", notes = "(首页)根据分类查询供应商商品数据")
    @SysLog("(首页)根据分类查询供应商商品数据" )
    @ApiImplicitParams({
            @ApiImplicitParam(name ="ishot",value = "是否热门(0为不热门数据，1为热门数据)",required = true,dataType = "String"),
            @ApiImplicitParam(name ="categroryId",value = "分类id(ishot为1时分类id为0，为0时就为分类的id)",required = true,dataType = "String"),
    })
    @PostMapping("/querySupplierProduct")
    public R<IPage<Product>> querySupplierProduct(Page page,String ishot,String categroryId){
        try {
            if(StringUtils.isEmpty(ishot)){
                throw new RuntimeException("ishot不能为空");
            }
            if(StringUtils.isEmpty(categroryId)){
                throw new RuntimeException("categroryId不能为空");
            }
            IPage<Product> productIPage = productService.querySupplierProduct(page, ishot, categroryId);
            List<Product> records = productIPage.getRecords();
            if(records!=null){
                for(int i=0;i<records.size();i++){
//                    records.get(i).setImageList(SysFileUtils.getSysFile(records.get(i).getImage()));
                    //设置商品上架件数
                    int count = productPulloutNumberService.count(Wrappers.<ProductPulloutNumber>query().lambda().eq(ProductPulloutNumber::getProductId, records.get(i).getId()));
                    records.get(i).setPullNumber(count);
                    String description = records.get(i).getDescription();
                    if(StringUtils.isNotBlank(description)){
                        records.get(i).setDescriptionTwo(HtmlRepliceUtils.parseAllHtmlInfo(records.get(i).getDescription()));
                    }
//                    List<ProductVariant> list = productVariantService.list(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getParentSku, records.get(i).getSku()));
//                    if(list!=null&&list.size()>0){
//                        for(int m=0;m<list.size();m++){
//                            list.get(m).setImageList(SysFileUtils.getSysFile(list.get(m).getImage()));
//                        }
//                        records.get(i).setVariantList(list);
//                    }
                }
            }
            productIPage.setRecords(records);
            return R.ok(productIPage);
        }catch (Exception e){
            e.printStackTrace();
            return R.failed(e.getMessage());
        }

    }


    /**
     * (产品页面)查询产品页面数据
     * @param
     * @return R
     */
    @ApiOperation(value = "(产品页面)查询产品页面数据", notes = "(产品页面)查询产品页面数据")
    @SysLog("(产品页面)查询产品页面数据" )
    @ApiImplicitParams({
            @ApiImplicitParam(name ="categroryId",value = "分类id(为空时默认显示全部产品数据)",required = false,dataType = "String"),
            @ApiImplicitParam(name ="timeSort",value = "时间排序（asc为升序,desc为降序,默认为）",required = false,dataType = "String"),
            @ApiImplicitParam(name ="priceSort",value = "价格排序（asc为升序,desc为降序默认为）",required = false,dataType = "String"),
            @ApiImplicitParam(name ="keyWord",value = "关键字模糊查询",required = false,dataType = "String")


    })
    @PostMapping("/queryByProduct")
    public R<IPage<Product>> queryByProduct(Page page,String categroryId,String timeSort,String priceSort,String keyWord){
        try {
            IPage<Product> productIPage = productService.queryByProduct(page,categroryId,timeSort,priceSort,keyWord);
//            List<Product> records = productIPage.getRecords();
//            if(records!=null){
//                for(int i=0;i<records.size();i++){
//                    records.get(i).setImageList(SysFileUtils.getSysFile(records.get(i).getImage()));
////                    List<ProductVariant> list = productVariantService.list(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getParentSku, records.get(i).getSku()));
////                    if(list!=null&&list.size()>0){
////                        for(int m=0;m<list.size();m++){
////                            list.get(m).setImageList(SysFileUtils.getSysFile(list.get(m).getImage()));
////                        }
////                        records.get(i).setVariantList(list);
////                    }
//
//                }
//            }
//            productIPage.setRecords(records);
            return R.ok(productIPage);
        }catch (Exception e){
            e.printStackTrace();
            return R.failed(e.getMessage());
        }

    }


    /**
     * 根据商品ids查询商品的数据
     * @param
     * @return R<List<Product>>
     */
    @ApiOperation(value = "根据商品ids查询商品的数据", notes = "根据商品ids查询商品的数据")
    @SysLog("根据商品ids查询商品的数据" )
    @PostMapping("/queryByProductIds")
    public R<List<Product>> queryByProductIds(String ids){
        RjkjUser user = SecurityUtils.getUser();
        //初始化redis
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        HashOperations hashOperations = redisTemplate.opsForHash();

        List<Product> listProduct=null;
        try {
            if(StringUtils.isEmpty(ids)){
                throw new RuntimeException("商品ids不能为空");
            }
            //如果传过来的ids不为空时
            if(StringUtils.isNoneBlank(ids)){
                String[] idss = ids.split(",");
                List<String> list=new ArrayList<>();
                for(int i=0;i<idss.length;i++){
                    hashOperations.put(user.getId(),idss[i],idss[i]);
                    list.add(idss[i]);
                }
                listProduct = productService.list(Wrappers.<Product>query().lambda().in(Product::getId, list));
                for(int j=0;j<listProduct.size();j++){
                    Product productd = listProduct.get(j);
                    List<ProductVariant> listVariant = productVariantService.list(Wrappers.<ProductVariant>query().lambda().eq(ProductVariant::getParentSku, productd.getSku()));
                    listProduct.get(j).setVariantList(listVariant);

                    //判断该商品描述为空时
                    if(StringUtils.isEmpty(productd.getDescription())){

                        //设置从亚马逊库提取的商品数据
                        List<AmazonProductInfo> amazonProductInfos = amazonProductInfoService.queryInfoByCategoryId(productd.getProductClassificationId());
                        if(amazonProductInfos!=null&& amazonProductInfos.size()>0){
                            Random r=new Random();
                            int a=r.nextInt(amazonProductInfos.size())-1;
                            productd.setKeyWord(amazonProductInfos.get(a).getHighlights().replaceAll("\\|\\|",","));
                            productd.setProductHighlights(amazonProductInfos.get(a).getHighlights());
                            productd.setDescription(amazonProductInfos.get(a).getDec());
                        }else{
                            productd.setKeyWord("defaultKeyWord");
                            productd.setProductHighlights("defaultHighlights");
                            productd.setDescription("defaultDescription");
                        }

                    }



                    //拉取商品时将数据添加到商品上架件数表
                    List<ProductPulloutNumber> listNumber = productPulloutNumberService.list(Wrappers.<ProductPulloutNumber>query().lambda().eq(ProductPulloutNumber::getUserId, user.getId()).eq(ProductPulloutNumber::getProductId, productd.getId()));
                    if(listNumber.size()<1){//判断该用户如果拉取过该产品就不处理
                        ProductPulloutNumber bean=new ProductPulloutNumber();
                        bean.setId(IDUtils.getGUUID(""));
                        bean.setNumber(1);
                        bean.setSupplierName(productd.getSupplierName());
                        bean.setProductId(productd.getId());
                        bean.setUserId(SecurityUtils.getUser().getId());
                        productPulloutNumberService.save(bean);


                        //判断供应商的id不为空时执行该代码(用于存放供应商的商品提成信息)
                        if(StringUtils.isNotEmpty(productd.getUserId())){
                            SupplierRoyaltyAppove supplierRoyaltyAppoveBean=new SupplierRoyaltyAppove();
                            supplierRoyaltyAppoveBean.setId(IDUtils.getGUUID(""));
                            supplierRoyaltyAppoveBean.setUserName(user.getUsername());
                            supplierRoyaltyAppoveBean.setSupplierId(productd.getUserId());
                            supplierRoyaltyAppoveBean.setProductId(productd.getId());
                            supplierRoyaltyAppoveBean.setCreateTime(LocalDateTime.now());
                            supplierRoyaltyAppoveBean.setUserId(user.getId());
                            supplierRoyaltyAppoveBean.setProductName(productd.getProductTitle());
                            BigDecimal bigOne=new BigDecimal("1");
                            supplierRoyaltyAppoveBean.setPrice(bigOne);
                            supplierRoyaltyAppoveService.save(supplierRoyaltyAppoveBean);
                        }


                    }
                }
            }
        }catch (Exception e){
            return R.failed(e.getMessage());
        }
        return R.ok(listProduct);
    }


    /**
     * 添加商品数据接口(从平台拉取下来的数据解析)
     * @param
     * @return
     */
    @ApiOperation(value = "添加商品数据接口(从平台拉取下来的数据解析)", notes = "添加商品数据接口(从平台拉取下来的数据解析)")
    @SysLog("添加商品数据接口" )
    @PostMapping(value = "/insertIntoProduct")
    @Transactional(rollbackFor = Exception.class)
    public R insertIntoProduct(@RequestBody String tesss){

        try {

            Product pro=null;
            List<ProductVariant> listVariant=null;
            List<SysFile> listFile=null;

            JSONObject object3 = JSONObject.parseObject(tesss);
            listFile=new ArrayList<>();//图片信息

            //商品主图片id
            String parentImageId=IDUtils.getGUUID("");

            //主商品sku
            String parentSku="BBIBM-201910-"+IDUtils.getGUUID("").substring(0,25);
            String msg = object3.getString("msg");



            JSONArray array = JSONArray.parseArray(msg);
//            if(array.size()>1){

                pro=new Product();
               String mainId =IDUtils.getGUUID("");
                pro.setId(mainId);

                pro.setProductClassificationName("玩具/童车/益智/积木/模型");
                pro.setProductClassificationId("50008737");
                pro.setImage(parentImageId);
                pro.setAuditStatus("0");
                pro.setRackStatus(0);
                pro.setBrandName("无");
                pro.setManufacturerCode("109909");
                pro.setSupplierName("供应商");
                pro.setSupplierNumber("1999");
                pro.setStock(1000);
                pro.setSku(parentSku);
                pro.setIshot("1");
                pro.setOriginArea("中国");

                JSONObject object1 = JSONObject.parseObject(array.get(0).toString());
                String status = object1.getString("status");
                if("0".equals(status)){
                    return R.ok();
                }

                pro.setLength(Double.parseDouble(object1.getString("package_length")));
                pro.setWide(Double.parseDouble(object1.getString("package_width")));
                pro.setHeight(Double.parseDouble(object1.getString("package_height")));

            JSONObject warehouse_list = object1.getJSONObject("warehouse_list");
            Iterator<Map.Entry<String, Object>> iterator = warehouse_list.entrySet().iterator();
            JSONObject objectsss=new JSONObject();
            while (iterator.hasNext()){
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                objectsss= JSONObject.parseObject(next.getValue().toString());
            }
//            JSONObject object2 = JSONObject.parseObject(JSONObject.parseObject().getString("YB"));
                Double aa=Double.parseDouble(objectsss.getString("price"));
                BigDecimal big1=new BigDecimal(aa);
                BigDecimal big2=new BigDecimal(7*2);
                BigDecimal divideAll = big1.multiply(big2);
                divideAll.setScale(2,BigDecimal.ROUND_HALF_UP);

                pro.setCostUnitPrice(divideAll.doubleValue());
                pro.setWeight(Double.parseDouble(object1.getString("volume_weight")));

                String newString=object1.getString("purchase_title").replaceAll("&amp;","").replaceAll("quot;","");
                pro.setProductTitle(newString);
                pro.setProductHighlights("产品价格实惠质量好");
                pro.setUrl(objectsss.getString("url"));
                pro.setPlatName("环球华品");

//                Whitelist addTag = new Whitelist();
//                ProductTranscation pt=new ProductTranscation();
//                pt.setId(mainId);
//                pt.setDesc(object1.getString("goods_desc"));
//                List<ProductTranscation> listPt=new ArrayList<ProductTranscation>();
//                listPt.add(pt);

            System.out.println(object1.getString("goods_desc"));
            String goods_desc1 = HtmlRepliceUtils.parseDivHtmlInfo(object1.getString("goods_desc"));
            String s2 = HtmlRepliceUtils.parseLinkHtmlInfo(goods_desc1);
            String s3 = HtmlRepliceUtils.parseImgHtmlInfo(s2);
            String s = s3.replaceAll("<strong>", "<b>");
            String s1 = s.replaceAll("</strong>", "</b>");
            System.out.println(s1);
            String s4 = s1.replaceAll("\\n", "<br/>");
            String s5 = s4.replaceAll("\\r", "");
            String s6 = s5.replaceAll("<br />", "");
            String s7 = s6.replaceAll("< BR />", "");
            String s8 = s7.replaceAll("</ A>", "").replaceAll("< BR>", "").replaceAll("< p>", "")
                    .replaceAll("< / p>", "").replaceAll("< / b>", "").replaceAll("< B>", "");


            R info = remoteTransactionFeignService.transcationProudcts(s8);

//                String goods_desc = Jsoup.clean(, addTag);
            String s9 = info.getData().toString();
            String substring = null;
            //找第一个汉字
            for (int index = 0; index <= s9.length() - 1; index++) {
                //将字符串拆开成单个的字符
                String w = s9.substring(index, index + 1);
                if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {// \u4e00-\u9fa5 中文汉字的范围
                    substring = s9.substring(index, s9.length());
                    break;
                }
            }
            pro.setDescription(substring);
            pro.setCreateTime(LocalDateTime.now());



                //主图片开始
                JSONArray img = object1.getJSONArray("original_img");
                int a=img.size();
                if(a>9){
                    for(int k=0;k<9;k++){
                        SysFile fi=new SysFile();
                        fi.setId(parentImageId);
                        fi.setPath(img.get(k).toString());
                        fi.setCreateTime(LocalDateTime.now());
                        listFile.add(fi);
                    }
                }else{
                    for(int k=0;k<img.size();k++){
                        SysFile fi=new SysFile();
                        fi.setId(parentImageId);
                        fi.setPath(img.get(k).toString());
                        fi.setCreateTime(LocalDateTime.now());
                        listFile.add(fi);
                    }
                }



                listVariant=new ArrayList<>();
                for(int i=0;i<array.size();i++){
                    JSONObject object = JSONObject.parseObject(array.get(i).toString());
                    if("0".equals(object.getString("status"))){
                        continue;
                    }
                    JSONObject objectTwo = JSONObject.parseObject(JSONObject.parseObject(object.getString("warehouse_list")).getString("YB"));

                    //商品子图片id
                    String childImageId=IDUtils.getGUUID("");
                    ProductVariant variant=new ProductVariant();
                    variant.setId(IDUtils.getGUUID(""));

                    JSONObject warehouse_list1 = object.getJSONObject("warehouse_list");
                    Iterator<Map.Entry<String, Object>> iterator1 = warehouse_list1.entrySet().iterator();
                    JSONObject objectsss1=new JSONObject();
                    while (iterator1.hasNext()){
                        Map.Entry<String, Object> next = iterator1.next();
                        String key = next.getKey();
                        objectsss1= JSONObject.parseObject(next.getValue().toString());
                    }



                    Double aa1=Double.parseDouble(objectsss1.getString("price"));
                    BigDecimal big11=new BigDecimal(aa1);
                    BigDecimal big21=new BigDecimal(7*2);
                    BigDecimal divideAll1 = big11.multiply(big21);
                    divideAll1.setScale(2,BigDecimal.ROUND_HALF_UP);
                    variant.setPrice(divideAll1.doubleValue());
                    variant.setVariantSize(object.getString("size"));
                    variant.setVariantColor(object.getString("color"));
                    variant.setStock(1000);
                    variant.setSku("BBIBM-201910-"+IDUtils.getGUUID("").substring(0,25));
                    variant.setImage(childImageId);
                    variant.setParentSku(parentSku);
                    variant.setCreateTime(LocalDateTime.now());

                    listVariant.add(variant);//变体信息设置完成


                    //变体图片开始
                    JSONArray imgs = JSONObject.parseObject(array.get(i).toString()).getJSONArray("original_img");
                    int b=imgs.size();
                    if(b>9){
                        for(int m=0;m<9;m++){
                            SysFile fis=new SysFile();
                            fis.setId(childImageId);
                            fis.setPath(imgs.get(m).toString());
                            fis.setCreateTime(LocalDateTime.now());
                            listFile.add(fis);
                        }
                    }else{
                        for(int m=0;m<imgs.size();m++){
                            SysFile fis=new SysFile();
                            fis.setId(childImageId);
                            fis.setPath(imgs.get(m).toString());
                            fis.setCreateTime(LocalDateTime.now());
                            listFile.add(fis);
                        }
                    }


                }

                productService.save(pro);
                productVariantService.saveBatch(listVariant);
              restTemplate.postForObject("http://rj-admin/sys-file/addSysFiles",listFile,R.class);
//                remoteFileService.addSysFiles(listFile,SecurityConstants.FROM_IN);

//            }


        }catch (Exception e){
            e.printStackTrace();
            return R.failed(e.getMessage());
        }
        return R.ok();

    }


        /**
         * 根据商品ids删除redis记录
         * @param
         * @return
         */
        @ApiOperation(value = "根据商品ids删除redis记录", notes = "根据商品ids删除redis记录")
        @SysLog("根据商品ids删除redis记录" )
        @PostMapping(value = "/deleteRedisGoodsId")
        @Transactional(rollbackFor = Exception.class)
       public R  deleteRedisGoodsId(@RequestBody List<String> ids){
         try {
             RjkjUser user = SecurityUtils.getUser();
             //初始化redis
             redisTemplate.setKeySerializer(new StringRedisSerializer());
             HashOperations hashOperations = redisTemplate.opsForHash();
             for(int i=0;i<ids.size();i++){
                 hashOperations.delete(user.getId(),ids.get(i));
             }
             return R.ok(true,"删除成功");
         }catch (Exception  e){
             return R.failed(e.getMessage());
         }
       }



//    /**
//     * 遍历图片(将图片放到产品数据上)
//     * @param
//     * @return
//     */
//    @ApiOperation(value = "遍历图片", notes = "遍历图片")
//    @SysLog("遍历图片" )
//    @PostMapping(value = "/tranImages")
//    @Transactional(rollbackFor = Exception.class)
//   public R tranImages(){
//
//        List<Product> list = productService.list();
//                list.forEach(iteam->{
//                    List<SysFile> sysFile = SysFileUtils.getSysFile(iteam.getImage());
//                    if (sysFile.size()>0) {
//                        iteam.setProductImages(JSONObject.toJSONString(sysFile));
//                        productService.updateById(iteam);
//                    }
//        });
//        return R.ok();
//    }



//    @ApiOperation(value = "deletedeleteGoodsId", notes = "deletedeleteGoodsId")
//    @SysLog("deletedeleteGoodsId" )
//    @PostMapping(value = "/deletedeleteGoodsId")
//    public R  deletedeleteGoodsId(String id){
//        try {
//            SysFileUtils.deleteFile(id);
//            return R.ok();
//        }catch (Exception  e){
//            return R.failed(e.getMessage());
//        }
//    }

    /**
     * 根据sku获取商品信息
     * @param
     * @return
     */
    @ApiOperation(value = "根据sku获取商品信息", notes = "根据sku获取商品信息")
    @SysLog("根据sku获取商品信息" )
    @PostMapping(value = "/getProductBySku")
    public R  getProductBySku(String sku){
        try {
            return R.ok(productService.getOne(Wrappers.<Product>query().lambda().eq(Product::getSku, sku)));
        }catch (Exception  e){
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据用户ID获取供应商上传的商品
     * @param
     * @return
     */
    @ApiOperation(value = "获取供应商上传的已通过商品", notes = "获取供应商上传的已通过商品")
    @SysLog("获取供应商上传的已通过商品" )
    @PostMapping(value = "/getSupplierProduct")
    public R  getSupplierProduct(Page page){
        try {
            RjkjUser user = SecurityUtils.getUser();
            return R.ok(productService.page(page,Wrappers.<Product>query().lambda().eq(Product::getUserId,user.getId())
            .eq(Product::getAuditStatus,"0")));
        }catch (Exception  e){
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据变体SKU查询父商品信息
     * @param
     * @return
     */
    @ApiOperation(value = "根据变体SKU查询父商品信息", notes = "根据变体SKU查询父商品信息")
    @SysLog("根据变体SKU查询父商品信息" )
    @PostMapping(value = "/getProductByVsku")
    public R  getProductByVsku(String sku){
        try {
            return R.ok(productService.getProductByVsku(sku));
        }catch (Exception  e){
            return R.failed(e.getMessage());
        }
    }

    /**
     * 根据变体SKU查询变体商品价格
     * @param
     * @return
     */
    @ApiOperation(value = "根据变体SKU查询变体商品价格", notes = "根据变体SKU查询变体商品价格")
    @SysLog("根据变体SKU查询变体商品价格" )
    @PostMapping(value = "/getVProducPricetByVsku")
    public R getVProductByVsku(String sku){
        try {
            return R.ok(new BigDecimal(productVariantService.selectBySku(sku).getPrice()));
        }catch (Exception  e){
            return R.failed(e.getMessage());
        }
    }

    /**
     * 测试获取商品信息
     * @return R
     */
    @ApiOperation(value = "测试获取商品信息", notes = "测试获取商品信息")
    @SysLog("测试获取商品信息" )
    @PostMapping("/testRestGet")
    public R testRestGet(String id) {
        return R.ok(productService.getById(id));
    }

    /**
     * 测试获取商品信息
     * @return R
     */
    @ApiOperation(value = "测试分布式事务", notes = "测试分布式事务")
    @SysLog("测试分布式事务" )
    @PostMapping("/testTran")
    public R testTran() {
        productService.testTran();
        return R.ok();
    }

}
