package xyz.cdbxinhe.cat.backend.util.common;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.StreamUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 * package xyz.cdbxinhe.cat.backend.util.common
 * project backend
 * Created by @author CaoXin on date 2023/03/01
 */
public class HttpUtils {
    /**
     * 发送HTTP POST请求
     *
     * @param url     请求地址
     * @param raw     是否格式化为JSON
     * @param body    请求体
     * @param headers 自定义请求Header
     * @param isJson 请求体是否为json
     * @return 请求返回信息
     */
    public static JSONObject doPost(String url, boolean raw, JSONObject body, JSONObject headers,boolean isJson) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost postMethod = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<>();
        if (body != null) {
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                try {
                    postMethod.setEntity(new UrlEncodedFormEntity(params));
                } catch (UnsupportedEncodingException ignored) {
                    return null;
                }
            }
        }
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                postMethod.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        postMethod.addHeader("User-Agent", "XiaYiXueYuan/1.0");

        try {
            HttpResponse response = httpClient.execute(postMethod);
            JSONObject result = new JSONObject();
            if (raw) {
                result.put("raw", StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
                result.put("statusCode", response.getStatusLine().getStatusCode());
                result.put("setCookie", response.getHeaders("Set-Cookie"));
            } else {
                result = JSONObject.parseObject(StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
                result.put("statusCode", response.getStatusLine().getStatusCode());
                result.put("setCookie", response.getHeaders("Set-Cookie"));

            }
            return result;
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * 发送HTTP GET请求
     *
     * @param url     请求地址
     * @param raw     是否格式化为JSON
     * @param headers 自定义请求Header
     * @return 请求返回信息
     */
    public static JSONObject doGet(String url, boolean raw, JSONObject headers) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet getMethod = new HttpGet(url);

        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                getMethod.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        getMethod.addHeader("User-Agent", "XiaYiXueYuan/1.0");

        try {
            HttpResponse response = httpClient.execute(getMethod);
            JSONObject result = new JSONObject();
            if (raw) {
                result.put("raw", StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
                result.put("statusCode", response.getStatusLine().getStatusCode());
            } else {
                result = JSONObject.parseObject(StreamUtils.copyToString(response.getEntity().getContent(), StandardCharsets.UTF_8));
                result.put("statusCode", response.getStatusLine().getStatusCode());
            }
            return result;
        } catch (Exception ignored) {
            return null;
        }
    }
}
