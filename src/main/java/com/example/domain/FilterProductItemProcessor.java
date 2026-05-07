package com.example.domain;

import org.springframework.batch.item.ItemProcessor;

import com.example.exception.MyException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FilterProductItemProcessor implements ItemProcessor<Product,Product> {

	@Override
	public Product process(Product item) throws Exception {
		System.out.println("Filter product itemproccessor for item"+ item);
		/**
		if(item.getPrice()>100) {
			return item;
		}else {
			return null;
		}
		*/
		
		if(item.getPrice()==500) {
			log.info("Exception Thrown");
			throw new MyException("Test exception"); 
		}
		return item;
	}

}
