package com.spider;



import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.http.ImageItem;
import weibo4j.model.StatusWapper;

import java.io.File;
import java.io.FileInputStream;

public class WeiBoApi {


    //"access_token":"2.00fZ9ojC1GbIuB3a7f994657LKatGB"
    public static void main(String[] args) throws Exception{
        Weibo weibo = new Weibo();
        String access_token ="2.00fZ9ojC0S7gkG26ecbb7f4ckHw4_E";//这里对应accessToken=XXXXXXXXXXXXXXXXXXX的值
        Timeline tm = new Timeline(access_token);

        StatusWapper statusWapper= tm.getFriendsTimeline();
        File file=new File("E:\\test\\微信图片_20181217134451.jpg");
        FileInputStream fis=new FileInputStream(file);
        byte[] bytes=new byte[fis.available()];
        fis.read(bytes);
        ImageItem imageItem=new ImageItem(bytes);

        tm.share("sdfsdfsdfsdfsdfsd https://www.baidu.com/",imageItem);

        System.out.println("sdfsdf");


    }

}
