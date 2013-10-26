package com.yfaney.hear;

public class TestDataModel {
	private int ID;
	private int setID;
	private int earSide;
	private int frequency;
	private short deciBel;
	public TestDataModel(int iD, int setID, int earSide, int frequency,
			short deciBel) {
		super();
		ID = iD;
		this.setID = setID;
		this.earSide = earSide;
		this.frequency = frequency;
		this.deciBel = deciBel;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public int getSetID() {
		return setID;
	}
	public void setSetID(int setID) {
		this.setID = setID;
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
