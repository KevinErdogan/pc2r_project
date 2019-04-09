package network;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class NetClient {
	//public static final int TIMEOUT = 10;
	private Socket sock = null;
	private String host;
	private int port;
	
	private NetClientInput chi;
	private NetClientOutput cho;
	
	private String name = null;
	private LinkedList<NetworkObserver> observers = new LinkedList<NetworkObserver>();
	
	public NetClient(String host, int port) {
		this.host=host;
		this.port=port;
	}
	
	public void connect(String name) throws IOException{
		sock = new Socket(host, port);
		chi = new NetClientInput(sock.getInputStream(), this);
		cho = new NetClientOutput(sock.getOutputStream());
		
		this.name=name;
		cho.connect(name);
		chi.start();
	}
	
	public void disconnect() throws IOException {
		if (cho != null)
			cho.exit(name);
		if (chi != null)
			chi.exit();
		if (sock != null)
			sock.close();
	}
	
	// Observers Pattern
	
	public void registerObserver(NetworkObserver o) {
		observers.add(o);
	}

	public void unregisterObserver(NetworkObserver o) {
		observers.remove(o);
	}

	public void notifyObservers(NetworkEvent ... events) {
		List<NetworkEvent> evts = new LinkedList<NetworkEvent>();
		for(NetworkEvent e : events) {
			evts.add(e);
		}
		for (NetworkObserver o : observers) {
			o.notify(evts);
		}
	}

	
	// Protocol Functions
	
	
	// Received Messages
	public void welcomeWait() {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.WELCOMEWAIT));
	}
	
	public void welcome(String phase, String scores, String coord) {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.WELCOMEGAME, scores, coord));
	}

	public void denied()  {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.DENIED));
	}

	public void playerLeft(String name) {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.PLAYERLEFT, name));
	}

	public void newPlayer(String name) {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.NEWPLAYER, name));
	}

	public void session(String coords, String coord) {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.NEWSESSION, coords, coord));
	}

	public void winner(String scores) {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.WINNER, scores));
	}

	public void tick(String vcoords) {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.TICK, vcoords));
	}

	public void newObj(String coord, String scores) {
		notifyObservers(new NetworkEvent(NetworkEvent.NetType.NEWOBJ, coord, scores));
	}
	
	// to Send Messages
	
	public void newCom(String cmds) {
		cho.newCom(cmds);
	}

}
