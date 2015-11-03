package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import com.cinergix.mapper.TestAbstractObjectMapper;
import com.cinergix.mapper.ObjectMapper;

public class CheckColumnLabelExistTest extends TestAbstractObjectMapper {
	
	private boolean invokeCheckColumnLabelExist( ResultSet result, String columnLabel ){
		try{
			
			return ( new ObjectMapper(){
						public boolean testCheckColumnLabelExist( ResultSet result, String columnLabel ) throws SQLException{
							return this.checkColumnLabelExist( result, columnLabel );
						}
					} ).testCheckColumnLabelExist( result, columnLabel );
			
		}catch( SQLException e ){
			e.printStackTrace();
			fail();
		}
		return false;
	}

	/****************************************************************************************
	 * checkColumnLabelExist tests															*
	 ****************************************************************************************/
	
	@Test
	public void checkColumnLabelExistShouldReturnTrueIfGivenColumnLabelExistInGivenResultset(){
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
			
			boolean exist = this.invokeCheckColumnLabelExist( result, "user_name");
			assertTrue( "checkColumnLabelExist should return true if given label exist in given result set", exist );
			
	}
	
	@Test
	public void checkColumnLabelExistShouldReturnFalseIfGivenColumnLabelDoesNotExistInGivenResultset(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "name");
		assertFalse( "checkColumnLabelExist should return false if given label does not exist in given result set", exist );
		
	}
	
	@Test
	public void checkColumnLabelExistShouldReturnFalseIfGivenResultSetIsNull(){
			
			boolean exist = this.invokeCheckColumnLabelExist( null, "user_name");
			
			assertFalse( "checkColumnLabelExist should return false if given ResultSet is null", exist );
			
	}
	
	@Test
	public void checkColumnLabelExistShouldReturnFalseIfGivenColumnNameIsNull(){
			
			ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
			boolean exist = this.invokeCheckColumnLabelExist( result, null );
			
			assertFalse( "checkColumnLabelExist should return false if given column name is null", exist );
			
	}
	
	@Test
	public void checkColumnLabelExistShouldReturnFalseIfGivenColumnNameIsEmptyString(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "" );
		
		assertFalse( "checkColumnLabelExist should return false if given column name is empty string", exist );
			
	}
	
	@Test
	public void checkColumnLabelExistShouldReturnTrueIfGivenColumnNameIsFirstColumnInResultSet(){
			
		ResultSet result = dbHelper.getResultSetForQuery( "SELECT id as user_id, name as user_name FROM user WHERE id LIKE 'testID'" );
		
		boolean exist = this.invokeCheckColumnLabelExist( result, "user_id" );
		
		assertTrue( "checkColumnLabelExist should return false if given column name is is first column in result", exist );
			
	}
}
