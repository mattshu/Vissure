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
	
	public final static double DEFAULT_DELAY = 400.0; // ms
	public final static int MAX_SLIDER_DELAY_TICKS = 2000; // JSlider ticks
	public static final int DEFAULT_SLIDER_DELAY_TICKS = 1057; // JSlider ticks
	private double delay = DEFAULT_DELAY;
	
	public final static int MAX_ARRAY_SIZE = 250;
	public final static int DEFAULT_ARRAY_SIZE = 100;
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
		int last_sorted = 0;
		while (!isSorted()) {
			//resetBarColors();
			//last_sorted = bubbleSort(last_sorted);
			selectionSort();
			//last_sorted = insertionSort(last_sorted);
		}
		if (isSorted())
			markBarsCompleted();
	}
	
	private void merge (int[] arr, int l, int m, int r) {
		int i, j, k;
		int n1 = m - l + 1;
		int n2 = r - m;
		
		// tmp arrays
		int[] L = new int[n1];
		int[] R = new int[n2];
		
		// copy to tmp arrays
		for (i = 0; i < n1; i++)
			L[i] = arr[l + i];
		for (j = 0; j < n2; j++)
			R[j] = arr[m + 1 + j];
		
		// merge tmp arrays back into arr[l..r]
		i = 0; 
		j = 0; 
		k = l;
		while (i < n1 && j < n2) {
			if (L[i] <= R[j]) {
				arr[k] = L[i];
				i++;
			}
			else {
				arr[k] = R[j];
				j++;
			}
			k++;
		}
		
		// copy remaining L[] if any
		while (i < n1) {
			arr[k] = L[i];
			i++;
			k++;
		}
		// copy remaining R[] if any
		while (j < n2) {
			arr[k] = R[j];
			j++;
			k++;
		}
	}
	
	void mergeSort(int arr[], int l, int r) {
		if (l < r) {
			// same as (l+r)/2 but avoids overflow for large l
			int m = l + (r - l) / 2;
			
			//sort first and second halves
			mergeSort(arr, l, m);
			mergeSort(arr, m+1, r);
			merge(arr, l, m, r);
			
		}
	}
	
	// returns last bar swapped
	private int bubbleSort(int sortUntilBar) {
		int sortUntil = bars.size();
		if (sortUntilBar > 0)
			sortUntil = sortUntilBar;
		int last_bar_swapped = 0;
		for (int i = 1; i < sortUntil; i++) {
			colorBar(Color.red, i);
			bars.get(i).resetColor();
			colorBar(Color.red, i - 1);
			bars.get(i - 1).resetColor();
			if (bars.get(i - 1).getHeight() > bars.get(i).getHeight()) {
				colorBar(Color.red, i, false);
				colorBar(Color.red, i - 1, false);
				swapBars(i, i - 1);
				last_bar_swapped = i;
			}
			resetBarColors();
		}
		return last_bar_swapped;
	}

	private int insertionSort(int sortUntil) {
		int size = bars.size();
		if (sortUntil > 0)
			size = sortUntil;
		int last = 0;
		for (int i = 1; i < size; i++) {
			int j = i;
			while (j > 0) {
				colorBar(Color.cyan, j - 1);
				if (bars.get(j - 1).getHeight() > bars.get(j).getHeight()) {
					swapBars(j, j - 1);
					last = j;
				}
				resetBarColors();
				j--;
			}
		}
		return last;
	}
	
	private void selectionSort() {
		int limit = bars.size();
		for (int i = 0; i < limit - 1; i++) {
			int min = i;
			if (min > 0)
				colorBar(Color.green, min - 1); // Colorize last least bar
			for (int j = i + 1; j < limit; j++) {
				colorBar(Color.red, j);
				colorBar(Color.white, j, false);
				colorBar(Color.red, min);
				if (bars.get(j).getHeight() < bars.get(min).getHeight()) {
					colorBar(Color.white, min, false);
					min = j;
					colorBar(Color.cyan, min, false);
				}
				else {
					colorBar(Color.white, j, false);
					colorBar(Color.cyan, min, false);


				}
			}
			swapBars(i, min);
			resetBarColors();
		}
	}
	
	private void swapBars(int x, int y) {
		Color oldColorX = bars.get(x).getColor();
		Color oldColorY = bars.get(y).getColor();
		colorBars(Color.red, x, y);
		Collections.swap(bars, x, y);
		repaint();
		sleep(delay);
		colorBar(oldColorX, x, false);
		colorBar(oldColorY, y, false);
	}
	private void colorBar(Color color, int index, Boolean enableDelay) {
		bars.get(index).setColor(color);
		repaint();
		if (enableDelay)
			sleep(delay);
	}
	
	private void colorBars(Color color, Bar... bars) {
		for (Bar bar : bars) {
			bar.setColor(color);
		}
		repaint();
		sleep(delay);		
	}
	
	private void colorBar(Color color, Bar bar) {
		bar.setColor(color);
		repaint();
		sleep(delay);
	}
	
	private void colorBars(Color color, int... barIndexes) {
		for (int index : barIndexes) {
			bars.get(index).setColor(color);
		}
		repaint();
		sleep(delay);
	}
	
	private void colorBar(Color color, int barIndex) {
		colorBar(color, bars.get(barIndex));
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
	
	private void resetBarColors() {
		for (Bar bar : bars)
			bar.resetColor();
	}
	
	private void markBarsCompleted() {
		int i = 0;
		for (; i < bars.size() - 1; i++) {
			colorBar(Color.green, i, false);
			colorBar(Color.red, i + 1);
		}
		colorBar(Color.green, i, false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getPreferredSize().width, getPreferredSize().height);
		super.paintComponent(g2);
		int barCount = bars.size();
		if (barCount <= 0) return;
		double width = getWidth();
		double height = getHeight();
		double barWidth = (width - (barCount - 1)) / (double)barCount;
		if (width <= (barCount-1)) barWidth = 0.0; // ?
		double barStep = barWidth + 1.0;
		if (Math.abs(barWidth - 1.0) < 0.1 && Math.abs(barStep - 2.0) < 0.1) {
			barWidth = 2;
			barStep = 2;
		}
		for (int i = 0; i < barCount; ++i) {
			Bar bar = bars.get(i);
			double x = i * barStep;
			double y = height - bar.getHeight();
			Color color = bar.getColor();
			g2.setColor(color);
			Rectangle2D rectBar =
					new Rectangle2D.Double(x, y,
					Math.max(1, (((i+1)*barStep) - (i*barStep)) - (barStep - barWidth)),
					bar.getHeight());
			g2.fill(rectBar);
		}
	}
}

