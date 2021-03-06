package com.java_academy.logic.json_model;

public class Message implements JsonMessage {
	
	private String dataType;
	private String message;
	
	public Message(String dataType, String message) {
		this.dataType = dataType;
		this.message = message;
	}

	public String getDataType() {
		return dataType;
	}

	public String getMessage() {
		return message;
	}
}
