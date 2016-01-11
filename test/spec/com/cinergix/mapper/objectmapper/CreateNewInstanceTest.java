package com.cinergix.mapper.objectmapper;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.cinergix.mapper.ObjectMapperTestAbstract;
import com.cinergix.mapper.data.AbstractUserMock;
import com.cinergix.mapper.data.InterfaceUserMock;
import com.cinergix.mapper.data.PrivateContructorUserMock;
import com.cinergix.mapper.exception.ObjectCreationException;

public class CreateNewInstanceTest extends ObjectMapperTestAbstract {

	@Test
	public void itShouldReturnNullIfGivenClassIsNull(){
		
		TestableObjectMapper mapper = new TestableObjectMapper();
		assertNull( "createNewInstance should return null if given class is null", mapper.testCreateNewInstance( null ) );
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfGivenDataClassIsAbstract(){
			
			TestableObjectMapper mapper = new TestableObjectMapper();
			
			mapper.testCreateNewInstance( AbstractUserMock.class );
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfGivenDataClassIsAnInterface(){
			
			TestableObjectMapper mapper = new TestableObjectMapper();
			
			mapper.testCreateNewInstance( InterfaceUserMock.class );
	}
	
	@Test( expected = ObjectCreationException.class )
	public void itShouldShouldThrowObjectCreationExceptionIfTheEmptyConstructorIsPrivate(){
		
		TestableObjectMapper mapper = new TestableObjectMapper();
		mapper.testCreateNewInstance( PrivateContructorUserMock.class );
	}
}
