package com.cinergix.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to indicate the class that can be mapped to a result set.
 * Indicates that the class has properties that map to result set's fields.
 * Properties that map to a field must use the <code>ResultField</code> annotation
 * @author rasekaran
 *
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.TYPE )
public @interface ResultMapped {

}
