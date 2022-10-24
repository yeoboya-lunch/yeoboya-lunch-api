package com.yeoboya.guinGujik.api.sms.model;

import lombok.Builder;
import lombok.Setter;

@Setter
@Builder
public class SmsRequestDto {

    public SmsRequestDto(String msgSlct, String sendPhone, String sendMemNo, String rcvPhone, String rcvMemId, String titleConts, String msgBody, String atchFile, String rsrvDt, String tranSlct) {
        this.msgSlct = msgSlct;             // 메세지 구분 (S:단문SMS, L:장문SMS, M:포토MMS)
        this.sendPhone = sendPhone;         // 발신자 전화번호
        this.sendMemNo = sendMemNo;         // 발신자 회원번호
        this.rcvPhone = rcvPhone;           // 수신자 전화번호 (다수 폰에 발송 가능)
        this.rcvMemId = rcvMemId;           // 수신자 회원아이디 (아이디 발송시 등록)
        this.titleConts = titleConts;       // 제목
        this.msgBody = msgBody;             // 내용
        this.atchFile = atchFile;           // 첨부포토파일(년월일 디렉토리 경로 + 파일명)
        this.rsrvDt = rsrvDt;               // 예약일시
        this.tranSlct = tranSlct;           // 발송구분 (코드표참조-c308)
    }

    @Builder.Default
    private String msgSlct = "S";           //CHAR(1)
    @Builder.Default
    private String sendPhone = "1577-6592"; //VARCHAR(16)
    @Builder.Default
    private String sendMemNo = "00000";     //INT UNSIGNED
    @Builder.Default
    private String rcvPhone = "";           //TEXT
    @Builder.Default
    private String rcvMemId = "";           //VARCHAR(20)
    @Builder.Default
    private String titleConts = "";         //VARCHAR(60)
    @Builder.Default
    private String msgBody = "";            //VARCHAR(2000)
    @Builder.Default
    private String atchFile = "";           //VARCHAR(128)
    @Builder.Default
    private String rsrvDt = "";             //VARCHAR(20)
    @Builder.Default
    private String tranSlct = "S";          //CHAR(1)

}
