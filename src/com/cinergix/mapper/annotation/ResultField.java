package com.cinergix.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an annotation used to specify a property that maps to a field of the result set.
 * The value specific field in the result set will assigned to this property.
 * @author rasekaran
 *
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface ResultField {

	/**
	 * To hold the column label which maps to the property
	 * @return Label of the column
	 */
	String[] value();
}
