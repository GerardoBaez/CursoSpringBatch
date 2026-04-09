package com.example.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBiulderFactory;
	
	@Autowired
	public StepBuilderFactory stepBiulderFactory;
	
	
	@Bean 
	public Step step1() {
		return this.stepBiulderFactory.get("step1").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("step1 executed!!");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean 
	public Step step2() {
		return this.stepBiulderFactory.get("step2").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				boolean fail= false;
				if(fail) {
				 throw new Exception("Test Exception");			
				}	
				log.info("step2 executed!!");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean 
	public Step step3() {
		return this.stepBiulderFactory.get("step3").tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("step3 executed!!");
				return RepeatStatus.FINISHED;
			}
		}).build();
	}
	
	@Bean 
	public Job firstJob() {
		return this.jobBiulderFactory.get("job1")
		.start(step1()).on("COMPLETED").to(step2())
		.from(step2()).on("COMPLETED").to(step3())
		.end()
		.build();
	}
	
}
