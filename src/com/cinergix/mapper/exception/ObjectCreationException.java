package com.cinergix.mapper.exception;

public class ObjectCreationException extends RuntimeException {

	public ObjectCreationException(){
		super();
	}
	
	public ObjectCreationException( String message ){
		super( message );
	}
	
	public ObjectCreationException( Throwable cause ){
		super( cause );
	}
	
	public ObjectCreationException( String message, Throwable cause ){
		super( message, cause );
	}
}
