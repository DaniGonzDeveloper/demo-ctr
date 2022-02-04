package com.demo.exceptions;

public class NotFoundInRedisException extends Exception{
	
	public NotFoundInRedisException(String msg) {
		super(msg);
	}
	
}
