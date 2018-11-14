package vissure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JTextField;

public class BarComponent extends JComponent implements Runnable {

	Thread runner;
	
	public final static double DEFAULT_DELAY = 10;
	public final static int DEFAULT_BAR_WIDTH = 3;
	public final static int DEFAULT_BAR_MARGIN = 1;
	
	private ArrayList<Bar> bars = new ArrayList<Bar>();
	
	private double delay = DEFAULT_DELAY;
	
	/*private JTextField barWidthText;
	private JTextField barMarginText;
	
	public BarComponent(JTextField barWidthText, JTextField barMarginText) {
		this.barWidthText = barWidthText;
		this.barMarginText = barMarginText;
	}*/
	
	public void generateBars() {
		clearBars();
		Dimension mainSize = getSize();
		for (int i = 0; i < mainSize.width; i += DEFAULT_BAR_WIDTH + DEFAULT_BAR_MARGIN) {
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
		addBar(height, Color.black);
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
			resetAllBarColors();
			selectionSort();
		}
		if (isSorted())
			markBarsCompleted();
	}

	private void insertionSort() {
		for (int i = 1; i < bars.size(); i++) {
			int j = i;
			while (j > 0 && bars.get(j - 1).getHeight() > bars.get(j).getHeight()) {
				Collections.swap(bars,  j - 1,  j);
				j--;
				sleep(delay);
				repaint();
			}
		}
	}
	private void selectionSort() {
		int min = 0;
		for (int i = 0; i < bars.size(); i++) {
			bars.get(i).setColor(Color.green);
			repaint();
			sleep(delay);
			min = i;
			for (int j = i + 1; j < bars.size(); j++) {
				bars.get(j).setColor(Color.yellow);
				repaint();
				sleep(delay);
				if (bars.get(j).getHeight() < bars.get(min).getHeight()) {
					min = j;
				}
				bars.get(j).setColor(Color.black);
				repaint();
			}
			bars.get(i).setColor(Color.red);
			bars.get(min).setColor(Color.red);
			repaint();
			sleep(delay);
			Collections.swap(bars, i, min);
			repaint();
			sleep(delay);
			bars.get(i).setColor(Color.black);
			bars.get(min).setColor(Color.black);
			repaint();
			sleep(delay);
			resetAllBarColors();
			//repaint();
		}
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
			bar.setColor(Color.black);
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
		g.clearRect(0, 0, getSize().width, getSize().height);
		int mainX = 0;
		for (int i = 0; i < bars.size(); i++, mainX += DEFAULT_BAR_WIDTH + DEFAULT_BAR_MARGIN) {
		//for (int i = 0; i < bars.size(); i++, mainX += getBarWidth() + getBarMargin()) {
			Color color = bars.get(i).getColor();
			int height = bars.get(i).getHeight();
			g.setColor(color);
			g.fillRect(mainX, getSize().height - height, DEFAULT_BAR_WIDTH, height); // Draw bar
			//g.fillRect(mainX, getSize().height - height, getBarWidth(), height); // Draw bar
		}
	}
	
	/*private int getBarWidth() {
		try {
			String text = barWidthText.getText();
			int width = Integer.parseInt(text);
			if (text.isEmpty() || width <= 0) {
				barWidthText.setText(Integer.toString(DEFAULT_BAR_WIDTH));
				return DEFAULT_BAR_WIDTH;
			}
			return width;
		} catch (NumberFormatException ex) {
			barWidthText.setText(Integer.toString(DEFAULT_BAR_WIDTH));
			return DEFAULT_BAR_WIDTH;
		}
	}
	
	private int getBarMargin() {
		try {
			String text = barMarginText.getText();
			int margin = Integer.parseInt(text);
			if (text.isEmpty() || margin < 0) {
				barMarginText.setText(Integer.toString(DEFAULT_BAR_MARGIN));
				return DEFAULT_BAR_MARGIN;
			}
			return margin;
		} catch (NumberFormatException ex) {
			barMarginText.setText(Integer.toString(DEFAULT_BAR_MARGIN));
			return DEFAULT_BAR_MARGIN;
		}
	}*/
}

