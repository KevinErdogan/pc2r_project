package graphics;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;


public class MyComponent extends Container{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8198187935149192864L;

	public MyComponent() {
		
	}
	
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		super.paint(g);
		g.setColor(Color.WHITE);
		g.draw3DRect(20, 20, 20, 20, true);
		
	}
}
