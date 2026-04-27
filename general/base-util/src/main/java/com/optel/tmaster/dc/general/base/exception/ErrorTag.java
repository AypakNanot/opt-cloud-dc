/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception;

/**
 * ClassName: ErrorTag
 * <ul>
 * <li>(errorTag  --  状态码)</li>
 * </ul>
 * @author LWX 2019年9月24日上午10:46:49
 */
public enum ErrorTag {
	
	/** 错误枚举定义 */
	IN_USE(1, 409, "in-use"),
	INVALID_VALUE(2, 400, "invalid-value"),
	TOO_BIG(3, 413, "too-big"),
	MISSING_ATTRIBUTE(4, 400,"missing-attribute"),
	BAD_ATTRIBUTE(5, 400,"bad-attribute"),
	UNKNOWN_ATTRIBUTE(6, 400,"unknown-attribute"),
	BAD_ELEMENT(7, 400,"bad-element"),
	UNKNOWN_ELEMENT(8, 400,"unknown-element"),
	UNKNOWN_NAMESPACE(9, 400,"unknown-namespace"),
	ACCESS_DENIED(10, 403,"bad-element"),
	LOCK_DENIED(11, 409, "lock-denied"),
	RESOURCE_DENIED(12, 409, "resource-denied"),
	ROLLBACK_FAILED(13, 500, "rollback-failed"),
	DATA_EXISTS(14, 409, "data-exists"),
	DATA_MISSING(15, 409, "data-missing"),
	OPERATION_NOT_SUPPORTED(16, 501, "operation-not-supported"),
	OPERATION_FAILED(17, 500, "operation-failed"),
	PARTIAL_OPERATION(18, 500, "partial-operation"),
	MALFORMED_MESSAGE(19, 400, "malformed-message");
	
	
	private int value;
	/**
	 * 状态码
	 */
	private int statusCode;
	private String errorTag;
	
	private ErrorTag(int value, int statusCode, String errorTag){
		this.value = value;
		this.statusCode = statusCode;
		this.errorTag = errorTag;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getErrorTag() {
		return errorTag;
	}

	public void setErrorTag(String errorTag) {
		this.errorTag = errorTag;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
	

}
