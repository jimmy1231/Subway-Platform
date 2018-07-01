package com.utilities;

import java.util.ArrayList;

import static com.utilities.GridHelper.*;
import static com.utilities.PedestrianOperations.*;


public class InputHelper {

	/* This function places the piece onto the grid (sim) */
	public static Boolean placeGridStructure(Button btn, GridPiece gp, SimWindow sim, ArrayList<Pedestrian> pedestrians, ArrayList<GridPiece> structgrids) {
		
		/* 
		 * Structure will be placed with the current GridPiece as the BOTTOM LEFT CORNER
		 * 	(1) Evaluate whether grids are not out of bounds for occupancy 
		 *  (2) Place grids
		 *  (3) Assign relations to each grid, this will be useful way later
		 *  
		 *  Note: We will only be working with SQUARE configurations, thus, y_num is # rows, x_num is # cols
		 */
		
		System.out.println("Hi we're here!");
		int x_num = btn.getS_piece().getX_num();
		int y_num = btn.getS_piece().getY_num();
		boolean computePedestrian = structgrids.size() == 0 ? true : false;
		
		GridPiece[][] grid = sim.getGrid();
		ArrayList<GridPiece> gpl = new ArrayList<>();
		RowCol rc = getIndexFromCoords(sim, new Coords(gp.getX_pos(), gp.getY_pos()));
		
		for (int row = rc.getRow(); (row >= 0) && (row > (rc.getRow() - x_num)); row-- ) {
			if (row >= grid.length) {
				gpl.clear();
				return false; 
			}
			
			for (int col = rc.getCol(); col < (rc.getCol() + y_num); col++ ) {
				if ((col >= grid[0].length) || grid[row][col].getOccupied()) {
					gpl.clear();
					return false;
				}
				gpl.add(grid[row][col]);
			}
		}
		
		/* If we made it here, that means the grid is valid */
		for (int row = rc.getRow(); row > (rc.getRow() - x_num); row-- ) {
			for (int col = rc.getCol(); col < (rc.getCol() + y_num); col++ ) {
				structgrids.add(grid[row][col]);
				grid[row][col].setRelations(gpl);
				grid[row][col].setOccupied(true);
				grid[row][col].setComponent(btn);
			}
		}
		
		if (computePedestrian) {
			recomputeDestination(sim, structgrids, pedestrians);
		}

		return true;
	}

	/*
	 * This function provides a non permanent outline of the structure onto the grid
	 */
	public static Boolean sketchStructure(Button btn, GridPiece gp, SimWindow sim, ArrayList<GridPiece> gpl) {

		/*
		 * Structure will be placed with the current GridPiece as the BOTTOM LEFT CORNER
		 * (1) Evaluate whether grids are not out of bounds for occupancy (2) Trace
		 * grids Note: We will only be working with SQUARE configurations, thus, y_num
		 * is # rows, x_num is # cols
		 */
		gpl.clear();

		int x_num = btn.getS_piece().getX_num();
		int y_num = btn.getS_piece().getY_num();

		GridPiece[][] grid = sim.getGrid();
		RowCol rc = getIndexFromCoords(sim, new Coords(gp.getX_pos(), gp.getY_pos()));

		for (int row = rc.getRow(); (row >= 0) && (row > (rc.getRow() - x_num)); row--) {
			if (row >= grid.length) {
				gpl.clear();
				return false;
			}

			for (int col = rc.getCol(); col < (rc.getCol() + y_num); col++) {
				if ((col >= grid[0].length) || grid[row][col].getOccupied()) {
					gpl.clear();
					return false;
				}
				gpl.add(grid[row][col]);
			}
		}

		return true;
	}
}
