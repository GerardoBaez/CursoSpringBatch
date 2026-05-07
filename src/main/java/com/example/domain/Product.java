package com.example.domain;




import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Product {

	private Integer productId;
	private String name;
	private String productCategory;
	@Max(100000)
	@Min(0)
	private Integer price; 
}
