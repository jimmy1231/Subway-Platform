package com.utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.enums.Directions;
import com.menu.MathHelper;

import static com.utilities.GridHelper.*;


public class PedestrianOperations {

	/*
	 * Tweak pedestrian positions by 1 block
	 * If pedestrian's next block is it's destination, delete the pedestrian
	 */
	public static void pedestrianMove(SimWindow sim, ArrayList<Pedestrian> pedestrians) {
		
		/* Put all finished pedestrians here so we can delete them */
		ArrayList<Pedestrian> finished = new ArrayList<>();
		Pedestrian ped = null;
		GridPiece next_move = null;
		boolean isDest = false;
//		RowCol next_rc = null, cur_rc = null;
		
		for (int i = 0; i < pedestrians.size(); i++ ) {
			ped = pedestrians.get(i); 
			List<Object> ret = nextPedestrianStep(sim, ped);
			
			isDest = (Boolean) ret.get(0);
			next_move = (GridPiece) ret.get(1);
			
			if (next_move == null) {
				System.out.println("we are null?");
				continue;
			} 
			
//			cur_rc = getIndexFromCoords(sim, new Coords(ped.getGp().getX_pos(), ped.getGp().getY_pos()));
//			next_rc = getIndexFromCoords(sim, new Coords(next_move.getX_pos(), next_move.getY_pos()));
			
			if (!ped.getGp().getOccupied()) {
				throw new NullPointerException();
			}
//			System.out.println("Pedestrian Moving: "
//					+ "cur(" + cur_rc.getRow() + "," + cur_rc.getCol() + ") "
//					+ "next(" + next_rc.getRow() + "," + next_rc.getCol() + ") "
//					+ "dest(" + ped.getDestination().getRow() + "," + ped.getDestination().getCol() + ")");
	
			if (isDest) {
				System.out.println("We've reached destination!");
				ped.getGp().setComponent(null);
				ped.getGp().setOccupied(false);
				ped.setGp(null);
				finished.add(ped);
				
				continue;
			}
			
			
			ped.setNext_move(next_move);
		}
		
		/* Clean up finished pedestrians */
		for (int i = 0; i < finished.size(); i++ ) {
			pedestrians.remove(finished.get(i));
		}
		finished.clear();
	}
	
	/*
	 * On Pedestrian creation, each Pedestrian should be given a destination Upon
	 * reaching that destination, the Pedestrian is destroyed
	 */

	public static Pedestrian spawnPedestrian(SimWindow sim, ArrayList<GridPiece> structgrids,
			ArrayList<Pedestrian> pedestrians) {

		/* Pick a number from 1 to number of grids */
		GridPiece[][] grid = sim.getGrid();
		int rows = grid.length;
		int cols = grid[0].length;
		int row, col;
		Random r = new Random();
		boolean valid = false;
		Pedestrian ped = null;

		while (!valid) {
			row = r.nextInt(rows - 1);
			col = r.nextInt(cols - 1);

			if (!grid[row][col].getOccupied()) {
				ped = new Pedestrian();
				ped.setDestination(randomDestination(sim, structgrids));

				if (ped.getDestination() != null)
					System.out.println("pedestrian destination: (" + ped.getDestination().getRow() + ","
							+ ped.getDestination().getCol() + ")");

				ped.setGp(grid[row][col]);
				grid[row][col].setOccupied(true);
				grid[row][col].setComponent(ped);
				pedestrians.add(ped);

				valid = true;
			}
		}

		return ped;
	}

	/*
	 * Returns RowCol of a random structure gridpiece calculated by taking random
	 * from 0 to size of structure array
	 */
	public static RowCol randomDestination(SimWindow sim, ArrayList<GridPiece> structgrids) {

		Random r = new Random();
		GridPiece gp;
		RowCol rc = null;
		int rand;

		if (structgrids.size() > 0) {
			rand = r.nextInt(structgrids.size() - 1);
			gp = structgrids.get(rand);
			rc = getIndexFromCoords(sim, new Coords(gp.getX_pos(), gp.getY_pos()));
		}

		return rc;
	}

	/* Compute destination for pedestrians that have not yet been assigned one */
	public static void recomputeDestination(SimWindow sim, ArrayList<GridPiece> structgrids,
			ArrayList<Pedestrian> pedestrians) {

		Pedestrian ped = null;

		for (int i = 0; i < pedestrians.size(); i++) {
			ped = pedestrians.get(i);

			if (ped.getDestination() == null) {
				ped.setDestination(randomDestination(sim, structgrids));

				System.out.println("new pedestrian destination: (" + ped.getDestination().getRow() + ","
						+ ped.getDestination().getCol() + ")");
			}
		}
	}
	
