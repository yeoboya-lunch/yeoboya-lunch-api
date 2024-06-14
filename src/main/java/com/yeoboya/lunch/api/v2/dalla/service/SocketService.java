package com.yeoboya.lunch.api.v2.dalla.service;

import com.yeoboya.lunch.api.v2.dalla.request.DallaPayload;
import com.yeoboya.lunch.api.v2.dalla.request.SocketVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocketService {

    private final DallaPayload dallaPayload;

    public void sendUmmMessage() {
        SocketVo vo = new SocketVo();
        vo.setMemNo("11678410359105");
        vo.setLogin(1);
        vo.setCommand("reqBcStart");
        vo.setMessage("엄준식 님이 입장하였습니다.");
        vo.setCtrlRole("0");
        vo.setRecvType("system");
        vo.setAuth(3);
        vo.setAuthName("운영자");
        sendSocketPost("17282", vo.toQueryString());
    }



    public void sendSocketPost(String roomNo, String params){

        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(dallaPayload.getSocketUrl()+roomNo);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("x-custom-header", dallaPayload.getAuthToken());
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setDefaultUseCaches(false);
            con.setAllowUserInteraction(true);

            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            OutputStream outputStream = con.getOutputStream();
            outputStream.write(params.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
                String line;
                while (!(line = br.readLine()).equals("")) {
                    result.append(line);
                }
                br.close();
            } else {
                result.append(con.getResponseMessage());
            }

            log.warn(result.toString());

        } catch (IOException e){
            log.error("sendSocketPost", e);
        } catch (Exception e){
            result.append(e);
            e.getStackTrace();
        }

    }
}
