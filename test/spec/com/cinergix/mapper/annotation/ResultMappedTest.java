package com.cinergix.mapper.annotation;

import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;

import com.cinergix.mapper.data.UserMock;

public class ResultMappedTest {

	@Test
	public void resultFieldAnnotationShouldBeAbleToMapToAClass(){
		Class userClass = UserMock.class;
		Annotation annotation = null;
		assertNotNull( userClass.getAnnotation( ResultMapped.class ) );
	}
}
