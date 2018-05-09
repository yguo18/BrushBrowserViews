package com.Browser2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yguo on 2018/5/5.
 */
public class BrushBrowserViews2 {

    private LinkedHashSet<String> urlSet;
    private String urlContent;

    public BrushBrowserViews2(){
        urlSet = new LinkedHashSet<>();
    }

    public LinkedHashSet<String> getUrlSet() {
        return urlSet;
    }
    public void setUrlSet(LinkedHashSet<String> urlSet) {
        this.urlSet = urlSet;
    }

    public void printURLSet(){
        System.out.println(urlSet.size());
        for(String url : urlSet){
            System.out.println(url);
        }
    }

    public void setUrlContent(String urlContent){
        this.urlContent = urlContent;
    }
    public String getUrlContent(){
        return urlContent;
    }

    public void paser(String urlStr){
        try{
            URL url = new URL(urlStr);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);

            String contentType = httpURLConnection.getContentType();
            String charSet = getCharSet(contentType);

            InputStreamReader streamReader = new InputStreamReader(httpURLConnection.getInputStream(),charSet);
            BufferedReader br = new BufferedReader(streamReader);

            String strLine = null;
            String rs = null;
            int i = 0;
            while ((strLine = br.readLine()) != null) {
//                i++;
//                System.out.println(i + ": " + str);
                rs = getHref(strLine);
                if (rs != null) {
                    urlSet.add(rs);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void urlNum(int num){
        String urlTmp="";
        for(int i = 1; i < num; i++){
            urlTmp = urlContent+(i+"?");
            paser(urlTmp);
        }
    }

    public void urlNum1(int num){
        String urlTmp="";
        urlTmp = urlContent+(num+"?");
        paser(urlTmp);
    }

    /*获取网页编码*/
    public String getCharSet(String str){
        Pattern pattern = Pattern.compile("charset=.*");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group(0).split("charset=")[1];
        }
        return null;
    }
    /*从一行中读取链接*/
    private String getHref(String str){
        Pattern pattern = Pattern.compile("<a href=.*yguoelect.+[0-9]{8}"); // 获取连接
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()){
            return matcher.group(0).split("\"")[1];
        }
        return null;
    }
    /*打开浏览网页*/
    public void urlConnect(String strUrl){
        try{
            HttpURLConnection con = (HttpURLConnection)new URL(strUrl).openConnection();
            con.setRequestMethod("HEAD");
            con.connect();
            if(con.getResponseCode() != HttpURLConnection.HTTP_OK){
                System.out.println("11");
            }
            Thread.sleep(2100);
            con.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    // 刷新次数
    public void flush(int n){
        for(int i = 0; i < n; i++){
            for(String url : urlSet){
                urlConnect(url);
            }
        }
    }

    public static void main(String[] args){
    	BrushBrowserViews2 views = new BrushBrowserViews2();
        views.setUrlContent("https://blog.csdn.net/yguoelect/article/list/");
        views.urlNum(5);
//        views.urlNum1(2);
//        views.printURLSet();
        views.flush(500);
    }
}

