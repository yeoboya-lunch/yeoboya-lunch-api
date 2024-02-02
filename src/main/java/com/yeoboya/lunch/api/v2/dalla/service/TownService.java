package com.yeoboya.lunch.api.v2.dalla.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yeoboya.lunch.api.v2.dalla.response.DallaResponse;
import com.yeoboya.lunch.config.util.OkhttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TownService {

    private final OkhttpClient client;
    private final ObjectMapper objectMapper;

    //미션보상받기
    public DallaResponse mission(String townCode, String townMsCode) {
        JSONObject json = new JSONObject();
        try {
            json.put("townCode", townCode);
            json.put("townMsCode", townMsCode);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String s = client.sendPost("/town/mission", json);
        log.error("{}", s);
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    //미션업데이트
    public DallaResponse missionUpdate(String townCode, String townMsCode) {
        JSONObject json = new JSONObject();
        try {
            json.put("townCode", townCode);
            json.put("townMsCode", townMsCode);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String s = client.sendPost("/town/mission-update", json);
        log.error("{}", s);
        try {
            return objectMapper.readValue(s, DallaResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}


