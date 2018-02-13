import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Whiteboard extends JFrame {

	public Whiteboard() {
		this.setTitle("Whiteboard");
		Canvas canvas = new Canvas();
		Controls controls = new Controls(canvas);
		canvas.setControls(controls);
		this.add(canvas, BorderLayout.CENTER);
		this.add(controls, BorderLayout.WEST);

		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@SuppressWarnings("unused")
	public static void main(String... args) {
		Whiteboard whiteboard = new Whiteboard();
		Whiteboard whiteboard2 = new Whiteboard();
	}
}