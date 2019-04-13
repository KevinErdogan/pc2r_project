package graphics;

public class GameScreenThread implements Runnable {

	private MyComponent gamePanel;
	private boolean isGameRunning = true;
	private long refresh_tickrate;
	
	public GameScreenThread(MyComponent c, long refresh_tickrate) {
		gamePanel = c;
		this.refresh_tickrate=refresh_tickrate;
	}
	
	@Override
	public void run() {
		while(isGameRunning) {
			try {
				Thread.sleep(refresh_tickrate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(gamePanel.isInGame())
				gamePanel.getGame().moveAllPlayersAndCollide();
			gamePanel.repaint();
			
		}
	}

	
	public void stop() {
		isGameRunning=false;
	}
}
