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
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {System.out.println("escape");
	         boolean state = frame.getEscapePanel().isVisible();
	         frame.getEscapePanel().setVisible(!state);
		}else if(frame.getClient().getMyClientPlayer() != null) {
			if(e.getKeyChar() =='Z' || e.getKeyChar() == 'z') {//thrust
					System.out.println("Z");
					frame.getClient().getMyClientPlayer().thrust();
			}else if(e.getKeyChar() =='Q' || e.getKeyChar() == 'q') {//clock
				System.out.println("Q");
				frame.getClient().getMyClientPlayer().anticlock();
			}else if(e.getKeyChar() =='D' || e.getKeyChar() == 'd') {//anticlock
				System.out.println("D");
				frame.getClient().getMyClientPlayer().clock();
			}else if(e.getKeyChar() =='S' || e.getKeyChar() == 's') {//anticlock
				System.out.println("S");
				frame.getClient().getMyClientPlayer().decelerate();
			}
		}else {
			System.out.println(frame.getClient().getMyClientPlayer());
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
