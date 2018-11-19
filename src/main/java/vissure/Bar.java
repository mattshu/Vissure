package vissure;

import java.awt.Color;

public class Bar {

	private int height;
	private Color color;
	private final static Color DEFAULT_COLOR = Color.white;
	
	public Bar(int height) {
		this(height, DEFAULT_COLOR);
	}
	
	public Bar(int height, Color color) {
		this.height = height;
		this.color = color;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void resetColor() {
		color = DEFAULT_COLOR;
	}
	
	public Color getColor() {
		return color;
	}
	
}
