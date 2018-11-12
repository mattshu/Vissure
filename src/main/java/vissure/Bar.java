package vissure;

import java.awt.Color;

public class Bar {

	private int height;
	private Color color;
	
	public Bar(int height) {
		this(height, Color.black);
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
	
	public Color getColor() {
		return color;
	}
	
}
