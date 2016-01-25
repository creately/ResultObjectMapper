package com.cinergix.mapper.data;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;

@ResultMapped
public class PersonalAssistantMock {

	@ResultField( "pa_id" )
	private String id;
	@ResultField( "pa_name" )
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
