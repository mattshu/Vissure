package vissure;

import java.awt.Color;

public class Bar {

		public int height;
		public Color color;
		
		public Bar(int height) {
			this(height, Color.black);
		}
		
		public Bar(int height, Color color) {
			this.height = height;
			this.color = color;
		}
}
