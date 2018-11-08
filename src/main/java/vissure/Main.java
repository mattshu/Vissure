package vissure;

import java.awt.BorderLayout;
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
	
	public final static Dimension DEFAULT_SIZE = new Dimension(520, 400);
	private final static JFrame barGraphFrame = new JFrame();
	private final static JSlider slider = new JSlider();
	private static BarComponent barComponent = new BarComponent();
	public static void main(String[] args) {
		barGraphFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		barComponent.setPreferredSize(DEFAULT_SIZE);
		barGraphFrame.getContentPane().add(barComponent, BorderLayout.CENTER);
		barGraphFrame.addWindowListener(new WindowListener() {

			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
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
		JPanel bottomPanel = new JPanel();
		JButton generateButton = new JButton("Generate");
		JButton sortButton = new JButton("Sort");
		JButton clearButton = new JButton("Clear");
		final JLabel editLabel = new JLabel("10ms delay");
		final JSlider slider = new JSlider();
		slider.setValue(barComponent.DEFAULT_DELAY);
		slider.setMaximum(100);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				editLabel.setText(slider.getValue() + "ms delay");
				barComponent.setDelay(slider.getValue());
			}
			
		});
		bottomPanel.add(generateButton);
		bottomPanel.add(sortButton);
		bottomPanel.add(clearButton);
		bottomPanel.add(slider);
		bottomPanel.add(editLabel);
		barGraphFrame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		generateButton.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) {
				barComponent.generateBars();
			}
		});
		sortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barComponent.startSort(slider.getValue());
			}
		});
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barComponent.clearBars();
			}
		});
		
		barGraphFrame.pack();
		barGraphFrame.setVisible(true);
	}
	
}
