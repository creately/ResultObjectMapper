package com.cinergix.mapper.objectmapper;

import java.lang.reflect.Method;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.exception.MethodRetrieveException;
import com.cinergix.mapper.transformer.ResultTransformerMock;

public class GetMethodByNameTest extends ObjectMapperTestAbstract {

	TestableObjectMapper mapper = new TestableObjectMapper();
	
	@Test( expected = MethodRetrieveException.class )
	public void itShouldThrowExceptionIfThereIsNoMethodFoundInSourceClassByGivenName(){
		mapper.testGetMethodByName( ResultTransformerMock.class, "getInitial" );
	}
	
	@Test( expected = MethodRetrieveException.class )
	public void itShouldThrowExceptionIfThereIsNoMethodFoundWithOneParameterInSourceClassByGivenName(){
		mapper.testGetMethodByName( ResultTransformerMock.class, "getOneChar" );
	}
	
	@Test( expected = MethodRetrieveException.class )
	public void itShouldThrowExceptionIfThereIsAPrivateMethodFoundWithOneParameterInSourceClassByGivenName(){
		mapper.testGetMethodByName( ResultTransformerMock.class, "getTwoChar" );
	}
	
	@Test( expected = MethodRetrieveException.class )
	public void itShouldThrowExceptionIfThereIsAProtectedMethodFoundWithOneParameterInSourceClassByGivenName(){
		mapper.testGetMethodByName( ResultTransformerMock.class, "getThreeChar" );
	}
	
	@Test
	public void itShouldReturnAPublicMethodFoundWithOneParameterInSourceClassByGivenName(){
		
		Method m = null;
		try{
			m = ResultTransformerMock.class.getMethod( "getFirstName", String.class );
		}catch( NoSuchMethodException ex ){
			ex.printStackTrace();
			fail( "Can not get the method with given name" );
		}
		Method method = mapper.testGetMethodByName( ResultTransformerMock.class, "getFirstName" );
		assertEquals("getMethodByName should return the method if given source class has a public method with given name and accepts only one parameter", m, method);
	}
	
	@Test
	public void itShouldReturnAPublicStaticMethodFoundWithOneParameterInSourceClassByGivenName(){
		
		Method m = null;
		try{
			m = ResultTransformerMock.class.getMethod( "findRoot", double.class );
		}catch( NoSuchMethodException ex ){
			ex.printStackTrace();
			fail( "Can not get the method with given name" );
		}
		Method method = mapper.testGetMethodByName( ResultTransformerMock.class, "findRoot" );
		assertEquals("getMethodByName should return the method if given source class has a public static method with given name and accepts only one parameter", m, method);
	}
	
	@Test( expected = MethodRetrieveException.class )
	public void itShouldThrowMethodRetrieveExceptionIfTheMethodNameIsNull(){
		
		Method method = mapper.testGetMethodByName( ResultTransformerMock.class, null );
	}
	
	@Test( expected = MethodRetrieveException.class )
	public void itShouldThrowMethodRetrieveExceptionIfMethodNameIsAnEmptyString(){
		
		Method method = mapper.testGetMethodByName( ResultTransformerMock.class, "" );
	}
	
	@Test( expected = MethodRetrieveException.class )
	public void itShouldThrowMethodRetrieveExceptionIfTrimedMethodNameIsAnEmptyString(){
		
		Method method = mapper.testGetMethodByName( ResultTransformerMock.class, "   " );
	}
}
