package com.example.proccessor;

import org.springframework.batch.item.ItemProcessor;

import com.example.domain.OSProduct;
import com.example.domain.Product;

public class TransformProductItemProcessor implements ItemProcessor<Product, OSProduct> {

	@Override
	public OSProduct process(Product item) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("transformprocces executed for item "+ item);
		OSProduct osProduct = new OSProduct();
		
		osProduct.setName(item.getName());
		osProduct.setPrice(item.getPrice());
		osProduct.setProductCategory(item.getProductCategory());
		osProduct.setProductId(item.getProductId());
		osProduct.setTaxPercent(item.getProductCategory().equals("Sports Accessories")? 5: 18);
		osProduct.setSku(item.getProductCategory().substring(0,3)+item.getProductId());		
		osProduct.setShippingRate(item.getPrice()< 1000 ? 75:0);
		
		/**
		if(item.getPrice()>500) {
			throw new Exception("Test Exception");
		}
		**/
		return osProduct;
	}

}
