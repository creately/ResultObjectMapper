package com.cinergix.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.cinergix.db.DBServiceHelper;

public abstract class ObjectMapperTestAbstract {

	protected static DBServiceHelper dbHelper = DBServiceHelper.getInstance();
	
	@BeforeClass
	public static void beforeFunction(){
		
		// To create the Test database
		dbHelper.createTestDatabase();
	}
	
	@AfterClass
	public static void afterFunction(){
		
		// To delete the Test database
		dbHelper.destroyDataBase();
	}
	
	// This class has public methods which are directly calling protected methods.
	// This is very useful to test protected methods of ObjectMapper class.
	protected class ExtendedObjectMapper<T> extends ObjectMapper<T>{
		
		public ExtendedObjectMapper(){
			super();
		}
		
		public boolean testCheckColumnLabelExist( ResultSet result, String columnLabel ) throws SQLException{
			return this.checkColumnLabelExist( result, columnLabel );
		}
	}
}
