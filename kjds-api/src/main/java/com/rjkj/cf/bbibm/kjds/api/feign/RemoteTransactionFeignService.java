package com.rjkj.cf.bbibm.kjds.api.feign;

import com.rjkj.cf.bbibm.kjds.api.entity.GoodsProduct;
import com.rjkj.cf.bbibm.kjds.api.entity.Product;
import com.rjkj.cf.bbibm.kjds.api.utils.Const;
import com.rjkj.cf.common.core.util.R;
import com.rjkj.cf.transcation.lang.LANG;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/9 15:03
 **/
@FeignClient(contextId = "remoteTransactionFeignService",value = Const.TRANSCATION_SERVICE_NAME)
public interface RemoteTransactionFeignService {

     @PostMapping(value = "/transaction/products")
     R  transcationProudcts(@RequestBody List<GoodsProduct> goodsProducts,
                            @RequestParam("fields")String fields,
                            @RequestParam("from") String from,
                            @RequestParam("to") String to);

    @PostMapping(value = "/transaction/translationProudcts")
    Product  translationProudcts(@RequestBody Product product,
                           @RequestParam("fields")String fields,
                           @RequestParam("to") String to);
     /**
      * 描述翻译
      * @param goodsProducts
      * @return
      */
     @PostMapping(value = "/transaction/products/desc")
     R  transcationProudcts(@RequestBody String goodsProducts);


  /**
   * 根据传进来的数据翻译信息
   * @return
   */
  @PostMapping(value = "/transaction/transcationSomeInfo")
  R<String>  transcationSomeInfo(@RequestParam(value = "message") String message,
                                 @RequestParam(value = "from") String from,
                                 @RequestParam(value = "to") String to);
}
