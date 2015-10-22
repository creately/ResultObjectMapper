package com.cinergix.mapper;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;

/**
 * This class provides main functionalities of “ResultObjectMapper”.
 * <code>ObjectMapper</code> converts <code>ResultSet</code> data into java objects. To do this 
 * conversion Java classes should be annotated with <code>ResultMapped</code> in class level. To 
 * assign the column data to a property of a mapped object that property should be annotated with 
 * <code>ResultField</code> and label of the column should be assigned to <code>fieldName</code> 
 * property of <code>ResultField</code>.
 * 
 * Column value from  <code>ResultSet</code> can be manipulated before assign to property. To do 
 * that, class should be annotated with<code>ResultTransformerClass</code> and the type of 
 * transformer (Which should implement <code>IResultTransformer</code>) should be assigned to 
 * <code>treasformerClass</code> property of <code>ResultTransformerClass</code>.
 * 
 * If a property is Custom type and that class also annotated as mentioned above then 
 * <code>ResultSet</code> will be assigned recursively.
 * @author rasekaran
 *
 * @param <T>
 */

public class ObjectMapper<T> {

	/**
	 * Converts and returns a list of objects of type <code>T</code>. Those objects will have 
	 * column values of given result of type <type>ResultSet</code>. 
	 * @param result SQL result of type <code>ResultSet</code>.
	 * @param dataClass Type of object in list which is returned by this method.
	 * @return This method returns a list of object f type <code>T</code>
	 */
public List<T> mapResultSetToObject( ResultSet result, Class<T> dataClass ) {
		
		if( result == null || dataClass == null  || dataClass.getAnnotation( ResultMapped.class ) == null ){
			return null;
		}
		
		List<T> resultObjectList = new ArrayList<T>();
		
		try{
			
			HashMap< Field, String > map = getFieldColumnMapping( dataClass );
			
			// To check atleast one mapped value found in the result.
			// If not we don't have to go further( Don't need to iterate or Don't need to return list of Objects )
			boolean valueFound = false;
			
			// Iterate through result
			while( result.next() ) {
				
				// Create instance of DataClass to populate the data
				T newObj = (T) dataClass.newInstance();
				
				for( Field field : map.keySet() ){
					try{
						
						Object resValue = parseValue( result, map.get( field ), field.getType() );
						if( resValue != null ){
							assignValueToField( newObj, field, resValue );
							valueFound = true;
						}
						
					}catch( SQLException sqlex ){
						// TODO Have no idea how to handle this.( May be we don't have to handle this )
						// sqlex.printStackTrace();
					}
				}
				if( !valueFound ){
					
					return resultObjectList;
				}
				
				resultObjectList.add( newObj );
			}
		} catch( InstantiationException ie ){
			ie.printStackTrace();
		} catch ( IllegalAccessException iae) {
			iae.printStackTrace();
		} catch ( SQLException sqle ) {
			sqle.printStackTrace();
		}
		return resultObjectList;
	}
	
	protected void assignValueToField( T createdObject, Field field, Object value ){
		try{
			
			field.setAccessible( true );
			field.set( createdObject, value );
			field.setAccessible( false );
		} catch( IllegalAccessException e ){
			// TODO IllegalAccessException should be handled
			//e.printStackTrace();
		} catch ( IllegalArgumentException e2) {
			// TODO IllegalArgumentException should be handled
			//e2.printStackTrace();
		}
	}
	
	private Object parseValue( ResultSet result, String columnName, Class typeClass ) throws SQLException {
			
		if( typeClass.equals( String.class ) ){
			
			return result.getString( columnName );
		} else if( typeClass.equals( Byte.class ) || typeClass.equals( byte.class ) ){
			
			return result.getByte( columnName );
		} else if( typeClass.equals( Short.class ) || typeClass.equals( short.class ) ){
			
			return result.getShort( columnName );
		} else if( typeClass.equals( Integer.class ) || typeClass.equals( int.class ) ){
			
			return result.getInt( columnName );
		} else if( typeClass.equals( Long.class ) || typeClass.equals( long.class ) ){
			
			return result.getLong( columnName );
		} else if( typeClass.equals( Double.class ) || typeClass.equals( double.class ) ){
			
			return result.getDouble( columnName );
		} else if( typeClass.equals( Float.class ) || typeClass.equals( float.class ) ){
			
			return result.getFloat( columnName );
		} else if( typeClass.equals( Boolean.class ) || typeClass.equals( boolean.class ) ){
			
			return result.getBoolean( columnName );
		} else if( typeClass.equals( Date.class ) ){
			
			return result.getTimestamp( columnName );
		}
		return null;
	}
	
	private HashMap<Field, String> getFieldColumnMapping( Class dataClass ){
		HashMap<Field, String> map = new HashMap<Field, String>();

		for( Field field : dataClass.getDeclaredFields() ){
			
			ResultField resultAnnotation = field.getAnnotation( ResultField.class );
			if( resultAnnotation != null ){
				
				String sqlFieldName = resultAnnotation.value();
				map.put( field, sqlFieldName );
			}
		}
		return map;
	}
}
