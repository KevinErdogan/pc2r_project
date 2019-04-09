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
	}
	
	public void exit(String name) {
		os.println("EXIT/"+name+"/");
	}
	
	public void newCom(String cmds) {
		os.println("NEWCOM/"+cmds+"/");
	}
}
