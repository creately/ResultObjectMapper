package com.cinergix.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.cinergix.db.DBServiceHelper;
import com.cinergix.mapper.data.AbstractUserMock;
import com.cinergix.mapper.data.SimpleUserMock;
import com.cinergix.mapper.data.UnMappedUserMock;
import com.cinergix.mapper.data.UserMock;
import com.cinergix.mapper.exception.DataTypeConversionException;
import com.cinergix.mapper.exception.ObjectCreationException;
import com.cinergix.mapper.exception.PropertyAccessException;

public class ObjectMapperTest {
	
	DBServiceHelper dbHelper = DBServiceHelper.getInstance();
	@Before
	public void beforeFunction(){
		
		// To create the Test database
		dbHelper.createTestDatabase();
	}
	
	@After
	public void afterFunction(){
		
		// To delete the Test database
		dbHelper.destroyDataBase();
	}
	
	
	/****************************************************************************************
	 * Test for mapResultSetToObject method													*
	 ****************************************************************************************/
	@Test
	public void mapResultSetToObjectShouldReturnEmptyListIfResultSetIsEmpty(){
		try{
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'ID'" );
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			List<UserMock> userList = mapper.mapResultSetToObject( result, UserMock.class );
			
			assertNotNull( userList );
			assertEquals( "mapResultSetToObject Should Return Empty List If ResultSet Is Empty", 0, userList.size() );
		} catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnNullIfResultSetIsNull(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			List<UserMock> userList = mapper.mapResultSetToObject( null, UserMock.class );
			assertNull("mapResultSetToObject Should Return Null If ResultSet Is Null", userList);
			
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnNullIfTheGivenClassTypeIsNotAnnotatedWithResultMapped(){
		try{
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
			
			ObjectMapper<UnMappedUserMock> mapper = new ObjectMapper<UnMappedUserMock>();
			List<UnMappedUserMock> userList = mapper.mapResultSetToObject( result, UnMappedUserMock.class );
			assertNull("mapResultSetToObject Should Return Null If Given class type is not annotated with ResultMapped", userList);
		
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnNullIfTheGivenClassTypeIsNull(){
		try{
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
			
			ObjectMapper mapper = new ObjectMapper();
			List userList = mapper.mapResultSetToObject( result, null );
			assertNull("mapResultSetToObject Should Return Null If Given class type is null", userList);
		
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnEmptyListIfResultSetDoesNotHaveAnyMappedColumn(){
		try{
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, email FROM user WHERE id LIKE 'testID'" );
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			//List<User> userList = mapper.mapResultSetToObject( result );
			List<UserMock> userList = mapper.mapResultSetToObject( result, UserMock.class );
			assertNotNull( userList );
			assertEquals( "mapResultSetToObject should return empty list if ResultSet does not have any mapped column", 0, userList.size() );
			
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnMappedObjectForGivenOneTupleOfValueInResultSet(){
		try{
			
			ObjectMapper<SimpleUserMock> mapper = new ObjectMapper<SimpleUserMock>();
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
			
			List<SimpleUserMock> userList = mapper.mapResultSetToObject( result, SimpleUserMock.class );
			assertEquals( "mapResultSetToObject should return mapped object for given one tuple of value in result set", 1, userList.size() );
			assertTrue( "mapResultSetToObject should return mapped object for given one tuple of value in result set", ( "Test Name" ).equals( userList.get( 0 ).getName() ) );
			
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnMappedObjectForGivenMulltipleTupleOfValueInResultSet(){
		try{
			
			dbHelper.updateData("INSERT INTO user ( id, name, email ) VALUES ( 'testID2', 'Test Name2', 'test2@cinergix.com' )");
			
			ObjectMapper<SimpleUserMock> mapper = new ObjectMapper<SimpleUserMock>();
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID' OR id LIKE 'testID2'" );
			
			List<SimpleUserMock> userList = mapper.mapResultSetToObject( result, SimpleUserMock.class );
			
			List<String> expectedStrings = new ArrayList<String>();
			expectedStrings.add( "Test Name" );
			expectedStrings.add( "Test Name2" );
			
			assertEquals( "mapResultSetToObject should return a list containing two SimpleUserMock", 2, userList.size() );
			assertTrue( "mapResultSetToObject should return a list containing two SimpleUserMock", expectedStrings.contains( userList.get( 0 ).getName() ) );
			assertTrue( "mapResultSetToObject should return a list containing two SimpleUserMock", expectedStrings.contains( userList.get( 1 ).getName() ) );
			dbHelper.updateData( "DELETE FROM user WHERE id LIKE 'testID2'" );
			
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnEmptyListIfThereIsNoMappingColumnLabelFoundInGivenResultSet(){
		try{
			
			Class userClass = UserMock.class;
			Field[] fields = userClass.getDeclaredFields();
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			Class mapperClass = ObjectMapper.class;
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, age FROM user WHERE id LIKE 'testID'" );
			
			List<UserMock> list = mapper.mapResultSetToObject( result, userClass );
			
			Field fID =userClass.getDeclaredField( "id" );
			Field fEmail =userClass.getDeclaredField( "email" );
			
			assertEquals( "mapResultSetToObject should return empty list if there is no mapping column is found in given result set", 0, list.size() );
			
		}catch( NoSuchFieldException e ){
			e.printStackTrace();
		}
	}
	
	@Test( expected = ObjectCreationException.class )
	public void mapResultSetToObjectShouldShouldThrowObjectCreationExceptionIfGivenDataClassIsAbstract(){
			
			Class userClass = AbstractUserMock.class;
			Field[] fields = userClass.getDeclaredFields();
			
			ObjectMapper<AbstractUserMock> mapper = new ObjectMapper<AbstractUserMock>();
			Class mapperClass = ObjectMapper.class;
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, age as user_age FROM user WHERE id LIKE 'testID'" );
			
			List<AbstractUserMock> list = mapper.mapResultSetToObject( result, userClass );
			
	}
	
}
