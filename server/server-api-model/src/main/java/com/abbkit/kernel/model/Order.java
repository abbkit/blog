package com.abbkit.kernel.model;

import lombok.Data;

@Data
public class Order implements Model {


	/**
	 * sort column 
	 */
	private String column;
	
	/**
	 * sort type .  desc/asc
	 */
	private String type;

}
