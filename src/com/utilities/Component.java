package com.utilities;

public class Component {

	protected float x_center;
	protected float y_center;
	protected float width;
	protected float height;
	
	public Component(float width, float height) {
		this.width = width; 
		this.height = height;
	}
	
	public Component() {
		
	}
	
	public float getX_center() {
		return x_center;
	}
	public void setX_center(float x_center) {
		this.x_center = x_center;
	}
	public float getY_center() {
		return y_center;
	}
	public void setY_center(float y_center) {
		this.y_center = y_center;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	
}
