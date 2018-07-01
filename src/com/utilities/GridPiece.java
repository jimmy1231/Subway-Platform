package com.utilities;

import java.util.ArrayList;

public class GridPiece {
	
	private float x_pos;
	private float y_pos;
	private float gridWidth;
	private float gridHeight;
	private Boolean clicked; 
	private Boolean occupied;
	private Component component;
	private ArrayList<GridPiece> relations;
	
	public GridPiece(float x_pos, float y_pos, float gridWidth, float gridHeight) {
		this.x_pos = x_pos;
		this.y_pos = y_pos;
		this.gridWidth = gridWidth;
		this.gridHeight = gridHeight;
		this.clicked = false; 
		this.setOccupied(false);
		this.setRelations(null);
	}
	
	public GridPiece() {
		
	}
	
	public Component getComponent() {
		return component;
	}
	public void setComponent(Component component) {
		this.component = component;
	}
	public float getGridWidth() {
		return gridWidth;
	}
	public void setGridWidth(float gridWidth) {
		this.gridWidth = gridWidth;
	}
	public float getGridHeight() {
		return gridHeight;
	}
	public void setGridHeight(float gridHeight) {
		this.gridHeight = gridHeight;
	}
	public float getX_pos() {
		return x_pos;
	}
	public void setX_pos(float x_pos) {
		this.x_pos = x_pos;
	}
	public float getY_pos() {
		return y_pos;
	}
	public void setY_pos(float y_pos) {
		this.y_pos = y_pos;
	}

	public Boolean getClicked() {
		return clicked;
	}

	public void setClicked(Boolean clicked) {
		this.clicked = clicked;
	}

	public Boolean getOccupied() {
		return occupied;
	}

	public void setOccupied(Boolean occupied) {
		this.occupied = occupied;
	}

	public ArrayList<GridPiece> getRelations() {
		return relations;
	}

	public void setRelations(ArrayList<GridPiece> relations) {
		this.relations = relations;
	}
}
