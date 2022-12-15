package com.yeoboya.lunch.api.v1.board.response;


import com.yeoboya.lunch.api.v1.board.domain.HashTag;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class HashTagResponse {

    private final String tag;

    public static HashTagResponse from(HashTag tag){
        return new HashTagResponse(tag.getTag());
    }
}
