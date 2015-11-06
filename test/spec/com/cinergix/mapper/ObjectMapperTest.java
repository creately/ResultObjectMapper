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
	
	
	/****************************************************************************************
	 * Test assignValueToField method														*
	 ****************************************************************************************/
	@Test
	public void assignValueToFieldShouldAssignGivenValueToField(){
		try{
			
			UserMock user = new UserMock();
			Class userClass = UserMock.class;
			Field field = userClass.getDeclaredField( "name" );
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "assignValueToField", Object.class, Field.class, Object.class );
			method.invoke( mapper, user, field, "Test User" );
			
			assertTrue( "assignValueToField should assign the value to given object's field", ( "Test User" ).equals( user.getName() ) );
			
		}catch( NoSuchFieldException e ){
			e.printStackTrace();
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}
		
	}
	
	@Test
	public void assignValueToFieldShouldAssignTheValueToProperty(){
		try{
			
			UserMock user = new UserMock();
			Class userClass = UserMock.class;
			Field field = userClass.getDeclaredField( "age" );
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "assignValueToField", Object.class, Field.class, Object.class );
			method.invoke( mapper, user, field, 20 );
			System.out.println( "user age " + user.getAge() );
			assertEquals( "assignValueToField should assign the value to the field", 20, user.getAge() );
			
			method.invoke( mapper, user, field, 25 );
			assertEquals( "assignValueToField should assign the value to the field", 25, user.getAge() );
			
		}catch( NoSuchFieldException e ){
			e.printStackTrace();
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}
		
	}
	
	
	@Test( expected = PropertyAccessException.class )
	public void assignValueToFieldShouldThrowPropertyAccessExceptionIfAssignValueIsIncompatableTypeOf(){
		try{
			
			UserMock user = new UserMock();
			Class userClass = UserMock.class;
			Field field = userClass.getDeclaredField( "age" );
			
			( new ObjectMapper<UserMock>(){
			
				public void testAssignValueToField( UserMock createdObject, Field field, Object value ){
					this.assignValueToField( createdObject, field, value );
				}
			}).testAssignValueToField( user, field, "Data" );
			
		}catch( NoSuchFieldException e ){
			e.printStackTrace();
		}
		
	}
	
	
	/****************************************************************************************
	 * Test parseValue method																*
	 ****************************************************************************************/
	@Test
	public void parseValueShouldConvertObjectToStringIfTypeIsStringClass(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			method.setAccessible(true);
			assertTrue( "parseValue should converd the given Object to String if type is String class", ( "Test Name" ).equals( (String)( method.invoke( mapper, result, "user_name", String.class ) ) ) );
			method.setAccessible(false);
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
	@Test
	public void parseValueShouldConvertObjectToIntegerIfTypeIsIntegerClass(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			method.setAccessible(true);
			assertEquals( "parseValue should converd the givenString to Integer if type is Integer class", (Integer)25, (Integer)( method.invoke( mapper, result, "user_age", Integer.class ) ) );
			assertEquals( "parseValue should converd the givenString to Integer if type is Integer class", 25, (int)( method.invoke( mapper, result, "user_age", int.class ) ) );
			method.setAccessible(false);
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
	@Test
	public void parseValueShouldConvertObjectToLongIfTypeIsLongClass(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			method.setAccessible(true);
			assertEquals( "parseValue should converd the givenString to Integer if type is Integer class", 25, (long)( method.invoke( mapper, result, "user_age", Long.class ) ) );
			assertEquals( "parseValue should converd the givenString to Integer if type is Integer class", 25, (long)( method.invoke( mapper, result, "user_age", long.class ) ) );
			method.setAccessible(false);
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
	@Test
	public void parseValueShouldConvertObjectToDoubleIfTypeIsDoubleClass(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			method.setAccessible(true);
			assertEquals( "parseValue should converd the givenString to Double if type is Double class", 65.52, (double)( method.invoke( mapper, result, "user_weight", Double.class ) ), 0 );
			assertEquals( "parseValue should converd the givenString to double if type is double class", 65.52, (double)( method.invoke( mapper, result, "user_weight", double.class ) ), 0 );
			method.setAccessible(false);
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
	@Test
	public void parseValueShouldConvertObjectToFloatIfTypeIsFloatClass(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			method.setAccessible(true);
			assertEquals( "parseValue should converd the givenString to Float if type is Float class", (float)65.52, (float)( method.invoke( mapper, result, "user_weight", Float.class ) ), 0 );
			assertEquals( "parseValue should converd the givenString to float if type is float class", (float)65.52, (float)( method.invoke( mapper, result, "user_weight", float.class ) ), 0 );
			method.setAccessible(false);
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
	@Test
	public void parseValueShouldConvertObjectToDateIfTypeIsDateClass(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			method.setAccessible(true);
			Date dob = (Date)( method.invoke( mapper, result, "user_dob", Date.class ) );
			method.setAccessible(false);
			
		    Calendar cal = Calendar.getInstance();
		    cal.setTime( dob );
		    int year = cal.get(Calendar.YEAR);
		    int month = cal.get(Calendar.MONTH);
		    int day = cal.get(Calendar.DAY_OF_MONTH);
			
			assertEquals( "parseValue should convert the given Object to Date if type is Date class", 1985, year );// In Java Calendar month is starting from 0
			assertEquals( "parseValue should convert the given Object to Date if type is Date class", ( 5 - 1 ), month );
			assertEquals( "parseValue should convert the given Object to Date if type is Date class", 5, day );
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
	@Test
	public void parseValueShouldConvertTimestampObjectToDateIfTypeIsDateClass(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			method.setAccessible(true);
			Date dob = (Date)( method.invoke( mapper, result, "user_last_update", Date.class ) );
			method.setAccessible(false);
			
		    Calendar cal = Calendar.getInstance();
		    cal.setTime( dob );
		    int year = cal.get(Calendar.YEAR);
		    int month = cal.get(Calendar.MONTH);
		    int day = cal.get(Calendar.DAY_OF_MONTH);
		    int hour = cal.get(Calendar.HOUR_OF_DAY);
		    int min = cal.get(Calendar.MINUTE);
		    int sec = cal.get(Calendar.SECOND);
			
			assertEquals( "parseValue should convert the given Timestamp Object to Date if type is Date class", 2015, year );// In Java Calendar month is starting from 0
			assertEquals( "parseValue should convert the given Timestamp Object to Date if type is Date class", ( 10 - 1 ), month );
			assertEquals( "parseValue should convert the given Timestamp Object to Date if type is Date class", 22, day );
			assertEquals( "parseValue should convert the given Timestamp Object to Date if type is Date class", 8, hour );
			assertEquals( "parseValue should convert the given Timestamp Object to Date if type is Date class", 30, min );
			assertEquals( "parseValue should convert the given Timestamp Object to Date if type is Date class", 20, sec );
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( IllegalAccessException e2 ){
			e2.printStackTrace();
		}catch( InvocationTargetException e3 ){
			e3.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
	@Test( expected = DataTypeConversionException.class )
	public void parseValueShouldThrowDataTypeConversionExceptionWhenTryingToConvertToIncompetableTypes(){
		try{
			
			ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
			
			Class mapperClass = ObjectMapper.class;
			Method method = mapperClass.getDeclaredMethod( "parseValue", ResultSet.class, String.class, Class.class );
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_age, email as user_email FROM user WHERE id LIKE 'testID'" );
			result.next();
			
			List<UserMock> list = mapper.mapResultSetToObject( result, UserMock.class );
			
		}catch ( NoSuchMethodException e1 ){
			e1.printStackTrace();
		}catch ( SQLException sqlex) {
			 sqlex.printStackTrace();
		}
	}
	
}
