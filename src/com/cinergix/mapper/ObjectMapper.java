package com.cinergix.mapper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ObjectMapper<T> {

	public List<T> mapResultSetToObject( ResultSet result ) {
		
		if( result == null ){
			
			return null;
		}
		
		List<T> resultObjectList = new ArrayList<T>();
		return resultObjectList;
	}
}
