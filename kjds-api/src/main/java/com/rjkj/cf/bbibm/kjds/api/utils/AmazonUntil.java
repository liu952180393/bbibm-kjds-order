package com.rjkj.cf.bbibm.kjds.api.utils;


import com.rjkj.cf.bbibm.kjds.api.entity.AmazonGetOrderListVo;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @description: 亚马逊订单api请求工具类
 */
public class AmazonUntil {


    /***
     * 初始化订单查询数据(请求准备)
     * @param secretKey
     * @param parameters
     * @return
     * @throws Exception
     */
    public static JSONArray listOrderPost(String marekPlace, HashMap<String, String> parameters, String secretKey, AmazonGetOrderListVo amazonGetOrderListVo) throws Exception {


        String url="";
        if("ATVPDKIKX0DER".equals(marekPlace)){//美国
            url="https://mws.amazonservices.com/";

        }else if("A1AM78C64UM0Y8".equals(marekPlace)){//墨西哥
            url="https://mws.amazonservices.com.mx/";

        }else if("A2EUQ1WTGCTBG2".equals(marekPlace)){//加拿大
            url="https://mws.amazonservices.ca/";

        }else if("A1F83G8C2ARO7P".equals(marekPlace)){//英国
            url="https://mws-eu.amazonservices.com/";

        }else if("A2Q3Y263D00KWC".equals(marekPlace)){//巴西
            url="https://mws.amazonservices.com/";

        }else if("A2VIGQ35RCS4UG".equals(marekPlace)){//阿拉伯联合酋长国（UAE）地区
            url="https://mws.amazonservices.ae/";

        }else if("A1PA6795UKMFR9".equals(marekPlace)){//德国
            url="https://mws-eu.amazonservices.com/";

        }else if("A1RKKUPIHCS9HS".equals(marekPlace)){//西班牙
            url="https://mws-eu.amazonservices.com/";

        }else if("A13V1IB3VIYZZH".equals(marekPlace)){//法国
            url="https://mws-eu.amazonservices.com/";

        }else if("A21TJRUUN4KGV".equals(marekPlace)){//印度
            url="https://mws.amazonservices.in/";

        }else if("APJ6JRA9NG5V4".equals(marekPlace)){//意大利
            url="https://mws-eu.amazonservices.com/";

        }else if("A33AVAJ2PDY3EV".equals(marekPlace)){//土耳其
            url="https://mws-eu.amazonservices.com/";

        }else if("A19VAU5U5O7RUS".equals(marekPlace)){//新加坡
            url="https://mws-fe.amazonservices.com/";

        }else if("A39IBJ37TRP1C6".equals(marekPlace)){//澳大利亚
            url="https://mws.amazonservices.com.au/";

        }else if("A1VC38T7YXB528".equals(marekPlace)){//日本
            url="https://mws.amazonservices.jp/";

        }else{//默认为美国站的请求
            url="https://mws.amazonservices.com/";

        }

        parameters.put("Action", AmazonUntil.urlEncode(AmazonConstant.ORDER_ACTION));
        parameters.put("Version", AmazonUntil.urlEncode(AmazonConstant.ORDER_VERSION));
//        parameters.put("OrderStatus.Status.1", AmazonConstant.ORDER_SHIPPED);
        parameters.put("LastUpdatedAfter", AmazonUntil.urlEncode(EbayUtils.localTimeToUTC(amazonGetOrderListVo.getStartTime())));
        parameters.put("LastUpdatedBefore", AmazonUntil.urlEncode(EbayUtils.localTimeToUTC(amazonGetOrderListVo.getEndTime())));
//        parameters.put("FulfillmentChannel.Channel.1","MFN");

        String formattedParameters = calculateStringToSignV2ForOrder(parameters, url+"Orders/2013-09-01");
//        logger.info("签名内容：{}", formattedParameters);
        String signature = sign(formattedParameters, secretKey);
//        logger.info("签名signature: {}", signature);
        parameters.put("Signature", AmazonUntil.urlEncode(signature));
        String paramStr = sortParams(new StringBuilder(), parameters);
//        logger.info("排序后参数：{}", paramStr);
        return doListOrderPost(url+"Orders/2013-09-01", paramStr);

    }


    /**
     * signV2签名内容对于订单查询
     *
     * @param parameters
     * @param serviceUrl
     * @return
     * @throws SignatureException
     * @throws URISyntaxException
     */
    private static String calculateStringToSignV2ForOrder(Map<String, String> parameters, String serviceUrl)throws  URISyntaxException {
        URI endpoint = new URI(serviceUrl.toLowerCase());
        StringBuilder data = new StringBuilder();
        data.append("POST\n");
        data.append(endpoint.getHost());
        data.append("\n/Orders/2013-09-01");
        data.append("\n");
        return sortParams(data, parameters);

    }


    /**
     * signV2签名内容对于上传
     *
     * @param parameters
     * @param serviceUrl
     * @return
     * @throws SignatureException
     * @throws URISyntaxException
     */
    private static String calculateStringToSignV2ForFeed(Map<String, String> parameters, String serviceUrl)throws  URISyntaxException {
        URI endpoint = new URI(serviceUrl.toLowerCase());
        StringBuilder data = new StringBuilder();
        data.append("POST\n");
        data.append(endpoint.getHost());
        data.append("\n/");
        data.append("\n");
        return sortParams(data, parameters);

    }


