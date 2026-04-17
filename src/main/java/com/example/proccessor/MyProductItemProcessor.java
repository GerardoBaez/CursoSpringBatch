package com.example.proccessor;

import org.springframework.batch.item.ItemProcessor;

import com.example.domain.Product;

public class MyProductItemProcessor implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product item) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("processor executed!!");
		Integer price = item.getPrice();
		item.setPrice((int) (price - ((0.1)*price)));
		return item;
	}

}
