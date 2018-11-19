package vissure;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main {
	
	//public final static Dimension DEFAULT_SIZE = new Dimension(620, 400);
	private final static JFrame barGraphFrame = new JFrame();
	private final static JLabel sliderLabel = new JLabel("10 ms");
	private final static JSlider slider = new JSlider();
	private static BarComponent barComponent = new BarComponent();
	public static void main(String[] args) {
		barGraphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		barGraphFrame.getContentPane().setBackground(Color.black);
		//barComponent.setPreferredSize(DEFAULT_SIZE);
		barGraphFrame.getContentPane().add(barComponent, BorderLayout.CENTER);
		barGraphFrame.addWindowListener(new WindowListener() {

			public void windowActivated(WindowEvent arg0) {
				barComponent.generateBars();
				
			}

			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void windowClosing(WindowEvent arg0) {
				System.out.println("Closing. Clearing bars.");
				barComponent.clearBars();
			}

			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		final JPanel bottomPanel = new JPanel();
		JButton generateButton = new JButton("Generate");
		JButton sortButton = new JButton("Sort");
		JButton clearButton = new JButton("Clear");
		slider.setMaximum(2000);
		slider.setValue(1000);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				double adjDelay = getAdjustedDelay(slider.getValue());
				barComponent.setDelay(adjDelay);
			}
		});
		bottomPanel.add(generateButton);
		bottomPanel.add(sortButton);
		bottomPanel.add(clearButton);
		bottomPanel.add(slider);
		bottomPanel.add(sliderLabel);
		barGraphFrame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		generateButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				barComponent.generateBars();
			}
		});
		sortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barComponent.startSort();
			}
		});
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barComponent.clearBars();
			}
		});
		barComponent.setBackground(Color.black);
		barGraphFrame.setBackground(Color.black);
		barGraphFrame.getContentPane().setBackground(Color.black);
		barGraphFrame.pack();
		barGraphFrame.setVisible(true);
	}
	/* Scale:
	   0.25ms|-------14ms--------100ms----------500ms-----------|2000ms
              9.9<>10     30<>34      110<>120        550<>650
	 */
	private static double getAdjustedDelay(int sliderValue) {
		double base = 4;
		double g_delay = Math.pow(4, sliderValue / 2000.0 * Math.log(2 * 1000.0 * 10.0) / Math.log(base)) / 10.0;
		if (sliderValue == 0) g_delay = 0;
		if (g_delay > 10)
			sliderLabel.setText(String.format("%.0f ms", g_delay));
		else if (g_delay > 1)
			sliderLabel.setText(String.format("%.1f ms", g_delay));
		else
			sliderLabel.setText(String.format("%.2f ms",  g_delay));
		return g_delay;
	}
	
}
