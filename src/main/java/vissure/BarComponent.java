package vissure;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JComponent;

public class BarComponent extends JComponent implements Runnable {

	Thread runner;
	
	public final int DEFAULT_BAR_SPACE = 4;
	public final int DEFAULT_DELAY = 10;
	private final int DEFAULT_BAR_WIDTH = 2;
	
	private ArrayList<Bar> bars = new ArrayList<Bar>();
	
	private int delay = DEFAULT_DELAY;
	private int iterator = 0;
	
	public void generateBars() {
		clearBars();
		for (int i = 1; i <= Main.DEFAULT_SIZE.getWidth() / DEFAULT_BAR_WIDTH / 2; i++)
			addBar(i * 2);
		shuffleBars();
		repaint();
	}
	
	public void clearBars() {
		stopSort();
		bars.clear();
		iterator = 0;
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
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void startSort() {
		startSort(DEFAULT_DELAY);
	}
	
	public void startSort(int delay) {
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}
	
	@Override
	public void run() {
		while (!isSorted()) {
			resetAllBarColors();
			if (iterator >= bars.size() - 1)
				iterator = 0;
			checkBarsAndSwap(iterator);
			iterator++;
			repaint();	
			try {
				if (delay <= 0) {
					Thread.sleep(1, 500000);
				}
				else
					Thread.sleep(delay);
			}
			catch (InterruptedException ex) { /* Ignore */ }
		}
		if (isSorted())
			markBarsCompleted();
	}
	
	private boolean isSorted() {
		for (int i = 0; i < bars.size() - 1; i++) {
			if (bars.get(i).height > bars.get(i + 1).height)
				return false;
		}
		return true;
	}

	private void resetAllBarColors() {
		for (Bar bar : bars)
			setBarColor(bar, Color.black);
	}
	
	private void checkBarsAndSwap(int iteration) {
		Bar currentBar = bars.get(iteration);
		Bar nextBar = bars.get(iteration + 1);
		setBarColor(currentBar, Color.green);
		if (currentBar.height > nextBar.height) {
			setBarColor(nextBar, Color.red);
			Collections.swap(bars, iteration,  iteration + 1);
		}
	}

	private void setBarColor(Bar bar, Color color) {
		bar.color = color;
	}

	private void markBarsCompleted() {
		for (Bar bar : bars) {
			setBarColor(bar, Color.green);
			repaint();
		}
	}
		
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, Main.DEFAULT_SIZE.width, Main.DEFAULT_SIZE.height);
		int mainX = 0;
		for (int i = 0; i < bars.size(); i++, mainX += DEFAULT_BAR_SPACE) {
			Color color = bars.get(i).color;
			int height = bars.get(i).height;
			g.setColor(color);
			g.fillRect(mainX, Main.DEFAULT_SIZE.height - height, DEFAULT_BAR_WIDTH, height);
			//System.out.println("Painting bar at (" + mainX + "," + (Main.DEFAULT_SIZE.height - height) + ") with (w,h): (" + DEFAULT_BAR_WIDTH + "," + height + ")");
		}
	}


}

