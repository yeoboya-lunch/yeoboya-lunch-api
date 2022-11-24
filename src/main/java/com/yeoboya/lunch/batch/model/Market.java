package com.yeoboya.lunch.batch.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
//@Entity
public class Market {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private int price;
	
	@Override
	public String toString() {
		return "Market [id=" + id + ", name=" + name + ", price=" + price + "]";
	}
}