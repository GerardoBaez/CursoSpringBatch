package com.example.listener;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

import com.example.domain.OSProduct;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyItemWriteListener implements ItemWriteListener<OSProduct> {

	@Override
	public void beforeWrite(Chunk<? extends OSProduct> items) {
		log.info("BeforeWrite for products {}", items);
	}

	@Override
	public void afterWrite(Chunk<? extends OSProduct> items) {
		log.info("afterWrite for products {}", items);
	}

	@Override
	public void onWriteError(Exception exception, Chunk<? extends OSProduct> items) {
		log.info("onWriteError for products {}", items);
	}

	
}
