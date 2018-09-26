package com.example.drawMe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * Simple drawing application that draws Lines and Rectangles on a Canvas.
 * 
 * @author jeffrey.v.bunagan@gmail.com
 *
 */
@SpringBootApplication
public class DrawMeApplication {

	private static boolean isCanvasDrawn = false;
	private static boolean terminate = false;
	private static String[][] plane;
	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static final String INVALID_PARAMS = "Invalid parameters";
	private static final String INVALID_CANVAS_PARAMS = "Invalid canvas parameters";
	private static final String INVALID_LINE_PARAMS = "Invalid line parameters";
	private static final String INVALID_RECT_PARAMS = "Invalid rectangle parameters";

	public static void main(String[] args) {
		while (!terminate) {
			System.out.print("Enter command: ");
			String line = "";
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (line != null && !line.isEmpty()) {
				String[] params = line.split(" ");
				// Line validation
				if (params.length == 1 || params.length == 3 || params.length == 5) {
					switch (params[0].toUpperCase()) {
					// Case C - Canvas
					case "C":
						try {
							// Input validation
							if (!isCanvasDrawn && params.length == 3 && Integer.parseInt(params[1]) > 2
									&& Integer.parseInt(params[2]) > 2) {
								isCanvasDrawn = true;
								// Create plane and draw canvas
								Canvas canvas = new Canvas(params[0].toUpperCase(), Integer.parseInt(params[1]),
										Integer.parseInt(params[2]));
								canvas.draw();
								plane = canvas.getPlane();
							} else {
								System.out.println(INVALID_CANVAS_PARAMS);
							}
						} catch (NumberFormatException ex) {
							System.out.println(INVALID_CANVAS_PARAMS);
						}
						break;
					// Case L - Line
					case "L":
						try {
							// Input validation
							if (isCanvasDrawn && isParamsValid(params)
									&& (Integer.parseInt(params[1]) == Integer.parseInt(params[3])
											|| Integer.parseInt(params[2]) == Integer.parseInt(params[4]))) {
								// Create line and draw on plane
								Rectangle straightline = new Rectangle(params[0].toUpperCase(),
										Integer.parseInt(params[1]), Integer.parseInt(params[2]),
										Integer.parseInt(params[3]), Integer.parseInt(params[4]), plane);
								straightline.draw();
							} else {
								System.out.println(INVALID_LINE_PARAMS);
							}
						} catch (NumberFormatException ex) {
							System.out.println(INVALID_LINE_PARAMS);
						}
						break;
					// Case R - Rectangle
					case "R":
						try {
							// Input validation
							if (isCanvasDrawn && isParamsValid(params)) {
								// Create rectangle and draw on plane
								Rectangle rectangle = new Rectangle(params[0].toUpperCase(),
										Integer.parseInt(params[1]), Integer.parseInt(params[2]),
										Integer.parseInt(params[3]), Integer.parseInt(params[4]), plane);
								rectangle.draw();
							} else {
								System.out.println(INVALID_RECT_PARAMS);
							}
						} catch (NumberFormatException ex) {
							System.out.println(INVALID_RECT_PARAMS);
						}
						break;
					// Quit
					case "Q":
						terminate = true;
						break;
					default:
						System.out.println(INVALID_PARAMS);
						break;

					}
				} else {
					System.out.println(INVALID_PARAMS);
				}

			}
		}
	}

	// method to validate if coordinates are within the canvas and init coordinates are less than final coordinates
	private static boolean isParamsValid(String[] params) {
		boolean valid = false;
		if (params.length == 5 && Integer.parseInt(params[1]) > 0 && Integer.parseInt(params[2]) > 0
				&& Integer.parseInt(params[3]) > 0 && Integer.parseInt(params[4]) > 0
				&& Integer.parseInt(params[1]) < plane[0].length - 1 && Integer.parseInt(params[2]) < plane.length - 1
				&& Integer.parseInt(params[3]) < plane[0].length - 1
				&& Integer.parseInt(params[4]) < plane.length - 1 && Integer.parseInt(params[1]) <= Integer.parseInt(params[3])
						&& Integer.parseInt(params[2]) <= Integer.parseInt(params[4])) {
			valid = true;
		}
		return valid;
	}

	// method for setting reader for testing or running
	public static void setReader(Reader reader) {
		DrawMeApplication.reader = new BufferedReader(reader);
	}

	public static void setTerminate(boolean terminate) {
		DrawMeApplication.terminate = terminate;
	}

}

// Abstract class Shape
abstract class Shape {

	// declare fields
	private String objectName = " ";
	private int startx, starty, endx, endy = 0;
	private String[][] plane;

	Shape(String name, int startx, int starty, int endx, int endy, String[][] plane) {
		this.objectName = name;
		this.startx = startx;
		this.starty = starty;
		this.endx = endx;
		this.endy = endy;
		this.plane = plane;
	}

	abstract public void draw();

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public int getStartx() {
		return startx;
	}

	public void setStartx(int startx) {
		this.startx = startx;
	}

	public int getStarty() {
		return starty;
	}

	public void setStarty(int starty) {
		this.starty = starty;
	}

	public int getEndx() {
		return endx;
	}

	public void setEndx(int endx) {
		this.endx = endx;
	}

	public int getEndy() {
		return endy;
	}

	public void setEndy(int endy) {
		this.endy = endy;
	}

	public String[][] getPlane() {
		return plane;
	}

	public void setPlane(String[][] plane) {
		this.plane = plane;
	}

}

// Rectangle class that plots and draws according to coordinates. Line is also considered a one-dimensional Rectangle
class Rectangle extends Shape {
	private static final int BUFFER = 1;
	private String xchar = "x";
	private String ychar = "x";

	Rectangle(String name, int startx, int starty, int endx, int endy, String[][] plane) {
		super(name, startx, starty, endx, endy, plane);
	}

	Rectangle(String name, String xchar, String ychar, int startx, int starty, int endx, int endy, String[][] plane) {
		super(name, startx, starty, endx, endy, plane);
		this.xchar = xchar;
		this.ychar = ychar;
	}

	@Override
	public void draw() {
		// plot horizontal line
		for (int i = 0; i <= this.getEndx() - this.getStartx(); i++) {
			this.getPlane()[this.getStarty()][this.getStartx() + i] = this.xchar;
			this.getPlane()[this.getEndy()][this.getStartx() + i] = this.xchar;
		}
		// plot vertical line
		for (int i = BUFFER; i <= this.getEndy() - this.getStarty() - BUFFER; i++) {
			this.getPlane()[this.getStarty() + i][this.getStartx()] = this.ychar;
			this.getPlane()[this.getStarty() + i][this.getEndx()] = this.ychar;
		}
		// print
		for (int y = 0; y < this.getPlane().length; y++) {
			for (int x = 0; x < this.getPlane()[0].length; x++) {
				System.out.print(this.getPlane()[y][x] != null ? this.getPlane()[y][x] : " ");
			}
			System.out.println("\n");
		}
	}
}

// Canvas is a Rectangle drawn with some added buffer
class Canvas extends Rectangle {
	private static final int BUFFER = 2;
	private static String xchar = "-";
	private static String ychar = "|";

	Canvas(String name, int endx, int endy) {
		super(name, xchar, ychar, 0, 0, endx + 1, endy + 1, new String[endy + BUFFER][endx + BUFFER]);
	}

}
