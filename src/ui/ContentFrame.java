import java.awt.*;
import javax.swing.*;

class ContentFrame extends JFrame {
	public ContentFrame() {
		setContentPane(new Menu(this));
		pack();
	}
}
