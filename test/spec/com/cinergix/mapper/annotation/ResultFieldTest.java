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
	public void valuePropertyOfResultFieldAnnotationShouldReturnCorrectValueSpecifiedInTheAnnotation(){
		Class userClass = UserMock.class;
		Field field = null;
		try{
		
			field = userClass.getDeclaredField( "name" );
		}catch ( NoSuchFieldException e ){
			e.printStackTrace();
		}
		assertNotNull( "value Property Of ResultField Annotation Should Return Correct Value Specified In The Annotation", field.getAnnotation( ResultField.class ) );
		assertEquals( "value property of ResultField annotation should return correct value specified in the annotaion", "user_name", field.getAnnotation( ResultField.class ).value()[0] );
	}
	
	@Test
	public void valuePropertyOfResultFieldAnnotationShouldReturnAnArrayOfCorrectValueSpecifiedInTheAnnotation(){
		Class userClass = UserMock.class;
		Field field = null;
		try{
		
			field = userClass.getDeclaredField( "email" );
		}catch ( NoSuchFieldException e ){
			e.printStackTrace();
		}
		
		String[] nameAnnotationValue = { "user_email", "user_office_mail", "user_personal_mail" };
		assertNotNull( "value Property Of ResultField Annotation Should Return Correct Value Specified In The Annotation", field.getAnnotation( ResultField.class ) );
		assertArrayEquals( "value property of ResultField annotation should return correct value specified in the annotaion", nameAnnotationValue, field.getAnnotation( ResultField.class ).value() );
	}
}
