package com.yeoboya.lunch.api.v1._sample.model.vo;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Greet {
  private final Long id;
  private final String country;
  private final String say;

  @Builder
  public Greet(Long id, String country, String say) {
    this.id = id;
    this.country = country;
    this.say = say;
  }
}