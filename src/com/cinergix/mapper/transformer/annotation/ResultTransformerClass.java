package com.cinergix.mapper.transformer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cinergix.mapper.transformer.IResultTransformer;

/**
 * This annotation is used to specify if the class uses transformer methods for mapping
 * properties. If a class uses this annotation it should be already annotated by <code>ResultMapped</code>
 * for this annotation to work.
 * 
 * The properties that use the transformers should annotate with <code>ResultTransformer</code> to
 * specify the method to use.
 * @author rasekaran
 *
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface ResultTransformerClass {

	/**
	 * To represent the type of transformer class which will have the methods to transform the data.
	 * @return Type of Teansformer class.
	 */
	Class< ? extends IResultTransformer> value();
}