	/*
	 * Compute next move for the pedestrian 
	 * 	- Negative x means going left
	 * 	- Negative y means going down 
	 * 	- Positive x means going right 
	 * 	- Positive y means going up
	 * 
	 * 	(1) Calculate x and y 
	 */
	public static List<Object> nextPedestrianStep(SimWindow sim, Pedestrian ped) {
		
		float x, y;
		boolean isDest = false;
		RowCol dest = ped.getDestination();
		RowCol ped_rc = getIndexFromCoords(sim, new Coords(ped.getGp().getX_pos(), ped.getGp().getY_pos()));
		GridPiece[][] grid = sim.getGrid();
		GridPiece dest_gp = grid[dest.getRow()][dest.getCol()];
		Directions dir = Directions.DIRECTIONLESS;
		
		Coords p1 = new Coords(ped.getGp().getX_pos(), ped.getGp().getY_pos());
		Coords p2 = new Coords(dest_gp.getX_pos(), dest_gp.getY_pos());
		Coords separate = new Coords();
		
		MathHelper.euclidist(p1, p2, separate);
		x = separate.getX();
		y = separate.getY();
		
		/* Determine directions */
		
		/* left down: could be LEFT, DOWN, or LEFTDOWN */
		if ((x < 0.0f) && (y <= 0.0f)) {
			x = Math.abs(x);
			y = Math.abs(y);
			
			if (x > y) 
				dir = Directions.LEFT;
			else if (x < y)
				dir = Directions.DOWN;
			else if (x == y) 
				dir = Directions.DOWNLEFT;
		}
		/* left up: could be LEFT, UP, or LEFTUP */
		else if ((x < 0.0f) && (y >= 0.0f)) {
			x = Math.abs(x);
			y = Math.abs(y);
			
			if (x > y) 
				dir = Directions.LEFT;
			else if (x < y)
				dir = Directions.UP;
			else if (x == y) 
				dir = Directions.UPLEFT;
		}
		/* right down: could be RIGHT, DOWN, RIGHTDOWN */
		else if ((x > 0.0f) && (y <= 0.0f)) {
			x = Math.abs(x);
			y = Math.abs(y);
			
			if (x > y) 
				dir = Directions.RIGHT;
			else if (x < y)
				dir = Directions.DOWN;
			else if (x == y) 
				dir = Directions.DOWNRIGHT;
			
		}
		/* right up: could be RIGHT, UP, RIGHTUP */
		else if ((x > 0.0f) && (y >= 0.0f)) {
			x = Math.abs(x);
			y = Math.abs(y);
			
			if (x > y) 
				dir = Directions.RIGHT;
			else if (x < y)
				dir = Directions.UP;
			else if (x == y) 
				dir = Directions.UPRIGHT;
		}
		/* in the case where x == 0.0f */
		else {
			if (y > 0.0f) 
				dir = Directions.UP;
			else
				dir = Directions.DOWN;
		}
		
		/* 
		 * Determine next RowCol 
		 * 	(1) See if the decided direction is available 
		 *  (2) If it is available, reserve it by marking it as occupied
		 *  		- Note: it is okay that we reserve 2 spots, the first spot will get freed when we actually move
		 *  (3) If not available, pick any other directions that are available and reserve it
		 *  (4) If no spots are available, the pedestrian stays still (return null)
		 */
		GridPiece gp = null;
		
		if (dir.equals(Directions.UP)) {
			gp = GridHelper.getUp(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		else if (dir.equals(Directions.DOWN)) {
			gp = GridHelper.getDown(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		else if (dir.equals(Directions.LEFT)) {
			gp = GridHelper.getLeft(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		else if (dir.equals(Directions.RIGHT)) {
			gp = GridHelper.getRight(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		else if (dir.equals(Directions.UPRIGHT)) {
			gp = GridHelper.getUpRight(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		else if (dir.equals(Directions.UPLEFT)) {
			gp = GridHelper.getUpLeft(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		else if (dir.equals(Directions.DOWNRIGHT)) {
			gp = GridHelper.getDownRight(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		else if (dir.equals(Directions.DOWNLEFT)) {
			gp = GridHelper.getDownLeft(sim.getGrid(), ped_rc.getRow(), ped_rc.getCol());
			if (gp == null || (gp.getOccupied() && gp.getComponent() instanceof Pedestrian))
				dir = Directions.DIRECTIONLESS;
			else {
				gp.setOccupied(true);
			}
		}
		
		if (!dir.equals(Directions.DIRECTIONLESS)) {
			if (gp.getComponent() instanceof Button) {
				/* Check if the structure is our destination */
				
				ArrayList<GridPiece> gpl = gp.getRelations();
				
				for (int i = 0; i < gpl.size(); i++ ) {
					GridPiece piece = gpl.get(i);
					RowCol struct_rc = getIndexFromCoords(sim, new Coords(piece.getX_pos(), piece.getY_pos()));
					RowCol gp_rc = getIndexFromCoords(sim, new Coords(gp.getX_pos(), gp.getY_pos()));
					if (MathHelper.sameRowCol(struct_rc, gp_rc)) {
						isDest = true;
						break;
					}
				}
			}
		}
		
		/* TODO: Handle case where optimal route is unavailable */
		
		/* Compose the return list: this is because we want to return 2 values and a list is a way to do it */
		List<Object> l = new ArrayList<>();
		l.add(isDest);
		l.add(gp);

		return l;
	}
	
}
