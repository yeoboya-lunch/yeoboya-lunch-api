package com.yeoboya.guinGujik.api.v1._sample.repository;

import com.yeoboya.guinGujik.api.v1._sample.model.vo.FanClubMemExp;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface SampleRepository {

    // master db
    @Select("CALL c_radio.p_radio_fan_club_mem_exp_list()")
    List<FanClubMemExp> pRadioFanClubMemExpListM();

    @Transactional(readOnly = true) // slave db 에서 조회
    @Select("CALL c_radio.p_radio_fan_club_mem_exp_list()")
    List<FanClubMemExp> pRadioFanClubMemExpList();

}
