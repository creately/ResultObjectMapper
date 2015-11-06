package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.cinergix.mapper.ObjectMapper;
import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.SimpleUserMock;
import com.cinergix.mapper.data.UserMock;
import com.cinergix.mapper.exception.DataTypeConversionException;

public class ParseValueTest extends ObjectMapperTestAbstract {
	
	public TestableObjectMapper mapper = new TestableObjectMapper();
	private static ResultSet result;
	
	@BeforeClass
	public static void getResultSet(){
		try{
			
			result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, dob as user_dob, last_update as user_last_update FROM user WHERE id LIKE 'testID'" );
			result.next();
			
		}catch( SQLException e ){
			e.printStackTrace();
			fail( e.getMessage() );
		}
	}

	/****************************************************************************************
	 * Test parseValue method																*
	 ****************************************************************************************/
	
	@Test
	public void itShouldReturnNullIfGivenResultSetIsNull(){
		
		assertNull( "parseValue should return null if given ResultSet is null", mapper.testParseValue( null, "user_name", String.class ) );
			
	}
	@Test
	public void itShouldReturnNullIfGivenColumnLabelIsNullOrEmptyIsNull(){
		
		assertNull( "parseValue should return null if given column label is null", mapper.testParseValue( result, null, String.class ) );
		assertNull( "parseValue should return null if given column label is empty", mapper.testParseValue( result, "", String.class ) );
			
	}
	@Test
	public void itShouldReturnNullIfGivenTypeIsNull(){
		
		assertNull( "parseValue should return null if given Type is null", mapper.testParseValue( result, "user_name", null ) );
			
	}
	
	@Test
	public void itConvertObjectToStringIfTypeIsStringClass(){
		
		assertTrue( "parseValue should convert given Object to String if type is String", ( "Test Name" ).equals( (String)( mapper.testParseValue( result, "user_name", String.class ) ) ) );
	}
	
	@Test
	public void parseValueShouldConvertObjectToIntegerIfTypeIsIntegerClass(){
		
		assertEquals( "parseValue should convert given object to Integer if type is Integer", (Integer)25, (Integer)( mapper.testParseValue( result, "user_age", Integer.class ) ) );
		assertEquals( "parseValue should convert given object to int if type is int", 25, (int)( mapper.testParseValue( result, "user_age", int.class ) ) );
	}
	
	@Test
	public void parseValueShouldConvertObjectToLongIfTypeIsLongClass(){
		
		assertEquals( "parseValue should convert given object to Integer if type is Integer", 25, (long)( mapper.testParseValue( result, "user_age", Long.class ) ) );
		assertEquals( "parseValue should convert given object to Integer if type is Integer", 25, (long)( mapper.testParseValue( result, "user_age", long.class ) ) );
			
	}
	
	@Test
	public void parseValueShouldConvertObjectToDoubleIfTypeIsDoubleClass(){
		
		assertEquals( "parseValue should convert given object to Double if type is Double", 65.52, (double)( mapper.testParseValue( result, "user_weight", Double.class ) ), 0 );
		assertEquals( "parseValue should convert given object to double if type is double", 65.52, (double)( mapper.testParseValue( result, "user_weight", double.class ) ), 0 );
			
	}
	
	@Test
	public void parseValueShouldConvertObjectToFloatIfTypeIsFloatClass(){
		
		assertEquals( "parseValue should convert given object to Float if type is Float", (float)65.52, (float)( mapper.testParseValue( result, "user_weight", Float.class ) ), 0 );
		assertEquals( "parseValue should convert given object to float if type is float", (float)65.52, (float)( mapper.testParseValue( result, "user_weight", float.class ) ), 0 );
		
	}
	
	@Test
	public void parseValueShouldConvertObjectToFloatIfTypeIsBooleanClass(){
		
		ResultSet rs = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, TRUE as user_married FROM user WHERE id LIKE 'testID'" );
		
		try{
			
			rs.next();
		}catch( SQLException e ){
			e.printStackTrace();
			fail( e.getMessage() );
		}
		
		assertTrue( "parseValue should convert given object to Boolean if type is Boolean", ( Boolean )mapper.testParseValue( rs, "user_married", Boolean.class ) );
		assertTrue( "parseValue should convert given object to boolean if type is booolean", ( boolean )mapper.testParseValue( rs, "user_married", boolean.class ) );
		
	}
	
