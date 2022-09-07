package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.cinergix.mapper.ObjectMapper;
import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.AbstractUserMock;
import com.cinergix.mapper.data.ExtendedUserMock;
import com.cinergix.mapper.data.InnerUserMock;
import com.cinergix.mapper.data.InterfaceUserMock;
import com.cinergix.mapper.data.PrivateContructorUserMock;
import com.cinergix.mapper.data.SimpleUserMock;
import com.cinergix.mapper.data.UnMappedUserMock;
import com.cinergix.mapper.data.UserMock;
import com.cinergix.mapper.exception.ObjectCreationException;

public class MapResultSetToObjectTest extends ObjectMapperTestAbstract {

	/****************************************************************************************
	 * Test for mapResultSetToObject method													*
	 ****************************************************************************************/
	
	@Test
	public void itShouldReturnNullIfResultSetIsNull(){
		
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		List<UserMock> userList = mapper.testMapResultSetToObject( null, UserMock.class );
		
		assertNull("mapResultSetToObject Should Return Null If ResultSet Is Null", userList);
	}
	
	@Test
	public void itShouldReturnNullIfTheGivenClassTypeIsNull(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper mapper = new TestableObjectMapper();
		List userList = mapper.testMapResultSetToObject( result, null );
		
		assertNull("mapResultSetToObject Should Return Null If Given class type is null", userList);
	}
	
