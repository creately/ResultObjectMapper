package com.cinergix.mapper;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;
import com.cinergix.mapper.exception.DataAccessException;
import com.cinergix.mapper.exception.DataTypeConversionException;
import com.cinergix.mapper.exception.ObjectCreationException;
import com.cinergix.mapper.exception.PropertyAccessException;

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
	public List<T> mapResultSetToObject( ResultSet result, Class<T> dataClass ) throws SQLException {
		
		if( result == null || dataClass == null  || ( !dataClass.isAnnotationPresent( ResultMapped.class ) ) ){
			return null;
		}
		
		if( Modifier.isInterface( dataClass.getModifiers() ) ) {
			throw new ObjectCreationException( "Given class " + dataClass.getName() + " is an Interface. dataClass should be instantiable." );
		} else if( Modifier.isAbstract( dataClass.getModifiers() ) ){
			throw new ObjectCreationException( "Given class " + dataClass.getName() + " is an Abstract class. dataClass should be instantiable." );
		}
		
		List<T> resultObjectList = new ArrayList<T>();
		
		// return empty list if the result does not have any values.
		result.last();
		if( result.getRow() == 0 ){
			return resultObjectList;
		}
		result.beforeFirst();
		
		// Get the mapped values of given class and the result
		HashMap< Field, String > map = getFieldColumnMapping( dataClass, result );
		
		if( map == null ){
			
			// If map is null then return null( dataClass or result is null ).
			return null;
			
		} else if( map.size() == 0 ){
			
			// Map size 0 means that the map is empty the no need to go further and return empty list. 
			return resultObjectList;
		}
		
		T newObj = null;
		
		// Iterate through result
		while( result.next() ) {
			
			for( Field field : map.keySet() ){
					
				Object resValue = parseValue( result, map.get( field ), field.getType() );
				if( resValue != null ){
					
					// if the newObj is null then create a new object 
					if( newObj == null ){
						try{
							newObj = (T) dataClass.newInstance();
						}catch( IllegalAccessException illEx ){
							throw new ObjectCreationException( "The class " + dataClass.getName() + " or its nullary constructor is not accessible ", illEx );
						}catch (InstantiationException insEx ) {
							throw new ObjectCreationException( "Could not create instance of " + dataClass.getName(), insEx );
						}
					}
					
					assignValueToField( newObj, field, resValue );
					
				}
			}
			
			// Add created object to the list
			resultObjectList.add( newObj );
			
			// Make newObj as null so that a new object will be created for the next tuple of data 
			newObj = null;
		}
			
		return resultObjectList;
	}
	
	protected void assignValueToField( T createdObject, Field field, Object value ){
		
		if( createdObject == null || field == null ){
			return;
		}
		
		if( !field.getDeclaringClass().equals( createdObject.getClass() ) ) {
			throw new PropertyAccessException( "Can not assign value to a field " + field.getName() + " of Type " + field.getDeclaringClass().getName() + " to instance of type " + createdObject.getClass().getName() );
		}
		
		if( Modifier.isFinal( field.getModifiers() ) ){
			throw new PropertyAccessException( "Can not assign value to a final field " + field.getName() + " of " + createdObject.getClass().getName() );
		}
		
		field.setAccessible( true );
		
		try{
			
			field.set( createdObject, value );
			
		} catch( IllegalAccessException illAccEx ){
			throw new PropertyAccessException( "Unable to access property " + field.getName() + " of " + createdObject.getClass().getName() + " " + illAccEx.getMessage(), illAccEx );
		} catch ( IllegalArgumentException illArgEx) {
			throw new PropertyAccessException( "Unable to access property " + field.getName() + " of " + createdObject.getClass().getName() + " " + illArgEx.getMessage(), illArgEx );
		}
		
		field.setAccessible( false );
	}
	
	/**
	 * Converts data from <code>ResultSet</code> to type a given Class type. This function 
	 * will throws <code>SQLException</code> since that uses the data conversion functionalities 
	 * from <code>ResultSet</code>.
	 * @param result Queried result set using a JDBC connection
	 * @param columnName Label of the column which the value to be converted
	 * @param typeClass Type of Class which the value should be convert to.
	 * @return returns a converted value.
	 * @throws SQLException
	 */
	protected Object parseValue( ResultSet result, String columnName, Class typeClass ) {
		
		if( result == null || columnName == null || columnName.trim() == "" || typeClass == null ){
			return null;
		}
		
		try{
			
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
			
		}catch( SQLException ex ){
			
			throw new DataTypeConversionException( "Unable to convert the value in " + columnName + " as " + typeClass.getName(), ex );
		}
		
		return null;
	}
	
	/**
	 * To generate a Field/Column-label pair from <code>ResultField</code> annotation of properties ofGiven Class type 
	 * @param dataClass
	 * @return 
	 */
	protected HashMap<Field, String> getFieldColumnMapping( Class dataClass, ResultSet result ) throws SQLException{
		
		if( dataClass == null || result == null ){
			return null;
		}
		
		HashMap<Field, String> map = new HashMap<Field, String>();

		for( Field field : dataClass.getDeclaredFields() ){
			
			ResultField resultAnnotation = field.getAnnotation( ResultField.class );
			if( resultAnnotation != null ){
				
				String sqlFieldName = resultAnnotation.value();
				
				if( checkColumnLabelExist( result, sqlFieldName ) ){
					map.put( field, sqlFieldName );
				}
			}
		}
			
		return map;
	}
	
	protected boolean checkColumnLabelExist( ResultSet result, String columnLabel ) throws SQLException{
		
		if( result == null || columnLabel == null || columnLabel.trim() =="" ){
			return false;
		}
		
		try{
			
			return result.findColumn( columnLabel ) > 0;
			
		}catch( SQLException ex ){
			
			if( ex.getMessage().matches( "Column(.*)not\\s+found\\." ) ){
				// "Column column_label not found." message is expected when filtering the column.
				// If the above message is thrown that means there is no such column found in given ResultSet.
			} else { 
				throw ex;
			}
		}
		
		return false;
	}
}
