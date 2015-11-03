package com.cinergix.mapper;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.cinergix.db.DBServiceHelper;

public abstract class TestAbstractObjectMapper {

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
}
