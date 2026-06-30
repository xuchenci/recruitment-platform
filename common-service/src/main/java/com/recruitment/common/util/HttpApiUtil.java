package com.recruitment.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP API调用工具类
 * 用于调用第三方招聘数据API
 */
@Component
public class HttpApiUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpApiUtil.class);

    /**
     * GET请求
     */
    public static String get(String urlString, Map<String, String> headers) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        // 设置默认Headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        // 设置自定义Headers
        if (headers != null) {
            headers.forEach(conn::setRequestProperty);
        }

        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            log.error("HTTP请求失败: {} - {}", responseCode, urlString);
        }

        conn.disconnect();
        return response.toString();
    }

    /**
     * POST请求 (JSON)
     */
    public static String postJson(String urlString, String jsonBody, Map<String, String> headers) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        // 设置Headers
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0");

        if (headers != null) {
            headers.forEach(conn::setRequestProperty);
        }

        // 写入请求体
        if (jsonBody != null && !jsonBody.isEmpty()) {
            try (var os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        int responseCode = conn.getResponseCode();
        StringBuilder response = new StringBuilder();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
        } else {
            log.error("HTTP POST请求失败: {} - {}", responseCode, urlString);
        }

        conn.disconnect();
        return response.toString();
    }
}
