package com.wt.piaoliuping.utils;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.haoxitech.HaoConnect.HaoConfig;
import com.haoxitech.HaoConnect.HaoUtility;
import com.wt.piaoliuping.App;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangmac
 * on 15/12/23.
 * Email: www.fangmu@qq.com
 * Phone：186 6120 1018
 * Purpose:HTTP工具类
 * update：细分发送方式 全部使用despost
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";
    private static final int TIMEOUT_IN_MILLIONS = 15 * 1000;

    /**
     * post 请求
     *
     * @param url   请求地址
     * @param param 请求内容
     * @return 返回DES加密数据
     */

    public static String doPost(String url, Map<String, String> param) {
        StringBuilder paramStr = new StringBuilder();
        for (Map.Entry<String, String> para : param.entrySet()) {
            try {
                paramStr.append(para.getKey()).append("=").append(URLEncoder.encode(para.getValue(), "UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        // 发送请求参数
        LogUtil.e(TAG, "http发送 " + url + "?" + paramStr.toString());
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //超时时间
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            out.print(paramStr.toString());
            // flush输出流的缓冲
            out.flush();

            try {
                // 定义BufferedReader输入流来读取URL的响应
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            } catch (FileNotFoundException e) {
                result = null;
            }
        } catch (SocketTimeoutException e) {
            LogUtil.e(TAG, "发送请求超时！");
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            return null;
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        LogUtil.e(TAG, "http返回" + result);
        return result;
    }

    public static Map<String, String> signParam(Map<String, String> paramMap) {
        paramMap.put("device", "android");
        paramMap.put("version", "");
        return paramMap;
    }


    private static String Userid = "";
    private static String Logintime = "";
    private static String Checkcode = "";

    /**
     * @param requestData
     * @param urlParam
     * @return 请求头数据，里面包括加密字段
     */
    public static Map<String, Object> getSecretHeaders(Map<String, Object> requestData, String urlParam) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Clientinfo", "android");
        headers.put("Clientversion","1.0");
        headers.put("Devicetype", "3");
        headers.put("Requesttime", (System.currentTimeMillis() / 1000) + "");
//        headers.put("Requesttime", (System.currentTimeMillis()) + "");
        headers.put("Devicetoken", "");
        headers.put("Isdebug", "0");

        if (TextUtils.isEmpty(Userid)) {
            Userid = getString("userID");
            Logintime = getString("loginTime");
            Checkcode = getString("checkCode");
        }
        headers.put("Userid", Userid);
        headers.put("Logintime", Logintime);
        headers.put("Checkcode", Checkcode);

        Map<String, Object> signMap = new HashMap<>();
        signMap.putAll(headers);
        if (requestData != null) {
            signMap.putAll(requestData);
        }
        Map<String, Object> linkMap = new HashMap<>();
        linkMap.put("link", HaoUtility.httpStringFilter("http://" + HaoConfig.getApiHost() + "/" + urlParam));
        signMap.putAll(linkMap);
        headers.put("Signature", getSignature(signMap));
        return headers;
    }

    public static String getString(String key) {
        try {
            SharedPreferences sharedPreferences = App.getInstance().getSharedPreferences("config", 0);
            return sharedPreferences.getString(key, "");
        } catch (Exception e) {
            Log.e("getStringInfo", e + "");
            return "";
        }
    }


    /**
     * 加密算法
     */
    private static String getSignature(Map<String, Object> map) {
        List<String> tmpArr = new ArrayList<>();
        String secret = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String data = entry.getKey() + "=" + entry.getValue();
            tmpArr.add(data);
        }
        tmpArr.add(HaoConfig.getSecretHax());
        Collections.sort(tmpArr);
        for (String string : tmpArr) {
            secret += string;
        }
        return HaoUtility.encodeMD5String(secret);
    }

}