	@Test
	public void parseValueShouldConvertObjectToDateIfTypeIsDateClass(){
		
		Date dob = (Date)( mapper.testParseValue( result, "user_dob", Date.class ) );
		
	    Calendar cal = Calendar.getInstance();
	    cal.setTime( dob );
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
		
		assertEquals( "parseValue should convert the given Object to Date and the year shoul be equal to the year in the data", 1985, year );
		assertEquals( "parseValue should convert the given Object to Date and the month index shoul be equal to the month - 1 in the data", ( 5 - 1 ), month ); // In Java Calendar month is starting from 0
		assertEquals( "parseValue should convert the given Object to Date and the day shoul be equal to the day in the data", 5, day );
			
	}
	
	@Test
	public void parseValueShouldConvertTimestampObjectToDateIfTypeIsDateClass(){
		
		Date dob = (Date)( mapper.testParseValue( result, "user_last_update", Date.class ) );
		
	    Calendar cal = Calendar.getInstance();
	    cal.setTime( dob );
	    int year = cal.get(Calendar.YEAR);
	    int month = cal.get(Calendar.MONTH);
	    int day = cal.get(Calendar.DAY_OF_MONTH);
	    int hour = cal.get(Calendar.HOUR_OF_DAY);
	    int min = cal.get(Calendar.MINUTE);
	    int sec = cal.get(Calendar.SECOND);
		
		assertEquals( "parseValue should convert the given Timestamp Object to Date and the year shoul be equal to the year in the data", 2015, year );
		assertEquals( "parseValue should convert the given Timestamp Object to Date and the month index shoul be equal to the month - 1 in the data", ( 10 - 1 ), month ); // In Java Calendar month is starting from 0
		assertEquals( "parseValue should convert the given Timestamp Object to Date and day shoul be equal to the day in the data", 22, day );
		assertEquals( "parseValue should convert the given Timestamp Object to Date and the hour shoul be equal to the hour in the data", 8, hour );
		assertEquals( "parseValue should convert the given Timestamp Object to Date and the minute shoul be equal to the minute in the data", 30, min );
		assertEquals( "parseValue should convert the given Timestamp Object to Date and the seconds shoul be equal to the seconds in the data", 20, sec );
		
	}
	
	@Test( expected = DataTypeConversionException.class )
	public void parseValueShouldThrowDataTypeConversionExceptionWhenTryingToConvertToIncompetableTypes(){
		
		mapper.testParseValue( result, "user_name", int.class );
	}
	
	@Test
	public void itShouldConvertAndReturnFirstMatchedColumnValueevenIfThereIsMoreThanOneColumn(){
		
		ResultSet rs = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, email as user_email, age as user_age, weight as user_weight, age as user_weight FROM user WHERE id LIKE 'testID'" );
		
		try{
			
			rs.next();
		}catch( SQLException e ){
			e.printStackTrace();
			fail( e.getMessage() );
		}
		
		double weight = ( double )( mapper.testParseValue( result, "user_weight", double.class ) );
		assertEquals( "parseValue should return converted value of first matched column's value", 65.52, weight, 0 );
	}
	
	@Test
	public void itShouldConvertAndReturnFirstMatchedColumnValueevenIfThereIsMoreThanOneColumnFromDifferentTable(){
		createMockTableManager();
		
		ResultSet rs = dbHelper.getResultSetForQuery( "SELECT user.id, user.name, user.email, manager.id, manager.name, manager.email FROM user INNER JOIN manager ON user.id = manager.id WHERE user.id LIKE 'testID'" );
		
		try{
			
			rs.next();
		}catch( SQLException e ){
			e.printStackTrace();
			fail( e.getMessage() );
		}
		
		String email = ( String )( mapper.testParseValue( rs, "email", String.class ) );
		assertTrue( "parseValue should return converted value of first matched column's value even if there are more than one column from different table", ( "test@cinergix.com" ).equals( email ) );
		
		dropMockTableManager();
	}
	
	@Test
	public void itShouldConvertAndReturnFirstMatchedColumnValueevenIfThereIsMoreThanOneColumnFromDifferentTableWithAlais(){
		createMockTableManager();
		
		ResultSet rs = dbHelper.getResultSetForQuery( "SELECT u.id, u.name, u.email, m.id, m.name, m.email FROM user u INNER JOIN manager m ON u.id = m.id WHERE u.id LIKE 'testID'" );
		
		try{
			
			rs.next();
		}catch( SQLException e ){
			e.printStackTrace();
			fail( e.getMessage() );
		}
		
		String email = ( String )( mapper.testParseValue( rs, "email", String.class ) );
		assertTrue( "parseValue should return converted value of first matched column's value even if there are more than one column from different table with alais", ( "test@cinergix.com" ).equals( email ) );
		
		dropMockTableManager();
	}
}
