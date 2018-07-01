package com.utilities;

import com.enums.Symbols;

public class Button extends Component {
	
	private Symbols symbol;
	private SimPiece s_piece; 
	
	public Button(float x_center, float y_center, float width, float height, Symbols symbol, SimPiece s_piece) {
		super.x_center = x_center;
		super.y_center = y_center;
		super.width = width;
		super.height = height;
		this.symbol = symbol;
		this.s_piece = s_piece; 
	}
	
	public SimPiece getS_piece() {
		return s_piece;
	}

	public void setS_piece(SimPiece s_piece) {
		this.s_piece = s_piece;
	}



	public Symbols getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbols symbol) {
		this.symbol = symbol;
	}
}
