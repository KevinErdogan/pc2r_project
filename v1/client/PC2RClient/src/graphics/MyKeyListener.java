package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MyKeyListener implements KeyListener {
	
	private ClientFrame frame;
	
	public MyKeyListener(ClientFrame frame) {
		this.frame=frame;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	         boolean state = frame.getEscapePanel().isVisible();
	         frame.getEscapePanel().setVisible(!state);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
