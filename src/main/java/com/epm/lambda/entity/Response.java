package com.epm.lambda.entity;

public class Response {

	private String message;
	private SNSMessage request;

	public Response(String message, SNSMessage request) {
		this.message = message;
		this.request = request;
	}

	public Response() {
	}

	public String getMessage() {
		return this.message;
	}

	public SNSMessage getRequest() {
		return this.request;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setRequest(SNSMessage request) {
		this.request = request;
	}

}
