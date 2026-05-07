package com.example.skippolicy;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.validator.ValidationException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MySkipPolicy implements SkipPolicy {

	@Override
	public boolean shouldSkip(Throwable t, long skipCount) throws SkipLimitExceededException {
		log.info("SkipCount {}", skipCount);
		if(skipCount< 3 ) {
			if (t instanceof ValidationException) {
				log.info("ValidationException");
				return true;
			}
			
			if(t instanceof FlatFileParseException ) {
				String line=((FlatFileParseException)t).getInput();
				String[] lineArr=line.split(",");
				if(lineArr.length >= 4) {
					log.info("FlatFileParseException");
					return true;
				}
			}
		}
		
		
		return false;
	}

}
