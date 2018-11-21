package vissure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSlider;

public class BarComponent extends JComponent implements Runnable {

	Thread runner;
	
	public final static double DEFAULT_DELAY = 600.0; // ms
	public final static int MAX_SLIDER_DELAY_TICKS = 2000; // JSlider ticks
	public static final int DEFAULT_SLIDER_DELAY_TICKS = 1757; // JSlider ticks
	private double delay = DEFAULT_DELAY;
	
	public final static int MAX_ARRAY_SIZE = 500;
	public final static int DEFAULT_ARRAY_SIZE = 15;
	public final static int BAR_MARGIN = 2;

	
	private int arraySize = DEFAULT_ARRAY_SIZE;
	
	private ArrayList<Bar> bars = new ArrayList<Bar>();
	
	public void setArraySize(int newSize) {
		if (newSize <= MAX_ARRAY_SIZE && newSize > 0)
			arraySize = newSize;
		generateBars();
	}

	public void generateBars() {
		clearBars();
		Dimension size = getPreferredSize();
		for (int i = 1; i <= arraySize; i++) {
			double width = (getPreferredSize().width / arraySize) - BAR_MARGIN;
			double height = (i / (double)arraySize) * size.getHeight();
			addBar(width, height);
		}
		shuffleBars();
		repaint();
	}
	
	public void clearBars() {
		stopSort();
		bars.clear();
		repaint();
	}
	
	private void addBar(double width, double height) {
		addBar(width, height, Color.white);
	}
	
	private void addBar(double width, double height, Color color) {
		bars.add(new Bar(width, height, color));
	}
	
	private void shuffleBars() {
        int n = bars.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            Collections.swap(bars, i, change);
        }
    }
	
	private void stopSort() {
		if (runner != null) {
			runner.interrupt();
			runner = null;
		}
	}
	
	public ArrayList<Bar> getBars() {
		return bars;
	}
	public void setDelay(double delay) {
		this.delay = delay;
	}
	
	public void startSort() {
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}
	
	public void refreshBars() {
		repaint();
	}
	
	public void run() {
		while (!isSorted()) {
			resetAllBarColors();
			insertionSort();
		}
		if (isSorted())
			markBarsCompleted();
	}

	private void insertionSort() {
		int size = bars.size();
		for (int i = 1; i < size; i++) {
			int j = i;
			while (j > 0) {
				if (bars.get(j - 1).getHeight() > bars.get(j).getHeight())
					swapBars(j, j - 1);
				j--;
			}
		}
	}
	
	private void selectionSort() {
		int min = 0;
		for (int i = 0; i < bars.size(); i++) {
			colorizeBar(Color.green, i);
			min = i;
			for (int j = i + 1; j < bars.size(); j++) {
				colorizeBar(Color.red, j);
				colorizeBar(Color.red, min);
				if (bars.get(j).getHeight() < bars.get(min).getHeight()) {
					colorizeBar(Color.cyan, j);
					colorizeBar(Color.white, min);
					swapBars(min, j);
					min = j;
				}
				else {
					colorizeBar(Color.cyan, j);
					colorizeBar(Color.white, j);
				}
			}
			resetAllBarColors();
		}
	}
	
	private void swapBars(int x, int y) {
		//Color oldColorX = bars.get(x).getColor();
		//Color oldColorY = bars.get(y).getColor();
		colorizeBar(Color.red, x);
		colorizeBar(Color.red, y);
		Collections.swap(bars, x, y);
		repaint();
		sleep(delay);
		colorizeBars(Color.white, x, y);
		//colorizeBar(Color.white, y);
	}
	
	private void colorizeBars(Color color, Bar... bars) {
		for (Bar bar : bars) {
			bar.setColor(color);
		}
		repaint();
		sleep(delay);		
	}
	
	private void colorizeBar(Color color, Bar bar) {
		bar.setColor(color);
		repaint();
		sleep(delay);
	}
	
	private void colorizeBars(Color color, int... barIndexes) {
		for (int index : barIndexes) {
			bars.get(index).setColor(color);
		}
		repaint();
		sleep(delay);
	}
	
	private void colorizeBar(Color color, int barIndex) {
		colorizeBar(color, bars.get(barIndex));
	}
	
	private void sleep(double time) {
		try {
			if (time > 1)
				Thread.sleep((int)time);
			else if (time < 1.0 && time > 0)
				java.util.concurrent.locks.LockSupport.parkNanos((int)(time * 1000000));
			else
				Thread.sleep(0);
		}
		catch (InterruptedException ex) { /* Ignore */ }
	}
	
	private boolean isSorted() {
		for (int i = 0; i < bars.size() - 1; i++) {
			if (bars.get(i).getHeight() > bars.get(i + 1).getHeight())
				return false;
		}
		return true;
	}

	private void resetAllBarColors() {
		for (Bar bar : bars)
			bar.resetColor();
	}
	
	private void checkBarsAndSwap(int iteration) {
		Bar currentBar = bars.get(iteration);
		Bar nextBar = bars.get(iteration + 1);
		currentBar.setColor(Color.green);
		if (currentBar.getHeight() > nextBar.getHeight()) {
			nextBar.setColor(Color.red);
			Collections.swap(bars, iteration,  iteration + 1);
		}
	}
	
	private void markBarsCompleted() {
		for (Bar bar : bars) {
			bar.setColor(Color.green);
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g2);
		if (bars.size() <= 0) return;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
		double x = 0, margin = BAR_MARGIN;
		for (int i = 0; i < arraySize; i++) {
			Bar bar = bars.get(i);
			Color color = bars.get(i).getColor();
			double width = bar.getWidth();
			double height = bar.getHeight();
			double y = getPreferredSize().height - height;
			g2.setColor(color);
			Rectangle2D rectBar = new Rectangle2D.Double(x, y, width, height);
			g2.fill(rectBar);
			//g.fillRect(x, y, width, height);
			if (width <= margin)
				margin = 0;
			x += width + (margin / 2);
		}
	}
}

