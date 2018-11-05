package vissure;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;

public class BarComponent extends JComponent {

	public final int DEFAULT_BAR_SPACE = 1;
	private final int DEFAULT_BAR_WIDTH = 2;
	private final int DEFAULT_DELAY = 10;
	private int delay = DEFAULT_DELAY;
	private ArrayList<Bar> bars = new ArrayList<Bar>();
	
	public void generateBars() {
		clearBars();
		for (int i = 0; i < Main.DEFAULT_SIZE.getWidth() / DEFAULT_BAR_WIDTH; i += DEFAULT_BAR_SPACE)
			addBar(i);
		repaint();
	}
	
	public void clearBars() {
		stopSort();
		iterator = 0;
		bars.clear();
		repaint();
	}
	
	private void stopSort() {
		timer.cancel();
		System.out.println("Timer cancelled.");
	}
	
	public void addBar(int height) {
		addBar(height, Color.black);
	}
	
	public void addBar(int height, Color color) {
		bars.add(new Bar(height, color));
	}
		
	public int getDelay() {
		return delay;
	}
	
	private Timer timer = new Timer("timer");
	public void startSort(int delay) {
		setDelay(delay);
		timer.scheduleAtFixedRate(sortNextBarTask, 0, delay);
	}
	
	public void setDelay(int delay) {
		timer.cancel();
		//if (timer.isRunning())
			//timer.stop();
		this.delay = delay;
		//timer = new Timer(delay, sortNextBar());
	}
	
	//private Date lastFired = Date.from(Instant.now());
	private int iterator = bars.size() - 1;
	private TimerTask sortNextBarTask = new TimerTask() {
			public void run() {
				//System.out.print("Timer fired! Delay: " + timer.getDelay());
				//System.out.println(" | Time since last fire: " + TimeUnit.MILLISECONDS.convert(Date.from(Instant.now()).getTime() - lastFired.getTime(), TimeUnit.MILLISECONDS));
				//lastFired = Date.from(Instant.now());
				if (iterator <= 0) {
					resetAllBarColors();
					iterator = bars.size() - 1;
					if (isSorted()) {
						cancel();
						markBarsCompleted();
						return;
					}
				}
				Bar currentBar = bars.get(iterator);
				Bar nextBar = bars.get(iterator - 1);
				setBarColor(currentBar, Color.green);
				if (currentBar.height < nextBar.height) {
					setBarColor(nextBar, Color.red);
					Collections.swap(bars, iterator,  iterator - 1);
				}
				else {
					setBarColor(currentBar, Color.black);
				}
				iterator--;
				repaint();
			}
		};

	private void resetAllBarColors() {
		for (Bar bar : bars)
			setBarColor(bar, Color.black);
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
	
	public void shuffleBars() {
        int n = bars.size();
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            Collections.swap(bars, i, change);
        }
    }
	
	public boolean isSorted() {
		for (int i = 0; i < bars.size() - 1; i++) {
			if (bars.get(i).height > bars.get(i + 1).height)
				return false;
		}
		return true;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.clearRect(0, 0, Main.DEFAULT_SIZE.width, Main.DEFAULT_SIZE.height);
		int mainX = 0;
		for (int i = 0; i < bars.size(); i++, mainX += DEFAULT_BAR_WIDTH) {
			Color color = bars.get(i).color;
			int height = bars.get(i).height;
			g.setColor(color);
			g.fillRect(mainX, Main.DEFAULT_SIZE.height - height, DEFAULT_BAR_WIDTH, height);
			//g.fillOval(mainX, Main.DEFAULT_SIZE.height - height, DEFAULT_BAR_WIDTH, DEFAULT_BAR_WIDTH);
		}
	}
}

