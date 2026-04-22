package com.example.domain;

import org.springframework.batch.item.ItemProcessor;

public class FilterProductItemProcessor implements ItemProcessor<Product,Product> {

	@Override
	public Product process(Product item) throws Exception {
		System.out.println("Filter product itemproccessor");
		if(item.getPrice()>100) {
			return item;
		}else {
			return null;
		}
	}

}
