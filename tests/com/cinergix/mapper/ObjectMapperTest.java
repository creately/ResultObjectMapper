package com.cinergix.mapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.cinergix.mapper.data.User;

public class ObjectMapperTest {

	private String userName = "root";
	private String password = "root";
	private String database = "jdbc:mysql://localhost";
	private Connection connection;
	
	@Before
	public void beforeFunction(){
			
		// To create the connection
		createConnection();
		
		updateData("CREATE DATABASE test_object_mapper");
		changeDB( "test_object_mapper" );
		
		String tableCreateSQL = "CREATE TABLE user " +
                "(id VARCHAR(255) not NULL, " +
                " name VARCHAR(255), " + 
                " email VARCHAR(255), " + 
                //" age INTEGER, " + 
                " PRIMARY KEY ( id ))";
//		
		updateData( tableCreateSQL );
		// To insert a test data
		updateData("INSERT INTO user ( id, name, email ) VALUES ( 'testID', 'Test Name', 'test@cinergix.com' )");
	}
	
	@After
	public void afterFunction(){
		
		// To delete the Test Data
//		updateData( "DELETE FROM user WHERE id LIKE 'rasi_1'" );
		updateData( "DROP DATABASE test_object_mapper");
	}
	
	private void createConnection(){
		try{
			
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection( database, userName, password );
		} catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	private void changeDB( String dbName ){
		try{
			connection.setCatalog( dbName );
		}catch( SQLException e ){
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
	
	private void updateData( String sql ){
		try{
			
			Statement stmt = connection.createStatement();
			stmt.executeUpdate( sql );
		} catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	@Test
	public void mapResultSetToObjectShouldReturnEmptyListIfResultSetIsEmpty(){
		
		ResultSet result = getResultSetForQuery( "SELECT * FROM user WHERE id LIKE 'testID'" );
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
