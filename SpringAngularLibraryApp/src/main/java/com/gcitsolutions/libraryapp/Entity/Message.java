package com.gcitsolutions.libraryapp.Entity;

public class Message {

	private boolean success;
	private String message;
	private Integer pageNo;
	
	public Message(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public Message() {
		// TODO Auto-generated constructor stub
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

}
