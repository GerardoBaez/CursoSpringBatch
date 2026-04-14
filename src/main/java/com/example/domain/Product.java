package com.example.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Product {

	private Integer productId;
	private String name;
	private String productCategory;
	private Integer price; 
}
