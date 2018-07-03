package com.menu;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.enums.Symbols;
import com.utilities.Button;
import com.utilities.Coords;
import com.utilities.GridPiece;
import com.utilities.Pedestrian;
import com.utilities.PedestrianOperations;
import com.utilities.RBG;
import com.utilities.SimPiece;
import com.utilities.SimWindow;
import com.vbo.Sidebar;
import com.vbo.SidebarSymbols;

import static com.menu.VboHelper.*;
import static com.utilities.GridHelper.*;
import static com.utilities.InputHelper.*;
import static com.utilities.PedestrianOperations.*;
import static com.menu.MathHelper.*;

public class GameMenu {

	/**
	 * We wish to accomplish the following tasks in this program:
	 * 
	 * (1) Create a side bar in the window which will act as a menu side bar 2.
	 *     Click and drag functionality for side bar to place different boxes at
	 *     different locations
	 * 
	 * (2) Snap objects on to grid, make buttons clickable and meaningful: so each
	 *     button represents a different object
	 * 
	 * (3) Have a basic, customizable layout for the subway - preloaded objects
	 *     (e.g. subway track, tunnel, emergency vehicle) - textures for each object
	 *     (instead of boxes) - keep at squares for now
	 * 
	 * BONUS: create pedestrians (as small boxes) which will travel from one box to
	 * another in a straight line
	 */
	private static int height = 800;
	private static int width = 1200;
	private static long window;
	private static float curr_xpos;
	private static float curr_ypos;
	private static float draw_xpos;
	private static float draw_ypos;

	private static SimWindow menu;
	private static SimWindow sim;

	/* VBO's */
	private static Sidebar sidebar;
	private static Sidebar simbar;
	private static SidebarSymbols cart;
	private static SidebarSymbols track;
	private static SidebarSymbols guardrail;
	private static SidebarSymbols guardrail2;
	private static SidebarSymbols pedestrian;

	/* Menu clicked */
	private static Boolean menu_clicked = false;
	private static Button button_clicked = null;

	/*
	 * Stores grids that have been hovered after clicked, displays trace for that
	 * structure Should be empty if there are no grids selected
	 */
	private static ArrayList<GridPiece> struct_trace = new ArrayList<>();
	private static ArrayList<Pedestrian> pedestrians = new ArrayList<>();
	
	/* 
	 * "structs" contains all GridPieces of all structures 
	 * This is used to determine pedestrian destination. We keep track of all grids 
	 * used for 1 "structure" because we want to spread out the pedestrian destinations 
	 * such that its not concentrated to a single grid of a multi-grided structure
	 */
	private static ArrayList<GridPiece> structgrids = new ArrayList<>();

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Hello World!");

		glfwInit();

		createWindow();
		setInputCallbacks();
		initVbos();

		gameLoop();

