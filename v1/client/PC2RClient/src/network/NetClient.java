package network;

import java.io.IOException;
import java.net.Socket;

public class NetClient {
	//public static final int TIMEOUT = 10;
	private Socket sock = null;
	private String host;
	private int port;
	
	private NetClientInput chi;
	private NetClientOutput cho;
	
	private String name;
	
	public NetClient(String name, Socket sock, String host, int port) {
		this.sock=sock;
		this.host=host;
		this.port=port;
		this.name=name;
	}
	
	public void connect() throws IOException{
		sock = new Socket(host, port);
		chi = new NetClientInput(sock.getInputStream(), this);
		cho = new NetClientOutput(sock.getOutputStream());
		
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
	
	// Game Functions
	
	
}
