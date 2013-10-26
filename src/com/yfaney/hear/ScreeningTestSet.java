package com.yfaney.hear;

public class ScreeningTestSet {
	private int frequency;
	private short deciBel;
	private int earSide;

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
	public int getEarSide() {
		return earSide;
	}
	public void setEarSide(int earSide) {
		this.earSide = earSide;
	}
	public ScreeningTestSet(int frequency, short deciBel, int earSide) {
		super();
		this.frequency = frequency;
		this.deciBel = deciBel;
		this.earSide = earSide;
	}
	
}
