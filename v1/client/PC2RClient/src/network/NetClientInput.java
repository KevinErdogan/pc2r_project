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
	
	Pattern welcome ; 
	Pattern denied ;
	Pattern playerLeft ;
	Pattern newPlayer ;
	Pattern session ;
	Pattern winner ;
	Pattern tick ;
	Pattern newObj;

	public NetClientInput(InputStream in, NetClient handler) {
		this.in = in;
		this.handler = handler;
		String floats = "-?[0-9]+\\.?[0-9]{0,10}";
		String coord = "X"+floats+"Y"+floats;
		String coords = "(\\|?[a-z]+:"+coord+")+";
		String scores = "(\\|?[a-z]+:[0-9]+)+";
		String vcoords = "(\\|?"+coords+"VX"+floats+"VY"+floats+"T"+floats+")+";
		String ocoords = "(\\|?"+coord+")+";
		welcome = Pattern.compile("WELCOME\\/(attente|jeu\\/"+scores+"\\/"+coord+"\\/"+ocoords+")\\/"); 
		denied = Pattern.compile("DENIED\\/");
		playerLeft = Pattern.compile("PLAYERLEFT\\/[a-z]+\\/");
		newPlayer = Pattern.compile("NEWPLAYER\\/[a-z]+\\/");
		session = Pattern.compile("SESSION\\/"+coords+"\\/"+coord+"\\/"+ocoords+"\\/");
		winner = Pattern.compile("WINNER\\/"+scores+"\\/");
		tick = Pattern.compile("TICK\\/"+vcoords+"\\/");
		newObj = Pattern.compile("NEWOBJ\\/"+coord+"\\/"+scores+"\\/");
	}

	public void exit() {
		stop = true;
	}

	public void run() {
		
		try (BufferedReader is = new BufferedReader(new InputStreamReader(in))) {
			while (!stop) {
				while(!is.ready()) {
					try {
						Thread.sleep(200/*server_tickrate/2*/);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String line = is.readLine();
				System.out.println("Received : <<"+line+">>");
				if (line != null) {
					if (welcome.matcher(line).matches()) {
						boolean isWaiting = false;
						StringBuilder s = new StringBuilder();
						String phase = null;
						String score = null;
						String cord = null;
						String ocoords = null;
						for(int i=8; i < line.length(); i++) {
							if(line.charAt(i)=='/') {
								if(phase==null) {
									phase = s.toString();
									if(phase.compareTo("attente")==0) {
										isWaiting=true;
										break;
									}
								}else if(score==null) {
									score = s.toString();
								}else if (cord==null){
									cord = s.toString();
								}else {
									ocoords = s.toString();
								}
								s.delete(0, s.length());
								continue;
							}
							s.append(line.charAt(i));
						}
						if(!isWaiting)
							handler.welcome(phase, score, cord, ocoords);
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
						int j = i+1;
						while(line.charAt(j)!='/') j++;
						String cord = line.substring(i+1,j);
						String ocoords = line.substring(j+1, line.length()-2); 
						handler.session(cords, cord, ocoords);
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
