package com.ll.vbc.enums;

public enum ReturnCode {
	SUCCESS             (0, "Records retrieved successfully"),
	FAILURE             (1, "Exception occurred"),
	NO_ROWS_FOUND       (100, "No records found"),
	UNKNOWN_ERROR       (500, "Unkown error found"),
	INVALID_PARAMETERS  (501, "Invalid paramater passed"),
	UNSUPPORTED_VERSION (502, "Unsupported version");

	private final int code;
	private String message;

	private ReturnCode(int code, String message) {
		this.code = code;
		this.message = message;
	}


	public int getCode() {
		return code;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public static ReturnCode fromCode(int code) {
		for(ReturnCode returnCode : values()) {
			if(returnCode.code == code) {
				return returnCode;
			}
		}
		return null;
	}
}
