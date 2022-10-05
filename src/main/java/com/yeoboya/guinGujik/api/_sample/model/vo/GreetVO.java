package com.yeoboya.guinGujik.api._sample.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GreetVO {
  private final Long id;
  private final String country;
  private final String say;

  @Builder
  public GreetVO(Long id, String country, String say) {
    this.id = id;
    this.country = country;
    this.say = say;
  }
}