package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.junit.Test;

import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.SimpleColumnMappedUserMock;
import com.cinergix.mapper.data.SimpleUserMock;
import com.cinergix.mapper.data.UserMock;

public class GetFieldColumnMappingTest extends ObjectMapperTestAbstract {
	
	public TestableObjectMapper mapper = new TestableObjectMapper();;
	
	
	/****************************************************************************************
	 * Test getFieldColumnMapping method													*
	 ****************************************************************************************/
	
	@Test
	public void itShouldReturnNullIfGivenResultSetIsNull(){
			
			HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( SimpleUserMock.class, null );
			
			assertNull( "getFieldColumnMapping should return null if given ResultSet is null", fieldsVsColumns );
			
	}
	
	@Test
	public void itShouldReturnNullIfGivenClassTypeIsNull(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( null, result );
		
		assertNull( "getFieldColumnMapping should return null if given class type is null", fieldsVsColumns );
			
	}
	
	@Test
	public void itShouldReturnAMapWithOneOrMoreValue(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age FROM user" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( SimpleUserMock.class, result );
		
		assertTrue( "getFieldColumnMapping should return a mapped set with one or more value", fieldsVsColumns.size() > 0 );
			
	}
	
	@Test
	public void itShouldReturnMappedSetOfFieldVsColumnNameFromTheAnnotationOnlyForAnnotatedFields(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( SimpleUserMock.class, result );
		
		assertTrue( "getFieldColumnMapping should returns a map with name field of SimpleUserMock as a key and user_name as value", ( "user_name" ).equals( fieldsVsColumns.get( getField( SimpleUserMock.class, "name" ) ) ) );
			
	}
	
	@Test
	public void itShouldReturnAHashMapOfAllTheAnnotatedFieldsWithCorrespondingColumnInResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( UserMock.class, result );
		
		assertTrue( "getFieldColumnMapping should return a map which has id field of UserMock class as key and user_id as value", ( "user_id" ).equals( fieldsVsColumns.get( getField( UserMock.class, "id" ) ) ) );
		assertTrue( "getFieldColumnMapping should return a map which has name field of UserMock class as key and user_name as value", ( "user_name" ).equals( fieldsVsColumns.get( getField( UserMock.class, "name" ) ) ) );
		assertTrue( "getFieldColumnMapping should return a map which has email field of UserMock class as key and user_email as value", ( "user_email" ).equals( fieldsVsColumns.get( getField( UserMock.class, "email" ) ) ) );
		
	}
	
	@Test
	public void itShouldReturnMapWhichShouldNotHaveMappingForAFieldIfThereIsNoCorrespondingColumnAvailableInResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( UserMock.class, result );
		
		assertNull( "getFieldColumnMapping should return a map which should no have mapping for a field if there is no corresponding column available in ResultSet", fieldsVsColumns.get( getField( UserMock.class, "name" ) ) );
		
		
	}
	
	@Test
	public void itShouldReturnEmptyMapIfThereIsNoMappingColumnLabelFoundInGivenResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( UserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should return empty map if there is no mapping column is found in given result set", 0, fieldsVsColumns.size() );
		
	}
	
	@Test
	public void itShouldMapOnlyOneColumnToAFieldEvenIfThereAreMultipleColumnWithSameLabelInResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_name FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( UserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should Map only one user_name even though there are two user_name in the ResultSet so that the size of HashMap should be 2.", 2, fieldsVsColumns.size() );
		assertTrue("getFieldColumnMapping should Map only one column to a field and the returned value should have user_name", ( "user_name" ).equals( fieldsVsColumns.get( getField( UserMock.class, "name" ) ) ) );
		
	}
	
	@Test
	public void itShouldMapOnlyOneColumnToAFieldEvenIfThereAreMultipleColumnWithSameLabelFromDifferentTableInResultSet(){
		
		createMockTableManager();
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT user.id, user.name, manager.id, manager.name FROM user INNER JOIN manager ON user.id = manager.id WHERE user.id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( SimpleColumnMappedUserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should Map only one column to a field even there is ID and name are duplicated twice from two different table and the size of HashMap should be 2", 2, fieldsVsColumns.size() );
		assertTrue( "getFieldColumnMapping should Map only one column to a field even there are two id columns from two different table and that id should availabe in result set", ( "id" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "id" ) ) ) );
		assertTrue( "getFieldColumnMapping should Map only one column to a field even there are two id columns from two different table and that name should availabe in result set", ( "name" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "name" ) ) ) );
		
		dropMockTableManager();
		
	}
	
	@Test
	public void itShouldMapOnlyOneColumnToAFieldEvenIfThereAreMultipleColumnWithSameLabelFromDifferentTableWithAlaisInResultSet(){
		
		createMockTableManager();
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT u.id, u.name, m.id, m.name FROM user u INNER JOIN manager m ON u.id = m.id WHERE u.id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( SimpleColumnMappedUserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should Map only one column to a field even there is ID and name are duplicated twice from two different tables with alias and the size of HashMap should be 2", 2, fieldsVsColumns.size() );
		assertTrue( "getFieldColumnMapping should Map only one column to a field even there are two id columns from two different tables with alias and that id should availabe in result set", ( "id" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "id" ) ) ) );
		assertTrue( "getFieldColumnMapping should Map only one column to a field even there are two id columns from two different tables with alias and that name should availabe in result set", ( "name" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "name" ) ) ) );
		
		dropMockTableManager();
		
	}
	
	@Test
	public void itShouldReturnMappedSetWithBothFieldsEvenIFthereAreMoreThanOneFieldsAnnotatedToMapASingleColumn(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( SimpleColumnMappedUserMock.class, result );
		
		assertTrue("getFieldColumnMapping should return mapped set which should have mapping for officeEmail even though email is mapped to two fields", ( "email" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "officeEmail" ) ) ) );
		assertTrue("getFieldColumnMapping should return mapped set which should have mapping for personalEmail even though email is mapped to two fields", ( "email" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "personalEmail" ) ) ) );
	}
	
	@Test
	public void itShouldMapOnlyOneColumnLableInTheResultSetEvenIfThereAreMoreThanOneValueInTheAnnotation(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( UserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should map only one column lable from ResultSet even if there are omre than one value is in the annotation", "user_name", fieldsVsColumns.get( getField( UserMock.class, "name" ) ) );
	}
	
	@Test
	public void itShouldMapFirstAvailableColumnLableInTheResultSetEvenIfThereAreMoreThanOneValueAvailableToMapToASingleFieldOfObject(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_office_mail, 'test_email@gmail.com' as user_personal_mail FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = mapper.testGetFieldColumnMapping( UserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should map first available column in the ResultSetEven if there are more than one value available to map to a single field of Object", "user_office_mail", fieldsVsColumns.get( getField( UserMock.class, "email" ) ) );
	}

}
