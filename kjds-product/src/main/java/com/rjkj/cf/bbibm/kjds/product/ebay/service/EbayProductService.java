package com.rjkj.cf.bbibm.kjds.product.ebay.service;

import com.rjkj.cf.bbibm.kjds.api.feign.RemoteGoodsFeignService;
import com.rjkj.cf.bbibm.kjds.api.utils.EbayUtils;
import com.rjkj.cf.bbibm.kjds.api.utils.KjdsUtils;
import lombok.AllArgsConstructor;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class EbayProductService {
    private final RemoteGoodsFeignService remoteGoodsFeignService;

    @Async
    public void endItem(Authentication authentication,String pid,String gid,String itemId,String token,String uid) {
        try {
            String callName = "EndItem";
            //获取URLConnection
//        URLConnection conn = EbayUtils.getUrlConnection(callName);
            Document document = DocumentHelper.createDocument();
            // 创建根节点
            document.addElement("EndItemRequest", "urn:ebay:apis:eBLBaseComponents");
            // 通过document对象获取根元素的信息
            document.getRootElement().addElement("RequesterCredentials").addElement("eBayAuthToken").setText(token);
            document.getRootElement().addElement("ItemID").setText(itemId);
            document.getRootElement().addElement("EndingReason").setText("NotAvailable");
            EbayUtils.sendRequest(callName, document.asXML());
            System.out.println("====Ebay下架====");
            KjdsUtils.callback(pid, gid, "", itemId, 1,uid);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("下架失败");
        }
    }
}
