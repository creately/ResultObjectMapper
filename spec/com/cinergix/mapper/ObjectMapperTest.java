package com.cinergix.mapper;

import java.sql.ResultSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.cinergix.db.DBServiceHelper;
import com.cinergix.mapper.data.UserMock;

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
	
	@Test
	public void mapResultSetToObjectShouldReturnEmptyListIfResultSetIsEmpty(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT * FROM user WHERE id LIKE 'testID'" );
		ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
		List<UserMock> userList = mapper.mapResultSetToObject( result, UserMock.class );
		
		assertNotNull( userList );
		assertEquals( "mapResultSetToObject Should Return Empty List If ResultSet Is Empty", 0, userList.size() );
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnNullIfResultSetIsNull(){
		
		ObjectMapper<UserMock> mapper = new ObjectMapper<UserMock>();
		List<UserMock> userList = mapper.mapResultSetToObject( null, UserMock.class );
		assertNull("mapResultSetToObject Should Return Null If ResultSet Is Null", userList);
	}
}
