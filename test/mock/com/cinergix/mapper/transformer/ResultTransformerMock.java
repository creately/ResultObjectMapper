package com.cinergix.mapper.transformer;

public class ResultTransformerMock implements IResultTransformer {

	public String getFirstName( String name ){
		return name.split( " " )[0];
	}
	
	public String getOneChar( String string, int index ){
		return string.substring( index, index + 1 );
	}
	
	private String getTwoChar( String string ){
		return string.substring( 0, 2 );
	}
	
	protected String getThreeChar( String string ){
		return string.substring( 0, 2 );
	}
	
	public static double findRoot( double num ){
		return Math.sqrt( num );
	}
	
	public String throwRuntimeException( String s ){
		throw new RuntimeException( "This is a mock exception." );
	}
}
