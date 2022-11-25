package com.abbkit.project.model;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface Page<T>  extends Model {

	Pageable pageable();

	/**
	 * 返回不可以修改的LIST
	 * @return
	 */
	List<T> data();
	
	int totalPageNumber();
	
	long totalRecordNumber();

	/**
	 * 替换分页对象里面的数据（LIST项）。Function输入为原始LIST项，输出为新的list项
	 * @param function
	 */
	void map(Function<T,?> function);

	default void forEach(Consumer<T> consumer){data().stream().forEach(consumer);}

	default boolean isEmpty(){return data().isEmpty();}
}
