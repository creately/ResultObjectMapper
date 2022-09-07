package com.cinergix.mapper.data;

import com.cinergix.mapper.annotation.ResultField;

public class SimpleColumnMappedUserMock {

	@ResultField( "id" )
	private String id;
	@ResultField( "name" )
	private String name;
	@ResultField( "email" )
	private String officeEmail;
	@ResultField( "email" )
	private String personalEmail;
	
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
	public String getOfficeEmail() {
		return officeEmail;
	}
	public void setOfficeEmail(String officeEmail) {
		this.officeEmail = officeEmail;
	}
	public String getPersonalEmail() {
		return personalEmail;
	}
	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}
	
}
