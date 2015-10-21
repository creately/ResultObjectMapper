package com.cinergix.mapper.transformer.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.data.UserMock;

public class ResultTransformerTest {

	@Test
	public void resultTransformerShouldBeAbleToMapToAField(){
		Class userClass = UserMock.class;
		Field field = null;
		try{
		
			field = userClass.getDeclaredField( "name" );
		}catch ( NoSuchFieldException e ){
			e.printStackTrace();
		}
		assertNotNull( "ResultaTransformer should be able to map to a filed", field.getAnnotation( ResultTransformer.class ) );
	}
	
	@Test
	public void methodNamePropertyOfResultTransformerAnnotationShouldReturnCorrectValueSpecifiedInTheAnnotation(){
		Class userClass = UserMock.class;
		Field field = null;
		try{
		
			field = userClass.getDeclaredField( "name" );
		}catch ( NoSuchFieldException e ){
			e.printStackTrace();
		}
		assertNotNull( field.getAnnotation( ResultTransformer.class ) );
		assertEquals( "Method name property of ResultTransformer annotation should return correct value specified in the annotaion", "getFirstName", field.getAnnotation( ResultTransformer.class ).methodName() );
	}
}
