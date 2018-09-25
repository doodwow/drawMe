package com.example.drawMe;

import java.util.Scanner;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DrawMeApplication {

	private static boolean isCanvasDrawn = false;
	private static boolean terminate = false;
	private static String[][] plane;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		while (!terminate) {
			System.out.println("Enter your command: ");
			String line = scanner.nextLine();
			if (line != null && !line.isEmpty()) {
				String[] params = line.split(" ");
				if (params.length == 1 || params.length == 3 || params.length == 5) {
					switch (params[0].toUpperCase()) {
					case "C":
						try {
							if (!isCanvasDrawn && params.length == 3 && Integer.parseInt(params[1]) > 2
									&& Integer.parseInt(params[2]) > 2) {
								isCanvasDrawn = true;
								Canvas canvas = new Canvas(params[0].toUpperCase(), Integer.parseInt(params[1]),
										Integer.parseInt(params[2]));
								canvas.draw();
								plane = canvas.getPlane();
							} else {
								System.out.println("Invalid canvas parameters");
							}
						} catch (NumberFormatException ex) {
							System.out.println("Invalid canvas parameters");
						}
						break;
					case "L":
					case "R":
						try {
							if (isCanvasDrawn && params.length == 5 && Integer.parseInt(params[1]) > 0
									&& Integer.parseInt(params[2]) > 0 && Integer.parseInt(params[3]) > 0
									&& Integer.parseInt(params[4]) > 0) {
								Rectangle rectangle = new Rectangle(params[0].toUpperCase(),
										Integer.parseInt(params[1]), Integer.parseInt(params[2]),
										Integer.parseInt(params[3]), Integer.parseInt(params[4]), plane);
								rectangle.draw();
							} else {
								System.out.println("Invalid rectangle/line parameters");
							}
						} catch (NumberFormatException ex) {
							System.out.println("Invalid rectangle/line parameters");
						}
						break;
					case "Q":
						terminate = true;
						break;
					}
				} else {
					System.out.println("Invalid parameters");
				}

			}
		}

		scanner.close();
	}

}

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

class Canvas extends Rectangle {
	private static final int BUFFER = 2;
	private static String xchar = "-";
	private static String ychar = "|";

	Canvas(String name, int endx, int endy) {
		super(name, xchar, ychar, 0, 0, endx + 1, endy + 1, new String[endy + BUFFER][endx + BUFFER]);
	}

}
