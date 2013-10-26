package com.yfaney.hear;

public class ScreeningModel {
	private String firstName;
	private String lastName;
	private String userId;
	private int earSide;
	private int frequency;
	private short deciBel;
	public ScreeningModel(String firstName, String lastName, String userId,
			int earSide, int frequency, short deciBel) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.userId = userId;
		this.earSide = earSide;
		this.frequency = frequency;
		this.deciBel = deciBel;
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
	public int getEarSide() {
		return earSide;
	}
	public void setEarSide(int earSide) {
		this.earSide = earSide;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public short getDeciBel() {
		return deciBel;
	}
	public void setDeciBel(short deciBel) {
		this.deciBel = deciBel;
	}
}
