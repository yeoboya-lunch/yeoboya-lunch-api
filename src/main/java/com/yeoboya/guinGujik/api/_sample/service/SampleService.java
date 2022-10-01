package com.yeoboya.guinGujik.api._sample.service;

import com.yeoboya.guinGujik.api._sample.model.vo.FanClubMemExpVO;
import com.yeoboya.guinGujik.api._sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleRepository sampleRepository;

    public Map<String, Object> sampleBiz(){
        List<FanClubMemExpVO> fanClubMemExpVOList = sampleRepository.pRadioFanClubMemExpList();

        Map<String, Object> data = new HashMap<String, Object>() {
            {
                put("cnt", fanClubMemExpVOList.size());
                put("list", fanClubMemExpVOList);
            }
        };

        return data;
    }

}
