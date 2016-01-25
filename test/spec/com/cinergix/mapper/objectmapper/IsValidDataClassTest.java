package com.cinergix.mapper.objectmapper;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.cinergix.mapper.ObjectMapper;
import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.AbstractUserMock;
import com.cinergix.mapper.data.InterfaceUserMock;
import com.cinergix.mapper.data.PrivateContructorUserMock;
import com.cinergix.mapper.data.UnMappedUserMock;
import com.cinergix.mapper.data.UserMock;
import com.cinergix.mapper.exception.ObjectCreationException;

public class IsValidDataClassTest extends ObjectMapperTestAbstract {

	@Test
	public void itShouldReturnFalseIfTheGivenClassTypeIsNull(){
		
		TestableObjectMapper mapper = new TestableObjectMapper();
		
		assertFalse( "testIsValidDataClass Should Return false If Given class type is null", mapper.testIsValidDataClass( null ) );
	}
	
	@Test
	public void itShouldReturnFalseIfTheGivenClassTypeIsNotAnnotatedWithResultMapped(){
		
		TestableObjectMapper<UnMappedUserMock> mapper = new TestableObjectMapper<UnMappedUserMock>();
		
		assertFalse("testIsValidDataClass Should Return false If Given class type is not annotated with ResultMapped", mapper.testIsValidDataClass( UnMappedUserMock.class ) );
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfGivenDataClassIsAbstract(){
		
		TestableObjectMapper<UnMappedUserMock> mapper = new TestableObjectMapper<UnMappedUserMock>();
		
		mapper.testIsValidDataClass( AbstractUserMock.class );
		
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfGivenDataClassIsAnInterface(){
		
		TestableObjectMapper<UnMappedUserMock> mapper = new TestableObjectMapper<UnMappedUserMock>();
		
		mapper.testIsValidDataClass( InterfaceUserMock.class );
		
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfGivenDataClassDoesNotHaveEmptyConstructor(){
		
		TestableObjectMapper<PrivateContructorUserMock> mapper = new TestableObjectMapper<PrivateContructorUserMock>();
		
		mapper.testIsValidDataClass( PrivateContructorUserMock.class );
		
	}
	
	@Test
	public void itShouldReturnTrueIfTheGivenClassIsValidDataClass(){
		
		TestableObjectMapper<UnMappedUserMock> mapper = new TestableObjectMapper<UnMappedUserMock>();
		
		assertTrue("testIsValidDataClass Should Return true If Given class is a valid data class", mapper.testIsValidDataClass( UserMock.class ) );
	}
}