    /**
     * signV2签名内容对于商品查询
     *
     * @param parameters
     * @param serviceUrl
     * @return
     * @throws SignatureException
     * @throws URISyntaxException
     */
    private static String calculateStringToSignV2ForIteam(Map<String, String> parameters, String serviceUrl)throws  URISyntaxException {
        URI endpoint = new URI(serviceUrl.toLowerCase());
        StringBuilder data = new StringBuilder();
        data.append("POST\n");
        data.append(endpoint.getHost());
        data.append("\n/Products/2011-10-01");
        data.append("\n");
        return sortParams(data, parameters);

    }

    /***
     * signV2签名方式
     * @param data
     * @param secretKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IllegalStateException
     * @throws UnsupportedEncodingException
     */
    private static String sign(String data, String secretKey)throws NoSuchAlgorithmException, InvalidKeyException,IllegalStateException, UnsupportedEncodingException {
        Mac mac = Mac.getInstance(AmazonConstant.ALGORITHM);
        mac.init(new SecretKeySpec(secretKey.getBytes(AmazonConstant.CHARACTER_ENCODING),
        		AmazonConstant.ALGORITHM));
        byte[] signature = mac.doFinal(data.getBytes(AmazonConstant.CHARACTER_ENCODING));
        String signatureBase64 = new String(Base64.encodeBase64(signature),
        		AmazonConstant.CHARACTER_ENCODING);
        return new String(signatureBase64);
    }

    /**
     * url非法字符转换
     *
     * @param rawValue
     * @return
     */
    public static String urlEncode(String rawValue) {
        String value = (rawValue == null) ? "" : rawValue;
        String encoded = null;

        try {
            encoded = URLEncoder.encode(value, AmazonConstant.CHARACTER_ENCODING)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unknown encoding: " + AmazonConstant.CHARACTER_ENCODING);
        }

        return encoded;
    }

    /***
     * 订单数据查询请求（执行请求方法）
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static JSONArray doListOrderPost(String url, String params) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url+"?"+params);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addHeader("User-Agent", "hyxun/amzn.mws.56af5b71-a0ef-1d49-e464-3909bad5ba91 (Language=Java/1.6.0.11; Platform=Windows/XP)");
        httpPost.addHeader("x-amazon-user-agent", "AmazonJavascriptScratchpad/1.0 (Language=Javascript)");
        httpPost.addHeader("X-Requested-With", "XMLHttpRequest");

//        String charSet = "UTF-8";
//        StringEntity entity = new StringEntity(params, charSet);
//        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            InputStream is = responseEntity.getContent();

//            SAXReader as=new SAXReader();
//            Document read = as.read(is);
//            String s = read.asXML();
//            System.out.println(s);


            return parseListOrderXML(is);
        } catch (Exception e) {
            throw e;
        }
//        finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    throw e;
//                }
//            }
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                throw e;
//            }
//        }
    }

    /**
     * 对传递参数转换
     *
     * @param data
     * @param parameters
     * @return
     */
    private static String sortParams(StringBuilder data, Map<String, String> parameters) {
        Map<String, String> sorted = new TreeMap<String, String>();
        sorted.putAll(parameters);

        Iterator<Entry<String, String>> pairs =
                sorted.entrySet().iterator();
        while (pairs.hasNext()) {
            Entry<String, String> pair = pairs.next();
            if (pair.getValue() != null) {
                data.append(pair.getKey() + "=" + pair.getValue());
            } else {
                data.append(pair.getKey() + "=");
            }
            if (pairs.hasNext()) {
                data.append("&");
            }
        }
        return data.toString();
    }

    /**
     * 解析查询订单获取的数据（解析订单查询返回的io数据）
     * @param is
     * @return
     */
    public static JSONArray parseListOrderXML(InputStream is) {

        JSON xml = new XMLSerializer().readFromStream(is);
        JSONObject jsonObject = JSONObject.fromObject(xml);
        System.out.println(jsonObject.toString());
        JSONArray orders = new JSONArray();
//        null != jsonObject.get("ListOrdersResult")
        if (!jsonObject.containsKey("Error")) {
            JSONObject listOrdersResult = (JSONObject) jsonObject.get("ListOrdersResult");
            if (listOrdersResult != null) {
                //获取到所有的订单内容 list
                JSONObject orderLists =null;
                try {
                    orderLists=(JSONObject) listOrdersResult.get("Orders");
                }catch (Exception e){

                }
                if (orderLists != null) {

                    String order = orderLists.getString("Order");
                    boolean b = order.startsWith("[");
                    if(b){
                        orders = (JSONArray) orderLists.get("Order");
                    }else {
                        orders.add((JSONObject) orderLists.get("Order"));
                    }
                    System.out.println(order);
                }
            }
        }else{
            //判断亚马逊账号信息是否错误
            JSONObject error = (JSONObject) jsonObject.get("Error");
            JSONObject errorCode=new JSONObject();
            errorCode.put("errortyped","true");
            errorCode.put("code",error.getString("Code"));
            errorCode.put("message",error.getString("Message"));
            orders.add(errorCode);
        }
        return orders;

    }


