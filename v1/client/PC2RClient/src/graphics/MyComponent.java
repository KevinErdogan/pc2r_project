package graphics;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;


public class MyComponent extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8198187935149192864L;

	public MyComponent() {
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paint(g);
		g.setColor(Color.WHITE);
		g.draw3DRect(20, 20, 20, 20, true);
		
	}
}
