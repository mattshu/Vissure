package vissure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JComponent;

public class BarComponent extends JComponent implements Runnable {

	Thread runner;
	
	public final static int DEFAULT_DELAY = 1500;
	public final static int DEFAULT_BAR_WIDTH = 5;
	public final static int DEFAULT_BAR_MARGIN = 1;
	
	private ArrayList<Bar> bars = new ArrayList<Bar>();
	
	private int getBarCount() {
		return bars.size();
	}
	private Bar getBar(int index) {
		return bars.get(index);
	}
	private double delay = getAdjustedDelay(DEFAULT_DELAY);
	private double getAdjustedDelay(int value) {
		double base = 4;
		double g_delay = Math.pow(4, value / 2000.0 * Math.log(2 * 1000.0 * 10.0) / Math.log(base)) / 10.0;
		if (value == 0) g_delay = 0;
		//if (g_delay > 10)
			//sliderLabel.setText(String.format("%.0f ms", g_delay));
		//else if (g_delay > 1)
			//sliderLabel.setText(String.format("%.1f ms", g_delay));
		//else
			//sliderLabel.setText(String.format("%.2f ms",  g_delay));
		return g_delay;
	}
	
	public void generateBars() {
		clearBars();
		Dimension mainSize = getParent().getSize();
		for (int i = 1; i <= mainSize.width; i += DEFAULT_BAR_WIDTH + DEFAULT_BAR_MARGIN) {
		//for (int i = 0; i < mainSize.width; i += getBarWidth() + getBarMargin()) {
			double projectedHeight = ((i + 1) / mainSize.getWidth()) * mainSize.getHeight();
			addBar((int)projectedHeight);
		}
		shuffleBars();
		repaint();
	}
	
	public void clearBars() {
		stopSort();
		bars.clear();
		repaint();
	}
	
	private void addBar(int height) {
		addBar(height, Color.white);
	}
	
	private void addBar(int height, Color color) {
		bars.add(new Bar(height, color));
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
			//resetAllBarColors();
			selectionSort();
		}
		if (isSorted())
			markBarsCompleted();
	}

	private void insertionSort() {
		for (int i = 1; i < bars.size(); i++) {
			int j = i;
			while (j > 0 && bars.get(j - 1).getHeight() > bars.get(j).getHeight()) {
				swapBars(j, j - 1);
				j--;
				sleep(delay);
				repaint();
			}
		}
	}
	
	private void selectionSort() {
		int min = 0;
		for (int i = 0; i < bars.size(); i++) {
			colorizeBar(Color.cyan, i);
			min = i;
			for (int j = i + 1; j < bars.size(); j++) {
				colorizeBars(Color.red, j, i);
				//colorizeBar(Color.red, i);
				if (bars.get(j).getHeight() < bars.get(min).getHeight()) {
					swapBars(i, min);
					min = j;
				}
				else
					colorizeBars(Color.white, j, i);
			}
			//swapBars(i, min);
			colorizeBar(Color.white, i);
			//resetAllBarColors();
		}
	}
	
	private void swapBars(int x, int y) {
		colorizeBars(Color.red, x, y);
		//colorizeBar(y, Color.red);
		Collections.swap(bars, x, y);
		repaint();
		sleep(delay);
		//colorizeBar(x, Color.white);
		colorizeBars(Color.white, x, y);
		//repaint();
		//sleep(delay);
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
			this.bars.get(index).setColor(color);
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
		super.paintComponent(g);
		int size = getBarCount();
		if (size <= 0) return;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, getSize().width, getSize().height);
		int mainX = 0;
		for (int i = 0; i < size; i++, mainX += BarComponent.DEFAULT_BAR_WIDTH + BarComponent.DEFAULT_BAR_MARGIN) {
			Color color = getBar(i).getColor();
			int height = getBar(i).getHeight();
			g.setColor(color);
			g.fillRect(mainX, getSize().height - height, BarComponent.DEFAULT_BAR_WIDTH, height); // Draw bar
		}
	}
}

