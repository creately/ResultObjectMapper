package com.cinergix.mapper.annotation;

import java.lang.reflect.Field;

import org.junit.Test;
import static org.junit.Assert.*;

import com.cinergix.mapper.data.UserMock;

public class ResultFieldTest {

	@Test
	public void resultFieldAnnotationShouldBeAbleToMapToAClass(){
		Class userClass = UserMock.class;
		Field field = null;
		try{
		
			field = userClass.getDeclaredField( "name" );
		}catch ( NoSuchFieldException e ){
			e.printStackTrace();
		}
		assertNotNull( field.getAnnotation( ResultField.class ) );
	}
	
	@Test
	public void fieldNamePropertyOfResultFieldAnnotationShouldReturnCorrectValueSpecifiedInTheAnnotation(){
		Class userClass = UserMock.class;
		Field field = null;
		try{
		
			field = userClass.getDeclaredField( "name" );
		}catch ( NoSuchFieldException e ){
			e.printStackTrace();
		}
		assertNotNull( field.getAnnotation( ResultField.class ) );
		assertEquals( "fieldName property of ResultField annotation should return correct value specified in the annotaion", "user_name", field.getAnnotation( ResultField.class ).fieldName() );
	}
}
