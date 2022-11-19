/**
 * 
 */
package com.abbkit.kernel.model;

import java.util.Map;

/**
 * @author J
 *
 */
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNatureName() {
		return natureName;
	}

	public void setNatureName(String natureName) {
		this.natureName = natureName;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public Map getMeta() {
		return meta;
	}

	public void setMeta(Map meta) {
		this.meta = meta;
	}

	public static final SimpleSessionUser DEFAULT_SESSION_USER =new SimpleSessionUser();
	static{
		DEFAULT_SESSION_USER.setId("SYS-ID");
		DEFAULT_SESSION_USER.setNatureName("SYS");
		DEFAULT_SESSION_USER.setUserName("SYS");
	}
	
	
}
