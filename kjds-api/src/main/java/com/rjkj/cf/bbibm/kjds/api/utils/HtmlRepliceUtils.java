package com.rjkj.cf.bbibm.kjds.api.utils;


/**
 * 去除html中的标签工具类
 */
public class HtmlRepliceUtils {

    public  static String parseDivHtmlInfo(String descrip){
        String html="<div[^>]*>";
        String htmls="</div>";
        String s1 = descrip.replaceAll(html, "");
        String s2 = s1.replaceAll(htmls, "");
        return s2;
    }

    public  static String parseLinkHtmlInfo(String descrip){
        String html="<link[^>]*>";
        String htmls="</link>";
        String s1 = descrip.replaceAll(html, "");
        String s2 = s1.replaceAll(htmls, "");
        return s2;
    }

    public  static String parseImgHtmlInfo(String descrip){
        String html="<img[^>]*>";
        String htmls="</img>";
        String s1 = descrip.replaceAll(html, "");
        String s2 = s1.replaceAll(htmls, "");
        return s2;
    }


    /**
     * 去除所有html标签
     * @param descrip
     * @return
     */
    public  static String parseAllHtmlInfo(String descrip){
        String html="</?[^<]+>";
        String s1 = descrip.replaceAll(html, "");
        return s1;
    }




}
