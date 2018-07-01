package com.utilities;

import java.lang.Math;

import com.enums.Symbols;

public class GridHelper {

	/**
	 * Finds available space for the component to be placed on the grid. Based on
	 * the following algorithm. Priority is on horizontal placement, once the row
	 * has been filled with components, move on to the next row
	 * 
	 * Set the component parameter for GridPieces that contain this component to
	 * component
	 * 
	 * @param grid: the grid matrix which will be the framework for all operations
	 * on the window component: the component we wish to find a place for on the
	 * grid system
	 */
	public static void snap(GridPiece[][] grid, Component comp, Boolean isSim) {
		
		/* 
		 * This is for all the SIM window snaps
		 * Anything after this should be from Menu click (Or pedestrian, but we haven't got that far yet)
		 */
		
		if (!(comp instanceof Button)) {
			throw new AssertionError("This is not supposed to happen!"); 
		}
		
		int rowNum = grid.length;
		int colNum = grid[0].length;

		GridPiece piece = grid[0][0];

		float gridHeight = piece.getGridHeight();
		float gridWidth = piece.getGridWidth();

		float compHeight = comp.getHeight();
		float compWidth = comp.getWidth();
		
		// Determine how many grid pieces we need to fit component
		int x_pieces = 0;
		int y_pieces = 0;
		
		if (gridWidth > compWidth)
			x_pieces = 1;
		else
			x_pieces = (int) Math.ceil((double) compWidth / gridWidth);

		if (gridHeight > compHeight)
			y_pieces = 1;
		else
			y_pieces = (int) Math.ceil((double) compHeight / gridHeight);

		System.out.println("X_PIECES: " + x_pieces + " Y_PIECES: " + y_pieces);
		System.out.println("ROW_NUM: " + rowNum + " COL_NUM: " + colNum);
		
		// Determine where to put the component on the grid
		if (y_pieces > rowNum || x_pieces > colNum)
			return;

		else {
			for (int row = 0; row < grid.length; row++) {
				for (int col = 0; col < grid[row].length; col++) {
					boolean test_result = pieceElligibility(grid, row, col, x_pieces, y_pieces);
					
					if (test_result) {						
						computeCenter(grid[row][col], x_pieces, y_pieces, comp);
						setGridToComponent(grid, row, col, x_pieces, y_pieces, comp);
						
						return;
					}
				}
			}
		}
	}

	/**
	 * This method is used for window autosnapping: (sort of like AutoCAD) where the
	 * window is represented by a grid. This method is called when the user has
	 * clicked on This method will take in the following as parameters:
	 * 
	 * @param grid: the grid matrix which will be the framework for all operations
	 * on the window component: the component that is currently viewed to be placed
	 * onto the grid coords: the current mouse position on hover
	 * 
	 */
	public static Coords autosnap(GridPiece[][] grid, Component component, Coords coords) {

		return null;
	}
	
	public static void printgrid(GridPiece[][] grid) {
		System.out.println("\nprinting grid:\n------------------------------------------------------\n");
		for (int row = 0; row < grid.length; row++ ) {
			for (int col = 0; col < grid[row].length; col++ ) {
				GridPiece gp = grid[row][col];
				boolean isComponent = gp.getComponent() == null ? false : true;
				
				System.out.println("X: " + gp.getX_pos() + "Y: " + gp.getY_pos() + ", Components: " + isComponent);
			}
		}
	}
	
	public static RowCol getIndexFromCoords(SimWindow window, Coords c) {
		float offset, height, g_width, g_height;
		float x = c.getX(), y = c.getY();
		int row, col;
		
		x = (x == 1.0f) ? (c.getX() - 0.001f) : x;
		
		offset = window.getOffset();
		height = window.getHeight();
		g_width = window.getGridWidth();
		g_height = window.getGridHeight();
		
		/* change to absolute coordinates for easier calculations (0 - 2 instead of -1 - 1) */
		x = (x + 1) - (1 + offset);
		y = height - (y + 1);
		
		col = (int) (x / (g_width));
		row = (int) (y / (g_height));
		
		return new RowCol(row, col);
	}
	
	/* 
	 * Takes in the actual mouse coordinates of the points clicked (x, y) in float
	 * Returns s: 	Which grid the click belongs to (SIM, or MENU) 
	 */
	public static GridPiece whichPiece(Coords c, SimWindow menu, SimWindow sim, Symbols s) {
		
		GridPiece gp = null; 
		
//		System.out.println("Finding piece for : (" + c.getX() + "," + c.getY() + ")");
		if (s.equals(Symbols.MENU)) 
		{
			RowCol rc = getIndexFromCoords(menu, c);
			gp = menu.getGrid()[rc.getRow()][rc.getCol()];
		}
		else if (s.equals(Symbols.SIM))
		{
			RowCol rc = getIndexFromCoords(sim, c);
			gp = sim.getGrid()[rc.getRow()][rc.getCol()];
		}
		
		return gp;
	}
	
