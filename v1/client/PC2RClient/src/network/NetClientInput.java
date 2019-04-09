package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetClientInput extends Thread{
	private NetClient handler;
	private InputStream in;
	private boolean stop = false;

	public NetClientInput(InputStream in, NetClient handler) {
		this.in = in;
		this.handler = handler;
	}

	public void exit() {
		stop = true;
	}

	public void run() {
		String coords = "";
		String coord = "X[-+]?[0-9]+\\.?[0-9]{0,4}";
		String scores = "";
		String vcoords = "";
		Pattern welcome = Pattern.compile("WELCOME/(attente|jeu)/"+scores+"/"+coord+"/"); 
		Pattern denied = Pattern.compile("DENIED/");
		Pattern playerLeft = Pattern.compile("PLAYERLEFT/[a-z]+/");
		Pattern newPlayer = Pattern.compile("NEWPLAYER/[a-z]+/");
		Pattern session = Pattern.compile("SESSION/"+coords+"/"+coord+"/");
		Pattern winner = Pattern.compile("WINNER/"+scores+"/");
		Pattern tick = Pattern.compile("TICK/"+vcoords+"/");
		Pattern newObj = Pattern.compile("NEWOBJ/"+coord+"/"+scores+"/");
		
		try (BufferedReader is = new BufferedReader(new InputStreamReader(in))) {
			while (!stop) {
				String line = is.readLine();
				if (line != null) {
					if (welcome.matcher(line).matches()) {
						handler.welcome(phase, scores, coord);
					}else if(denied.matcher(line).matches()) {
						handler.denied();
					}else if(playerLeft.matcher(line).matches()) {
						handler.playerLeft(name);
					}else if(newPlayer.matcher(line).matches()) {
						handler.newPlayer(name);
					}else if(session.matcher(line).matches()) {
						handler.session(coords, coord);
					}else if(winner.matcher(line).matches()) {
						handler.winner(scores);
					}else if(tick.matcher(line).matches()) {
						handler.tick(vcoords);
					}else if(newObj.matcher(line).matches()) {
						handler.newObj(coord, scores);
					}
				}
			}
		}catch (IOException e) {
			if(!stop) {
				System.err.println("InputStream lost");
			}
		}

	}
}
