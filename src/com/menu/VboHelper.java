package com.menu;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import com.enums.Symbols;
import com.utilities.Button;
import com.utilities.SimWindow;
import com.vbo.Sidebar;
import com.vbo.SidebarSymbols;

public class VboHelper {
	
	
	public static Sidebar initSidebarVbo(SimWindow menu) {
		float offset = menu.getOffset();
		float width = menu.getWidth();
		
		/* This draws 2 triangles to form a square */
		float[] vertices = new float[] {
				offset, 1.0f,
				offset, -1.0f,
				offset + width, 1.0f,
				
				offset + width, 1.0f,
				offset, -1.0f,
				offset + width, -1.0f
		};
		
		return new Sidebar(vertices);
	}
	
	/* This draws 2 triangles to form a square */
	public static Sidebar initMainSimWindow(SimWindow sim) {
		float offset = sim.getOffset();
		float width = sim.getWidth();
		
		float[] vertices = new float[] {
				offset, 1.0f,
				offset, -1.0f,
				offset + width, 1.0f,
				
				offset + width, 1.0f,
				offset, -1.0f,
				offset + width, -1.0f
		};
		
		return new Sidebar(vertices);
	}
	
	public static SidebarSymbols initSidebarSymbolsVbo(Button button, Symbols symbol) {
		float x = button.getX_center();
		float y = button.getY_center();
		float width = button.getWidth();
		float height = button.getHeight();
		
		float[] vertices = new float[] {
				x - (width / 2), y + (height / 2),
				x + (width / 2), y + (height / 2), 
				x - (width / 2), y - (height / 2),
				
				x - (width / 2), y - (height / 2),
				x + (width / 2), y - (height / 2), 
				x + (width / 2), y + (height / 2)
		};
		
		return new SidebarSymbols(vertices, symbol);
	}
	
	public static int initVertexBuffer(float[] vertices) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(vertices.length);
		buffer.put(vertices);
		buffer.flip();
		
		int v_id = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, v_id);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		return v_id;
	}

}
