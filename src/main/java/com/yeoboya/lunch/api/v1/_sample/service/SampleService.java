package com.yeoboya.lunch.api.v1._sample.service;

import com.yeoboya.lunch.api.v1._sample.model.vo.FanClubMemExp;
import com.yeoboya.lunch.api.v1._sample.repository.SampleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        List<FanClubMemExp> fanClubMemExpList = sampleRepository.pRadioFanClubMemExpList();

        Map<String, Object> data = new HashMap<String, Object>() {
            {
                put("cnt", fanClubMemExpList.size());
                put("list", fanClubMemExpList);
            }
        };

        return data;
    }

}
