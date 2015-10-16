package com.cinergix.mapper.annotation;

/**
 * This Annotation class used to annotate the field and to indicate which result field is mapped to this field.
 * @author rasekaran
 *
 */
public @interface ResultField {

	/**
	 * To hold the field name which maps to the property
	 */
	String fieldName();
}
