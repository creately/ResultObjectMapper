package com.cinergix.mapper.data;

import com.cinergix.mapper.annotation.ResultField;
import com.cinergix.mapper.annotation.ResultMapped;
import com.cinergix.mapper.annotation.ResultObject;

@ResultMapped
public class ProjectManagerMock {

	@ResultField( "manager_id" )
	private String id;
	@ResultField( "manager_name" )
	private String name;
	@ResultObject
	private PersonalAssistantMock personalAssistant;
	@ResultObject
	private InnerUserMock seniorManager;
	
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
	public PersonalAssistantMock getPersonalAssistant() {
		return personalAssistant;
	}
	public void setPersonalAssistant(PersonalAssistantMock personalAssistant) {
		this.personalAssistant = personalAssistant;
	}
	public InnerUserMock getSeniorManager() {
		return seniorManager;
	}
	public void setSeniorManager(InnerUserMock seniorManager) {
		this.seniorManager = seniorManager;
	}
	
}
