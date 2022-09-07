package com.cinergix.mapper.exception;

public class MethodInvocationException extends RuntimeException {

	public MethodInvocationException(){
		super();
	}
	
	public MethodInvocationException( String message ){
		super( message );
	}
	
	public MethodInvocationException( Throwable cause ){
		super( cause );
	}
	
	public MethodInvocationException( String message, Throwable cause ){
		super( message, cause );
	}
}
