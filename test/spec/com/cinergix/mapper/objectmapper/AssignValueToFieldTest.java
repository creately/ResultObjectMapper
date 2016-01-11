package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;

import com.cinergix.mapper.ObjectMapper;
import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.FieldTestUserMock;
import com.cinergix.mapper.data.UserMock;
import com.cinergix.mapper.exception.PropertyAccessException;

public class AssignValueToFieldTest extends ObjectMapperTestAbstract {
	
	private TestableObjectMapper mapper = new TestableObjectMapper();

	/****************************************************************************************
	 * Test assignValueToField method														*
	 ****************************************************************************************/
	@Test
	public void itShouldAssignGivenValueToField(){
		
		UserMock user = new UserMock();
		Field field = this.getField( UserMock.class, "name" );
		
		mapper.testAssignValueToField( user, field, "Test User" );
		
		assertTrue( "assignValueToField should assign the value to given object's field", ( "Test User" ).equals( user.getName() ) );
		
	}
	
	@Test
	public void itFieldShouldAssignIntValueToFieldIfFieldtypeIsInt(){
		
		UserMock user = new UserMock();
		Field field = this.getField( UserMock.class, "age" );
		
		mapper.testAssignValueToField( user, field, 20 );
		assertEquals( "assignValueToField should assign given integer value to the field", 20, user.getAge() );
		
		mapper.testAssignValueToField( user, field, 25 );
		assertEquals( "assignValueToField should assign given integer value to the field", 25, user.getAge() );
		
	}
	
	
	@Test( expected = PropertyAccessException.class )
	public void itShouldThrowPropertyAccessExceptionIfAssignValueIsIncompatableType(){
		
		UserMock user = new UserMock();
		Field field = this.getField( UserMock.class, "age" );
		
		mapper.testAssignValueToField( user, field, "Data" );
		
	}
	
	@Test
	public void itShouldAssignValueToPrivateProperty(){
		
		FieldTestUserMock user = new FieldTestUserMock();
		Field field = this.getField( FieldTestUserMock.class, "name" );
		
		mapper.testAssignValueToField( user, field, "Test User" );
		
		assertTrue( "assignValueToField should assign the value to private property of given object", ( "Test User" ).equals( user.getName() ) );
		
	}
	
	@Test
	public void itShouldAssignValueToPublicProperty(){
		
		FieldTestUserMock user = new FieldTestUserMock();
		Field field = this.getField( FieldTestUserMock.class, "firstName" );
		
		mapper.testAssignValueToField( user, field, "Test User" );
		
		assertTrue( "assignValueToField should assign the value to public property of given object", ( "Test User" ).equals( user.getFirstName() ) );
		
	}
	
	@Test
	public void itShouldAssignValueToProtectedProperty(){
		
		FieldTestUserMock user = new FieldTestUserMock();
		Field field = this.getField( FieldTestUserMock.class, "lastName" );
		
		mapper.testAssignValueToField( user, field, "Test User" );
		
		assertTrue( "assignValueToField should assign the value to protected property of given object", ( "Test User" ).equals( user.getLastName() ) );
		
	}
	
	@Test
	public void itShouldAssignValueToStaticProperty(){
		
		FieldTestUserMock user = new FieldTestUserMock();
		Field field = this.getField( FieldTestUserMock.class, "email" );
		
		mapper.testAssignValueToField( user, field, "test@cinergix.com" );
		
		assertTrue( "assignValueToField should assign the value to protected property of given object", ( "test@cinergix.com" ).equals( user.getEmail() ) );
		
	}
	
	@Test( expected = PropertyAccessException.class )
	public void itShouldThrowExceptionWhenAssignValueToFinalProperty(){
		
		FieldTestUserMock user = new FieldTestUserMock();
		Field field = this.getField( FieldTestUserMock.class, "personalMail" );
		
		mapper.testAssignValueToField( user, field, "test@cinergix.com" );
		
	}
	
	@Test( expected = PropertyAccessException.class )
	public void itShouldThrowExceptionWhenAssignValueToStaticFinalProperty(){
		
		FieldTestUserMock user = new FieldTestUserMock();
		Field field = this.getField( FieldTestUserMock.class, "officeMail" );
		
		mapper.testAssignValueToField( user, field, "test@cinergix.com" );
		
	}
	
	@Test( expected = PropertyAccessException.class )
	public void itShouldThrowExceptionWhenCreatedObjectIsNotAnInstanceOfFieldDeclairedClass(){
		
		UserMock user = new UserMock();
		Field field = this.getField( FieldTestUserMock.class, "officeMail" );
		
		mapper.testAssignValueToField( user, field, "test@cinergix.com" );
		
	}
	
	@Test
	public void itShouldNotThrowNullPointerExceptionWhenGivenCreatedObjectIsNull(){
		
		Field field = this.getField( FieldTestUserMock.class, "firstName" );
		
		try{
			
			mapper.testAssignValueToField( null, field, "Test User" );
			
		}catch( NullPointerException e ){
			fail( e.getMessage() );
		}
		
	}
	
	@Test
	public void itShouldNotThrowNullPointerExceptionWhenGivenFieldIsNull(){
		
		FieldTestUserMock user = new FieldTestUserMock();
		
		try{
			
			mapper.testAssignValueToField( user, null, "Test User" );
			
		}catch( NullPointerException e ){
			fail( e.getMessage() );
		}
		
	}
}