	@Test
	public void itShouldReturnNullIfTheGivenClassTypeIsNotAnnotatedWithResultMapped(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<UnMappedUserMock> mapper = new TestableObjectMapper<UnMappedUserMock>();
		List<UnMappedUserMock> userList = mapper.testMapResultSetToObject( result, UnMappedUserMock.class );
		
		assertNull("mapResultSetToObject Should Return Null If Given class type is not annotated with ResultMapped", userList);
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfGivenDataClassIsAbstract(){
		try{
			
			ObjectMapper<AbstractUserMock> mapper = new ObjectMapper<AbstractUserMock>();
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, age as user_age FROM user WHERE id LIKE 'testID'" );
			
			mapper.mapResultSetToObject( result, AbstractUserMock.class );
			
		}catch( SQLException ex ){
			ex.printStackTrace();
			fail();
		}
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfGivenDataClassIsAnInterface(){
		try{
			
			ObjectMapper<InterfaceUserMock> mapper = new ObjectMapper<InterfaceUserMock>();
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, age as user_age FROM user WHERE id LIKE 'testID'" );
			
			mapper.mapResultSetToObject( result, InterfaceUserMock.class );
			
		}catch( SQLException ex ){
			ex.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void itShouldReturnEmptyListIfResultSetIsEmpty(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'ID'" );
		
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		List<UserMock> userList = mapper.testMapResultSetToObject( result, UserMock.class );
		
		assertNotNull( "mapResultSetToObject should not return null even if ResultSet size is 0.", userList );
		assertEquals( "mapResultSetToObject Should Return Empty List If ResultSet Is Empty", 0, userList.size() );
		
	}
	
	@Test
	public void itShouldReturnEmptyListIfResultSetDoesNotHaveAnyMappedColumn(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id, name, email FROM user WHERE id LIKE 'testID'" );
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		List<UserMock> userList = mapper.testMapResultSetToObject( result, UserMock.class );
		
		assertNotNull( "mapResultSetToObject should not return null when all input parameters are correct but ResultSet does not have any matching column for given object", userList );
		assertEquals( "mapResultSetToObject should return empty list if ResultSet does not have any mapped column", 0, userList.size() );
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfTheEmptyConstructorIsPrivate(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email FROM user WHERE id LIKE 'testID'" );
		TestableObjectMapper<PrivateContructorUserMock> mapper = new TestableObjectMapper<PrivateContructorUserMock>();
		mapper.testMapResultSetToObject( result, PrivateContructorUserMock.class );
	}
	
	@Test
	public void itShouldReturnMappedObjectForGivenOneTupleOfValueInResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		List<UserMock> userList = mapper.testMapResultSetToObject( result, UserMock.class );
		
		assertEquals( "mapResultSetToObject should return mapped object for given one tuple of value in result set", 1, userList.size() );
		assertTrue( "mapResultSetToObject should return mapped object string value for given one tuple of value in result set", ( "testID" ).equals( userList.get( 0 ).getId() ) );
		assertEquals( "mapResultSetToObject should return mapped object with int value for given one tuple of value in result set", 25, userList.get( 0 ).getAge() );
	}
	
	@Test
	public void itShouldReturnMappedObjectForGivenMulltipleTupleOfValueInResultSet(){
		
		dbHelper.updateData("INSERT INTO user ( id, name, email ) VALUES ( 'testID2', 'Test Name2', 'test2@cinergix.com' )");
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID' OR id LIKE 'testID2'" );
		
		TestableObjectMapper<SimpleUserMock> mapper = new TestableObjectMapper<SimpleUserMock>();
		List<SimpleUserMock> userList = mapper.testMapResultSetToObject( result, SimpleUserMock.class );
		
		List<String> expectedStrings = new ArrayList<String>();
		expectedStrings.add( "Test Name" );
		expectedStrings.add( "Test Name2" );
		
		assertEquals( "mapResultSetToObject should return a list containing two SimpleUserMock", 2, userList.size() );
		assertTrue( "mapResultSetToObject should return a list containing two SimpleUserMock", expectedStrings.contains( userList.get( 0 ).getName() ) );
		assertTrue( "mapResultSetToObject should return a list containing two SimpleUserMock", expectedStrings.contains( userList.get( 1 ).getName() ) );
		dbHelper.updateData( "DELETE FROM user WHERE id LIKE 'testID2'" );
	}
	
	@Test
	public void itShouldReturnMappedObjectWithTransformedValueIfAFieldIsAnnotatedWithTransformerMethod(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		List<UserMock> userList = mapper.testMapResultSetToObject( result, UserMock.class );
		
		assertEquals( "mapResultSetToObject should return mapped objects with manipulated value if a peoperty is annotated with a transformer method", "Test", userList.get( 0 ).getName() );
	}
	
	@Test
	public void itShouldReturnMappedObjectWhichHasInnerObjectForGivenOneTupleOfValueInResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, 'Test Manager' as manager_name FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		List<UserMock> userList = mapper.testMapResultSetToObject( result, UserMock.class );
		
		assertEquals( "mapResultSetToObject should return mapped object for given one tuple of value in result set", 1, userList.size() );
		assertTrue( "mapResultSetToObject should return mapped object string value for given one tuple of value in result set", ( "testID" ).equals( userList.get( 0 ).getId() ) );
		assertEquals( "mapResultSetToObject should return mapped object with int value for given one tuple of value in result set", 25, userList.get( 0 ).getAge() );
		assertEquals( "mapResultSetToObject should return mapped object with int value for given one tuple of value in result set", "Test Manager", userList.get( 0 ).getManager().getName() );
	}
	
	@Test
	public void itShouldMapSecondLevelOfInnerObjectIfProperMappingsAreFound(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, 'Test Manager' as manager_name, 'Per_Assis' as pa_name FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<InnerUserMock> mapper = new TestableObjectMapper<InnerUserMock>();
		List<InnerUserMock> userList = mapper.testMapResultSetToObject( result, InnerUserMock.class );
		
		assertEquals( "mapResultSetToObject should return mapped object with int value for given one tuple of value in result set", "Per_Assis", userList.get( 0 ).getManager().getPersonalAssistant().getName() );
	}
	
	@Test
	public void itShouldReturnGivenObjectMappedValueInResultSet() throws SQLException{
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<UserMock> mapper = new TestableObjectMapper<UserMock>();
		UserMock userMock = new UserMock();
		UserMock user = mapper.mapResultSetToObject( result, userMock );
		
		assertTrue( "mapResultSetToObject should return mapped object string value for given one tuple of value in result set", ( "testID" ).equals( user.getId() ) );
		assertEquals( "mapResultSetToObject should return mapped object with int value for given one tuple of value in result set", 25, user.getAge() );
		assertEquals( userMock, user );
	}
	
	@Test
	public void itShouldReturnMappedValueInResultSetEvenIfItIsExtended() throws SQLException{
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age FROM user WHERE id LIKE 'testID'" );
		
		TestableObjectMapper<ExtendedUserMock> mapper = new TestableObjectMapper<ExtendedUserMock>();
		ExtendedUserMock userMock = new ExtendedUserMock();
		ExtendedUserMock user = mapper.mapResultSetToObject( result, userMock );
		
		assertTrue( "mapResultSetToObject should return mapped object string value for given one tuple of value in result set", ( "testID" ).equals( user.getId() ) );
		assertEquals( "mapResultSetToObject should return mapped object with int value for given one tuple of value in result set", 25, user.getAge() );
		assertEquals( userMock, user );
	}
}
