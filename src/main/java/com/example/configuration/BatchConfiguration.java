package com.example.configuration;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import com.example.decider.MyJobExecutionDecider;
import com.example.listener.MyStepExecutionListener;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	
	@Bean
	public JobExecutionDecider decider() {
		return new MyJobExecutionDecider();
	}
	
	@Bean 
	public StepExecutionListener mystepExecutionListener() {
		return new MyStepExecutionListener();
	}
	
	@Bean 
	public Step step1(JobRepository jobrep, PlatformTransactionManager tx) {
		return new StepBuilder("step1", jobrep).tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("step1 executed!!");
				return RepeatStatus.FINISHED;
			}
		},tx).build();
	}
	
	
	
	
	@Bean 
	public Step step2(JobRepository jobrep, PlatformTransactionManager tx) {
		return new StepBuilder("step2",jobrep).tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				boolean fail= false;
				if(fail) {
				 throw new Exception("Test Exception");			
				}	
				log.info("step2 executed!!");
				return RepeatStatus.FINISHED;
			}
		},tx).build();
	}
	
	@Bean 
	public Step step3(JobRepository jobrep, PlatformTransactionManager tx) {
		return new StepBuilder("step3",jobrep).tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("step3 executed on thread:"+ Thread.currentThread().getName());
				return RepeatStatus.FINISHED;
			}
		}, tx).build();
	}
	
	@Bean 
	public Step step4(JobRepository jobrep, PlatformTransactionManager tx) {
		return new StepBuilder("step4",jobrep).tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("step4 executed on thread:"+ Thread.currentThread().getName());
				return RepeatStatus.FINISHED;
			}
		}, tx).build();
	}
	
	@Bean 
	public Step step5(JobRepository jobrep, PlatformTransactionManager tx) {
		return new StepBuilder("step5",jobrep).tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("step5 executed on thread:"+ Thread.currentThread().getName());
				return RepeatStatus.FINISHED;
			}
		}, tx).build();
	}
	
	@Bean 
	public Step step6(JobRepository jobrep, PlatformTransactionManager tx) {
		return new StepBuilder("step6",jobrep).tasklet(new Tasklet(){
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				log.info("step6 executed on thread:"+ Thread.currentThread().getName());
				return RepeatStatus.FINISHED;
			}
		}, tx).build();
	}
	
	@Bean
	public Step job3step(JobRepository jobrep, Job job3) {
		return new StepBuilder("job3step",jobrep).job(job3).build();
	}
	
	
	
	@Bean
	public Flow flow1(Step step3, Step step4) {
		FlowBuilder<Flow> flowBuilder= new FlowBuilder<>("flow1");
		flowBuilder.start(step3)
					.next(step4)
					.end();
		return flowBuilder.build();
	}
	
	@Bean
	public Flow flow2(Step step5, Step step6) {
		FlowBuilder<Flow> flowBuilder= new FlowBuilder<>("flow2");
		flowBuilder.start(step5)
					.next(step6)
					.end();
		return flowBuilder.build();
	}
	
	
	@Bean 
	public Job job1(JobRepository jobrep,Step step1, Step step2, Flow flow1) {
		return new JobBuilder("job1",jobrep)
		.start(step1)
		.next(step2)
		.on("COMPLETED").to(flow1)
		.end()
		.build();
	}
	
	@Bean 
	public Job job2(JobRepository jobrep, Step job3step, Flow flow1, Flow flow2) {		
		return new JobBuilder("job2",jobrep)
		.start(flow1)
		.split(new SimpleAsyncTaskExecutor())
		.add(flow2)
		.end()
		.build();
	}
	
	
	@Bean 
	public Job job3(JobRepository jobrep,Step step5, Step step6, Flow flow1) {		
		return new JobBuilder("job3",jobrep)
		.start(step5)
		.next(step6)
		.build();
	}
	
	
	
}
