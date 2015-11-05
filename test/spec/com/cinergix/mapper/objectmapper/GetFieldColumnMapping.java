package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.Test;

import com.cinergix.mapper.ObjectMapper;
import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.SimpleUserMock;
import com.cinergix.mapper.data.UserMock;

public class GetFieldColumnMapping extends ObjectMapperTestAbstract {
	
	private HashMap<Field, String> invokeCheckColumnLabelExist( Class dataClass, ResultSet result ){
		try{
			
			
			return ( new ObjectMapper(){
						public HashMap<Field, String> testGetFieldColumnMapping( Class dataClass, ResultSet result ) throws SQLException{
							return this.getFieldColumnMapping( dataClass, result );
						}
					} ).testGetFieldColumnMapping( dataClass, result );
			
		}catch( SQLException e ){
			e.printStackTrace();
			fail();
		}
		return null;
	}
	
	private Field getField( Class type, String fieldName ){
		try{
			
			return type.getDeclaredField( fieldName );
			
		}catch( NoSuchFieldException e ){
			e.printStackTrace();
			fail();
		}
		return null;
	}
	
	/****************************************************************************************
	 * Test getFieldColumnMapping method													*
	 ****************************************************************************************/
	@Test
	public void getFieldColumnMappingShouldReturnMappedSetOfFieldVsColumnNameFromTheAnnotationInHashMap(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleUserMock.class, result );
		
		assertTrue("getFieldColumnMapping should return a mapped set of field Vs Column names( which are in the annotation )", ( "user_name" ).equals( fieldsVsColumns.get( getField( SimpleUserMock.class, "name" ) ) ) );
			
	}
	
	@Test
	public void getFieldColumnMappingShouldReturnMappedSetOfFieldVsColumnNameFromTheAnnotationOnlyForAnnotatedFieldsInHashMap(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleUserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should return a mapped set of field Vs Column names( which are in the annotation )", 1, fieldsVsColumns.size() );
		assertTrue( "getFieldColumnMapping should return a mapped set of field Vs Column names( which are in the annotation )", ( "user_name" ).equals( fieldsVsColumns.get( getField( SimpleUserMock.class, "name" ) ) ) );
			
	}
	
	@Test
	public void getFieldColumnMappingShouldReturnNullIfGivenResultSetIsNull(){
			
			HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleUserMock.class, null );
			
			assertNull( "getFieldColumnMapping should return null if given ResultSet is null", fieldsVsColumns );
			
	}
	
	@Test
	public void getFieldColumnMappingShouldReturnNullIfGivenClassTypeIsNull(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( null, result );
		
		assertNull( "getFieldColumnMapping should return null if given class type is null", fieldsVsColumns );
			
	}
	
	@Test
	public void getFieldColumnMappingShouldReturnNullIfBothInputPatametersAreNull(){
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( null, null );
		
		assertNull( "getFieldColumnMapping should return null if both input parameters are null", fieldsVsColumns );
			
	}
	
	@Test
	public void getFieldColumnMappingShouldReturnAHashMapOfAllTheAnnotatedFields(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( UserMock.class, result );
		
		assertTrue("getFieldColumnMapping should return All the annotated fields with its mapped colum name", ( "user_id" ).equals( fieldsVsColumns.get( getField( UserMock.class, "id" ) ) ) );
		assertTrue("getFieldColumnMapping should return All the annotated fields with its mapped colum name", ( "user_email" ).equals( fieldsVsColumns.get( getField( UserMock.class, "email" ) ) ) );
		
	}
	
	@Test
	public void getFieldColumnMappingShouldReturnEmptyMapIfThereIsNoMappingColumnLabelFoundInGivenResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( UserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should return empty map if there is no mapping column is found in given result set", 0, fieldsVsColumns.size() );
		
	}

}
