/**
 * 
 */
package com.abbkit.project.model;

import java.util.List;

/**
 * @author J
 */
public interface Pageable extends Model {

	/**
	 * Returns the page to be returned.
	 * 
	 * @return the page to be returned.
	 */
	int pageNumber();

	/**
	 * Returns the number of items to be returned.
	 * 
	 * @return the number of items of that page
	 */
	int pageSize();
	
	/**
	 * Returns the sorting parameters.
	 * @return 
	 */
	List<Order> orders();
	
}
