package com.cinergix.mapper.data;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;

@ResultMapped
public class PrivateContructorUserMock {

	@ResultField( "user_name" )
	private String name;
	private PrivateContructorUserMock(){
		
	}
}
