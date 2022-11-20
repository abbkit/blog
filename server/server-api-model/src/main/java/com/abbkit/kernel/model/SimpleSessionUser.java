/**
 * 
 */
package com.abbkit.kernel.model;

import lombok.Data;

import java.util.Map;

/**
 * @author J
 *
 */
@Data
public class SimpleSessionUser implements SessionUser {

	/**
	 * the primary key of the login user , uuid
	 */
	private String id;
	
	/**
	 * username
	 */
	private String userName;

	/**
	 * username to be display
	 */
	private String natureName;

	private String password;

	private Map meta;

	public static final SimpleSessionUser DEFAULT_SESSION_USER =new SimpleSessionUser();
	static{
		DEFAULT_SESSION_USER.setId("SYS-ID");
		DEFAULT_SESSION_USER.setNatureName("SYS");
		DEFAULT_SESSION_USER.setUserName("SYS");
	}
	
	
}
