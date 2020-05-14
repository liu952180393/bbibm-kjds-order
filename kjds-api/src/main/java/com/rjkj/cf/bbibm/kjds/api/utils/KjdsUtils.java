package com.rjkj.cf.bbibm.kjds.api.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.rjkj.cf.admin.api.entity.UserInfoRsp;
import com.rjkj.cf.bbibm.kjds.api.entity.*;
import com.rjkj.cf.common.core.util.R;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
public class KjdsUtils {
    /**
     * 商品数据转为JSON数据的商品模板
     *
     * @param product
     * @return
     */
    public static JSONObject classToJson(@RequestBody GoodsProduct product) {
        JSONObject baseJson = new JSONObject();
        baseJson.put("name", product.getProductTitle());
        baseJson.put("description", product.getDescription());
        baseJson.put("category_id", product.getProductClassificationId());
        baseJson.put("item_sku", product.getSku());
//        baseJson.put("price",new BigDecimal(product.getCostUnitPrice()).divide(raetByArea, 1, RoundingMode.HALF_UP));
        baseJson.put("price", product.getCostUnitPrice());
        baseJson.put("stock", product.getStock());

        //商品图片
        List<String> imageList = product.getImageList();
        JSONArray imageListArr = new JSONArray();
        imageListArr.addAll(imageList);
        baseJson.put("images_url", imageListArr);

        List<ProductVariant> productVariants = product.getProductVariants();
        JSONArray variationsArr = new JSONArray();
        TreeSet<String> variantColorSet = new TreeSet<>();
        TreeSet<String> variantSizeSet = new TreeSet<>();
        String color;
        String size;
        List<String> imageList1 = null;
        if (productVariants != null) {
            for (ProductVariant productVariant : productVariants) {
                color = productVariant.getVariantColor();
                size = productVariant.getVariantSize();
                if (StringUtils.isNotEmpty(color)) {
                    variantColorSet.add(color);
                }
                if (StringUtils.isNotEmpty(size)) {
                    variantSizeSet.add(size);
                }
                imageList1 = productVariant.getImageList();
            }

            ArrayList<String> variantColorList = new ArrayList<>(variantColorSet);
            ArrayList<String> variantSizeList = new ArrayList<>(variantSizeSet);

            JSONArray variationArr = new JSONArray();
            for (ProductVariant productVariant : productVariants) {
                String variantColor = productVariant.getVariantColor();
                String variantSize = productVariant.getVariantSize();
                List<String> variantImageList = productVariant.getImageList();
                JSONArray variantImagesUrl = new JSONArray();
                variantImagesUrl.addAll(variantImageList);

                //双变体
                if (variantColorList.size() > 0 && variantSizeList.size() > 0) {
                    JSONArray tierIndex = new JSONArray();
                    //拼接下标数据
                    tierIndex.add(variantColorList.indexOf(productVariant.getVariantColor()));
                    tierIndex.add(variantSizeList.indexOf(productVariant.getVariantSize()));
                    JSONObject variationJson = new JSONObject();
                    variationJson.put("tier_index", tierIndex);
                    variationJson.put("stock", productVariant.getStock());
                    variationJson.put("variation_sku", productVariant.getSku());
//                    variationJson.put("images_url", variantImagesUrl);
//                    variationJson.put("price", new BigDecimal(productVariant.getPrice()).divide(raetByArea, 1, RoundingMode.HALF_UP));
                    variationJson.put("price", productVariant.getPrice());
                    variationArr.add(variationJson);

                } else if (StringUtils.isBlank(variantColor) || StringUtils.isBlank(variantSize)) {
                    JSONObject jsonObject = new JSONObject();
                    if (StringUtils.isBlank(variantColor)) {
                        jsonObject.put("type", "Size");
                        jsonObject.put("name", variantSize);
                    } else {
                        jsonObject.put("type", "Color");
                        jsonObject.put("name", variantColor);
                    }
//                    jsonObject.put("price",new BigDecimal(productVariant.getPrice()).divide(raetByArea, 1, RoundingMode.HALF_UP));
                    jsonObject.put("price", productVariant.getPrice());
                    jsonObject.put("stock", productVariant.getStock());
                    jsonObject.put("variation_sku", productVariant.getSku());
//                    jsonObject.put("images_url", variantImagesUrl);
                    variationsArr.add(jsonObject);
                    //单变体
                    baseJson.put("variations", variationsArr);
                }
                baseJson.put("variation", variationArr);
            }

            //tier_variation
            if (variationArr.size() > 0) {
                JSONArray tierVariation = new JSONArray();
                JSONArray variantImageListArr = new JSONArray();
                variantImageListArr.addAll(imageList1);

                JSONObject variantColorJson = new JSONObject();
                //双层变体图片
//            variantColorJson.put("images_url", variantImageListArr);
                variantColorJson.put("name", "Color");
                JSONArray colorArr = new JSONArray();
                colorArr.addAll(variantColorList);
                variantColorJson.put("options", colorArr);

                JSONObject variantSizeJson = new JSONObject();
                variantSizeJson.put("name", "Size");
                JSONArray sizeArr = new JSONArray();
                sizeArr.addAll(variantSizeList);
                variantSizeJson.put("options", sizeArr);
                tierVariation.add(variantColorJson);
                tierVariation.add(variantSizeJson);
                baseJson.put("tier_variation", tierVariation);
            }
        }

        return baseJson;
    }

