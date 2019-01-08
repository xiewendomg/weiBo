package com.spider;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * 蜂鸟网 http://www.fengniao.com/
 * 光热帖板块 包括风光、旅行、生态、建筑
 */
public class FengNiaoPageProcessor implements PageProcessor {
    public static final String URL_LIST = "http://blog\\.sina\\.com\\.cn/s/articlelist_1487828712_0_\\d+\\.html";

    public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";

    private static String ENTRY_URL = "http://bbs\\.fengniao\\.com/forum/forum_\\d+\\.html";
    private static String NEW = "新";
    private static String ENTRY_URL_LIST = "//div[@class='bbsListAll bbsList  module1200 yinying clearfix']/ul[@class='txtList']/li/h3";
    private static String BBG_NEWTOPICON = "i[@class='bBg newTopIcon']/text()";
    private static String BBS_FENGNIAO = "http://bbs.fengniao.com";
    private static String INVITATION_URL = "http://bbs\\.fengniao\\.com/forum/\\d+\\.html";
    // https://bbs.qn.img-space.com/201901/8/e2be344c39b54704a4f1ee0b2cdf0d14.jpg
    private static String FIRST_IMG_REGEX = "^https://bbs\\.qn\\.img-space\\.com/\\d+/\\d+/\\w+\\.jpg";

    private Site site = Site
            .me()
            .setDomain("blog.sina.com.cn")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");

    @Override
    public void process(Page page) {
        if (page.getUrl().regex(ENTRY_URL).match()) {
            List<Selectable> list = page.getHtml().xpath(ENTRY_URL_LIST).nodes();
            for (Selectable selectable : list) {
                if (NEW.equals(selectable.xpath(BBG_NEWTOPICON).get())) {
                    page.addTargetRequest(BBS_FENGNIAO + selectable.xpath("a/@href").get());
                    System.out.println(selectable.xpath("a/@href").get());
                }
            }
        }
        if (page.getUrl().regex(INVITATION_URL).match()) {
            String title = page.getHtml().xpath("//div[@class='titleBox module1200']/h3/text()").get();
            System.out.println(title);
            listImg(title,page.getHtml().xpath("//img/@src").regex(FIRST_IMG_REGEX).all());
        }

    }

    private void listImg(String title,List<String> list) {
        int i=0;
        System.out.println(list);
        for (String string : list) {
            URL url = null;
            URLConnection con = null;
            try {
                url = new URL(string);
                con = url.openConnection();
                InputStream inStream = con.getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len = 0;
                while ((len = inStream.read(buf)) != -1) {
                    outStream.write(buf, 0, len);
                }
                inStream.close();
                outStream.close();
                File file = new File("E://test//" + System.currentTimeMillis()+i++ + ".jpg");    //图片下载地址
                FileOutputStream op = new FileOutputStream(file);
                op.write(outStream.toByteArray());
                op.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    private static final Downloader downloader = new HttpClientDownloader();
    private static final SeleniumDownloader seleniumDownloader= new SeleniumDownloader("");


    private static Spider spider = Spider.create(new FengNiaoPageProcessor()).setDownloader(seleniumDownloader)
            .setDownloader(downloader).addUrl("http://bbs.fengniao.com/forum/forum_125.html");

    public static void main(String[] args) {
        spider.run();
    }

}
