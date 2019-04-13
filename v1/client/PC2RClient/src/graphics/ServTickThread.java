package graphics;

public class ServTickThread implements Runnable{

	private ClientFrame myClientFrame;
	private long serv_tickrate;
	private boolean isGameRunning = true;
	
	
	
	public ServTickThread(ClientFrame myClientFrame, long serv_tickrate) {
		this.myClientFrame = myClientFrame;
		this.serv_tickrate = serv_tickrate;
	}

	@Override
	public void run() {//tous les serv_tickrate envoie une commande NEWCOM au serveur
		
		while(isGameRunning) {
			try {
				Thread.sleep(serv_tickrate);
				myClientFrame.sendNewCom();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void stop() {
		this.isGameRunning = false;
	}

}
