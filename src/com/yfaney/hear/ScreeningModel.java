package com.yfaney.hear;

public class ScreeningModel {
	private int ID;
	private String firstName;
	private String lastName;
	private String userId;
	private String createdOn;
	public ScreeningModel(int ID, String firstName, String lastName, String userId, String createdOn) {
		super();
		this.ID = ID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
		this.createdOn = createdOn;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
}
