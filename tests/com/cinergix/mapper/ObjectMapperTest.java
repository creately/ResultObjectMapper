package com.cinergix.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.cinergix.mapper.data.User;

public class ObjectMapperTest {

	private Connection connection;
	
	@Before
	public void beforeFunction(){
			
		// To create the connection
		createConnection();
		
		// To insert a test data
		ResultSet result = getResultSetForQuery("INSERT INTO user ( 'id', 'name', 'email' ) VALUES ( 'testID', 'Test Name', 'test@cinergix.com' )");
	}
	
	@After
	private void afterFunction(){
		
		// To delete the Test Data
		ResultSet result = getResultSetForQuery( "DELETE FROM user WHERE id LIKE 'testID'" );
	}
	
	private void createConnection(){
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			String database = "jdbc:mysql://localhost/tddb"; 
			connection = DriverManager.getConnection( database ,"root","p4ssw0rd");
		} catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	private ResultSet getResultSetForQuery( String queryString ){
		try{
			PreparedStatement statement = connection.prepareStatement( queryString );
			return statement.executeQuery();
		} catch( Exception e ){
			e.printStackTrace();
		}
		return null;
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnEmptyListIfResultSetIsEmpty(){
		
		ResultSet result = getResultSetForQuery( "SELECT * FROM User WHERE id LIKE ''" );
		ObjectMapper<User> mapper = new ObjectMapper<User>();
		List<User> userList = mapper.mapResultSetToObject( result );
//		System.out.println("Result length is " + userList.size());
		assertNotNull( userList );
		assertEquals( "mapResultSetToObjectShouldReturnEmptyListIfResultSetIsEmpty - Expected 0 actual " + userList.size(), 0, userList.size() );
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnNullIfResultSetIsNull(){
		
		ObjectMapper<User> mapper = new ObjectMapper<User>();
		List<User> userList = mapper.mapResultSetToObject( null );
		assertNull("mapResultSetToObjectShouldReturnNullIfResultSetIsNull", userList);
	}
}
