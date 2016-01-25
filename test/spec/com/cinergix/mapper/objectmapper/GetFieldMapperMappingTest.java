package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.cinergix.mapper.ObjectMapper;
import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.InnerUserMock;
import com.cinergix.mapper.data.ProjectManagerMock;
import com.cinergix.mapper.data.UserMock;

public class GetFieldMapperMappingTest extends ObjectMapperTestAbstract {

	@Test
	public void itShouldReturnNullIfTheGivenClassIsNull(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		TestableObjectMapper mapper = new TestableObjectMapper();
		HashMap<Field, ObjectMapper> fieldMapperMap = mapper.testGetFieldMapperMapping( null, result );
		
		assertNull("mapResultSetToObject Should Return Null If the given class Is Null", fieldMapperMap );
	}
	
	@Test
	public void itShouldReturnHashMapWithFieldMapperObjectMapping(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, 'Test Manager' as manager_name FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		HashMap<Field, ObjectMapper> fieldMapperMap = mapper.testGetFieldMapperMapping( UserMock.class, result );
		
		Field field = getField( UserMock.class, "manager" );
		
		assertTrue( "getFieldMapperMapping should return field vs Mapper Object mapping for a given correct mapped class", ( fieldMapperMap.get( field ) instanceof ObjectMapper ) );
	}
	
	@Test
	public void itShouldReturnAHashMapWithOutFieldMapperObjectMappingIfTheInnerObjectDoesNotHaveAnyFieldColumnMapping(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<InnerUserMock> mapper = new TestableObjectMapper<InnerUserMock>();
		HashMap<Field, ObjectMapper> fieldMapperMap = mapper.testGetFieldMapperMapping( InnerUserMock.class, result );
		assertNull( "getFieldMapperMapping should return a hashmap without field mapper-object mapping if the inner object does not have any field column mapping", fieldMapperMap );
		
	}
	
	@Test
	public void itShouldReturnAHashMapWithSecondDepthOfFieldMapperObjectMapping(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, 'ManagerName' as manager_name, 'PAName' as pa_name FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<InnerUserMock> mapper = new TestableObjectMapper<InnerUserMock>();
		HashMap<Field, ObjectMapper> fieldMapperMap = mapper.testGetFieldMapperMapping( InnerUserMock.class, result );
		Field field = getField( InnerUserMock.class, "manager" );
		ObjectMapper innerOM = fieldMapperMap.get( field );
		Field mapperField = getField( ObjectMapper.class, "fieldMapperMap" );
		
		HashMap< Field, ObjectMapper > innerMap = null;
		
		try{
			
			mapperField.setAccessible( true );
			innerMap = ( HashMap< Field, ObjectMapper > )( mapperField.get( innerOM ) );
			mapperField.setAccessible( false );
			
		}catch( IllegalAccessException ex ){
			ex.printStackTrace();
			fail();
		}
		
		Field personalAssistantField = getField( ProjectManagerMock.class, "personalAssistant" );
		
		assertTrue( "getFieldMapperMapping should return field vs Mapper Object mapping for a given correct mapped class and the inner ObjectMapper should have FieldColumnMapper( Second level mapping)", ( innerMap.get( personalAssistantField ) instanceof ObjectMapper ) );
	}
	
	@Test
	public void itShouldReturnAHashMapWithOutSecondDepthOfFieldMapperObjectMappingIfThereIsNoFieldMappingExistInInnerObject(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, 'ManagerName' as manager_name FROM user WHERE id LIKE 'testID'" );
		
		Field usedDataClassesField = getField( ObjectMapper.class, "usedDataClasses" );
		HashSet<Class> usedDataClasses = new HashSet<Class>();
		usedDataClasses.add( InnerUserMock.class );
		
		TestableObjectMapper<InnerUserMock> mapper = new TestableObjectMapper<InnerUserMock>();
		
		try{
			
			usedDataClassesField.setAccessible( true );
			usedDataClassesField.set( mapper, usedDataClasses );
			usedDataClassesField.setAccessible( false );
			
		}catch( IllegalAccessException ex ){
			ex.printStackTrace();
			fail();
		}

		HashMap<Field, ObjectMapper> fieldMapperMap = mapper.testGetFieldMapperMapping( InnerUserMock.class, result );
		Field field = getField( InnerUserMock.class, "manager" );
		ObjectMapper innerOM = fieldMapperMap.get( field );
		Field mapperField = getField( ObjectMapper.class, "fieldMapperMap" );
		
		HashMap< Field, ObjectMapper > innerMap = null;
		
		try{
			
			mapperField.setAccessible( true );
			innerMap = ( HashMap< Field, ObjectMapper > )( mapperField.get( innerOM ) );
			mapperField.setAccessible( false );
			
		}catch( IllegalAccessException ex ){
			ex.printStackTrace();
			fail();
		}
		assertNull( "getFieldMapperMapping should return field vs Mapper Object mapping for a given correct mapped class and the inner ObjectMapper should not have FieldColumnMapper( Second level mapping) If the InnerObject does not have any mapped field", innerMap );
	}
	
	@Test
	public void itShouldReturnAHashMapWithoutTheRepetingTypeOfObjects(){ // This Omits the loop in the objects
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, 'ManagerName' as manager_name, 'PAName' as pa_name FROM user WHERE id LIKE 'testID'" );
		
		Field usedDataClassesField = getField( ObjectMapper.class, "usedDataClasses" );
		HashSet<Class> usedDataClasses = new HashSet<Class>();
		usedDataClasses.add( InnerUserMock.class );
		
		TestableObjectMapper<InnerUserMock> mapper = new TestableObjectMapper<InnerUserMock>();
		
		try{
			
			usedDataClassesField.setAccessible( true );
			usedDataClassesField.set( mapper, usedDataClasses );
			usedDataClassesField.setAccessible( false );
			
		}catch( IllegalAccessException ex ){
			ex.printStackTrace();
			fail();
		}
		HashMap<Field, ObjectMapper> fieldMapperMap = mapper.testGetFieldMapperMapping( InnerUserMock.class, result );
		Field field = getField( InnerUserMock.class, "manager" );
		ObjectMapper innerOM = fieldMapperMap.get( field );
		Field mapperField = getField( ObjectMapper.class, "fieldMapperMap" );
		
		HashMap< Field, ObjectMapper > innerMap = null;
		
		try{
			
			mapperField.setAccessible( true );
			innerMap = ( HashMap< Field, ObjectMapper > )( mapperField.get( innerOM ) );
			mapperField.setAccessible( false );
			
		}catch( IllegalAccessException ex ){
			ex.printStackTrace();
			fail();
		}
		
		Field seniorManagerField = getField( ProjectManagerMock.class, "seniorManager" );
		
		assertNull( "getFieldMapperMapping should return field vs Mapper Object mapping for a given correct mapped class and the inner ObjectMapper should not have mapping for Already used data class", innerMap.get( seniorManagerField ) );
	}
}
