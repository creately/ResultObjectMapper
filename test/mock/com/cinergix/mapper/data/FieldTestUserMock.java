package com.cinergix.mapper.data;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;

@ResultMapped
public class FieldTestUserMock {

	@ResultField( "name" )
	private String name;
	@ResultField( "name" )
	public String firstName;
	@ResultField( "name" )
	protected String lastName;
	@ResultField( "email" )
	public static String email;
	@ResultField( "email" )
	public final String personalMail = "";
	@ResultField( "email" )
	public static final String officeMail = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		FieldTestUserMock.email = email;
	}

	public String getPersonalMail() {
		return personalMail;
	}

	public static String getOfficemail() {
		return officeMail;
	}
	
}
