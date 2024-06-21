package com.banking.system.response;

import lombok.Data;

public @Data class ResponseEntityObject<T> {
	private Boolean status;
	private String message;
	private T object;
	private Long totalItems;
	
	public ResponseEntityObject() {
	}

	
	public ResponseEntityObject( boolean status, String message, T object, Long totalItems) {
		super();
		this.status = status;
		this.message = message;
		this.object = object;
		this.totalItems = totalItems;
	}
	
	public ResponseEntityObject(boolean status, String message, Long totalItems) {
		super();
		this.status = status;
		this.message = message;
		this.totalItems = totalItems;
	}
	
	public ResponseEntityObject( boolean status, String message, T object) {
		super();
		this.status = status;
		this.message = message;
		this.object = object;
	
		
	}

	public ResponseEntityObject( boolean status, String message) {
		super();
		this.status = status;
		this.message = message;
		
		
	}


	
}