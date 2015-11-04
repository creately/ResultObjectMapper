package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.Test;

import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.ObjectMapper;

public class CheckColumnLabelExistTest extends ObjectMapperTestAbstract {
	
	private ExtendedObjectMapper mapper = new ExtendedObjectMapper();
	
	private boolean invokeCheckColumnLabelExist( ResultSet result, String columnLabel ){
		try{
			
			return mapper.testCheckColumnLabelExist( result, columnLabel );
			
		}catch( SQLException e ){
			e.printStackTrace();
			fail( e.getMessage() );
		}
		return false;
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
	 * checkColumnLabelExist tests															*
	 ****************************************************************************************/
	
	@Test
	public void itShouldReturnTrueIfGivenColumnLabelExistInGivenResultset(){
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
			
			boolean exist = this.invokeCheckColumnLabelExist( result, "user_name");
			assertTrue( "checkColumnLabelExist should return true if given label exist in given result set", exist );
			
	}
	
	@Test
	public void itShouldReturnFalseIfGivenColumnLabelDoesNotExistInGivenResultset(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "name");
		assertFalse( "checkColumnLabelExist should return false if given label does not exist in given result set", exist );
		
	}
	
	@Test
	public void itShouldReturnFalseIfGivenResultSetIsNull(){
			
			boolean exist = this.invokeCheckColumnLabelExist( null, "user_name");
			
			assertFalse( "checkColumnLabelExist should return false if given ResultSet is null", exist );
			
	}
	
	@Test
	public void itShouldReturnFalseIfGivenColumnNameIsNullOrEmptyString(){
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
			
			boolean exist = this.invokeCheckColumnLabelExist( result, null );
			
			assertFalse( "checkColumnLabelExist should return false if given column name is null", exist );
			
			exist = this.invokeCheckColumnLabelExist( result, "" );
			
			assertFalse( "checkColumnLabelExist should return false if given column name is empty string", exist );
	}
	
	@Test
	public void itShouldReturnTrueIfGivenColumnNameIsFirstColumnInResultSet(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "user_id" );
		
		assertTrue( "checkColumnLabelExist should return false if given column name is is first column in result", exist );
			
	}
	
	@Test
	public void itShouldReturnTrueWhenThereAreTwoColumnsWithSameLabelInGivenResultSet(){
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name, name as user_id FROM user WHERE id LIKE 'testID'" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "user_id" );
		
		assertTrue( "checkColumnLabelExist should return true when there are two columns with same label in given ResultSet", exist );
		
	}
	
	@Test
	public void itShouldReturnTrueWhenThereAreTwoColumnsWithSameLabelFromTwoDifferentTabel(){
		
		createMockTableManager();
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT user.id, user.name, manager.id, manager.name FROM user INNER JOIN manager ON user.id = manager.id" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "name" );
		
		assertTrue( "checkColumnLabelExist should return true when there are two column with same label from two different tabel", exist );
		
		exist = this.invokeCheckColumnLabelExist( result, "id" );
		
		assertTrue( "checkColumnLabelExist should return true when there are two column with same label from two different tabel", exist );
		
		dropMockTableManager();
	}
	
	@Test
	public void itShouldReturnTrueWhenThereAreTwoColumnsWithSameLabelFromTwoDifferentTableAlais(){
		
		createMockTableManager();
		
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT u.id, u.name, m.id, m.name FROM user u INNER JOIN manager m ON u.id = m.id" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "name" );
		
		assertTrue( "checkColumnLabelExist should return true when there are two columns with same label from two different table alias", exist );
		
		exist = this.invokeCheckColumnLabelExist( result, "id" );
		
		assertTrue( "checkColumnLabelExist should return true when there are two columns with same label from two different table alias", exist );
		
		dropMockTableManager();
	}
}
