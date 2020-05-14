package com.rjkj.cf.bbibm.kjds.api.utils;

import com.rjkj.cf.admin.api.entity.SysFile;
import com.rjkj.cf.admin.api.feign.RemoteFileService;
import com.rjkj.cf.common.core.constant.SecurityConstants;
import com.rjkj.cf.common.core.util.SpringContextHolder;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @描述：
 * @项目：bbibm-kjds
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/10/21 14:21
 **/
@UtilityClass
public class SysFileUtils {

    @Setter
    private static RemoteFileService  remoteFileService;

    @Setter
    private static RestTemplate  restTemplate;


    public static List<SysFile> getSysFile(String id){
          init();
          return remoteFileService.getSysFilesById(id, SecurityConstants.FROM_IN).getData();
    }

    public static void deleteFile(String id){
        init();
        remoteFileService.deleteFiles(id,SecurityConstants.FROM_IN);
    }

    public static void saveBeatch(List<SysFile> list){
        init();
        remoteFileService.addSysFiles(list,SecurityConstants.FROM_IN);
    }

    private  void init() {
        if(remoteFileService==null){
            RemoteFileService bean = SpringContextHolder.getApplicationContext().getBean(RemoteFileService.class);
            remoteFileService=bean;
        }
    }

    public static RestTemplate  getRestTemplate(){
        if(restTemplate==null){
             restTemplate = SpringContextHolder.getApplicationContext().getBean(RestTemplate.class);
        }
       return restTemplate;
    }
}
