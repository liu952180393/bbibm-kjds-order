package com.rjkj.cf.bbibm.kjds.api.utils;

import cn.hutool.json.XML;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EbayUtils {
    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static OkHttpClient.Builder ClientBuilder = new OkHttpClient.Builder();
    private final static int READ_TIMEOUT = 200;
    private final static int CONNECT_TIMEOUT = 120;
    private final static int WRITE_TIMEOUT = 120;

    /**
     * 本地时间转化为UTC时间
     */
    public static String localTimeToUTC(String dateTime) {
        if (dateTime.length() < 11) {
            dateTime = dateTime + " 00:00:00";
        }
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
            long timeInMillis = calendar.getTimeInMillis();
            return new DateTime(timeInMillis, DateTimeZone.UTC).toString("yyyy-MM-dd'T'HH:mm:ss'Z'");
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("时间格式错误！格式应为：yyyy-MM-dd HH:mm:ss");
        }
    }

//    /**
//     * 创建URLConnection
//     *
//     * @return
//     * @throws IOException
//     */
//    public static URLConnection getUrlConnection(String callName) {
//        URLConnection conn = null;
//        try {
//            //发送xml请求
//            URL url = new URL(EbayConstant.EBAY_URL);
//            conn = url.openConnection();
//            conn.setUseCaches(false);
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setRequestProperty("X-EBAY-API-SITEID", "0");
//            conn.setRequestProperty("X-EBAY-API-COMPATIBILITY-LEVEL", "967");
//            conn.setRequestProperty("X-EBAY-API-CALL-NAME", callName);
//            conn.setConnectTimeout(10000);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return conn;
//    }


    /**
     * 发送请求，并返回执行结果
     *
     * @return
     * @throws IOException
     */
//    public static String sendRequest(URLConnection conn, String requestBody) {
//        InputStream is = null;
//        try {
//            OutputStream ops = conn.getOutputStream();
//            OutputStreamWriter osw = new OutputStreamWriter(ops, "utf-8");
//            //发送请求
//            osw.write(requestBody);
//            osw.flush();
//            is = conn.getInputStream();
//            ops.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return isToJson(is);
//    }

    /**
     * 发送请求，并返回执行结果，不转为JSON，原格式返回
     *
     * @return
     * @throws IOException
     */
//    public static String sendRequestNotJson(URLConnection conn, String requestBody) {
//        StringBuilder sb = null;
//        try {
//            OutputStream ops = conn.getOutputStream();
//            OutputStreamWriter osw = new OutputStreamWriter(ops, "utf-8");
//            //发送请求
//            osw.write(requestBody);
//            osw.flush();
//            InputStream is = conn.getInputStream();
//            ops.close();
//            sb = new StringBuilder();
//            String line;
//            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//            br.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return sb.toString();
//    }
    public static String sendRequestNotJson(String callName, String requestBody) {
        try {
            System.out.println("===========OKHTTP新方式=========");
            RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), requestBody);
            Request request = new Request.Builder()
                    .url(EbayConstant.EBAY_URL)
                    .addHeader("X-EBAY-API-SITEID", "0")
                    .addHeader("X-EBAY-API-COMPATIBILITY-LEVEL", "967")
                    .addHeader("X-EBAY-API-CALL-NAME", callName)
                    .post(body)
                    .build();
            ClientBuilder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);//读取超时
            ClientBuilder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);//连接超时
            ClientBuilder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);//写入超时
            okHttpClient = ClientBuilder.build();
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sendRequest(String callName, String requestBody) throws Exception {
        log.info("===========OKHTTP新方式=========");
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), requestBody);
        Request request = new Request.Builder()
                .url(EbayConstant.EBAY_URL)
                .addHeader("X-EBAY-API-SITEID", "0")
                .addHeader("X-EBAY-API-COMPATIBILITY-LEVEL", "967")
                .addHeader("X-EBAY-API-CALL-NAME", callName)
                .post(body)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
