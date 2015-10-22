package com.cinergix.mapper.transformer.annotation;

import static org.junit.Assert.*;
import org.junit.Test;

import com.cinergix.mapper.data.UserMock;
import com.cinergix.mapper.transformer.ResultTransformerMock;

public class ResultTransformerClassTest {

	@Test
	public void resultTransformerClassAnnotationShouldBeAbleToMapToAClass(){
		Class userClass = UserMock.class;
		assertNotNull( "ResultTransformerClass should be able to map a Class", userClass.getAnnotation( ResultTransformerClass.class ) );
	}
	
	@Test
	public void valuePropertyOfResultTransformerClassAnnotationShouldReturnCorrectValueSpecifiedInTheAnnotation(){
		Class<UserMock> userClass = UserMock.class;
		assertNotNull( userClass.getAnnotation( ResultTransformerClass.class ) );
		assertEquals( "value property of ResultTransformerClass annotation should return correct value specified in the annotaion", ResultTransformerMock.class, ( userClass.getAnnotation( ResultTransformerClass.class ).value() ) );
	}
}
