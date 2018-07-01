package com.utilities;

public class SimWindow {
	
	/*
	 * Menubar is set at the origin position, so we must translate it to its correct position (e.g. +0.5f) 
	 * for the Buttons to be displayed at the correct positions on the window
	 * 
	 * -1				0	MENU		  1
	 * |----------------|-------|---------|  1
	 * |				|		|		  |
	 * |				|		|		  |
	 * |		height	|		|		  |  0
	 * |				|		|		  |
	 * |----------------|-------|---------| -1
	 * 					width		
	 */		
	
	private Button cart;
	private Button track;
	private Button guardrail;
	private Button guardrail2;
	private Button pedestrian;
	
	// Normalized width and height
	private float width; 
	private float height;
	private float offset;
	
	// Position of each available slot for a component at its center
	private GridPiece[][] grid;
	
	private float gridWidth;
	private float gridHeight;
	private float paddingWidth;
	private float paddingHeight;
	
	public SimWindow(float width, float height, float offset) {
		this.width = width;
		this.height = height;
		this.offset = offset;
	}
	
	public void initGrid(int x_grids, int y_grids, float padding) {
		// Padding: Leave padding% of space on the sides of the menu
		this.paddingWidth = (width * padding) / 2;
		this.paddingHeight = (height * padding) / 2;
		
		this.gridWidth = (width - this.paddingWidth * 2) / x_grids;
		this.gridHeight = (height - this.paddingHeight * 2) / y_grids;
		
		// Grid initialization stage: 
		grid = new GridPiece[y_grids][x_grids];
		
		System.out.println("Initializing Menu Grid with the following specifications:");
		System.out.println("    Num X Grids: " + x_grids);
		System.out.println("    Num Y Grids: " + y_grids);
		System.out.println("    Padding: " + padding + "%" + ", Vertical Pad: " + this.paddingHeight + ", Horizontal Pad: " + this.paddingWidth);
		System.out.println("    Offset from origin: " + this.offset);
		System.out.println("    Cell Width: " + this.gridWidth);
		System.out.println("    Cell Height: " + this.gridHeight);
		
		System.out.println("\nCoordinates: ");
		for (int row = 1; row <= y_grids; row++ ) {
			for (int col = 1; col <= x_grids; col++ ) {
				float x = this.offset + this.paddingWidth + ( (this.gridWidth * col) - (this.gridWidth / 2) );
				float y = (this.height / 2) - this.paddingHeight - (this.gridHeight * row) + (this.gridHeight / 2); 
				
				// This is - 1 since our for loop indexes from 1
				grid[row - 1][col - 1] = new GridPiece(x, y, gridWidth, gridHeight);
				
				System.out.println("       " + "(" + (row - 1) + "," + (col - 1) + ")" + " -> X: " + x + ", Y: " + y);
			}
		}
	}
	
	public GridPiece getPiece(Coords c) {
		
		return null;
	}
		
	public GridPiece[][] getGrid() {
		return grid;
	}

	public void setGrid(GridPiece[][] grid) {
		this.grid = grid;
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

	public float getOffset() {
		return offset;
	}

	public void setOffset(float offset) {
		this.offset = offset;
	}

	public Button getCart() {
		return cart;
	}

	public void setCart(Button cart) {
		this.cart = cart;
	}
	
	public Button getTrack() {
		return track;
	}

	public void setTrack(Button track) {
		this.track = track;
	}

	public Button getGuardrail() {
		return guardrail;
	}

	public void setGuardrail(Button guardrail) {
		this.guardrail = guardrail;
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

	public float getPaddingWidth() {
		return paddingWidth;
	}

	public void setPaddingWidth(float paddingWidth) {
		this.paddingWidth = paddingWidth;
	}

	public float getPaddingHeight() {
		return paddingHeight;
	}

	public void setPaddingHeight(float paddingHeight) {
		this.paddingHeight = paddingHeight;
	}

	public Button getGuardrail2() {
		return guardrail2;
	}

	public void setGuardrail2(Button guardrail2) {
		this.guardrail2 = guardrail2;
	}

	public Button getPedestrian() {
		return pedestrian;
	}

	public void setPedestrian(Button pedestrian) {
		this.pedestrian = pedestrian;
	}

}