    /**
     * 根据xml文件流计算md5值
     * @param fis
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String computeContentMD5HeaderValue(FileInputStream fis)throws IOException, NoSuchAlgorithmException {

    DigestInputStream dis = new DigestInputStream(fis,MessageDigest.getInstance("MD5"));

    byte[] buffer = new byte[8192];
    while( dis.read( buffer ) > 0 );

    String md5Content = new String(Base64.encodeBase64(dis.getMessageDigest().digest()) );

    fis.getChannel().position( 0 );

    return md5Content;
    }

//    /**
//     * 初始化商品上传需要的参数
//     * @param url
//     * @param parameters
//     * @param secretKey
//     * @throws Exception
//     */
//    public static JSONArray addPostOne(String url,Map<String,String> parameters,String secretKey) throws Exception {
//
//        String str = DateTime.now(DateTimeZone.UTC).toString(TIME_FORMAT_STR);
//        parameters.put("Timestamp", AmazonUntil.urlEncode(str));
//        parameters.put("Action", AmazonUntil.urlEncode(SUBMIT_FEED_ACTION));
//        parameters.put("SignatureMethod", AmazonUntil.urlEncode(ALGORITHM));
//        parameters.put("Version", AmazonUntil.urlEncode(ADD_VERSION));
//        parameters.put("SignatureVersion", AmazonUntil.urlEncode(SIGNATURE_VERSION));
//
//
//        parameters.put("FeedType",AmazonUntil.urlEncode("_POST_INVENTORY_AVAILABILITY_DATA_"));
//        parameters.put("PurgeAndReplace",AmazonUntil.urlEncode("false"));
//
//
//        File file = new File("C:\\Users\\EDZ\\Desktop\\home  demo\\packge\\testQuantity.xml");
//        FileInputStream fis=new FileInputStream(file);
//        String content_md5 = computeContentMD5HeaderValue(fis);
//        parameters.put("ContentMD5Value", AmazonUntil.urlEncode(content_md5));
//
//
//
//
//
//
//        String formattedParameters = calculateStringToSignV2ForFeed(parameters, ADD_POST_URL);
//        logger.info("签名内容：{}"+formattedParameters);
//        String signature = sign(formattedParameters, secretKey);
////        logger.info("签名后的数据：{}", signature);
//        parameters.put("Signature", AmazonUntil.urlEncode(signature));
//        String paramStr = sortParams(new StringBuilder(), parameters);
////        logger.info("排序后参数：{}", paramStr);
//
////            int lengt=0;
////            byte[] bb=new byte[1024];
////            while ((lengt=fis.read(bb))!=-1){
////                System.err.println(new String(bb,0,lengt));
////            }
//
//        return addPostTwo(url,paramStr,fis,content_md5);
//
//    }

//    /***
//     * 商品上传post请求
//     * @param url
//     * @param params
//     * @return
//     * @throws Exception
//     */
//    public static JSONArray addPostTwo(String url, String params,FileInputStream fis,String content_md5) throws Exception {
//        CloseableHttpClient httpClient=HttpClients.createDefault();
//        HttpPost httpPost=new HttpPost(url+"?"+params);
//        httpPost.addHeader("Content-Type","text/xml");
////        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
//        httpPost.addHeader("Content-MD5",content_md5);
//        httpPost.addHeader("x-amazon-user-agent", "hyxun/amzn.mws.56af5b71-a0ef-1d49-e464-3909bad5ba91 (Language=Java/1.8.0_201/52.0/Oracle Corporation; Platform=Windows 7/amd64/6.1; MWSClientVersion=2016-09-21)");
////        httpPost.addHeader("X-Requested-With","XMLHttpRequest");
////        httpPost.addHeader("Host","mws.amazonservices.com");
//
////        StringEntity steingEnty=new StringEntity(XML,"utf-8");
////        httpPost.setEntity(steingEnty);
//
//
//        InputStreamEntity inputStreamEntity = new InputStreamEntity(fis,-1);
////        InputStream content = inputStreamEntity.getContent();
////         byte[] aa=new byte[1024];
////        int lengths=0;
////        while((lengths=content.read(aa))!=-1){
////            System.err.println(new String(aa,0,lengths));
////        }
//
//
//        httpPost.setEntity(inputStreamEntity);
//
//
////        File fs = new File("C:\\Users\\EDZ\\Desktop\\testaa.xml");
////        FileOutputStream fio=new FileOutputStream(fs);
////        byte[] aa=new byte[2024];
////        int length=0;
////        while((length=fis.read(aa))!=-1){
////            fio.write(aa,0,length);
////        }
//
//
////        String charSet = "UTF-8";
////        StringEntity entity = new StringEntity(params, charSet);
////        httpPost.setEntity(entity);
//
//
//        CloseableHttpResponse reponse=null;
//
//        try {
//            reponse = httpClient.execute(httpPost);
//            HttpEntity httpEntity = reponse.getEntity();
//            InputStream is = httpEntity.getContent();
//
//            int lengt=0;
//            byte[] bb=new byte[1024];
//            while ((lengt=is.read(bb))!=-1){
//                System.err.println(new String(bb,0,lengt));
//            }
//
//
//            return parseCommodityXml(is);
//        }catch (Exception e){
//            throw e;
//        }finally {
//            if(reponse!=null){
//                try {
//                    reponse.close();
////                    fio.close();
//                    fis.close();
//                }catch(IOException e){
//                    throw e;
//                }
//
//                try {
//                    httpClient.close();
//                }catch (IOException e){
//                    throw e;
//                }
//
//            }
//        }
//
//
//    }


    /**
     *解析商品上传返回SubmissionId查询Result信息xml
     * @param is
     * @return
     */
    private static JSONArray parseCommodityResultXml(InputStream is){
        JSON xml = new XMLSerializer().readFromStream(is);
        JSONObject obj= JSONObject.fromObject(xml);
        JSONArray arr=new JSONArray();
        if(null!=obj.get("Message")){
        	arr= JSONArray.fromObject(obj.get("Message"));
        }
        return arr;
    }


