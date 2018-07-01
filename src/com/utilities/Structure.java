package com.utilities;

import com.enums.Symbols;

public class Structure extends Component {

	/* 
	 * A structure can be formed from multiple gridblocks 
	 * This object will contain all related information on a structure
	 * Its inherited objects will contain more detailed data
	 */
	private Symbols symbol;

	public Symbols getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbols symbol) {
		this.symbol = symbol;
	}
	
	
}