//        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        //读取超时
        ClientBuilder.readTimeout(200, TimeUnit.SECONDS);
        //连接超时
        ClientBuilder.connectTimeout(120, TimeUnit.SECONDS);
        //写入超时
        ClientBuilder.writeTimeout(120, TimeUnit.SECONDS);
        okHttpClient = ClientBuilder.build();
        Response response = okHttpClient.newCall(request).execute();
        cn.hutool.json.JSONObject jsonObject = XML.toJSONObject(response.body().string());
        return jsonObject.toString();
    }

    /**
     * InputString转为JSON字符串
     *
     * @return
     * @throws IOException
     */
    public static String isToJson(InputStream is) {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            cn.hutool.json.JSONObject jsonObject = XML.toJSONObject(sb.toString());
            return jsonObject.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * UTC时间转LocalDateTime
     *
     * @param UTCStr
     * @return
     */
    public static LocalDateTime utcToLocal(String UTCStr) {
        SimpleDateFormat sdf2 = null;
        Calendar calendar = null;
        LocalDateTime ldt = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(UTCStr);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + 8);
            ldt = LocalDateTime.parse(sdf2.format(calendar.getTime()), df);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ldt;
    }

    /**
     * 根据分类ID查询必填属性
     *
     * @return
     */
    public static ArrayList<String> getCategorySpecifics(String categoryId, String token) {
        try {
            String callName = "GetCategorySpecifics";
            //获取URLConnection
//            URLConnection conn = EbayUtils.getUrlConnection(callName);
            Document document = DocumentHelper.createDocument();
            // 创建根节点
            document.addElement("GetCategorySpecificsRequest", "urn:ebay:apis:eBLBaseComponents");
            // 通过document对象获取根元素的信息
            document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
            document.getRootElement().addElement("CategorySpecific", "urn:ebay:apis:eBLBaseComponents").addElement("CategoryID").setText(categoryId);
            String result = EbayUtils.sendRequest(callName, document.asXML());
            if (StringUtils.isNotEmpty(result)) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject getCategorySpecificsResponse = jsonObject.getJSONObject("GetCategorySpecificsResponse");
                if (getCategorySpecificsResponse == null) {
                    return null;
                }
                JSONArray jsonArray = jsonObject.getJSONObject("GetCategorySpecificsResponse").getJSONObject("Recommendations").getJSONArray("NameRecommendation");
                ArrayList<String> specifics = new ArrayList<>();
                //得到所有的必填属性
                if(jsonArray != null){
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject parse = JSONObject.parseObject(jsonArray.get(i).toString());
                    String usageConstraint = parse.getJSONObject("ValidationRules").getString("UsageConstraint");
                    if ("Required".equalsIgnoreCase(usageConstraint)) {
                        String name = parse.getString("Name");
                        if ("MPN".equals(name)) {
                            continue;
                        }
                        specifics.add(name);
                    }
                }
                }
                return specifics;
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检查账户状态，Token是否失效等问题。
     *
     * @return
     */
    public static Boolean checkToken(String token) {
        try {
            String callName = "GetUser";
            //获取URLConnection
//        URLConnection conn = EbayUtils.getUrlConnection(callName);
            Document document = DocumentHelper.createDocument();
            // 创建根节点
            document.addElement("GetUserRequest", "urn:ebay:apis:eBLBaseComponents");
            // 通过document对象获取根元素的信息,设置Token
            document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
            JSONObject resultObj = JSONObject.parseObject(EbayUtils.sendRequest(callName, document.asXML()));
            System.out.println(resultObj.toString());
            if ("Success".equals(resultObj.getJSONObject("GetUserResponse").getString("Ack"))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 根据商品ID获取商品信息
     *
     * @return
     */
    public static String getItem(String itemId, String token) {
        try {
            String callName = "GetItem";
            //获取URLConnection
//            URLConnection conn = EbayUtils.getUrlConnection(callName);
            Document document = DocumentHelper.createDocument();
            // 创建根节点
            document.addElement("GetItemRequest", "urn:ebay:apis:eBLBaseComponents");
            // 通过document对象获取根元素的信息
            document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
            document.getRootElement().addElement("ItemID").setText(itemId);
            return EbayUtils.sendRequestNotJson(callName, document.asXML());
        } catch (Exception e) {
            e.printStackTrace();
            return "系统异常！";
        }
    }

    /**
     * 根据订单号发货
     *
     * @param token
     * @param orderId
     * @param shipNumber      运单号
     * @param shippingCarrier 物流商
     * @return
     */
    public static JSONObject orderToship(String token, String orderId, String shipNumber, String shippingCarrier) {
        try {
            String callName = "CompleteSale";
            //获取URLConnection
//        URLConnection conn = EbayUtils.getUrlConnection(callName);
            Document document = DocumentHelper.createDocument();
            // 创建根节点
            document.addElement("CompleteSaleRequest", "urn:ebay:apis:eBLBaseComponents");
            // 通过document对象获取根元素的信息,设置Token
            Element rootElement = document.getRootElement();
            rootElement.addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
            rootElement.addElement("OrderID").setText(orderId);
            rootElement.addElement("Shipped").setText("true");
            Element element = rootElement.addElement("Shipment").addElement("ShipmentTrackingDetails");
            element.addElement("ShipmentTrackingNumber").setText(shipNumber);
            element.addElement("ShippingCarrierUsed").setText(shippingCarrier);
            return JSONObject.parseObject(EbayUtils.sendRequest(callName, document.asXML()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
