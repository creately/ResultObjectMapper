package com.cinergix.mapper.transformer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation used to bind a transformer method from a IResultTransformer class to a property.
 * The bound method will be used to convert the result and the return value of the method will be
 * assigned to the property. The input parameters for the method will be the filed(s) specified by the
 * <code>ResultField</code> annotation.
 * 
 * For this annotation to be used, the class using the annotation should be annotated with
 * <code>ResultTransformerClass</code>
 * @author rasekaran
 *
 */
@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface ResultTransformer {

	/**
	 * Name of the method which will be use to convert the data before assign to the property.
	 * @return name of the method as strings.
	 */
	String value();
}
