package vissure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;

public class Main {
	
	public final static Dimension DEFAULT_SIZE = new Dimension(620, 400);
	private final static JFrame barGraphFrame = new JFrame();
	private final static JTextField barWidthText = new JTextField(String.valueOf(BarComponent.DEFAULT_BAR_WIDTH));
	private final static JTextField barMarginText = new JTextField(String.valueOf(BarComponent.DEFAULT_BAR_MARGIN));
	private static BarComponent barComponent = new BarComponent(barWidthText, barMarginText);
	public final static int getBarWidth() {
		int width = Integer.parseInt(barWidthText.getText());
		if (width <= 0)
			width = 1;
		return width;
	}
	public final static int getBarMargin() {
		int margin = Integer.parseInt(barMarginText.getText());
		if (margin <= 0)
			margin = 1;
		return margin;
	}
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
		final JPanel bottomPanel = new JPanel();
		final JLabel barWidthLabel = new JLabel("Width:");
		final JLabel barMarginLabel = new JLabel("Margin:");
		barWidthText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (barWidthText.getText().isEmpty()) {
					barWidthText.setText("0");
					barComponent.refreshBars();
				}				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (barWidthText.getText().isEmpty()) {
					barWidthText.setText("0");
					barComponent.refreshBars();
				}
			}
			
		});
		barMarginText.addCaretListener(new CaretListener(){

			@Override
			public void caretUpdate(CaretEvent e) {
				if (barWidthText.getText().isEmpty()) {
					barWidthText.setText("0");
					barComponent.refreshBars();
				}		
			}

		});
		barWidthText.setPreferredSize(new Dimension(24, 21));
		barMarginText.setPreferredSize(new Dimension(24, 21));
		JButton generateButton = new JButton("Generate");
		JButton sortButton = new JButton("Sort");
		JButton clearButton = new JButton("Clear");
		final JLabel editLabel = new JLabel("10ms delay");
		final JSlider slider = new JSlider();
		slider.setValue(BarComponent.DEFAULT_DELAY);
		slider.setMaximum(100);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				editLabel.setText(slider.getValue() + "ms delay");
				barComponent.setDelay(slider.getValue());
			}
		});
		bottomPanel.add(barWidthLabel);
		bottomPanel.add(barWidthText);
		bottomPanel.add(barMarginLabel);
		bottomPanel.add(barMarginText);
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
