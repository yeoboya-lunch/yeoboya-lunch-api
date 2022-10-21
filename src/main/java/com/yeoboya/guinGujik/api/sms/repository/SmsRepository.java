package com.yeoboya.guinGujik.api.sms.repository;

import com.yeoboya.guinGujik.api.sms.dto.SmsRequestDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsRepository {

    // -9:에러, 1:정상
    @Select("CALL c_radio_sms.p_hpMsg_send(#{msgSlct}, #{sendPhone}, #{sendMemNo}, #{rcvPhone}, #{rcvMemId}, #{titleConts}, #{msgBody}, #{atchFile}, #{rsrvDt}, #{tranSlct})")
    int pHpMsgSend(SmsRequestDto dto);

}
