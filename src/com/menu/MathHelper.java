package com.menu;

import com.utilities.Coords;
import com.utilities.RowCol;

public class MathHelper {

	public static float norm(float coord, String axis, float width, float height) {

		float normalized = 0.0f;

		if (axis.equals("x")) {
			normalized = (coord / (width / 2.0f));

			if (normalized < 1.0f)
				normalized = (1.0f - normalized) * (-1.0f);
			else if (normalized > 1.0f)
				normalized = normalized - 1.0f;

		} else if (axis.equals("y")) {
			normalized = (coord / (height / 2.0f));

			if (normalized < 1.0f) {
				normalized = 1.0f - normalized;
			} else if (normalized > 1.0f) {
				normalized = (1.0f - normalized);
			}
		}

		return normalized;
	}
	
	/* 
	 * Computes Euclidian distance between points p1 and p2, reference point is p1
	 * Returns the component distance (p2.x - p1.x, p2.y - p1.y)
	 */
	public static float euclidist(Coords p1, Coords p2, Coords ret) {
		float x, y;
		
		x = p2.getX() - p1.getX();
		y = p2.getY() - p1.getY();
		
		ret.setX(x);
		ret.setY(y);
		
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
	
	public static boolean sameRowCol(RowCol one, RowCol two) {
		return ((one.getRow() == two.getRow()) && (one.getCol() == two.getCol()));
	}
}
