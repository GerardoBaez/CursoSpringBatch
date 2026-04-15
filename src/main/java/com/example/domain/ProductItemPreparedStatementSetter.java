package com.example.domain;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class ProductItemPreparedStatementSetter implements ItemPreparedStatementSetter<Product> {

	@Override
	public void setValues(Product item, PreparedStatement ps) throws SQLException {
		ps.setInt(1, item.getProductId());
		ps.setString(2, item.getName());
		ps.setString(3, item.getProductCategory());
		ps.setInt(4, item.getPrice());
	}

}
