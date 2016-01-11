package com.cinergix.mapper.objectmapper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.exception.MethodInvocationException;
import com.cinergix.mapper.transformer.ResultTransformerMock;

public class TransformValueTest extends ObjectMapperTestAbstract {
	
	TestableObjectMapper mapper = new TestableObjectMapper();

	@Test
	public void itShouldReturnNullIfTheInputValueIsNull(){
		
		ResultTransformerMock rtMock = new ResultTransformerMock();
		Method method = null;
		try{
			
		method = ResultTransformerMock.class.getMethod( "getFirstName", String.class );
		
		}catch( NoSuchMethodException ex ){
			
			ex.printStackTrace();
			fail( "There should be method called 'getFirstName' in transformer mock class" );
		}
		
		Object transformedValue = mapper.testTransformValue( rtMock, method, null );
		assertNull( "transformValue should return null if the given value is null", transformedValue );
	}
	
	@Test
	public void itShouldReturnManipulatedValueIfAllParametersAreRight(){
		
		ResultTransformerMock rtMock = new ResultTransformerMock();
		Method method = null;
		try{
			
		method = ResultTransformerMock.class.getMethod( "getFirstName", String.class );
		
		}catch( NoSuchMethodException ex ){
			
			ex.printStackTrace();
			fail( "There should be method called 'getFirstName' in transformer mock class" );
		}
		
		Object transformedValue = mapper.testTransformValue( rtMock, method, "Test User" );
		assertEquals( "transformValue should return manipulated value if all input parameters are correct", "Test", transformedValue );
	}
	
	@Test
	public void itShouldReturnManipulatedValueIfAllParametersAreRightAndValueIsDouble(){
		
		ResultTransformerMock rtMock = new ResultTransformerMock();
		Method method = null;
		try{
			
		method = ResultTransformerMock.class.getMethod( "findRoot", double.class );
		
		}catch( NoSuchMethodException ex ){
			
			ex.printStackTrace();
			fail( "There should be method called 'getFirstName' in transformer mock class" );
		}
		
		Object transformedValue = mapper.testTransformValue( rtMock, method, 25 );
		assertEquals( "transformValue should return manipulated value if all input parameters are correct and the value is a number(double)", 5.0, transformedValue );
	}
	
	@Test( expected = MethodInvocationException.class )
	public void itShouldThrowInvocationExceptionIfTheMethodIsPrivate(){
		
		ResultTransformerMock rtMock = new ResultTransformerMock();
		Method method = null;
		try{
			
		method = ResultTransformerMock.class.getMethod( "throwRuntimeException", String.class );
		
		}catch( NoSuchMethodException ex ){
			
			ex.printStackTrace();
			fail( "There should be method called 'getFirstName' in transformer mock class" );
		}
		
		Object transformedValue = mapper.testTransformValue( rtMock, method, "Test User" );
	}
}