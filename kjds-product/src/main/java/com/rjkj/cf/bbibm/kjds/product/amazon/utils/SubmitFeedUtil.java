package com.rjkj.cf.bbibm.kjds.product.amazon.utils;

import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import com.rjkj.cf.bbibm.kjds.product.amazon.model.*;
import com.rjkj.cf.bbibm.kjds.product.amazon.service.MarketplaceWebService;
import com.rjkj.cf.bbibm.kjds.product.amazon.service.MarketplaceWebServiceClient;
import com.rjkj.cf.bbibm.kjds.product.amazon.service.MarketplaceWebServiceConfig;
import com.rjkj.cf.bbibm.kjds.product.amazon.service.MarketplaceWebServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NoHttpResponseException;
import org.dom4j.DocumentException;
import org.springframework.security.core.Authentication;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;


/**
 * 亚马逊上传
 * @author EDZ
 *
 */
public class SubmitFeedUtil {
	
	
	
	/**
	 * 亚马逊商品上传信息
	 * @param accessKeyId
	 * @param secretAccessKey
	 * @param appName
	 * @param appVersion
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 * @throws DocumentException
	 */
	public String SubmitFeed(String accessKeyId, String secretAccessKey, String appName, String appVersion, String merchantId, String sellerDevAuthToken, String submitType, String xmlPath, String marketplaceId, List<GoodsProduct> goodsProduct,String sid,String type) throws MarketplaceWebServiceException {

		MarketplaceWebServiceConfig config = new MarketplaceWebServiceConfig();

		//判断不同的国家亚马逊上传请求不同的地址
		String actionUrl="";
		if("ATVPDKIKX0DER".equals(marketplaceId)){//美国
			actionUrl="https://mws.amazonservices.com/";

		}else if("A1AM78C64UM0Y8".equals(marketplaceId)){//墨西哥
			actionUrl="https://mws.amazonservices.com.mx/";

		}else if("A2EUQ1WTGCTBG2".equals(marketplaceId)){//加拿大
			actionUrl="https://mws.amazonservices.ca/";

		}else if("A1F83G8C2ARO7P".equals(marketplaceId)){//英国
			actionUrl="https://mws-eu.amazonservices.com/";

		}else if("A2Q3Y263D00KWC".equals(marketplaceId)){//巴西
			actionUrl="https://mws.amazonservices.com/";

		}else if("A2VIGQ35RCS4UG".equals(marketplaceId)){//阿拉伯联合酋长国（UAE）地区
			actionUrl="https://mws.amazonservices.ae/";

		}else if("A1PA6795UKMFR9".equals(marketplaceId)){//德国
			actionUrl="https://mws-eu.amazonservices.com/";

		}else if("A1RKKUPIHCS9HS".equals(marketplaceId)){//西班牙
			actionUrl="https://mws-eu.amazonservices.com/";

		}else if("A13V1IB3VIYZZH".equals(marketplaceId)){//法国
			actionUrl="https://mws-eu.amazonservices.com/";

		}else if("A21TJRUUN4KGV".equals(marketplaceId)){//印度
			actionUrl="https://mws.amazonservices.in/";

		}else if("APJ6JRA9NG5V4".equals(marketplaceId)){//意大利
			actionUrl="https://mws-eu.amazonservices.com/";

		}else if("A33AVAJ2PDY3EV".equals(marketplaceId)){//土耳其
			actionUrl="https://mws-eu.amazonservices.com/";

		}else if("A19VAU5U5O7RUS".equals(marketplaceId)){//新加坡
			actionUrl="https://mws-fe.amazonservices.com/";

		}else if("A39IBJ37TRP1C6".equals(marketplaceId)){//澳大利亚
			actionUrl="https://mws.amazonservices.com.au/";

		}else if("A1VC38T7YXB528".equals(marketplaceId)){//日本
			actionUrl="https://mws.amazonservices.jp/";

		}else{//默认为美国站的请求
			actionUrl="https://mws.amazonservices.com/";

		}
		config.setServiceURL(actionUrl);


		MarketplaceWebService service = new MarketplaceWebServiceClient(
				accessKeyId, secretAccessKey, appName, appVersion, config);


		final IdList marketplaces = new IdList(Arrays.asList(marketplaceId));//亚马逊区域代码

		SubmitFeedRequest request = new SubmitFeedRequest();

		try{

			request.setMerchant(merchantId);
			request.setMWSAuthToken(sellerDevAuthToken);
			request.setMarketplaceIdList(marketplaces);

			request.setFeedType(submitType);//上传类型

			FileInputStream fileInputStream = new FileInputStream(new File(xmlPath));
			request.setFeedContent(fileInputStream);
			String md5HeaderValues = MarketplaceWebServiceClient.computeContentMD5HeaderValues(fileInputStream);
			request.setContentMD5(md5HeaderValues);


		}catch (Exception e){
			e.printStackTrace();
		}
		return invokeSubmitFeed(service, request,goodsProduct,sid,type);
	}
	
	
	/**
	 * 解析亚马逊返回的xml数据信息
	 * @param service
	 * @param request
	 */
    public static String invokeSubmitFeed(MarketplaceWebService service, SubmitFeedRequest request,List<GoodsProduct> goodsProduct,String sid,String type) throws MarketplaceWebServiceException {
    		String submissionId = "";

        try {

            SubmitFeedResponse response = service.submitFeed(request);
//            if (response.isSetSubmitFeedResult()) {
//                System.out.print("        SubmitFeedResult");
//                System.out.println();
//                SubmitFeedResult submitFeedResult = response.getSubmitFeedResult();
//                if (submitFeedResult.isSetFeedSubmissionInfo()) {
//                    System.out.print("            FeedSubmissionInfo");
//                    System.out.println();
//                    FeedSubmissionInfo feedSubmissionInfo = submitFeedResult.getFeedSubmissionInfo();
//                    if (feedSubmissionInfo.isSetFeedSubmissionId()) {
//                        System.out.print("                FeedSubmissionId");
//                        System.out.println();
//                        System.out.print("                    "
//                                + feedSubmissionInfo.getFeedSubmissionId());
//                        System.out.println();
//                    }
//                    if (feedSubmissionInfo.isSetFeedType()) {
//                        System.out.print("                FeedType");
//                        System.out.println();
//                        System.out.print("                    "
//                                + feedSubmissionInfo.getFeedType());
//                        System.out.println();
//                    }
//                    if (feedSubmissionInfo.isSetSubmittedDate()) {
//                        System.out.print("                SubmittedDate");
//                        System.out.println();
//                        System.out.print("                    "
//                                + feedSubmissionInfo.getSubmittedDate());
//                        System.out.println();
//                    }
//                    if (feedSubmissionInfo.isSetFeedProcessingStatus()) {
//                        System.out
//                        .print("                FeedProcessingStatus");
//                        System.out.println();
//                        System.out.print("                    "
//                                + feedSubmissionInfo.getFeedProcessingStatus());
//                        System.out.println();
//                    }
//                    if (feedSubmissionInfo.isSetStartedProcessingDate()) {
//                        System.out
//                        .print("                StartedProcessingDate");
//                        System.out.println();
//                        System.out
//                        .print("                    "
//                                + feedSubmissionInfo
//                                .getStartedProcessingDate());
//                        System.out.println();
//                    }
//                    if (feedSubmissionInfo.isSetCompletedProcessingDate()) {
//                        System.out
//                        .print("                CompletedProcessingDate");
//                        System.out.println();
//                        System.out.print("                    "
//                                + feedSubmissionInfo
//                                .getCompletedProcessingDate());
//                        System.out.println();
//                    }
//                }
//            }
//            if (response.isSetResponseMetadata()) {
//                System.out.print("        ResponseMetadata");
//                System.out.println();
//                ResponseMetadata responseMetadata = response
//                .getResponseMetadata();
//                if (responseMetadata.isSetRequestId()) {
//                    System.out.print("            RequestId");
//                    System.out.println();
//                    System.out.print("                "
//                            + responseMetadata.getRequestId());
//                    System.out.println();
//                }
//            }
//            System.out.println(response.getResponseHeaderMetadata());
//            System.out.println();
//            System.out.println();
            
            
          SubmitFeedResult submitFeedResult = response.getSubmitFeedResult();
          FeedSubmissionInfo feedSubmissionInfo = submitFeedResult.getFeedSubmissionInfo();
          
          if(feedSubmissionInfo.isSetFeedSubmissionId()) {
        	  submissionId= feedSubmissionInfo.getFeedSubmissionId();
          }
        } catch (Exception ex) {
			submissionId="xxxxx";
        	ex.printStackTrace();
			System.err.println("Caught Exception: " + ex.getMessage());


			if(ex instanceof ConnectException){
				System.out.println("请求亚马逊失败");
				for(int u=0;u<goodsProduct.size();u++){
					KjdsUtils.callback( goodsProduct.get(u).getId(), sid, "请求亚马逊接口异常 443异常", "", 5,goodsProduct.get(0).getUid());
				}
			}else if(ex instanceof RuntimeException){
				System.out.println("数据异常");
				for(int u=0;u<goodsProduct.size();u++){
					KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "系统运行时异常", "", 5,goodsProduct.get(0).getUid());
				}
			}else if(ex instanceof SocketTimeoutException){
				System.out.println("请求亚马逊失败");
				for(int u=0;u<goodsProduct.size();u++){
					KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口异常 443 failed to respond", "", 5,goodsProduct.get(0).getUid());
				}
			}else if(ex instanceof SocketException){
				System.out.println("请求亚马逊连接重置");
				for(int u=0;u<goodsProduct.size();u++){
					KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊连接重置", "", 5,goodsProduct.get(0).getUid());
				}
			}else if(ex instanceof NoHttpResponseException){
				System.out.println("请求亚马逊数据失败");
				for(int u=0;u<goodsProduct.size();u++){
					KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口异常 443 failed to respond", "", 5,goodsProduct.get(0).getUid());
				}
			}else if(ex instanceof MarketplaceWebServiceException){
				System.out.println("亚马逊内部异常错误");
				if(ex.toString().contains("Request is throttled") || ex.getMessage().contains("Request is throttled")){//亚马逊接口请求限制
					System.out.println("亚马逊接口请求限制sbumitfeed");
					if(StringUtils.equals(type,"1") || StringUtils.equals(type,"2")){//当type为1时代表商品上传步骤
						for(int u=0;u<goodsProduct.size();u++){
							KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口超过限制的频率,请稍后再试。", "", 5,goodsProduct.get(0).getUid());
						}
					}
				}else if(ex.toString().contains("You exceeded your quota of") || ex.getMessage().contains("You exceeded your quota of")){//亚马逊接口请求限制
					System.out.println("亚马逊接口请求限制sbumitfeed");
					if(StringUtils.equals(type,"1") || StringUtils.equals(type,"2")) {//当type为1时代表商品上传步骤
						for (int u = 0; u < goodsProduct.size(); u++) {
							KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊接口超过限制的频率,请稍后再试。", "", 5,goodsProduct.get(0).getUid());
						}
					}
				}else if(ex.toString().contains("Internal Error") || ex.getMessage().contains("Internal Error")){//亚马逊内部错误
					System.out.println("亚马逊请求出错");
					if(StringUtils.equals(type,"1") || StringUtils.equals(type,"2")) {//当type为1时代表商品上传步骤
						for (int u = 0; u < goodsProduct.size(); u++) {
							KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "请求亚马逊内部错误", "", 5,goodsProduct.get(0).getUid());
						}
					}
				}else{
					System.out.println("亚马逊内部其它异常问题---------`");
					if(StringUtils.equals(type,"1") || StringUtils.equals(type,"2")) {//当type为1时代表商品上传步骤
						for (int u = 0; u < goodsProduct.size(); u++) {
							KjdsUtils.callback(goodsProduct.get(u).getId(), sid, ex.getMessage(), "", 5,goodsProduct.get(0).getUid());
						}
					}
				}


			}else{
				for(int u=0;u<goodsProduct.size();u++){
					System.out.println("系统异常");
					KjdsUtils.callback(goodsProduct.get(u).getId(), sid, "系统开小差了", "", 5,goodsProduct.get(0).getUid());
				}
			}


//             System.out.println("Response Status Code: " + ex.getStatusCode());
//             System.out.println("Error Code: " + ex.getErrorCode());
//             System.out.println("Error Type: " + ex.getErrorType());
//             System.out.println("Request ID: " + ex.getRequestId());
//             System.out.print("XML: " + ex.getXML());
//             System.out.println("ResponseHeaderMetadata: " + ex.getResponseHeaderMetadata());
        }

        return submissionId;
    }

}
