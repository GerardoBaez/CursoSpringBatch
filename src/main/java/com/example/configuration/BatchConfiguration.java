package com.example.configuration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.example.domain.Product;
import com.example.domain.ProductFieldMapper;
import com.example.domain.ProductRowMapper;
import com.example.reader.ProductNameItemReader;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableBatchProcessing
@Slf4j
public class BatchConfiguration {

	@Autowired
	public JobBuilderFactory jobBiulderFactory;
	
	@Autowired
	public StepBuilderFactory stepBiulderFactory;
	
	@Autowired
	public DataSource dataSource;

	
	@Bean 
	public ItemReader<Product> jdbcCursorItemReader(){
		JdbcCursorItemReader<Product> itemReader = new JdbcCursorItemReader<>();
		itemReader.setDataSource(dataSource);
		itemReader.setSql("SELECT * FROM product_details order by product_id");
		itemReader.setRowMapper(new ProductRowMapper());
		return itemReader;
	}
	
	@Bean
	public ItemReader<Product>jdbcPagingItemReader() throws Exception{
		JdbcPagingItemReader<Product> itemReader = new JdbcPagingItemReader<>();
		itemReader.setDataSource(dataSource);
		
		SqlPagingQueryProviderFactoryBean factory = new SqlPagingQueryProviderFactoryBean();
		factory.setDataSource(dataSource);
		factory.setSelectClause("select product_id, product_name, product_category, product_price");
		factory.setFromClause("from product_details");
		factory.setSortKey("product_id");
		
		
		itemReader.setQueryProvider(factory.getObject());
		itemReader.setRowMapper(new ProductRowMapper());
		itemReader.setPageSize(3);
		
		
		return itemReader;
	}
	
	
	
	@Bean 
	public Step step1() throws Exception {
		return this.stepBiulderFactory.get("chunkBasedStep1").<Product,Product>chunk(3)
				.reader(jdbcPagingItemReader())
				.writer(new ItemWriter<Product>() {

					@Override
					public void write(List<? extends Product > items) throws Exception {
						log.info("Chunk processing started");
						for (Product item : items) {
							log.info(item.toString());
							
						}
					//items.forEach(System.out::println);
				}}).build();		
	}
	
	
	@Bean 
	public Job firstJob() throws Exception {			
		return this.jobBiulderFactory.get("job2")
		.start(step1())
		.build();
	}
	
	
	
}
