package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		String coord = "X[-+]?[0-9]+\\.?[0-9]{0,4}Y[-+]?[0-9]+\\\\.?[0-9]{0,4}";
		String coords = "([a-z]+:"+coord+"|?)+";
		String scores = "([a-z]:[0.9]+|?)+";
		String vcoords = "("+coord+"VX[-+]?[0-9]+\\\\.?[0-9]{0,4}VY[-+]?[0-9]+\\\\.?[0-9]{0,4}T[-+]?[0-9]+\\\\.?[0-9]{0,4}|?)+";
		Pattern welcome = Pattern.compile("WELCOME/(attente|(jeu/"+scores+"/"+coord+"))/"); 
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
						boolean isWaiting = false;
						StringBuilder s = new StringBuilder();
						String phase = null;
						String score = null;
						String cord = null;
						for(int i=0; i < line.length(); i++) {
							if(s.charAt(i)=='/') {
								if(phase==null) {
									phase = s.toString();
									if(phase=="attente") {
										isWaiting=true;
										break;
									}
								}else if(score==null) {
									score = s.toString();
								}else {
									cord = s.toString();
								}
								s.delete(0, s.length()-1);
								continue;
							}
							s.append(s.charAt(i));
						}
						if(!isWaiting)
							handler.welcome(phase, score, cord);
						else
							handler.welcomeWait();
					}else if(denied.matcher(line).matches()) {
						handler.denied();
					}else if(playerLeft.matcher(line).matches()) {
						String name = line.substring(11, line.length()-2);
						handler.playerLeft(name);
					}else if(newPlayer.matcher(line).matches()) {
						String name = line.substring(11, line.length()-2);
						handler.newPlayer(name);
					}else if(session.matcher(line).matches()) {
						int i= 8;
						while(line.charAt(i)!='/') i++;
						String cords = line.substring(8,i);
						String cord = line.substring(i+2, line.length()-2); 
						handler.session(cords, cord);
					}else if(winner.matcher(line).matches()) {
						String scors = line.substring(7, line.length()-2);
						handler.winner(scors);
					}else if(tick.matcher(line).matches()) {
						String vcords = line.substring(5, line.length()-2);
						handler.tick(vcords);
					}else if(newObj.matcher(line).matches()) {
						int i= 7;
						while(line.charAt(i)!='/') i++;
						String cord = line.substring(7, i) ;
						String scors = line.substring(i+2, line.length()-2);
						handler.newObj(cord, scors);
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
