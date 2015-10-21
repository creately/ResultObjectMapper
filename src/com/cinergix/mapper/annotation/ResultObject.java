package com.cinergix.mapper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be used to indicate recursive mapping. Mapping will continue into the
 * property if this annotation is added. Should only be used on a property that is of
 * custom type and, the type class is annotated by <code>ResultMapped</code>.
 * The ObjectResultMapper will create the class and map all it's fields and attach the class
 * instance to this property.
 * @author rasekaran
 *
 */

@Retention( RetentionPolicy.RUNTIME )
@Target( ElementType.FIELD )
public @interface ResultObject {

}
