package network;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class NetClientOutput {
	private PrintWriter os;

	public NetClientOutput(OutputStream out) throws IOException {
		os = new PrintWriter(out, true);
	}
	
	public void connect(String name) {
		os.println("CONNECT/"+name+"/");
		System.out.println("sending connect");
	}
	
	public void exit(String name) {
		os.println("EXIT/"+name+"/");
		System.out.println("sending exit");
	}
	
	public void newCom(String cmds) {
		os.println("NEWCOM/"+cmds+"/");
		//System.out.println("sending cmds");
	}
}
