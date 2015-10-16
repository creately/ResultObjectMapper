package com.cinergix.mapper.transformer.annotation;

import com.cinergix.mapper.transformer.IResultTransformer;

/**
 * This annotation uses to indicate Transformer classes which will be used to convert the result before assign the value to property.
 * @author rasekaran
 *
 */
public @interface ResultTransformerClass {

	Class< ? extends IResultTransformer> transformerClass();
}
