import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class App {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			var frame = new ContentFrame();
			frame.setTitle("EULAIRE");
			frame.setSize(1000, 1000);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}