    /**
     *解析商品查询返回list信息xml
     * @param is
     * @return
     */
    private static JSONArray parseCommodityListtXml(InputStream is){
        JSON xml = new XMLSerializer().readFromStream(is);
        JSONObject obj= JSONObject.fromObject(xml);
        JSONObject justObj=new JSONObject();
        JSONArray arr=new JSONArray();
        if(null!=obj.get("GetFeedSubmissionListResult")){
            justObj= JSONObject.fromObject(obj.get("GetFeedSubmissionListResult"));
            if(justObj!=null) {
                String info=justObj.getString("FeedSubmissionInfo");
                //判断返回的是array还是object
                if(info.contains("[")){
                    arr=(JSONArray)justObj.get("FeedSubmissionInfo");
                }else{
                    JSONObject objs=(JSONObject)justObj.get("FeedSubmissionInfo");
                    arr.add(objs);
                }
            }
        }
        return arr;
    }


    /**
     * 根据上传数据提交的唯一编码查询信息
     * @param feedSubmissionId
     * @return
     */
    public static JSONArray searchByFeedSubmissionId(String feedSubmissionId,String sellerId,String mwsToken,String secretKEY,String awsAccessKeyId,String marketPlaceID) throws URISyntaxException, NoSuchAlgorithmException, InvalidKeyException, IOException {

            JSONArray jsonArray=null;
        try{
            String url="";
            if("ATVPDKIKX0DER".equals(marketPlaceID)){//美国
                url="https://mws.amazonservices.com/";

            }else if("A1AM78C64UM0Y8".equals(marketPlaceID)){//墨西哥
                url="https://mws.amazonservices.com.mx/";

            }else if("A2EUQ1WTGCTBG2".equals(marketPlaceID)){//加拿大
                url="https://mws.amazonservices.ca/";

            }else if("A1F83G8C2ARO7P".equals(marketPlaceID)){//英国
                url="https://mws-eu.amazonservices.com/";

            }else if("A2Q3Y263D00KWC".equals(marketPlaceID)){//巴西
                url="https://mws.amazonservices.com/";

            }else if("A2VIGQ35RCS4UG".equals(marketPlaceID)){//阿拉伯联合酋长国（UAE）地区
                url="https://mws.amazonservices.ae/";

            }else if("A1PA6795UKMFR9".equals(marketPlaceID)){//德国
                url="https://mws-eu.amazonservices.com/";

            }else if("A1RKKUPIHCS9HS".equals(marketPlaceID)){//西班牙
                url="https://mws-eu.amazonservices.com/";

            }else if("A13V1IB3VIYZZH".equals(marketPlaceID)){//法国
                url="https://mws-eu.amazonservices.com/";

            }else if("A21TJRUUN4KGV".equals(marketPlaceID)){//印度
                url="https://mws.amazonservices.in/";

            }else if("APJ6JRA9NG5V4".equals(marketPlaceID)){//意大利
                url="https://mws-eu.amazonservices.com/";

            }else if("A33AVAJ2PDY3EV".equals(marketPlaceID)){//土耳其
                url="https://mws-eu.amazonservices.com/";

            }else if("A19VAU5U5O7RUS".equals(marketPlaceID)){//新加坡
                url="https://mws-fe.amazonservices.com/";

            }else if("A39IBJ37TRP1C6".equals(marketPlaceID)){//澳大利亚
                url="https://mws.amazonservices.com.au/";

            }else if("A1VC38T7YXB528".equals(marketPlaceID)){//日本
                url="https://mws.amazonservices.jp/";

            }else{//默认为美国站的请求
                url="https://mws.amazonservices.com/";

            }


            /**开发者密钥(访问密钥？)**/
            String secretKey = secretKEY;
            Map<String, String> parameters = mustByParams();

            /**开发者id**/
            parameters.put("AWSAccessKeyId", AmazonUntil.urlEncode(awsAccessKeyId));
            /***商家提供授权token*/
            parameters.put("MWSAuthToken", AmazonUntil.urlEncode(mwsToken));
            /***商家提供卖家id*/
            parameters.put("SellerId", AmazonUntil.urlEncode(sellerId));
            parameters.put("MarketplaceId.Id.1", AmazonUntil.urlEncode(marketPlaceID));
            parameters.put("Action", AmazonUntil.urlEncode("GetFeedSubmissionResult"));
            parameters.put("Version", AmazonUntil.urlEncode("2009-01-01"));

            if(StringUtils.isNoneBlank(feedSubmissionId)){
                parameters.put("FeedSubmissionId",feedSubmissionId);
            }

            String formattedParameters = calculateStringToSignV2ForFeed(parameters, url);
//        logger.info("签名内容：{}", formattedParameters);
            String signature = sign(formattedParameters, secretKey);
            parameters.put("Signature", urlEncode(signature));
            String sortParams = sortParams(new StringBuilder(), parameters);


            CloseableHttpClient httpClient= HttpClients.createDefault();
            HttpPost httpPost=new HttpPost(url);
            URL endpendi=new URL(url.toLowerCase());
            httpPost.addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3724.8 Safari/537.36");
            httpPost.addHeader("Host",endpendi.getHost());
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            httpPost.addHeader("x-amazon-user-agent", "AmazonJavascriptScratchpad/1.0 (Language=Javascript)");

            String char_set="UTF-8";
            StringEntity entity=new StringEntity(sortParams,char_set);
            httpPost.setEntity(entity);
            CloseableHttpResponse reponse=null;
            reponse = httpClient.execute(httpPost);
            InputStream is = reponse.getEntity().getContent();
            return parseCommodityResultXml(is);
        }catch (Exception e){
            e.printStackTrace();
            throw new NoHttpResponseException("");
        }
    }




