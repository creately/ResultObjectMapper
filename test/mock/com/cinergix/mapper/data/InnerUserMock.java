package com.cinergix.mapper.data;

import java.util.Date;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;
import com.cinergix.mapper.annotation.ResultObject;

@ResultMapped
public class InnerUserMock {

	@ResultField( "user_id" )
	private String id;
	@ResultField( "user_name" )
	private String name;
	@ResultObject
	private ProjectManagerMock manager;

	
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
	public ProjectManagerMock getManager() {
		return manager;
	}
	public void setManager(ProjectManagerMock manager) {
		this.manager = manager;
	}
	
}
