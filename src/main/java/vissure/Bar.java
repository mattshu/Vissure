package vissure;

import java.awt.Color;

public class Bar {

	private double width;
	private double height;
	private Color color;
	private final static Color DEFAULT_COLOR = Color.white;
	
	public Bar(double width, double height) {
		this(width, height, DEFAULT_COLOR);
	}
	
	public Bar(double width, double height, Color color) {
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setHeight(double height) {
		this.height = height;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
	
	public void resetColor() {
		color = DEFAULT_COLOR;
	}
	
}
