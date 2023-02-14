import java.awt.*;
import javax.swing.*;

class ContentFrame extends JFrame {
	public ContentFrame() {
		try {
			setContentPane(new Menu(this));
		} catch (Exception e) {
			System.out.println("goulougoulou");
		}
		pack();
	}
}
