package graphics;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.util.List;

import javax.swing.JComponent;

import model.Game;
import model.Objectif;
import model.Obstacle;
import model.Player;
import utils.Pair;
import utils.Point2D;


public class MyComponent extends JComponent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8198187935149192864L;
	
	public static final long refresh_tickrate = 1000/60; // in milliseconds
	private GameScreenThread refresher;
	private Game game;
	private boolean isInGame = false;

	public MyComponent() {
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// background
		g.setColor(new java.awt.Color(102, 102, 102));
		g.fillRect(0, 0, getWidth(), getHeight());
		if(isInGame) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(3));
			Arc2D arc = new Arc2D.Double();
			
			List<Player> players = game.getPlayers();
			int i =1;
			for(Player p : players) {
				// update playernameList in interface
				Point2D pos = p.getPos();
				arc.setArcByCenter(pos.getX(),pos.getY(), 10, 0, 360, Arc2D.OPEN);
				g2.draw(arc);
				/*String value = String.valueOf(i);
				g2.drawString(value, (float) (pos.getX() +(game.getMap().getHeight()/2)),
						(float) (pos.getY()+(game.getMap().getHeight()/2)));*///dessine pas au bon endroit 
			}
			
			Objectif obj = game.getObj();
			Point2D pos = obj.getPos();
			arc.setArcByCenter(pos.getX(), pos.getY(), 10, 0, 360, Arc2D.OPEN);
			g2.setColor(Color.red);
			g2.drawOval(((int)pos.getX())-8, ((int)pos.getY())-8, 8, 8);
			
			List<Obstacle> obstacles = game.getObstacles();
			
			g2.setColor(Color.BLUE);
			for(Obstacle o : obstacles) {
				pos = o.getPos();
				arc.setArcByCenter(pos.getX()+(game.getMap().getWidth()/2), 
						pos.getY()+(game.getMap().getHeight()/2), 10, 0, 360, Arc2D.OPEN);
				g2.draw(arc);
			}
		}
	}
	
	public void start(Game currentGame) {
		game = currentGame;
		isInGame=true;
		refresher = new GameScreenThread(this, MyComponent.refresh_tickrate);
		new Thread(refresher).start();
	}
	
	public void stop() {
		isInGame=false;
		game=null;
		refresher.stop();
	}

	public boolean isInGame() {
		return isInGame;
	}

	public Game getGame() {
		return game;
	}
	
	
}
