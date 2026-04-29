package com.example.listener;

import org.springframework.batch.core.ItemProcessListener;

import com.example.domain.OSProduct;
import com.example.domain.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyItemProcessListener implements ItemProcessListener<Product, OSProduct> {

	@Override
	public void beforeProcess(Product item) {
		log.info("BeforeProcess for : {}", item.getProductId());
	}

	@Override
	public void afterProcess(Product item, OSProduct result) {
		log.info("afterProcess for : {}", item.getProductId());
	}

	@Override
	public void onProcessError(Product item, Exception e) {
		log.info("onProcessError for : {}", item.getProductId());
	}

	
	
}
