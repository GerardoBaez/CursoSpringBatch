package com.example.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.item.ExecutionContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyJobExecutionListener {

	
	@BeforeJob
	public void beforeJob(JobExecution jobExecution) {
		log.info("Job Name:{}",jobExecution.getJobInstance().getJobName());
		log.info("Job Parameters:{}",jobExecution.getJobParameters());
		log.info("Job End Time :{}",jobExecution.getEndTime());
		ExecutionContext jobExecutionContext = jobExecution.getExecutionContext();
		jobExecutionContext.put("jk1","XYZ");
		
	}
	
	@AfterJob
	public void afterJob(JobExecution jobExecution) {
		log.info("Job Name:{}",jobExecution.getJobInstance().getJobName());
		log.info("Job Parameters:{}",jobExecution.getJobParameters());
		log.info("Job End Time :{}",jobExecution.getEndTime());
	}
	
}
