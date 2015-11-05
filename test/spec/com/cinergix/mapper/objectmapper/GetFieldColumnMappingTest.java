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
	
	private ExtendedObjectMapper mapper = new ExtendedObjectMapper();
	
	private HashMap<Field, String> invokeCheckColumnLabelExist( Class dataClass, ResultSet result ){
		try{
			
			return mapper.testGetFieldColumnMapping( dataClass, result );
			
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
	
	private void createMockTableManager(){
		
		String tableCreateSQL = "CREATE TABLE manager " +
                "(id VARCHAR(255) not NULL, " +
                " name VARCHAR(255), " +
                " email VARCHAR(255), " +
                " PRIMARY KEY ( id ))";
				
		dbHelper.updateData( tableCreateSQL );
		// To insert a test data
		dbHelper.updateData("INSERT INTO manager ( id, name, email ) VALUES ( 'testID', 'Test Name', 'manager@cinergix.com' )");
	}
	
	private void dropMockTableManager(){
		dbHelper.updateData( "DROP TABLE manager" );
	}
	
	/****************************************************************************************
	 * Test getFieldColumnMapping method													*
	 ****************************************************************************************/
	
	@Test
	public void itShouldReturnNullIfGivenResultSetIsNull(){
			
			HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleUserMock.class, null );
			
			assertNull( "getFieldColumnMapping should return null if given ResultSet is null", fieldsVsColumns );
			
	}
	
	@Test
	public void itShouldReturnNullIfGivenClassTypeIsNull(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( null, result );
		
		assertNull( "getFieldColumnMapping should return null if given class type is null", fieldsVsColumns );
			
	}
	
	@Test
	public void itShouldReturnNullIfBothInputPatametersAreNull(){
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( null, null );
		
		assertNull( "getFieldColumnMapping should return null if both input parameters are null", fieldsVsColumns );
			
	}
	
	@Test
	public void itShouldReturnMappedSetOfFieldVsColumnNameFromTheAnnotation(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleUserMock.class, result );
		
		assertTrue("getFieldColumnMapping should return a mapped set of field Vs Column names( which are in the annotation )", ( "user_name" ).equals( fieldsVsColumns.get( getField( SimpleUserMock.class, "name" ) ) ) );
			
	}
	
	@Test
	public void itShouldReturnMappedSetOfFieldVsColumnNameFromTheAnnotationOnlyForAnnotatedFields(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleUserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should return a mapped set of field Vs Column names( which are in the annotation )", 1, fieldsVsColumns.size() );
		assertTrue( "getFieldColumnMapping should return a mapped set of field Vs Column names( which are in the annotation )", ( "user_name" ).equals( fieldsVsColumns.get( getField( SimpleUserMock.class, "name" ) ) ) );
			
	}
	
	@Test
	public void itShouldReturnAHashMapOfAllTheAnnotatedFieldsWithCorrespondingColumnInResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( UserMock.class, result );
		
		assertTrue("getFieldColumnMapping should return All the annotated fields with its mapped colum name", ( "user_id" ).equals( fieldsVsColumns.get( getField( UserMock.class, "id" ) ) ) );
		assertTrue("getFieldColumnMapping should return All the annotated fields with its mapped colum name", ( "user_name" ).equals( fieldsVsColumns.get( getField( UserMock.class, "name" ) ) ) );
		assertTrue("getFieldColumnMapping should return All the annotated fields with its mapped colum name", ( "user_email" ).equals( fieldsVsColumns.get( getField( UserMock.class, "email" ) ) ) );
		
	}
	
	@Test
	public void itShouldReturnEmptyMapIfThereIsNoMappingColumnLabelFoundInGivenResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( UserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should return empty map if there is no mapping column is found in given result set", 0, fieldsVsColumns.size() );
		
	}
	
	@Test
	public void itShouldMapOnlyOneColumnToAFieldEvenIfThereAreMultipleColumnWithSameLabelInResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_name FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( UserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label in ResultSet( Check size of Map )", 2, fieldsVsColumns.size() );
		assertTrue("getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label in ResultSet", ( "user_id" ).equals( fieldsVsColumns.get( getField( UserMock.class, "id" ) ) ) );
		assertTrue("getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label in ResultSet", ( "user_name" ).equals( fieldsVsColumns.get( getField( UserMock.class, "name" ) ) ) );
		
	}
	
	@Test
	public void itShouldMapOnlyOneColumnToAFieldEvenIfThereAreMultipleColumnWithSameLabelFromDifferentTableInResultSet(){
		
		createMockTableManager();
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT user.id, user.name, manager.id, manager.name FROM user INNER JOIN manager ON user.id = manager.id WHERE user.id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleColumnMappedUserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label from different table in ResultSet( Check size of Map )", 2, fieldsVsColumns.size() );
		assertTrue("getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label from different table in ResultSet", ( "id" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "id" ) ) ) );
		assertTrue("getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label from different table in ResultSet", ( "name" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "name" ) ) ) );
		
		dropMockTableManager();
		
	}
	
	@Test
	public void itShouldMapOnlyOneColumnToAFieldEvenIfThereAreMultipleColumnWithSameLabelFromDifferentTableWithAlaisInResultSet(){
		
		createMockTableManager();
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT u.id, u.name, m.id, m.name FROM user u INNER JOIN manager m ON u.id = m.id WHERE u.id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleColumnMappedUserMock.class, result );
		
		assertEquals( "getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label from different table with alias in ResultSet( Check size of Map )", 2, fieldsVsColumns.size() );
		assertTrue("getFieldColumnMapping should Map only one column to a field even if there are multiple column with same label from different table with alias in ResultSet", ( "id" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "id" ) ) ) );
		assertTrue("getFieldColumnMapping should Map only one column to a field even if there are multiple column with same labelfrom different table with alias in ResultSet", ( "name" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "name" ) ) ) );
		
		dropMockTableManager();
		
	}
	
	@Test
	public void itShouldReturnMappedSetWithBothFieldsEvenIFthereAreMoreThanOneFieldsAnnotatedToMapASingleColumn(){
		
		createMockTableManager();
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, email FROM user WHERE id LIKE 'testID'" );
		
		HashMap<Field, String> fieldsVsColumns = this.invokeCheckColumnLabelExist( SimpleColumnMappedUserMock.class, result );
		
		assertTrue("getFieldColumnMapping should return mapped set with Both fields even if there are more than one fields annotated to map a single column", ( "email" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "officeEmail" ) ) ) );
		assertTrue("getFieldColumnMapping should return mapped set with Both fields even if there are more than one fields annotated to map a single column", ( "email" ).equals( fieldsVsColumns.get( getField( SimpleColumnMappedUserMock.class, "personalEmail" ) ) ) );
		
		dropMockTableManager();
		
	}

}
