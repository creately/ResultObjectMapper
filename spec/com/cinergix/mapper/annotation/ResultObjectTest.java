package com.cinergix.mapper.annotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.reflect.Field;

import org.junit.Test;

import com.cinergix.mapper.data.UserMock;

public class ResultObjectTest {

	@Test
	public void resultFieldAnnotationShouldBeAbleToMapToAClass(){
		Class userClass = UserMock.class;
		Field field = null;
		try{
		
			field = userClass.getDeclaredField( "manager" );
		}catch ( NoSuchFieldException e ){
			e.printStackTrace();
		}
		assertNotNull( field.getAnnotation( ResultObject.class ) );
	}
	
}