	/* 
	 * Takes in the actual mouse coordinates of the points clicked (x, y) in float
	 * Returns s: 	Which grid the click belongs to (SIM, or MENU) 
	 */
	public static Symbols whichGrid(Coords c, SimWindow menu, SimWindow sim) {
		
		float menu_xstart = menu.getOffset(); 
		float menu_xend = menu.getOffset() + menu.getWidth();
		
		float sim_xstart = sim.getOffset(); 
		float sim_xend = sim.getOffset() + sim.getWidth();
		
		float x = c.getX();
		
		if ((x >= menu_xstart) && (x <= menu_xend)) 
		{
			return Symbols.MENU;
		}
		else if ((x > sim_xstart) && (x < sim_xend)) 
		{
			return Symbols.SIM;
		}
		
		return null;
	}
	
	public static GridPiece getDown(GridPiece[][] grid, int row, int col) {
		if (row + 1 > (grid.length - 1))
			return null;
		else
			return grid[row + 1][col];
	}

	public static GridPiece getUp(GridPiece[][] grid, int row, int col) {
		if (row - 1 < 0)
			return null;
		else
			return grid[row - 1][col];
	}

	public static GridPiece getLeft(GridPiece[][] grid, int row, int col) {
		if (col - 1 < 0)
			return null;
		else
			return grid[row][col - 1];
	}

	public static GridPiece getRight(GridPiece[][] grid, int row, int col) {
		if (col + 1 > (grid[row].length - 1))
			return null;
		else
			return grid[row][col + 1];
	}
	
	public static GridPiece getUpLeft(GridPiece[][] grid, int row, int col) {
		if (((row - 1) < 0) || ((col - 1) < 0)) 
			return null;
		else 
			return grid[row - 1][col - 1];
	}
	
	public static GridPiece getUpRight(GridPiece[][] grid, int row, int col) {
		if (((row - 1) < 0) || ((col + 1) > grid[row].length - 1))
			return null;
		else 
			return grid[row - 1][col + 1];
	}
	
	public static GridPiece getDownLeft(GridPiece[][] grid, int row, int col) {
		if (((row + 1) > (grid.length - 1)) || ((col - 1) < 0))
			return null;
		else
			return grid[row + 1][col - 1];
	}
	
	public static GridPiece getDownRight(GridPiece[][] grid, int row, int col) {
		if (((row + 1) > (grid.length - 1)) || ((col + 1) > grid[row].length - 1))
			return null;
		else
			return grid[row + 1][col + 1];
	}

	private static boolean pieceElligibility(GridPiece[][] grid, int row, int col, int x_pieces, int y_pieces) {

		boolean test_result = true;

		// Test for horizontal
		for (int i = col; i < col + x_pieces; i++) {
			if (i > grid[row].length)
				break;
			else {
				GridPiece gp = grid[row][i];
				if (gp.getComponent() != null) {
					test_result = false;
					break;
				}
			}
		}

		// Test for veritcal
		if (test_result) {
			for (int j = row; j < row + y_pieces; j++) {
				if (j > grid.length)
					break;
				else {
					GridPiece gp = grid[j][col];
					if (gp.getComponent() != null) {
						test_result = false;
						break;
					}
				}
			}
		}

		return test_result;
	}
	
	private static void setGridToComponent(GridPiece[][] grid, int row, int col, int x_pieces, int y_pieces, Component comp) {
		for (int i = row; i < row + y_pieces; i++ ) {
			for (int j = col; j < col + x_pieces; j++ ) {
				GridPiece gp = grid[i][j];
				gp.setComponent(comp);
			}
		}
	}
	
	private static void computeCenter(GridPiece gp, int x_pieces, int y_pieces, Component comp) {		
		float x_left = gp.getX_pos() - (gp.getGridWidth() / 2);
		float y_top = gp.getY_pos() + (gp.getGridHeight() / 2);
		
		float width = gp.getGridWidth() * x_pieces;
		float height = gp.getGridHeight() * y_pieces;
		
		float x_cen = x_left + (width) / 2;
		float y_cen = y_top - (height) / 2;
		
		comp.setX_center(x_cen);
		comp.setY_center(y_cen);
	}

}
