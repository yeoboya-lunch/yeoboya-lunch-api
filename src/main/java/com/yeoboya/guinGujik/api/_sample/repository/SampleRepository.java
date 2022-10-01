package com.yeoboya.guinGujik.api._sample.repository;

import com.yeoboya.guinGujik.api._sample.model.vo.FanClubMemExpVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface SampleRepository {

    // master db
    @Select("CALL c_radio.p_radio_fan_club_mem_exp_list()")
    List<FanClubMemExpVO> pRadioFanClubMemExpListM();

    @Transactional(readOnly = true) // slave db
    @Select("CALL c_radio.p_radio_fan_club_mem_exp_list()")
    List<FanClubMemExpVO> pRadioFanClubMemExpList();

}
