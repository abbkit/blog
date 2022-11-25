package com.abbkit.project.model;

public enum ResponseStatus {

	SUCCESS(0,"SUCCESS"),
	FAIL(-1,"FAIL"),
	MESSAGE(1,"MESSAGE")
;
	private int code ;

	private String desc;

	ResponseStatus(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
