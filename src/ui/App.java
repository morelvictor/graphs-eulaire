import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class App {

	public static void main(String[]args) {
		EventQueue.invokeLater(() -> {
			var frame = new Frame();
			frame.setTitle("EULAIRE");
			frame.setSize(1000, 1000);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}

class Frame extends JFrame {
	private JPanel container;

	public Frame() {
		add(new Editeur());
		pack();
	}
}
