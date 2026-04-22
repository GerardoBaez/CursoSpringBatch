package com.example.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Product {

	private Integer productId;
	private String name;
	@Pattern(regexp = "Mobile Phones|Tablets|Televisions|Sports Accessories")
	private String productCategory;
	@Max(100000)
	private Integer price; 
}
