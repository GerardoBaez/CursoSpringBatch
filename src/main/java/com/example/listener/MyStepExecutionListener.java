package com.example.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyStepExecutionListener {

	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		log.info("Step Name:{}", stepExecution.getStepName());
		log.info("Step Exit Status:{}",stepExecution.getExitStatus());
		log.info("Step Start Time:{}", stepExecution.getStartTime());
		log.info(stepExecution.getStepName(),"executed on thread {}", Thread.currentThread());
	}
	
	
	
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		log.info("Step Name:{}", stepExecution.getStepName());
		log.info("Step Exit Status:{}",stepExecution.getExitStatus());
		log.info("Step Start Time:{}", stepExecution.getStartTime());
		log.info(stepExecution.getStepName()+"executed on thread {}",Thread.currentThread());
		
		return null;
	}

}
