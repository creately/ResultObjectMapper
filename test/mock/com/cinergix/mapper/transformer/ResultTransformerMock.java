package com.cinergix.mapper.transformer;

public class ResultTransformerMock implements IResultTransformer {

	public String getFirstName( String name ){
		return name.split( " " )[0];
	}
}
