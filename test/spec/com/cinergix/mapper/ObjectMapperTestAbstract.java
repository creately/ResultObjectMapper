package com.cinergix.mapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.fail;

import com.cinergix.db.DBServiceHelper;

public abstract class ObjectMapperTestAbstract {

	protected static DBServiceHelper dbHelper = DBServiceHelper.getInstance();
	
	@BeforeClass
	public static void beforeFunction(){
		
		// To create the Test database
		dbHelper.createTestDatabase();
	}
	
	@AfterClass
	public static void afterFunction(){
		
		// To delete the Test database
		dbHelper.destroyDataBase();
	}
	
	protected void createMockTableManager(){
		
		if( dbHelper.checkTableExist( "manager" ) ){
			
			dropMockTableManager();
		}
		
		String tableCreateSQL = "CREATE TABLE manager " +
                "(id VARCHAR(255) not NULL, " +
                " name VARCHAR(255), " +
                " email VARCHAR(255), " +
                " PRIMARY KEY ( id ))";
				
		dbHelper.updateData( tableCreateSQL );
		// To insert a test data
		dbHelper.updateData("INSERT INTO manager ( id, name, email ) VALUES ( 'testID', 'Test Name', 'manager@cinergix.com' )");
	}
	
	protected void dropMockTableManager(){
		
		dbHelper.updateData( "DROP TABLE manager" );
	}
	
	protected Field getField( Class type, String fieldName ){
		try{
			
			return type.getDeclaredField( fieldName );
			
		}catch( NoSuchFieldException e ){
			e.printStackTrace();
			fail( e.getMessage() );
		}
		return null;
	}
	
	// This class has public methods which are directly calling protected methods.
	// This is very useful to test protected methods of ObjectMapper class.
	protected class TestableObjectMapper<T> extends ObjectMapper<T>{
		
		public TestableObjectMapper(){
			super();
		}
		
		public boolean testCheckColumnLabelExist( ResultSet result, String columnLabel ) throws SQLException{
			return this.checkColumnLabelExist( result, columnLabel );
		}
		
		public HashMap<Field, String> testGetFieldColumnMapping(Class dataClass, ResultSet result ){
			try{
				
				return this.getFieldColumnMapping( dataClass, result );
				
			}catch( SQLException e ){
				e.printStackTrace();
				fail( e.getMessage() );
			}
			return null;
		}
		
		public Object testParseValue( ResultSet result, String columnName, Class typeClass ) {
			return this.parseValue( result, columnName, typeClass );
		}
		
		public void testAssignValueToField( T createdObject, Field field, Object value ){
			this.assignValueToField( createdObject, field, value );
		}
		
		public List<T> testMapResultSetToObject( ResultSet result, Class<T> dataClass ) {
			try{
				
				return this.mapResultSetToObject( result, dataClass );
				
			}catch( SQLException ex ){
				ex.printStackTrace();
				fail();
			}
			
			return null;
		}
	}
}
