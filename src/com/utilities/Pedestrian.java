package com.utilities;

public class Pedestrian extends Component {
	
	/* These should be rowcol of SIM window */
	private RowCol destination; 
	private GridPiece next_move;
	private GridPiece gp;
	
	public Pedestrian() {
		
	}
	
	public RowCol getDestination() {
		return destination;
	}
	public void setDestination(RowCol destination) {
		this.destination = destination;
	}
	public GridPiece getNext_move() {
		return next_move;
	}
	public void setNext_move(GridPiece next_move) {
		this.next_move = next_move;
	}

	public GridPiece getGp() {
		return gp;
	}

	public void setGp(GridPiece gp) {
		this.gp = gp;
	}
}
