package graphics;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.Game;

public class MyKeyListener implements KeyListener {
	
	private ClientFrame frame;
	private Game game;
	
	public MyKeyListener(ClientFrame frame) {
		this.frame=frame;
	}
	
	public void init (Game game) {
		this.game=game;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	         boolean state = frame.getEscapePanel().isVisible();
	         frame.getEscapePanel().setVisible(!state);
		}
		if(game.getMyPlayer() != null) {  
			if(e.getKeyCode() == KeyEvent.VK_SPACE) {//thrust
					System.out.println("THRUST");
					game.getMyPlayer().thrust();
			}else if(e.getKeyChar() =='Q' || e.getKeyChar() == 'q') {
				System.out.println("TurnLeft");
				game.getMyPlayer().anticlock();
			}else if(e.getKeyChar() =='D' || e.getKeyChar() == 'd') {
				System.out.println("TurnRight");
				game.getMyPlayer().clock();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
