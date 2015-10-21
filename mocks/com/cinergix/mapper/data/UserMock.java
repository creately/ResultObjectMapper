package com.cinergix.mapper.data;

import java.util.Date;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;
import com.cinergix.mapper.annotation.ResultObject;
import com.cinergix.mapper.transformer.ResultTransformerMock;
import com.cinergix.mapper.transformer.annotation.ResultTransformer;
import com.cinergix.mapper.transformer.annotation.ResultTransformerClass;

@ResultMapped
@ResultTransformerClass( transformerClass = ResultTransformerMock.class )
public class UserMock {

	@ResultField( fieldName = "user_id" )
	private String id;
	@ResultField( fieldName = "user_name" )
	@ResultTransformer( methodName = "getFirstName" )
	private String name;
	@ResultField( fieldName = "user_email" )
	private String email;
	@ResultField( fieldName = "user_age" )
	private int age;
	@ResultObject
	@ResultField( fieldName = "manager" )
	private SimpleUserMock manager;
	
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public SimpleUserMock getManager() {
		return manager;
	}
	public void setManager(SimpleUserMock manager) {
		this.manager = manager;
	}
	
}