    /**
     * 商品上传状态回调
     *
     * @param pid
     * @param gid
     * @param errorMsg
     * @param itemId
     * @param rackStatus
     */
    public static void callback(String pid, String gid, String errorMsg, String itemId, int rackStatus,String uid) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
//        OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) aToken.getDetails();
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
        CallBackVo callBackVo = new CallBackVo();
        callBackVo.setPid(pid);
        callBackVo.setUid(uid);
        callBackVo.setGid(gid);
        callBackVo.setErrorMsg(errorMsg);
        callBackVo.setItemId(itemId);
        callBackVo.setRackStatus(rackStatus);
//        HttpEntity request = new HttpEntity(callBackVo, headers);
        HttpEntity request = new HttpEntity(callBackVo);
        restTemplate.postForObject("http://kjds-goods/goodsproduct/upload/callback", request, R.class);
    }


    /**
     * 商品修改价格状态回调
     *
     * @param aToken
     * @param pid
     * @param gid
     */
    public static void callPriceBack(Authentication aToken, String pid, String gid, BigDecimal price) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) aToken.getDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
        CallPriceVo callPriceVo = new CallPriceVo();
        callPriceVo.setPid(pid);
        callPriceVo.setGid(gid);
        callPriceVo.setPrice(price);
        HttpEntity request = new HttpEntity(callPriceVo, headers);
        restTemplate.postForObject("http://kjds-goods/goodsproduct/updatePriceInfo", request, R.class);
    }
    /**
     * 根据区域查询运费模板信息
     */
    public static PricingRules queryPriceInfoOne(String area) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        HttpEntity request = new HttpEntity(area);
        return restTemplate.postForObject("http://kjds-product/pricingrules/queryPriceInfoOne?area="+area, request, PricingRules.class);
    }
    /**
     * 根据区域查询运费模板信息
     */
    public static UserInfoRsp getUserInfoById(String uid) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        HttpEntity request = new HttpEntity(uid);
        return restTemplate.postForObject("http://rj-admin/user/get/my/infobyid?uid="+uid, request, UserInfoRsp.class);
    }
    /**
     * 通过区域查询汇率信息
     */
    public static BigDecimal queryByRateOne(String area) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        HttpEntity request = new HttpEntity(area);
        return restTemplate.postForObject("http://kjds-product/rate/queryByRateOne?area="+area, request, BigDecimal.class);
    }
    /**
     * 根据传进来的数据翻译信息
     * @return
     */
    public static R<String> transcationSomeInfo(String message,String from,String to) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        HttpEntity request = new HttpEntity(message);
        return restTemplate.postForObject("http://kjds-transcation/transaction/transcationSomeInfo?message="+message+"&from="+from+"&to="+to, request, R.class);
    }
    /**
     * 根据id获取店铺信息
     * @return
     */
    public static R<LinkedHashMap> getGoodsById(String id) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        HttpEntity request = new HttpEntity(id);
        return restTemplate.postForObject("http://kjds-goods/goods/get/"+id, request, R.class);
    }
    /**
     * 获取产品根据id
     * @return
     */
    public static R<LinkedHashMap> getGoodsProductById(String pid,String gid,String uid) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        HttpEntity request = new HttpEntity(pid);
        return restTemplate.postForObject("http://kjds-goods/goodsproduct/get/product/one?pid="+pid+"&gid="+gid+"&uid="+uid, request, R.class);
    }

    /**
     * 获取字典内容
     *
     * @param aToken
     */
    public static R getDictValueByType(Authentication aToken, String type) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) aToken.getDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
        HttpEntity request = new HttpEntity(type, headers);
        return restTemplate.postForObject("http://rj-admin/dict/getDictValueByType?type=" + type, request, R.class);
    }

    /**
     * 商品翻译
     *
     * @param aToken
     */
    public static Product translationProudcts(Authentication aToken, Product product, String field, String area) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) aToken.getDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
        HttpEntity request = new HttpEntity(product, headers);
        return restTemplate.postForObject("http://kjds-transcation/transaction/translationProudcts?fields=" + field + "&to=" + area, request, Product.class);
    }

    /**
     * 文本翻译
     *
     * @param aToken
     */
    public static R transcationSomeInfo(Authentication aToken, String message, String from, String to) {
        RestTemplate restTemplate = SysFileUtils.getRestTemplate();
        OAuth2AuthenticationDetails token1 = (OAuth2AuthenticationDetails) aToken.getDetails();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token1.getTokenType() + " " + token1.getTokenValue());
        HttpEntity request = new HttpEntity(null, headers);
        return restTemplate.postForObject("http://kjds-transcation/transaction/transcationSomeInfo?message=" + message + "&from=" + from + "&to=" + to, request, R.class);
    }


    /**
     * 根据地区返回国家货币
     *
     * @param marktPlaceId
     */
    public static String translateCurrency(String marktPlaceId) {

        String currencyType = "";

        if (StringUtils.equals("MX", marktPlaceId)) {
            currencyType = "MXN";

        } else if (StringUtils.equals("US", marktPlaceId)) {
            currencyType = "USD";

        } else if (StringUtils.equals("GB", marktPlaceId)) {
            currencyType = "GBP";

        } else if (StringUtils.equals("CA", marktPlaceId)) {
            currencyType = "CAD";

        } else if (StringUtils.equals("FR", marktPlaceId)) {
            currencyType = "FRF";

        } else if (StringUtils.equals("ES", marktPlaceId)) {
            currencyType = "ESP";

        } else if (StringUtils.equals("TW", marktPlaceId)) {
            currencyType = "TWD";

        } else if (StringUtils.equals("IT", marktPlaceId)) {
            currencyType = "ITL";

        } else if (StringUtils.equals("AU", marktPlaceId)) {
            currencyType = "AUD";

        } else if (StringUtils.equals("JP", marktPlaceId)) {
            currencyType = "JPY";

        } else if (StringUtils.equals("SG", marktPlaceId)) {
            currencyType = "SGD";

        } else if (StringUtils.equals("DE", marktPlaceId)) {
            currencyType = "SGD";

        }

        return currencyType;
    }


    /**
     * 根据区域获取亚马逊开发者id和秘钥
     *
     * @return
     */
    public static String getSecretyKeyByArea(String area) {

        String info = "";

        if (StringUtils.equals("MX", area)) {
            info = "AKIAI4SKJMBRKZLSLLTQ_ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34";

        } else if (StringUtils.equals("US", area)) {
            info = "AKIAI4SKJMBRKZLSLLTQ_ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34";

        } else if (StringUtils.equals("GB", area)) {
            info = "AKIAJZFGDAZ3SRDCCHOA_D5DCCkRLGZLuXMBo7B5v0yVuXnjMUlnggtRdYSYE";

        } else if (StringUtils.equals("CA", area)) {
            info = "AKIAI4SKJMBRKZLSLLTQ_ACwKeR01w8oANxjG9cOqCkk4VXhMMiFeJmOPYu34";

        } else if (StringUtils.equals("FR", area)) {
            info = "AKIAJZFGDAZ3SRDCCHOA_D5DCCkRLGZLuXMBo7B5v0yVuXnjMUlnggtRdYSYE";

        } else if (StringUtils.equals("ES", area)) {
            info = "AKIAJZFGDAZ3SRDCCHOA_D5DCCkRLGZLuXMBo7B5v0yVuXnjMUlnggtRdYSYE";

        } else if (StringUtils.equals("IT", area)) {
            info = "AKIAJZFGDAZ3SRDCCHOA_D5DCCkRLGZLuXMBo7B5v0yVuXnjMUlnggtRdYSYE";

        } else if (StringUtils.equals("JP", area)) {
            info = "AKIAJAPAADHXZ2JAPXEQ_MT/GDmMYOkaREZs7Pxm4QroQg3pCRqjMmJqsYZ48";

        } else if (StringUtils.equals("DE", area)) {
            info = "AKIAJZFGDAZ3SRDCCHOA_D5DCCkRLGZLuXMBo7B5v0yVuXnjMUlnggtRdYSYE";

        }

        return info;
    }

    /**
     * 判断字符串是否有中文
     *
     * @param str
     * @return
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 获取商品SKU,16位
     *
     * @param
     * @return
     */
    public static String getProductSku() {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = String.valueOf(System.currentTimeMillis());
        return uuid.substring(uuid.length() - 10) + time.substring(time.length() - 6);
    }

    /**
     * 根据BaseSKU获取拉取的Goods表的SKU
     *
     * @param baseSku
     * @return
     */
    public static String getGoodsSkuByBaseSku(String baseSku) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(baseSku);
        stringBuilder.append("#");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String time = String.valueOf(System.currentTimeMillis());
        String productSku = uuid.substring(uuid.length() - 5) + time.substring(time.length() - 3);
        stringBuilder.append(productSku);
        return stringBuilder.toString();
    }

    /**
     * 根据店铺信息获取上传时的SKU
     *
     * @param goodsSku
     * @param area
     * @param shopId
     * @param createTime
     * @return
     */
    public static String getUploadSkuByGoodsSku(String goodsSku, String area, String shopId, LocalDateTime createTime) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(goodsSku).append("##").append(area);
        if (StringUtils.isBlank(shopId)) {
            throw new RuntimeException("生成上传SKU失败：shopId为空");
        }
        stringBuilder.append(shopId.substring(2, 5));
        String time = String.valueOf(createTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        String productSku = time.substring(time.length() - 6);
        stringBuilder.append(productSku);
        return stringBuilder.toString();
    }

    /**
     * 将字符文本首字母大写，排除某些介词冠词
     */
    public static String capitalizationOfWordsText(String wordsText) {
        String excludeWords[] = {"at", "on", "behind", "during", "from", "into", "between","before","onto", "inside", "outside", "without", "throughout"
                , "and", "but", "or", "so", "however", "although", "even", "though", "even", "if", "such", "as", "in","a", "an", "the","for","to","about","through"
        };
        String[] strs = wordsText.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String strTmp : strs) {
            if (!determineIfItExists(excludeWords, strTmp)) {
                char[] ch = strTmp.toCharArray();
                if (ch[0] >= 'a' && ch[0] <= 'z') {
                    ch[0] = (char) (ch[0] - 32);
                }
                String strT = new String(ch);
                sb.append(strT).append(" ");
            }else {
                sb.append(strTmp).append(" ");
            }
        }
        return sb.toString().trim();
    }
    /**
     * 判断数组中是否存在指定元素
     */
    public static boolean determineIfItExists(String[] arr, String targetValue) {
        for (String s : arr) {
            if (s.contains(targetValue)) {
                return true;
            }
        }
        return false;
    }
}
