import java.awt.*;
import java.io.*;
import javax.swing.JFrame;

public class App {

	JFrame frame;
	Settings settings;

	App() {
		settings = new Settings();
	}

	public static void main(String[] args) {
		App app = new App();

		EventQueue.invokeLater(() -> {
			app.frame = new JFrame();
			app.frame.setTitle("EULAIRE");
			app.frame.setSize(1250, 1000);
			app.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			app.frame.setVisible(true);
			app.frame.setContentPane(new Menu(app));
			app.frame.pack();
		});
	}
}
