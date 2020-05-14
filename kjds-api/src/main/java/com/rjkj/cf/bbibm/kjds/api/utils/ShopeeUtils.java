package com.rjkj.cf.bbibm.kjds.api.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.rjkj.cf.bbibm.kjds.api.utils.ShopeeConstant.*;

/**
 * @描述：Shopee商城工具类
 * @项目：跨境电商
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019-09-28 15:37:54
 **/
@Slf4j
public class ShopeeUtils {
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
    /**
     * 超时时间设置
     */
    private final static int READ_TIMEOUT = 200;
    private final static int CONNECT_TIMEOUT = 120;
    private final static int WRITE_TIMEOUT = 120;

    /**
     * 获取UrlConnection
     * @param requestUrl
     * @param requestBody
     * @return
     * @throws Exception
     */
    public static String getUrlConnection(String requestUrl, String requestBody) throws Exception {
        log.info("======OKHTTP新方式======");
        String authorization = getAuthorization(requestUrl, requestBody);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestBody);
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("Authorization", authorization)
                .post(body)
                .build();
        //读取超时
        ClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        //连接超时
        ClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        //写入超时
        ClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        okHttpClient = ClientBuilder.build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("错误" + response);
        }
    }

    /**
     * 根据HmacSHA256算法生成Authorization
     *
     * @param url
     * @param requestBody
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws java.security.InvalidKeyException
     */
    public static String getAuthorization(String url, String requestBody)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, java.security.InvalidKeyException {
        String baseStr = url + "|" + requestBody;
        Mac password = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(ShopeeConstant.PARTNER_KEY.getBytes("UTF-8"), "HmacSHA256");
        password.init(secretKey);
        return Hex.encodeHexString(password.doFinal(baseStr.getBytes("UTF-8")));
    }

    /**
     * 根据重定向连接和partnerKey生成授权链接
     *
     * @param redirectUrl
     * @param partnerKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     * @throws java.security.InvalidKeyException
     */
    public static String calToken(String redirectUrl, String partnerKey) {
        String baseStr = partnerKey + redirectUrl;
        return org.apache.commons.codec.digest.DigestUtils.sha256Hex(baseStr);
    }

    /**
     * 解析发送请求后Shopee的返回结果
     *
     * @return
     * @throws IOException
     */
    private static String isToJson(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    /**
     * 获取当前时间的时间戳
     *
     * @return
     */
    public static Long getTimestamp() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 读取json文件，返回json字符串
     *
     * @return
     */
    public String readJsonFile(String fileName) {
        try {
            String jarPath = this.getClass().getClassLoader().getResource("Shopee\\" + fileName).getPath();
            File jsonFile = new File(jarPath);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获得商店的物流信息
     *
     * @return
     * @throws Exception
     */
    public static JSONArray getLogistics(String shopId) {
        try {
            JSONObject jsonObject = new JSONObject();
            //开发者ID
            jsonObject.put("partner_id", PARTNER_ID);
            //目标商店ID
            jsonObject.put("shopid", Long.valueOf(shopId));
            //时间戳
            jsonObject.put("timestamp", ShopeeUtils.getTimestamp());
            //发送请求，返回执行的结果
            JSONObject jsonLogistics = JSONObject.parseObject(ShopeeUtils.getUrlConnection(GET_LOGISTICS_URL, jsonObject.toString()));
            JSONArray arrLogistics = jsonLogistics.getJSONArray("logistics");
            for (Object arrLogistic : arrLogistics) {
                JSONObject arrLogistic1 = (JSONObject) arrLogistic;
                Boolean enabled = arrLogistic1.getBoolean("enabled");
                if (enabled) {
                    JSONObject jsonObject2 = new JSONObject();
                    //拼接上传商品时需要的物流格式
                    jsonObject2.put("logistic_id", arrLogistic1.getInteger("logistic_id"));
                    jsonObject2.put("enabled", true);
                    //返回拼接好的物流信息
                    JSONArray objects = new JSONArray();
                    objects.add(jsonObject2);
                    return objects;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增双层变体
     *
     * @return
     * @throws Exception
     */
    public static String initTierVariation(Long itemId, String shopId, JSONObject json) {
        try {
            JSONObject jsonObject = new JSONObject();
            //商品ID
            jsonObject.put("item_id", itemId);
            //开发者ID
            jsonObject.put("partner_id", PARTNER_ID);
            //目标商店ID
            jsonObject.put("shopid", Long.valueOf(shopId));
            //时间戳
            jsonObject.put("timestamp", ShopeeUtils.getTimestamp());
            //拼接双层变体信息
            jsonObject.put("tier_variation", json.getJSONArray("tier_variation"));
            JSONArray variation = json.getJSONArray("variation");
            jsonObject.put("variation", variation);
            System.out.println("双变：" + jsonObject.toString());
            //发送请求，返回执行的结果
            String urlConnection = ShopeeUtils.getUrlConnection(INIT_TIERVARIATION_URL, jsonObject.toString());
            System.out.println(urlConnection);
            return urlConnection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获得类别下需要填写的属性,返回必填属性
     *
     * @return
     * @throws Exception
     */
    public static JSONArray getAttributes(String shopId, String categoryId,String brandName) {
        try {
            //获得URLConnection对象，并生成Authorization
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("partner_id", PARTNER_ID);
            jsonObject.put("language", "zh-Hant");
            jsonObject.put("shopid", Long.valueOf(shopId));
            jsonObject.put("category_id", Integer.valueOf(categoryId));
            jsonObject.put("timestamp", ShopeeUtils.getTimestamp());
            //发送请求，获得执行的结果，其中包含所有的必填非必填属性
            JSONArray jsonAttributes = JSONObject.parseObject(ShopeeUtils.getUrlConnection(GET_ATTRIBUTES_URL, jsonObject.toString())).getJSONArray("attributes");
            System.out.println(jsonAttributes.toJSONString());
            JSONArray requiredAttributes = new JSONArray();
            if (0 < jsonAttributes.size()) {
                ArrayList<String> arrAttributes = new ArrayList<>();
                for (int i = 0; i < jsonAttributes.size(); i++) {
                    JSONObject jsonObject3 = JSONObject.parseObject(jsonAttributes.get(i).toString());
                    //is_mandatory的属性为true则为必填属性，添加到arrlist
                    if("品牌".equals(jsonObject3.getString("attribute_name"))) {
                        arrAttributes.add("brand"+jsonObject3.getString("attribute_id"));
                    }else if ("true".equals(jsonObject3.getString("is_mandatory"))) {
                        arrAttributes.add(jsonObject3.getString("attribute_id"));
                    }

                }
                //把获取到的必填属性拼接value
                for (int i = 0; i < arrAttributes.size(); i++) {
                    JSONObject json = new JSONObject();
                    String id = arrAttributes.get(i);
                    if(!id.startsWith("brand")){
                        json.put("attributes_id", Integer.valueOf(id));
                        json.put("value", "default");
                    }else {
                        json.put("attributes_id", Integer.valueOf(id.substring(5,id.length())));
                        json.put("value", brandName);
                    }
                    requiredAttributes.add(json);
                }
            }
            return requiredAttributes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取整个商品的详情信息
     *
     * @return
     * @throws Exception
     */
    public static JSONObject getItemDetail(String shopId, String itemId) {
        try {
            //获得URLConnection对象，并生成Authorization
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("partner_id", PARTNER_ID);
            jsonObject.put("item_id", Long.valueOf(itemId));
            jsonObject.put("shopid", Long.valueOf(shopId));
            jsonObject.put("timestamp", ShopeeUtils.getTimestamp());
            //发送请求，返回执行的结果
            String result = ShopeeUtils.getUrlConnection(GET_ITEMDETAIL_URL, jsonObject.toString());
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 修改没有变体的商品的价格
     *
     * @return
     * @throws Exception
     */
    public static String updateItemPrice(String shopId, String itemId, String price) {
        try {
            //获得URLConnection对象，并生成Authorization
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("partner_id", PARTNER_ID);
            jsonObject.put("item_id", Long.valueOf(itemId));
            jsonObject.put("shopid", Long.valueOf(shopId));
            jsonObject.put("price", Double.valueOf(price));
            jsonObject.put("timestamp", ShopeeUtils.getTimestamp());
            //发送请求，返回执行的结果
            return ShopeeUtils.getUrlConnection(UPDATE_PRICE_URL, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 批量修改商品变体价格
     *
     * @return
     * @throws Exception
     */
    public static String updateVariationPriceBatch(String shopId, JSONArray variantsArr) {
        try {
            //获得URLConnection对象，并生成Authorization
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("partner_id", PARTNER_ID);
            jsonObject.put("variations", variantsArr);
            jsonObject.put("shopid", Long.valueOf(shopId));
            jsonObject.put("timestamp", ShopeeUtils.getTimestamp());
            //发送请求，返回执行的结果
            return ShopeeUtils.getUrlConnection(UPDATE_VARIATION_PRICE_BATCH, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取发货的面单地址，直接下载。
     *
     * @return
     * @throws Exception
     */
    public static String getForderWaybill(String shopId, String orderId) {
        try {
            JSONObject orderJson = new JSONObject();
            orderJson.put("ordersn", orderId);
            JSONArray orderArr = new JSONArray();
            orderArr.add(orderJson);
            //获得URLConnection对象，并生成Authorization
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("partner_id", PARTNER_ID);
            jsonObject.put("shopid", Long.valueOf(shopId));
            jsonObject.put("orders_list", orderArr);
            jsonObject.put("timestamp", ShopeeUtils.getTimestamp());
            //发送请求，返回执行的结果
            return ShopeeUtils.getUrlConnection(GET_FORDER_WAYBILL, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据商品ID进行下架
     *
     * @return
     * @throws Exception
     */
    public static String endItem(String shopId, String itemId) {
        try {
            //获得URLConnection对象，并生成Authorization
            JSONObject requestJson = new JSONObject();
            //获得URLConnection对象，并生成Authorization
            requestJson.put("partner_id", PARTNER_ID);
            requestJson.put("item_id", Long.valueOf(itemId));
            requestJson.put("shopid", Long.valueOf(shopId));
            requestJson.put("timestamp", ShopeeUtils.getTimestamp());
            //发送请求，返回执行的结果
            return ShopeeUtils.getUrlConnection(DELETE_ITEM_URL, requestJson.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
