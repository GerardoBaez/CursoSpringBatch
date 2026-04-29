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
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.validator.BeanValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.domain.FilterProductItemProcessor;
import com.example.domain.OSProduct;
import com.example.domain.Product;
import com.example.domain.ProductRowMapper;
import com.example.listener.MyChunkListener;
import com.example.listener.MyItemProcessListener;
import com.example.listener.MyItemReadListener;
import com.example.listener.MyItemWriteListener;
import com.example.proccessor.TransformProductItemProcessor;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BatchConfiguration {

	
	
	@Autowired
	public DataSource dataSource;

	@Bean
	public ItemProcessor<Product, OSProduct> transformProductItemProcessor(){
		return new TransformProductItemProcessor();
	}
	
	
	@Bean 
	public MyChunkListener mychunkListener() {
		return new MyChunkListener();
	}
	
	
	@Bean
	public MyItemProcessListener myprocesslistener() {
		return new MyItemProcessListener();
	}
	
	@Bean
	public MyItemReadListener myreadlistener() {
		return new MyItemReadListener();
	}
	
	@Bean
	public MyItemWriteListener mywritelistener() {
		return new MyItemWriteListener();
	}
	
	@Bean
	public JdbcBatchItemWriter<OSProduct> jdbcBatchItemWriter(){
		JdbcBatchItemWriter<OSProduct> itemWriter= new JdbcBatchItemWriter<OSProduct>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("insert into OS_PRODUCT_DETAILS values (:productId,:name,:productCategory,:price,:taxPercent,:sku,:shippingRate)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		
	
		return itemWriter;
	}
	
	
	/**
	@Bean
	public JdbcBatchItemWriter<Product> jdbcBatchItemWriter(){
		JdbcBatchItemWriter<Product> itemWriter= new JdbcBatchItemWriter<Product>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql("insert into PRODUCT_DETAILS_OUTPUT values (:productId,:name,:productCategory,:price)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider());
		
		//itemWriter.setSql("insert into product_details_output values (?,?,?,?)");
		//itemWriter.setItemPreparedStatementSetter(new ProductItemPreparedStatementSetter());
		return itemWriter;
	}
	**/
	
	@Bean
	public ItemWriter<Product> flatFileItemWriter(){
		FlatFileItemWriter<Product> itemWriter = new FlatFileItemWriter<>();
		itemWriter.setResource(new FileSystemResource("output/Product_Details_Output.csv"));
		
		DelimitedLineAggregator<Product> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(",");
		
		BeanWrapperFieldExtractor<Product> fieldExtractor = new BeanWrapperFieldExtractor<>();
		fieldExtractor.setNames(new String[] {"productId", "name", "productCategory", "price"});
		
		lineAggregator.setFieldExtractor(fieldExtractor);
		itemWriter.setLineAggregator(lineAggregator);
		
		return itemWriter;
	}
	
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
	
	/**
	@Bean
	public ValidatingItemProcessor<Product> validateProductItemProcessor(){
		ValidatingItemProcessor<Product> validatingProductItemProcessor = new ValidatingItemProcessor<>(new ProductValidator());
		//validatingProductItemProcessor.setFilter(true);
		return validatingProductItemProcessor;
	}
	*/
	
	@Bean 
	public CompositeItemProcessor<Product, OSProduct>itemProcessor(){
		CompositeItemProcessor<Product, OSProduct> itemProcessor = new CompositeItemProcessor();
		List itemProcessors = new ArrayList();
		itemProcessors.add(validateProductItemProcessor());
		itemProcessors.add(filterProductItemProcessor());
		itemProcessors.add(transformProductItemProcessor());
		itemProcessor.setDelegates(itemProcessors);
		return itemProcessor;
	}
	
	@Bean
	public BeanValidatingItemProcessor<Product> validateProductItemProcessor(){
		BeanValidatingItemProcessor<Product> beanValidatingProductItemProcessor = new BeanValidatingItemProcessor<>();
		beanValidatingProductItemProcessor.setFilter(true);
		//validatingProductItemProcessor.setFilter(true);
		return beanValidatingProductItemProcessor;
	}
	
	
	@Bean 
	public Step step1(JobRepository jobrepo, PlatformTransactionManager tx) throws Exception {
		return new StepBuilder("chunkBasedStep1",jobrepo).<Product,OSProduct>chunk(3,tx)
				.listener(mychunkListener())
				.listener(myprocesslistener())
				.listener(myreadlistener())
				.listener(mywritelistener())
				.reader(jdbcPagingItemReader())
				.processor(itemProcessor())
				.writer(jdbcBatchItemWriter()).build();
		
	}
	
	
	@Bean 
	public Job firstJob(JobRepository jobrepo, Step step1 ) throws Exception {			
		return new JobBuilder("job2",jobrepo)
		.start(step1)
		.build();
	}
	
	
	@Bean 
	public ItemProcessor<Product, Product>filterProductItemProcessor(){
		return new FilterProductItemProcessor();
	}
	
	
	
}
