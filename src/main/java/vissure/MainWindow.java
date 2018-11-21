package vissure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSlider;
import javax.swing.JLabel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class MainWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	JPanel panel = new JPanel();
	JPanel panelLeft = new JPanel();
	JPanel panelRight = new JPanel();
	BarComponent barComponent = new BarComponent();
	public MainWindow() {
		setType(Type.UTILITY);
		setTitle("Vissure");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 893, 558);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		contentPane.add(panel);
		panelLeft.setBounds(10, 10, 604, 509);
		panelLeft.setBackground(Color.BLACK);
		panelLeft.setBorder(new EmptyBorder(10, 10, 10, 10));
		panel.add(panelLeft);
		panelRight.setBounds(627, 0, 240, 509);
		panel.add(panelRight);
		panelLeft.setLayout(null);
		panel.setLayout(null);
		panelRight.setLayout(null);
		barComponent.setBorder(new EmptyBorder(10, 10, 10, 10));
		barComponent.setBounds(10, 10, 584, 489);
		barComponent.setPreferredSize(new Dimension(584, 489));
		barComponent.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				int i = 0;
				for (Bar bar : barComponent.getBars()) {
					System.out.println("Index: " + i + " | Height: " + bar.getHeight() + " | Width: " + bar.getWidth());
					i++;
				}
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		panelLeft.add(barComponent);
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(10, 226, 102, 23);
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barComponent.generateBars();
			}
		});
		panelRight.add(btnGenerate);
		JButton btnStop = new JButton("Stop");
		btnStop.setBounds(128, 11, 102, 23);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO
			}
		});
		panelRight.add(btnStop);
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(128, 45, 102, 23);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barComponent.clearBars();
			}
		});
		panelRight.add(btnClear);
		JButton btnRun = new JButton("Run");
		btnRun.setBounds(10, 11, 102, 23);
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barComponent.startSort();
			}
		});
		panelRight.add(btnRun);
		JLabel lblDelay = new JLabel("Delay:");
		lblDelay.setBounds(20, 110, 46, 14);
		panelRight.add(lblDelay);
		JLabel lblDelayText = new JLabel("600ms"); // TODO hard coded
		lblDelayText.setBounds(94, 110, 75, 14);
		panelRight.add(lblDelayText);
		JSlider sliderDelay = new JSlider();
		sliderDelay.setBounds(10, 79, 220, 26);
		sliderDelay.setMaximum(BarComponent.MAX_SLIDER_DELAY_TICKS);
		sliderDelay.setValue(BarComponent.DEFAULT_SLIDER_DELAY_TICKS);
		sliderDelay.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int sliderTicks = sliderDelay.getValue();
				 // Magically spans the delay slider from 0.1ms to 2s
				double delay = sliderTicks == 0 ? 0 : Math.pow(4, (double)sliderTicks / BarComponent.MAX_SLIDER_DELAY_TICKS * Math.log(BarComponent.MAX_SLIDER_DELAY_TICKS * 10.0) / Math.log(4)) / 10.0;
				barComponent.setDelay(delay);
				lblDelayText.setText(String.format(delay > 10 ? "%.0f ms" : delay > 1 ? "%.1f ms" : "%.2f ms", delay));
			}
		});
		panelRight.add(sliderDelay);
		JLabel lblArraySize = new JLabel("Array size:");
		lblArraySize.setBounds(20, 297, 64, 14);
		panelRight.add(lblArraySize);
		JLabel lblArraySizeText = new JLabel(String.valueOf(BarComponent.DEFAULT_ARRAY_SIZE));
		lblArraySizeText.setBounds(94, 297, 46, 14);
		panelRight.add(lblArraySizeText);
		JSlider sliderArraySize = new JSlider();
		sliderArraySize.setBounds(10, 260, 220, 26);
		sliderArraySize.setMaximum(500);
		sliderArraySize.setValue(BarComponent.DEFAULT_ARRAY_SIZE);
		sliderArraySize.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int newSize = sliderArraySize.getValue();
				lblArraySizeText.setText(String.valueOf(newSize));
				barComponent.setArraySize(newSize);
			}
		});
		panelRight.add(sliderArraySize);

	}
}
