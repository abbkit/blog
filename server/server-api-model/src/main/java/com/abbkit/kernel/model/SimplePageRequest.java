package com.abbkit.kernel.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class SimplePageRequest implements Pageable {

	private int pageNumber;
	
	private int pageSize=10;
	
	private List<Order> orders=new ArrayList<Order>();
	
	public SimplePageRequest(int pageNumber, int pageSize) {
		this.pageNumber=pageNumber;
		this.pageSize=pageSize;
	}

	@Override
	public int pageNumber() {
		return getPageNumber();
	}

	@Override
	public int pageSize() {
		return getPageSize();
	}

	@Override
	public List<Order> orders() {
		return this.orders;
	}

	public void addOrder(String column,String ascDesc){
		Order order=new Order();
		order.setColumn(column);
		order.setType(ascDesc);
		this.orders.add(order);
	}
	
}
