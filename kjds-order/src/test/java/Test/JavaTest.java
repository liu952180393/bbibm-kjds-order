package Test;

import net.sf.json.JSONObject;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class JavaTest {

    @Test
    public void Test(){
        testRest("tw","123456");
    }

    public void testRest(String area,String shopId) {
        String url = "http://192.168.0.199:8666/goods/user/getOrder?area="+area+"&shopId="+shopId;
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            JSONObject jsonObject = JSONObject.fromObject(response.body().string());
            Object o = JSONObject.toBean(jsonObject);
            System.out.println(o);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
