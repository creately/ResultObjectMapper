package com.cinergix.mapper.transformer.annotation;

/**
 * This annotation uses to indicate Transformer method name which will be used to convert the result before assign the value to property.
 * This to work 
 * @author rasekaran
 *
 */
public @interface ResultTransformer {

	String methodName();
}
