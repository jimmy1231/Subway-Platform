package com.vbo;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import static com.menu.VboHelper.*;

import com.enums.Symbols;

public class SidebarSymbols {
	
	/* Symbols are things that you can click on the Sidebar to populate the window
	 * 
	 * 								Sidebar
	 * |----------------------------------|
	 * |							|x x  |
	 * |							|x x  |
	 * |							|x x  |
	 * |							|	  |
	 * |----------------------------------|
	 * The x's represent symbols
	 */	
	
	private int v_id;
	private int draw_count;
	private Symbols symbol;
	
	public SidebarSymbols(float[] vertices, Symbols symbol) {
		this.draw_count = vertices.length;
		this.v_id = initVertexBuffer(vertices);
		this.symbol = symbol;
	}
	
	public void render() {
		
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		
		glVertexPointer(2, GL_FLOAT, 0, 0);
		setSymbolColour(this.symbol);
		glDrawArrays(GL_TRIANGLES, 0, this.draw_count);
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	/*
	 * Color codes for Symbols Enums (for now):
	 * S_TRACK 		- Green (0, 255, 0, 0)
	 * S_CART 		- RED (255, 0, 0, 0)
	 * S_GUARD_RAIL - BLUE (0, 0, 128, 0)
	 * S_PEDESTRIAN - PURPLE 
	 */
	private void setSymbolColour(Symbols symbol) {
		if (symbol.equals(Symbols.S_TRACK))
			glColor4f(0.4f, 0.6f, 0.0f, 0.0f);
		else if (symbol.equals(Symbols.S_CART))
			glColor4f(0.4f, 0.4f, 1.0f, 0);
		else if (symbol.equals(Symbols.S_GUARD_RAIL))
			glColor4f(1.0f, 0.6f, 0.6f, 0);
		else if (symbol.equals(Symbols.S_PEDESTRIAN))
			glColor4f(0.8f, 0.4f, 0.6f, 0.0f);
	}

}
