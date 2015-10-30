package com.cinergix.mapper.exception;

public class PropertyAccessException extends RuntimeException {

	public PropertyAccessException(){
		super();
	}
	
	public PropertyAccessException( String message ){
		super( message );
	}
	
	public PropertyAccessException( Throwable cause ){
		super( cause );
	}
	
	public PropertyAccessException( String message, Throwable cause ){
		super( message, cause );
	}
	
}
