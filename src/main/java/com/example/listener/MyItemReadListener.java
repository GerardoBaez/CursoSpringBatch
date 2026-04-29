package com.example.listener;

import org.springframework.batch.core.ItemReadListener;

import com.example.domain.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyItemReadListener implements ItemReadListener<Product> {

	@Override
	public void beforeRead() {
		log.info("Before Read");
		
	}

	@Override
	public void afterRead(Product item) {
		log.info("AfterRead for product {}" ,item.getProductId());
	}

	@Override
	public void onReadError(Exception ex) {
		log.info("onReadError..");
	}
	
}
