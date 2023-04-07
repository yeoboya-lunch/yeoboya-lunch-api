package com.yeoboya.lunch.api.v2.dalla.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class Gift {
      private String roomNo;
      private String memNo;   //선물받을유저 memNo
      private String userMemNo;     //선물보낼유저 memNo
      private String itemNo="G1684";
      private String itemCnt="1";
      private String isSecret="false";
}