		glfwTerminate();
		System.exit(0);
	}

	public static void setInputCallbacks() {

		/**
		 * This function is called on mouse hover interrupt, for now, it only handles
		 * structure tracing (after the user has clicked on menu to place an object to
		 * sim window
		 * 
		 * Trivial case is when menu has not been clicked, in which case we can do
		 * nothing so, just return
		 * 
		 * (1) Check which side of the canvas user is hovering (2) If user is hovering
		 *     on menu, we also return as we can do nothing (3) If user is hovering over SIM
		 *     side, we trace out the structure they want to place - run the program to see
		 *     how it works (4) We set a global list of gridpieces to be shown as
		 *     "structure trace". This list of gridpieces will be rendered in the rendering
		 *     loop - see renderGrid()
		 */
		glfwSetCursorPosCallback(window, (long win, double xpos, double ypos) -> {

			curr_xpos = (float) xpos;
			curr_ypos = (float) ypos;

			if (!menu_clicked) {
				return;
			}

			draw_xpos = norm(curr_xpos, "x", (float) width, (float) height);
			draw_ypos = norm(curr_ypos, "y", (float) width, (float) height);

			Coords c = new Coords(draw_xpos, draw_ypos);
			Symbols s = whichGrid(c, menu, sim);
			GridPiece gp = whichPiece(c, menu, sim, s);

			if (s.equals(Symbols.SIM) && menu_clicked && (button_clicked != null)) {
				sketchStructure(button_clicked, gp, sim, struct_trace);
			}

		});

		/**
		 * This function is called on mouse click interrupt. It handles all clicks to
		 * the window. For now, the only case handled is if the user clicks on the menu
		 * to select an object.
		 * 
		 * We classify clicks as FirstClick, and SecondClick. We keep track of this by
		 * setting the menu_clicked variable, which indicates whether the menu has been
		 * clicked. FirstClick: User clicked on menu when menu_clicked is false
		 * SecondClick: User clicked on menu when menu_clicked is true
		 * 
		 * The logic behind this is to know when we're "picking up" - FirstClick - an
		 * object and when we want to "place" the object that was clicked - SecondClick.
		 * 
		 * On FirstClick: (1) Set menu_clicked to TRUE; set all related globals to
		 * appropriate values/objects
		 * 
		 * On SecondClick: (1) Put object down on SIM window (2) Set menu_clicked (and
		 * all related globals) to FALSE/null
		 */
		glfwSetMouseButtonCallback(window, (long win, int button, int action, int mods) -> {
			if ((button == GLFW_MOUSE_BUTTON_RIGHT || button == GLFW_MOUSE_BUTTON_LEFT) && action == GLFW_PRESS) {
				draw_xpos = norm(curr_xpos, "x", (float) width, (float) height);
				draw_ypos = norm(curr_ypos, "y", (float) width, (float) height);

				System.out.println("Clicked on: " + draw_xpos + ", " + draw_ypos);

				Coords c = new Coords(draw_xpos, draw_ypos);
				Symbols s = whichGrid(c, menu, sim);
				GridPiece gp = whichPiece(c, menu, sim, s);

				System.out.println("menu clicked: " + menu_clicked + ", button_clicked: " + (button_clicked == null ? null
								: (button_clicked.getX_center() + ", " + button_clicked.getY_center())));

				if (s.equals(Symbols.MENU) && (!menu_clicked) && (button_clicked == null)
						&& (gp.getComponent() != null)) {

					/* Handle FirstClick */
					if (!menu_clicked && (button_clicked == null) && (gp != null)) {
						Button btn = (Button) gp.getComponent();
						
						if (btn.getSymbol().equals(Symbols.S_PEDESTRIAN)) {
							/* Spawn pedestrians */
							spawnPedestrian(sim, structgrids, pedestrians);
						}
						else {
							button_clicked = (Button) gp.getComponent();
							menu_clicked = true;
						}
					}

				} else if (s.equals(Symbols.SIM) && (menu_clicked) && (button_clicked != null)) {
					/*
					 * Handle SecondClick here, this will wait until a click is valid (meaning it
					 * will the entire grid, before setting everything back to null
					 */
					Boolean constructed = placeGridStructure(button_clicked, gp, sim, pedestrians, structgrids);

					if (constructed) {
						struct_trace.clear();
						button_clicked = null;
						menu_clicked = false;
					}
				}
			}
		});
	}

	/**
	 * We can customize our initialization process by passing in different
	 * parameters for the components we want to attach to the window. Below is a
	 * description of each component and how their parameters can be manipulated:
	 * 
	 * SimWindow(float width, float height, float offset)
	 * 
	 * @param width:
	 *            width of the component (to normalized coordinates) -1 to 1 height:
	 *            height of the component (normalized) offset: the x component
	 *            offset from the origin (0) of the normalized coordinate
	 * 
	 *            Button(float x_center, float y_center, float width, float height,
	 *            Symbols symbol) EXTENDS Component
	 * @param x/y_center:
	 *            center normalized coordinates of the component on the window
	 *            width: width of the component (normalized) height: height of the
	 *            component (normalized) symbol: classifier for the component;
	 *            S_CART, S_TRACK, S_GUARD_RAIL; depending on the symbol, different
	 *            textures are loaded when displaying that component
	 */
	public static void initVbos() {

		/* 
		 * Managing all components (e.g. buttons, marks etc.) - storage space for all
		 * Backend components not involving pedestrians
		 */
		menu = new SimWindow(0.5f, 2.0f, 0.5f);
		menu.initGrid(2, 4, 0.0f);

		/* Part of the window which carries out all the simulations, and control */
		sim = new SimWindow(1.5f, 2.0f, -1.0f);
		sim.initGrid((int) (sim.getWidth() / 0.07f), (int) (sim.getHeight() / 0.1f), 0.0f);

		/* Side bar of the game (located on the left strip of the window) */
		sidebar = initSidebarVbo(menu);
		simbar = initMainSimWindow(sim);

		/* Initialize cart buttons and snap them to the menu grid */
		Button cart_btn = new Button(-0.1f, 0.3f, 0.1f, 0.1f, Symbols.S_CART, new SimPiece(3, 2));
		menu.setCart(cart_btn);
		snap(menu.getGrid(), cart_btn, false);
		cart = initSidebarSymbolsVbo(cart_btn, cart_btn.getSymbol());

		Button track_btn = new Button(0.5f, 0.5f, 0.1f, 0.1f, Symbols.S_TRACK, new SimPiece(4, 2));
		menu.setTrack(track_btn);
		snap(menu.getGrid(), track_btn, false);
		track = initSidebarSymbolsVbo(track_btn, track_btn.getSymbol());

		Button guardrail_btn = new Button(0.1f, 0.3f, 0.1f, 0.1f, Symbols.S_GUARD_RAIL, new SimPiece(5, 1));
		menu.setGuardrail(guardrail_btn);
		snap(menu.getGrid(), guardrail_btn, false);
		guardrail = initSidebarSymbolsVbo(guardrail_btn, guardrail_btn.getSymbol());
		
		Button guardrail_btn2 = new Button(0.1f, 0.3f, 0.1f, 0.1f, Symbols.S_GUARD_RAIL, new SimPiece(1, 5));
		menu.setGuardrail(guardrail_btn2);
		snap(menu.getGrid(), guardrail_btn2, false);
		guardrail2 = initSidebarSymbolsVbo(guardrail_btn2, guardrail_btn2.getSymbol());
		
		Button pedestrian_btn = new Button(0.1f, 0.3f, 0.1f, 0.1f, Symbols.S_PEDESTRIAN, new SimPiece(1,1));
		menu.setPedestrian(pedestrian_btn);
		snap(menu.getGrid(), pedestrian_btn, false);
		pedestrian = initSidebarSymbolsVbo(pedestrian_btn, pedestrian_btn.getSymbol());
	}

	public static void gameLoop() throws InterruptedException {
		
		while (!glfwWindowShouldClose(window)) {
			glfwPollEvents();
			PedestrianOperations.pedestrianMove(sim, pedestrians);
			
			render();
			glfwSwapBuffers(window);
		}
	}

	public static void render() throws InterruptedException {

		glClear(GL_COLOR_BUFFER_BIT);

		/* Render the side bar through Sidebar VBO */
		sidebar.render();
		simbar.render();

		renderGrid(menu.getGrid(), menu.getGridWidth(), menu.getGridHeight(), new RBG(0.75f, 0.75f, 0.75f, 0.0f),
				Symbols.MENU);
		renderGrid(sim.getGrid(), sim.getGridWidth(), sim.getGridHeight(), new RBG(0.90f, 0.90f, 0.90f, 0.0f),
				Symbols.SIM);

		cart.render();
		track.render();
		guardrail.render();
		guardrail2.render();
		pedestrian.render();
	}

	public static void renderGrid(GridPiece[][] grid, float gridWidth, float gridHeight, RBG rbg, Symbols s) throws InterruptedException {

		/*
		 * Here's the plan for snapping grids: (1) Set fixed sizes for each object to be
		 * perfectly aligned with the size of the grid a). This means only the size of
		 * the grid is up to us to decide, the rest are based on the grid size (i.e.
		 * button size, object size, pedestrian size, etc.)
		 * 
		 * (2) The grids that are snapped by an object will be "invalid", which means
		 *     that object placements are not allowed to go there: this is convenient in
		 *     several ways: i). Pedestrians cannot go on "invalid" squares ii). New objects
		 *     (i.e. guardrails) cannot go on this path iii). When detecting validity of
		 *     placement, can scan the surrounding grids to provide visual indication for
		 *     invalid placement
		 * 
		 * (3) Snap flow: a). User clicks button - first click (i.e. guardrail) b).
		 *     dragging time is defined by the time between first and second clicks c).
		 *     During dragging time, the object will appear on screen (for visual) d). Once
		 *     user clicks mouse a second time, dragging time is over e). The object will be
		 *     placed where the user specified (if valid)
		 */

		glBegin(GL_QUADS);

		gridWidth -= 0.0025f;
		gridHeight -= 0.0025f;
		
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				GridPiece piece = grid[row][col];

				if (piece.getOccupied() && (piece.getComponent() != null)) {
					Symbols symbol;

					if (piece.getComponent() instanceof Button) {
						Button btn = (Button) piece.getComponent();
						symbol = btn.getSymbol();
					}
					else if (piece.getComponent() instanceof Pedestrian) {

						TimeUnit.MILLISECONDS.sleep(50);
						symbol = Symbols.S_PEDESTRIAN;
						Pedestrian ped = (Pedestrian) piece.getComponent();
						if (ped.getNext_move() != null) {
							renderPedestrian(ped);
						}
					}
					else {
						System.out.println("Piece Class: " + piece.getComponent());
						throw new NullPointerException("Piece is NULL when drawing");
					}
					
					if (symbol.equals(Symbols.S_TRACK))
						glColor4f(0.4f, 0.6f, 0.0f, 0.0f);
					else if (symbol.equals(Symbols.S_CART))
						glColor4f(0.4f, 0.4f, 1.0f, 0);
					else if (symbol.equals(Symbols.S_GUARD_RAIL))
						glColor4f(1.0f, 0.6f, 0.6f, 0);
					else if (symbol.equals(Symbols.S_PEDESTRIAN))
						glColor4f(0.8f, 0.4f, 0.6f, 0.0f);

					glVertex2f(piece.getX_pos() - gridWidth / 2, piece.getY_pos() - gridHeight / 2);
					glVertex2f(piece.getX_pos() - gridWidth / 2, piece.getY_pos() + gridHeight / 2);
					glVertex2f(piece.getX_pos() + gridWidth / 2, piece.getY_pos() + gridHeight / 2);
					glVertex2f(piece.getX_pos() + gridWidth / 2, piece.getY_pos() - gridHeight / 2);
					continue;
				}
				
				/*
				 * glColor4f: Setting the colors for each of the points glVertex2f: Drawing each
				 * vertex of the quad, and at the end, is filled in
				 */
				glColor4f(rbg.getR(), rbg.getB(), rbg.getG(), rbg.getAlpha());
				glVertex2f(piece.getX_pos() - gridWidth / 2, piece.getY_pos() - gridHeight / 2);
				glColor4f(rbg.getR(), rbg.getB(), rbg.getG(), rbg.getAlpha());
				glVertex2f(piece.getX_pos() - gridWidth / 2, piece.getY_pos() + gridHeight / 2);
				glColor4f(rbg.getR(), rbg.getB(), rbg.getG(), rbg.getAlpha());
				glVertex2f(piece.getX_pos() + gridWidth / 2, piece.getY_pos() + gridHeight / 2);
				glColor4f(rbg.getR(), rbg.getB(), rbg.getG(), rbg.getAlpha());
				glVertex2f(piece.getX_pos() + gridWidth / 2, piece.getY_pos() - gridHeight / 2);
			}
		}

		if (s.equals(Symbols.SIM)) {
			for (int i = 0; i < struct_trace.size(); i++) {
				GridPiece piece = struct_trace.get(i);

				glColor4f(0.25f, 0.9f, 0.2f, 0.0f);
				glVertex2f(piece.getX_pos() - gridWidth / 2, piece.getY_pos() - gridHeight / 2);
				glVertex2f(piece.getX_pos() - gridWidth / 2, piece.getY_pos() + gridHeight / 2);
				glVertex2f(piece.getX_pos() + gridWidth / 2, piece.getY_pos() + gridHeight / 2);
				glVertex2f(piece.getX_pos() + gridWidth / 2, piece.getY_pos() - gridHeight / 2);
			}
		}
		glEnd();
	}
	
	public static void renderPedestrian(Pedestrian ped) {

		GridPiece gp = ped.getNext_move();
		gp.setComponent(ped);
		gp.setOccupied(true);
		ped.getGp().setComponent(null);
		ped.getGp().setOccupied(false);
		ped.setGp(gp);
		ped.setNext_move(null);
	}

	public static void createWindow() {
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		window = glfwCreateWindow(width, height, "GameMenu", NULL, NULL);

		glfwMakeContextCurrent(window);

		createCapabilities();
	}
}
