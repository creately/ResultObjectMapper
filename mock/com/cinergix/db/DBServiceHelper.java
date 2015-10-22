package com.cinergix.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBServiceHelper provide database related functionality for testing purpose.
 * Created database should be removed after the using this object.
 * @author rasekaran
 *
 */
public class DBServiceHelper {

	private static DBServiceHelper instance = null;
	
	private String userName = "db_user_name"; // Database login user name
	private String password = "******"; // Database login Password
	private String database = "jdbc:mysql://localhost";// Database URL path
	private Connection connection;
	
	private DBServiceHelper(){
		
	}
	
	public static DBServiceHelper getInstance(){
		if( instance == null ){
			instance = new DBServiceHelper();
		}
		
		return instance;
	}
	
	public void createConnection(){
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
	
	public ResultSet getResultSetForQuery( String queryString ){
		try{
			PreparedStatement statement = connection.prepareStatement( queryString );
			return statement.executeQuery();
		} catch( Exception e ){
			e.printStackTrace();
		}
		return null;
	}
	
	public void updateData( String sql ){
		try{
			
			Statement stmt = connection.createStatement();
			stmt.executeUpdate( sql );
		} catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	public void createTestDatabase(){
		
		// To create the connection
		createConnection();
		
		try{
			
			ResultSet result = connection.getMetaData().getCatalogs();
			
			while( result.next() ){
				
				if( ( "test_object_mapper" ).equals( result.getString( 1 ) ) ){
					return;
				}
			}
			
		} catch( SQLException e){
			e.printStackTrace();
		}
		
		updateData("CREATE DATABASE test_object_mapper");
		changeDB( "test_object_mapper" );
		
		String tableCreateSQL = "CREATE TABLE user " +
                "(id VARCHAR(255) not NULL, " +
                " name VARCHAR(255), " + 
                " email VARCHAR(255), " + 
                " age INT, " +
                " weight DECIMAL( 6, 3 ), " + 
                " dob DATE, " +
                " last_update TIMESTAMP, " +
                " PRIMARY KEY ( id ))";
				
		updateData( tableCreateSQL );
		// To insert a test data
		updateData("INSERT INTO user ( id, name, email, age, weight, dob, last_update ) VALUES ( 'testID', 'Test Name', 'test@cinergix.com', 25, 65.52, '1985-05-05', '2015-10-22 08:30:20' )");
	}
	
	public void destroyDataBase(){
		
		updateData( "DROP DATABASE test_object_mapper");
	}
}
