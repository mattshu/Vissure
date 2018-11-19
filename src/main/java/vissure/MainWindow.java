package vissure;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

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
	public void generateBars() {
		barComponent.generateBars();
	}
	public void startSort() {
		barComponent.startSort();
	}
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
		
		JButton btnGenerate = new JButton("Generate");
		JButton btnSort = new JButton("Sort");
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateBars();
			}
		});
		btnSort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSort();
			}
		});
		btnGenerate.setBounds(10, 39, 89, 23);
		btnSort.setBounds(109, 39, 89, 23);
		panelRight.add(btnGenerate);
		panelRight.add(btnSort);
		barComponent.setBorder(new EmptyBorder(10, 10, 10, 10));
		barComponent.setBounds(10, 10, 584, 489);
		barComponent.setPreferredSize(new Dimension(584, 489));
		panelLeft.add(barComponent);
	}
}
