package com.example.listener;

import java.io.FileWriter;
import java.io.IOException;

import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;

import com.example.domain.OSProduct;
import com.example.domain.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySkipListener implements SkipListener<Product, OSProduct> {

	@Override
	public void onSkipInRead(Throwable t) {
		if(t instanceof FlatFileParseException) {
			log.info("Skipped item {}" ,((FlatFileParseException)t).getInput());
			writeToFile(((FlatFileParseException)t).getInput());
		}
	
	}

	@Override
	public void onSkipInWrite(OSProduct item, Throwable t) {
		// TODO Auto-generated method stub
		SkipListener.super.onSkipInWrite(item, t);
	}

	@Override
	public void onSkipInProcess(Product item, Throwable t) {
		// TODO Auto-generated method stub
		log.info("onSkipInProcess for item:{}", item);
		writeToFile(item.toString());
	}
	
	
	public void writeToFile(String data) {
		try {
			FileWriter fileWriter = new FileWriter("rejected/Product_Details_Rejected.txt",true);
			fileWriter.write(data + "\n");
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
