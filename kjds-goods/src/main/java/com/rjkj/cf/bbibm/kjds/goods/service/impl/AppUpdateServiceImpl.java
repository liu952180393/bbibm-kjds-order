package com.rjkj.cf.bbibm.kjds.goods.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rjkj.cf.bbibm.kjds.api.entity.AppUpdate;
import com.rjkj.cf.bbibm.kjds.api.utils.IDUtils;
import com.rjkj.cf.bbibm.kjds.goods.mapper.AppUpdateMapper;
import com.rjkj.cf.bbibm.kjds.goods.service.AppUpdateService;
import org.springframework.stereotype.Service;


/**
 *@描述：版本更新
 *@项目：
 *@公司：软江科技
 *@作者：YiHao
 *@时间：2019-10-24 10:45:42
 **/
@Service
public class AppUpdateServiceImpl extends ServiceImpl<AppUpdateMapper, AppUpdate> implements AppUpdateService {


    @Override
    public boolean save(AppUpdate au) {
        au.setId(IDUtils.getGUUID(""));
//        Object img = au.getUrl();
//        if(img!=null){
//            if(img.toString().startsWith("[")){
//                if(img.toString().equals("[]")){
//                    au.setUrl("");
//                }else{
//                    au.setUrl(img.toString().substring(1,img.toString().length()-1));
//                }
//            }else{
//                au.setUrl(img.toString());
//            }
//        }
        return super.save(au);
    }

    @Override
    public boolean updateById(AppUpdate au) {
//        Object img = au.getUrl();
//        if(img!=null){
//            if(img.toString().startsWith("[")){
//                if(img.toString().equals("[]")){
//                    au.setUrl("");
//                }else{
//                    au.setUrl(img.toString().substring(1,img.toString().length()-1));
//                }
//            }else{
//                au.setUrl(img.toString());
//            }
//        }
        return super.updateById(au);
    }
}
