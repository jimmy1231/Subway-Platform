package com.utilities;

public class RBG {
	
	private float r;
	private float b;
	private float g;
	private float alpha;
	
	public RBG(float r, float b, float g, float alpha) {
		this.r = r;
		this.b = b;
		this.g = g;
		this.alpha = alpha;
	}
	
	public float getR() {
		return r;
	}
	public void setR(float r) {
		this.r = r;
	}
	public float getB() {
		return b;
	}
	public void setB(float b) {
		this.b = b;
	}
	public float getG() {
		return g;
	}
	public void setG(float g) {
		this.g = g;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
}
