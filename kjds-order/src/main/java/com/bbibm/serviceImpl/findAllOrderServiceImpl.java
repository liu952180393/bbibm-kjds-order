package com.bbibm.serviceImpl;

import com.bbibm.service.findAllOrderService;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class findAllOrderServiceImpl implements findAllOrderService {

    @Override
    public String testRest(String area, String shopId) {
        String url = "http://192.168.0.199:8666/goods/user/getOrder?area="+area+"&shopId="+shopId;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }


}
