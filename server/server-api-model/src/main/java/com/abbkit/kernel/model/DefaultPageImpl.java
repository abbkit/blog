/**
 * 
 */
package com.abbkit.kernel.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author J
 */
@Data
public class DefaultPageImpl<T> implements Page<T> {
	
	private long totalRecordNumber=Long.MAX_VALUE;

	private int totalPageNumber;
	
	private Pageable pageable;
	
	private List<T> data=new ArrayList<>();

	public long totalRecordNumber() {
		return totalRecordNumber;
	}

	public int totalPageNumber() {
		return totalPageNumber;
	}

	public Pageable pageable() {
		return pageable;
	}

	public List<T> data(){
		return Collections.unmodifiableList(data);
	}

	@Override
	public void map(Function<T, ?> function) {
		List target=data.stream().map(function).collect(Collectors.toList());
		this.data=target;
	}


}
