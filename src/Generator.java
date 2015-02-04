// Copyright Filippo Sanfilippo
import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Generator {

	/**
	 * create the GUI and show it
	 */
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Modular Manipulator Generator");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.add(new GeneratorPanel(), BorderLayout.NORTH);
		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}
}
