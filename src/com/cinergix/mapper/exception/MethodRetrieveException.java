package com.cinergix.mapper.exception;

public class MethodRetrieveException extends RuntimeException {

	public MethodRetrieveException(){
		super();
	}
	
	public MethodRetrieveException( String message ){
		super( message );
	}
	
	public MethodRetrieveException( Throwable cause ){
		super( cause );
	}
	
	public MethodRetrieveException( String message, Throwable cause ){
		super( message, cause );
	}
}