    /***
     * 查询上传记录信息
     * @return
     * @throws Exception
     */
    public static JSONArray feedList(String summbinId,String sellerId,String mwsToken,String secretKEY,String awsAccessKeyId,String marketPlaceID) throws Exception {
            String url="";
            String paramStr="";
        try{

            if("ATVPDKIKX0DER".equals(marketPlaceID)){//美国
                url="https://mws.amazonservices.com/";

            }else if("A1AM78C64UM0Y8".equals(marketPlaceID)){//墨西哥
                url="https://mws.amazonservices.com.mx/";

            }else if("A2EUQ1WTGCTBG2".equals(marketPlaceID)){//加拿大
                url="https://mws.amazonservices.ca/";

            }else if("A1F83G8C2ARO7P".equals(marketPlaceID)){//英国
                url="https://mws-eu.amazonservices.com/";

            }else if("A2Q3Y263D00KWC".equals(marketPlaceID)){//巴西
                url="https://mws.amazonservices.com/";

            }else if("A2VIGQ35RCS4UG".equals(marketPlaceID)){//阿拉伯联合酋长国（UAE）地区
                url="https://mws.amazonservices.ae/";

            }else if("A1PA6795UKMFR9".equals(marketPlaceID)){//德国
                url="https://mws-eu.amazonservices.com/";

            }else if("A1RKKUPIHCS9HS".equals(marketPlaceID)){//西班牙
                url="https://mws-eu.amazonservices.com/";

            }else if("A13V1IB3VIYZZH".equals(marketPlaceID)){//法国
                url="https://mws-eu.amazonservices.com/";

            }else if("A21TJRUUN4KGV".equals(marketPlaceID)){//印度
                url="https://mws.amazonservices.in/";

            }else if("APJ6JRA9NG5V4".equals(marketPlaceID)){//意大利
                url="https://mws-eu.amazonservices.com/";

            }else if("A33AVAJ2PDY3EV".equals(marketPlaceID)){//土耳其
                url="https://mws-eu.amazonservices.com/";

            }else if("A19VAU5U5O7RUS".equals(marketPlaceID)){//新加坡
                url="https://mws-fe.amazonservices.com/";

            }else if("A39IBJ37TRP1C6".equals(marketPlaceID)){//澳大利亚
                url="https://mws.amazonservices.com.au/";

            }else if("A1VC38T7YXB528".equals(marketPlaceID)){//日本
                url="https://mws.amazonservices.jp/";

            }else{//默认为美国站的请求
                url="https://mws.amazonservices.com/";

            }

            /**开发者密钥(访问密钥？)**/
            String secretKey = secretKEY;
            HashMap<String, String> parameters = AmazonUntil.mustByParams();
            /**开发者id**/
            parameters.put("AWSAccessKeyId", AmazonUntil.urlEncode(awsAccessKeyId));
            parameters.put("MarketplaceId.Id.1", AmazonUntil.urlEncode(marketPlaceID));
            /***商家提供授权token*/
            parameters.put("MWSAuthToken", AmazonUntil.urlEncode(mwsToken));
            /***商家提供卖家id*/
            parameters.put("SellerId", AmazonUntil.urlEncode(sellerId));
            parameters.put("Action", AmazonUntil.urlEncode("GetFeedSubmissionList"));
            parameters.put("Version", AmazonUntil.urlEncode("2009-01-01"));
            if(StringUtils.isNoneBlank(summbinId)){
                parameters.put("FeedSubmissionIdList.Id.1", AmazonUntil.urlEncode(summbinId));
            }


            String formattedParameters = calculateStringToSignV2ForFeed(parameters, url);
//        logger.info("签名内容：{}", formattedParameters);
            String signature = sign(formattedParameters, secretKey);
//        logger.info("签名signature: {}", signature);
            parameters.put("Signature", AmazonUntil.urlEncode(signature));
            paramStr = sortParams(new StringBuilder(), parameters);
//        logger.info("排序后参数：{}", paramStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return feedListPost(url, paramStr);

    }


    /***
     * 查询上传记录信息
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static JSONArray feedListPost(String url, String params) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url+"?"+params);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addHeader("User-Agent", "hyxun/amzn.mws.56af5b71-a0ef-1d49-e464-3909bad5ba91 (Language=Java/1.6.0.11; Platform=Windows/XP)");
        httpPost.addHeader("x-amazon-user-agent", "AmazonJavascriptScratchpad/1.0 (Language=Javascript)");
        httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
//        String charSet = "UTF-8";
//        StringEntity entity = new StringEntity(params, charSet);
//        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            InputStream is = responseEntity.getContent();

//            int lengt=0;
//            byte[] bb=new byte[1024];
//            while ((lengt=is.read(bb))!=-1){
//                System.err.println(new String(bb,0,lengt));
//            }

            return parseCommodityListtXml(is);
        } catch (Exception e) {
            throw e;
        }
//        finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    throw e;
//                }
//            }
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                throw e;
//            }
//        }
    }


    /***
     * 根据订单id查询订单信息（拼装请求参数）
     * @param secretKey
     * @param parameters
     * @return
     * @throws Exception
     */
    public static JSONArray getOrder(String url, HashMap<String, String> parameters, String secretKey) throws Exception {
        //TODO暂未使用该功能


//    	  parameters.put("MarketplaceId.Id.1", AmazonUntil.urlEncode(AmazonConstant.US));
//        parameters.put("Action", AmazonUntil.urlEncode(AmazonConstant.GET_ORDER));
//        parameters.put("Version", AmazonUntil.urlEncode(AmazonConstant.ORDER_VERSION));
//
//        String formattedParameters = calculateStringToSignV2ForOrder(parameters, AmazonConstant.ORDER_POST_URL);
////        logger.info("签名内容：{}", formattedParameters);
//        String signature = sign(formattedParameters, secretKey);
////        logger.info("签名signature: {}", signature);
//        parameters.put("Signature", AmazonUntil.urlEncode(signature));
//        String paramStr = sortParams(new StringBuilder(), parameters);
////        logger.info("排序后参数：{}", paramStr);
//        return doGetOrderPost(url, paramStr);
        return null;

    }


    /***
     * 订单id数据查询请求（执行请求方法）
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static JSONArray doGetOrderPost(String url, String params) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url+"?"+params);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addHeader("User-Agent", "hyxun/amzn.mws.56af5b71-a0ef-1d49-e464-3909bad5ba91 (Language=Java/1.6.0.11; Platform=Windows/XP)");
        httpPost.addHeader("x-amazon-user-agent", "AmazonJavascriptScratchpad/1.0 (Language=Javascript)");
        httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
//        String charSet = "UTF-8";
//        StringEntity entity = new StringEntity(params, charSet);
//        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            InputStream is = responseEntity.getContent();

//            int lengt=0;
//            byte[] bb=new byte[1024];
//            while ((lengt=is.read(bb))!=-1){
//                System.err.println(new String(bb,0,lengt));
//            }

            return parseGetOrderXML(is);
        } catch (Exception e) {
            throw e;
        }
//        finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    throw e;
//                }
//            }
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                throw e;
//            }
//        }
    }


    /**
     * 解析根据id查询订单获取的数据（解析io流数据）
     * @param is
     * @return
     */
    public static JSONArray parseGetOrderXML(InputStream is) {

        JSON xml = new XMLSerializer().readFromStream(is);

        JSONObject jsonObject = JSONObject.fromObject(xml);
        JSONArray orders = new JSONArray();
        if (null != jsonObject.get("GetOrderResult")) {
            JSONObject listOrdersResult = (JSONObject) jsonObject.get("GetOrderResult");
            if (listOrdersResult != null) {
                //获取到所有的订单内容 list
                JSONObject orderLists =null;
                if(listOrdersResult.get("Orders")!=null){
                    orderLists=(JSONObject) listOrdersResult.get("Orders");
                    if (orderLists.get("Order") != null) {
                        JSONObject order = (JSONObject)orderLists.get("Order");
                        orders.add(order);

                    }
                }
            }
        }
        return orders;

    }


    /***
     * 根据订单id查询商品信息
     * @param secretKey
     * @param parameters
     * @return
     * @throws Exception
     */
    public static JSONArray getOrderItems(String marekPlace, HashMap<String, String> parameters, String secretKey) throws Exception {

        String url="";
        if("ATVPDKIKX0DER".equals(marekPlace)){//美国
            url="https://mws.amazonservices.com/";

        }else if("A1AM78C64UM0Y8".equals(marekPlace)){//墨西哥
            url="https://mws.amazonservices.com.mx/";

        }else if("A2EUQ1WTGCTBG2".equals(marekPlace)){//加拿大
            url="https://mws.amazonservices.ca/";

        }else if("A1F83G8C2ARO7P".equals(marekPlace)){//英国
            url="https://mws-eu.amazonservices.com/";

        }else if("A2Q3Y263D00KWC".equals(marekPlace)){//巴西
            url="https://mws.amazonservices.com/";

        }else if("A2VIGQ35RCS4UG".equals(marekPlace)){//阿拉伯联合酋长国（UAE）地区
            url="https://mws.amazonservices.ae/";

        }else if("A1PA6795UKMFR9".equals(marekPlace)){//德国
            url="https://mws-eu.amazonservices.com/";

        }else if("A1RKKUPIHCS9HS".equals(marekPlace)){//西班牙
            url="https://mws-eu.amazonservices.com/";

        }else if("A13V1IB3VIYZZH".equals(marekPlace)){//法国
            url="https://mws-eu.amazonservices.com/";

        }else if("A21TJRUUN4KGV".equals(marekPlace)){//印度
            url="https://mws.amazonservices.in/";

        }else if("APJ6JRA9NG5V4".equals(marekPlace)){//意大利
            url="https://mws-eu.amazonservices.com/";

        }else if("A33AVAJ2PDY3EV".equals(marekPlace)){//土耳其
            url="https://mws-eu.amazonservices.com/";

        }else if("A19VAU5U5O7RUS".equals(marekPlace)){//新加坡
            url="https://mws-fe.amazonservices.com/";

        }else if("A39IBJ37TRP1C6".equals(marekPlace)){//澳大利亚
            url="https://mws.amazonservices.com.au/";

        }else if("A1VC38T7YXB528".equals(marekPlace)){//日本
            url="https://mws.amazonservices.jp/";

        }else{//默认为美国站的请求
            url="https://mws.amazonservices.com/";

        }

        parameters.put("Action", AmazonUntil.urlEncode(AmazonConstant.ORDER_ITEM_ACTION));
        parameters.put("Version", AmazonUntil.urlEncode(AmazonConstant.ORDER_VERSION));

        String formattedParameters = calculateStringToSignV2ForOrder(parameters, url+"Orders/2013-09-01");
//        logger.info("签名内容：{}", formattedParameters);
        String signature = sign(formattedParameters, secretKey);
//        logger.info("签名signature: {}", signature);
        parameters.put("Signature", AmazonUntil.urlEncode(signature));
        String paramStr = sortParams(new StringBuilder(), parameters);
//        logger.info("排序后参数：{}", paramStr);
        return doOrderIteam(url+"Orders/2013-09-01", paramStr);

    }


    /***
     * 订单id数据查询商品请求
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static JSONArray doOrderIteam(String url, String params) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url+"?"+params);
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        httpPost.addHeader("User-Agent", "hyxun/amzn.mws.56af5b71-a0ef-1d49-e464-3909bad5ba91 (Language=Java/1.6.0.11; Platform=Windows/XP)");
        httpPost.addHeader("x-amazon-user-agent", "AmazonJavascriptScratchpad/1.0 (Language=Javascript)");
        httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
//        String charSet = "UTF-8";
//        StringEntity entity = new StringEntity(params, charSet);
//        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            InputStream is = responseEntity.getContent();


            return parseOrderIteamXML(is);
        } catch (Exception e) {
            throw e;
        }
//        finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    throw e;
//                }
//            }
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                throw e;
//            }
//        }
    }


    /**
     * 解析订单查询商品的流数据
     * @param is
     * @return
     */
    public static JSONArray parseOrderIteamXML(InputStream is) {

        JSON xml = new XMLSerializer().readFromStream(is);

        JSONObject jsonObject = JSONObject.fromObject(xml);
        JSONArray orders = new JSONArray();
        if (null != jsonObject.get("ListOrderItemsResult")) {
            JSONObject listOrdersResult = (JSONObject) jsonObject.get("ListOrderItemsResult");
            if (listOrdersResult != null) {
                //获取到所有商品信息
                JSONObject orderLists =null;
                if(listOrdersResult.get("OrderItems")!=null){
                    orderLists=(JSONObject) listOrdersResult.get("OrderItems");
                    if (orderLists.get("OrderItem") != null) {
                        String orderItem = orderLists.get("OrderItem").toString();
                        //如果返回的array
                        if("[".equals(orderItem.substring(0, 1))){
                            orders = (JSONArray)orderLists.get("OrderItem");
                        }else{
                            JSONObject order = (JSONObject)orderLists.get("OrderItem");
                            orders.add(order);
                        }


                    }
                }
            }
        }
        return orders;

    }


    /***
     * 根据订单id查询商品信息
     * @param secretKey
     * @param parameters
     * @return
     * @throws Exception
     */
    public static JSONObject getOrderIteam(String marekPlace, HashMap<String, String> parameters, String secretKey) throws Exception {

        String url="";
        if("ATVPDKIKX0DER".equals(marekPlace)){//美国
            url="https://mws.amazonservices.com/";

        }else if("A1AM78C64UM0Y8".equals(marekPlace)){//墨西哥
            url="https://mws.amazonservices.com.mx/";

        }else if("A2EUQ1WTGCTBG2".equals(marekPlace)){//加拿大
            url="https://mws.amazonservices.ca/";

        }else if("A1F83G8C2ARO7P".equals(marekPlace)){//英国
            url="https://mws-eu.amazonservices.com/";

        }else if("A2Q3Y263D00KWC".equals(marekPlace)){//巴西
            url="https://mws.amazonservices.com/";

        }else if("A2VIGQ35RCS4UG".equals(marekPlace)){//阿拉伯联合酋长国（UAE）地区
            url="https://mws.amazonservices.ae/";

        }else if("A1PA6795UKMFR9".equals(marekPlace)){//德国
            url="https://mws-eu.amazonservices.com/";

        }else if("A1RKKUPIHCS9HS".equals(marekPlace)){//西班牙
            url="https://mws-eu.amazonservices.com/";

        }else if("A13V1IB3VIYZZH".equals(marekPlace)){//法国
            url="https://mws-eu.amazonservices.com/";

        }else if("A21TJRUUN4KGV".equals(marekPlace)){//印度
            url="https://mws.amazonservices.in/";

        }else if("APJ6JRA9NG5V4".equals(marekPlace)){//意大利
            url="https://mws-eu.amazonservices.com/";

        }else if("A33AVAJ2PDY3EV".equals(marekPlace)){//土耳其
            url="https://mws-eu.amazonservices.com/";

        }else if("A19VAU5U5O7RUS".equals(marekPlace)){//新加坡
            url="https://mws-fe.amazonservices.com/";

        }else if("A39IBJ37TRP1C6".equals(marekPlace)){//澳大利亚
            url="https://mws.amazonservices.com.au/";

        }else if("A1VC38T7YXB528".equals(marekPlace)){//日本
            url="https://mws.amazonservices.jp/";

        }else{//默认为美国站的请求
            url="https://mws.amazonservices.com/";

        }

        parameters.put("Action", AmazonUntil.urlEncode(AmazonConstant.ITEM_ACTION));
        parameters.put("Version", AmazonUntil.urlEncode(AmazonConstant.ITEM_VERSION));

        String formattedParameters = calculateStringToSignV2ForIteam(parameters, url+"Products/2011-10-01");
//        logger.info("签名内容：{}", formattedParameters);
        String signature = sign(formattedParameters, secretKey);
//        logger.info("签名signature: {}", signature);
        parameters.put("Signature", AmazonUntil.urlEncode(signature));
        String paramStr = sortParams(new StringBuilder(), parameters);
//        logger.info("排序后参数：{}", paramStr);
        return doGetIteamPost(url+"Products/2011-10-01", paramStr);

    }


    /***
     * 订单id数据查询请求
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static JSONObject doGetIteamPost(String url, String params) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url+"?"+params);
        httpPost.addHeader("Content-Type", "text/xml");
        httpPost.addHeader("User-Agent", "hyxun/amzn.mws.56af5b71-a0ef-1d49-e464-3909bad5ba91 (Language=Java/1.6.0.11; Platform=Windows/XP)");
        httpPost.addHeader("x-amazon-user-agent", "AmazonJavascriptScratchpad/1.0 (Language=Javascript)");
        httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
//        String charSet = "UTF-8";
//        StringEntity entity = new StringEntity(params, charSet);
//        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            InputStream is = responseEntity.getContent();

            return parseAsinItemXML(is);

        } catch (Exception e) {
            throw e;
        }
//        finally {
//            if (response != null) {
//                try {
//                    response.close();
//                } catch (IOException e) {
//                    throw e;
//                }
//            }
//            try {
//                httpclient.close();
//            } catch (IOException e) {
//                throw e;
//            }
//        }
    }


    /**
     * 解析根据asin查询商品的流数据
     * @param is
     * @return
     */
    public static JSONObject parseAsinItemXML(InputStream is) {

        String xml=new XMLSerializer().readFromStream(is).toString().replaceAll("@","");
        JSONObject jsonObject = JSONObject.fromObject(xml);
        JSONObject obj = new JSONObject();
        if (null != jsonObject.get("GetMatchingProductForIdResult")) {
            JSONObject objcet = (JSONObject) jsonObject.get("GetMatchingProductForIdResult");
            //判断该sku值有效
            if(StringUtils.equals("Success",objcet.getString("status"))){
                obj.put("type",objcet.getString("IdType"));
                obj.put("asin",objcet.getString("Id"));
                //获取亚马逊返回商品信息图片层级(查看亚马逊返回的xml层级)
                JSONObject objcet1 = (JSONObject) objcet.get("Products");
                JSONObject objcet2 = (JSONObject) objcet1.get("Product");
                JSONObject objcet3 = (JSONObject) objcet2.get("AttributeSets");
                JSONObject objcet4 = (JSONObject) objcet3.get("ns2:ItemAttributes");
                JSONObject objcet5 = (JSONObject) objcet4.get("ns2:SmallImage");
                obj.put("image",objcet5.getString("ns2:URL"));

                //判断订单获取变体的color和size
                if(objcet4.has("ns2:Color")&&objcet4.has("ns2:Size")){
                    obj.put("title",objcet4.getString("ns2:Title")+"-"+objcet4.getString("ns2:Color")+"-"+objcet4.getString("ns2:Size"));
                }else if(objcet4.has("ns2:Color")){
                    obj.put("title",objcet4.getString("ns2:Title")+"-"+objcet4.getString("ns2:Color"));
                }else if(objcet4.has("ns2:Size")){
                    obj.put("title",objcet4.getString("ns2:Title")+"-"+objcet4.getString("ns2:Size"));
                }else{
                    obj.put("title",objcet4.getString("ns2:Title"));
                }
            }else{
                obj.put("type",objcet.getString("IdType"));
                obj.put("asin",objcet.getString("Id"));
                obj.put("image","");
                obj.put("title","");
            }
        }

        return obj;

    }


    /**
     * 封装亚马逊必须请求参数map
     * @return
     */
    public static HashMap<String,String> mustByParams(){

        HashMap<String, String> parameters = new HashMap<String, String>();
        /***当前时间（格林时间格式）*/
        String str = DateTime.now(DateTimeZone.UTC).toString(AmazonConstant.TIME_FORMAT_STR);
        parameters.put("Timestamp", urlEncode(str));
        /***签名方式*/
        parameters.put("SignatureMethod", AmazonUntil.urlEncode(AmazonConstant.ALGORITHM));
        /***签名版本*/
        parameters.put("SignatureVersion", AmazonUntil.urlEncode(AmazonConstant.SIGNATURE_VERSION));

        return parameters;

    }


    /**
     * 根据店铺标识区分亚马逊区域id
     * @param type
     * @return
     */
    public static String formatteMarketplaceId(String type){
        //区域标识
        String marketplaceId="";
        if("BR".equals(type)){//巴西地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_BR;
        }else if("CA".equals(type)){//加拿大地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_CA;
        }else if("MX".equals(type)){//墨西哥地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_MX;
        }else if("US".equals(type)){//美国地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_US;
        }else if("AE".equals(type)){//阿拉伯联合酋长国（UAE）地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_AE;
        }else if("DE".equals(type)){//德国地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_DE;
        }else if("ES".equals(type)){//西班牙地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_ES;
        }else if("FR".equals(type)){//法国地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_FR;
        }else if("GB".equals(type)){//英国地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_GB;
        }else if("IN".equals(type)){//印度地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_IN;
        }else if("IT".equals(type)){//意大利地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_IT;
        }else if("TR".equals(type)){//土耳其地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_TR;
        }else if("SG".equals(type)){//新加坡地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_SG;
        }else if("AU".equals(type)){//澳大利亚地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_AU;
        }else if("JP".equals(type)){//日本地区
            marketplaceId=AmazonConstant.COUNTRY_CODE_JP;
        }
        return marketplaceId;
    }


}
