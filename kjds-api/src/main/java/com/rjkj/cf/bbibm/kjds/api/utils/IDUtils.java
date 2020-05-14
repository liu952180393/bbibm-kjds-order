package com.rjkj.cf.bbibm.kjds.api.utils;

import java.util.UUID;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/8 16:18
 **/
public class IDUtils {

    public static String getGUUID(String id){
          return UUID.randomUUID().toString().replaceAll("-","");
    }
}
