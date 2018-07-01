package com.utilities;

public class Coords {

	private float x;
	private float y;
	
	public Coords(float x, float y) {
		this.x = x; 
		this.y = y;
	}
	
	public Coords() {
		/* -10 is magic number for invalid */
		this.x = -10;
		this.y = -10;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
}
