/**
 * 
 */
package com.abbkit.kernel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author J
 */
public class DefaultPageImpl<T> implements Page<T> {
	
	private long totalRecordNumber=Long.MAX_VALUE;

	private int totalPageNumber;
	
	private Pageable pageable;
	
	private List<T> data=new ArrayList<>();

	public long totalRecordNumber() {
		return totalRecordNumber;
	}


	public void setTotalRecordNumber(long totalRecordNumber) {
		this.totalRecordNumber = totalRecordNumber;
	}



	public int totalPageNumber() {
		return totalPageNumber;
	}


	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}


	public Pageable pageable() {
		return pageable;
	}


	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}

	public List<T> data(){
		return Collections.unmodifiableList(data);
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public long getTotalRecordNumber() {
		return totalRecordNumber;
	}

	public int getTotalPageNumber() {
		return totalPageNumber;
	}

	public Pageable getPageable() {
		return pageable;
	}

	public List<T> getData() {
		return data;
	}

	@Override
	public void map(Function<T, ?> function) {
		List target=data.stream().map(function).collect(Collectors.toList());
		this.data=target;
	}


}
