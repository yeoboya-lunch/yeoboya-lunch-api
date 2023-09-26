package com.yeoboya.lunch.config.util;

import com.yeoboya.lunch.api.v2.dalla.request.DallaPayload;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class OkhttpClient {

    private final DallaPayload dallaPayload;

    // one instance, reuse
    OkHttpClient client = new OkHttpClient().newBuilder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build();

    public String sendGet(String url, Map<String, String> params){
        HttpUrl.Builder httpBuilder = Objects.requireNonNull(HttpUrl.parse(dallaPayload.getBaseUrl() + url)).newBuilder();
        if (params != null) {
            for(Map.Entry<String, String> param : params.entrySet()) {
                httpBuilder.addQueryParameter(param.getKey(),param.getValue());
            }
        }

        Request request = new Request.Builder()
                .header("authToken", dallaPayload.getAuthToken())
                .url(httpBuilder.build())
                .build();
        try {
            return Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String sendPost(String url) {
        RequestBody body = RequestBody.create("", MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .header("authToken", dallaPayload.getAuthToken())
                .url(dallaPayload.getBaseUrl()+url)
                .method("POST", body)
                .build();
        try {
            return Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String sendPost(String url, RequestBody formBody) {
        Request request = new Request.Builder()
                .header("authToken", dallaPayload.getAuthToken())
                .url(dallaPayload.getBaseUrl()+url)
                .post(formBody)
                .build();
        try {
            return Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public String sendPost(String url, JSONObject json) {
        RequestBody body = RequestBody.create(String.valueOf(json), MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .header("authToken", dallaPayload.getAuthToken())
                .url(dallaPayload.getBaseUrl()+url)
                .post(body)
                .build();
        try {
            return Objects.requireNonNull(client.newCall(request).execute().body()).string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
