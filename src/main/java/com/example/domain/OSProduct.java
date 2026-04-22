package com.example.domain;

import lombok.Data;

@Data
public class OSProduct extends Product {

	private Integer taxPercent;
	private String sku;
	private Integer shippingRate;
}
