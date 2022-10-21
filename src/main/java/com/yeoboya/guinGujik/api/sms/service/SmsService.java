package com.yeoboya.guinGujik.api.sms.service;

import com.yeoboya.guinGujik.api.sms.repository.SmsRepository;
import com.yeoboya.guinGujik.api.sms.dto.SmsRequestDto;
import com.yeoboya.guinGujik.config.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    private final SmsRepository smsRepository;

    public SmsService(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    @Retry(value = 5)
    public int sendSMS(SmsRequestDto dto) {
        int sendResult = smsRepository.pHpMsgSend(dto);
        if (sendResult != 1) {
            throw new IllegalStateException("예외발생");
        }
        return sendResult;
    }

}
