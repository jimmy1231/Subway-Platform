package com.vbo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;

import static com.menu.VboHelper.*;

public class Sidebar {
	
	private int draw_count; 
	private int v_id;
	
	/*
	 * 								Sidebar
	 * |----------------------------------|
	 * |							|	  |
	 * |							|	  |
	 * |							|	  |
	 * |							|	  |
	 * |----------------------------------|
	 */
	public Sidebar(float[] vertices) {
		draw_count = vertices.length;
		v_id = initVertexBuffer(vertices);
	}
	
	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		
		glVertexPointer(2, GL_FLOAT, 0, 0);
		glColor4f(1, 1, 1, 0);
		glDrawArrays(GL_TRIANGLES, 0, draw_count);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glDisableClientState(GL_VERTEX_ARRAY);
	}

}
