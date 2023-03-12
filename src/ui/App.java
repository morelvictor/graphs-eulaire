import java.awt.EventQueue;
import javax.swing.JFrame;

public class App {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			var frame = new JFrame();
			frame.setTitle("EULAIRE");
			frame.setSize(1000, 1000);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
			frame.setContentPane(new Menu(frame));
			frame.pack();
		});
	}
}
