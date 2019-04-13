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
	
	private int scoresLength = 150;
	private int scaleX = Game.MAP_WIDTH/2;
	private int scaleY = Game.MAP_HEIGHT/2;

	public MyComponent() {
		
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// background
		g.setColor(new java.awt.Color(102, 102, 102));
		g.fillRect(scoresLength, 0, getWidth(), getHeight());
		if(isInGame) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.WHITE);
			g2.setStroke(new BasicStroke(3));
			Arc2D arc = new Arc2D.Double();
			
			List<Player> players = game.getPlayers();
			int i =1;
			for(Player p : players) {
				Point2D pos = p.getPos();
				arc.setArcByCenter(scoresLength+scaleX+pos.x,scaleY+pos.y, 10, 0, 360, Arc2D.OPEN);
				g2.draw(arc);
				String value = String.valueOf(i);
				g2.drawString(value, (float) (scoresLength+pos.x + scaleX),
						(float) (pos.y+scaleY));//dessine pas au bon endroit 
			}
			
			Objectif obj = game.getObj();
			Point2D pos = obj.getPos();
			arc.setArcByCenter(scoresLength+scaleX+pos.x, scaleY+pos.y, 10, 0, 360, Arc2D.OPEN);
			g2.setColor(Color.red);
			g2.drawOval(((int)pos.x)+scoresLength+scaleX-8, ((int)pos.y)+scaleY-8, 8, 8);
			
			List<Pair<String, Integer>> scores = game.getScores();
			g2.setColor(Color.BLACK);
			int yCmp=20;
			for(Pair<String, Integer> p : scores) {
				g2.drawString(p.getLeft()+" : "+p.getRight(), 10, yCmp);
				yCmp+=20;
			}
			
			List<Obstacle> obstacles = game.getObstacles();
			
			g2.setColor(Color.BLUE);
			for(Obstacle o : obstacles) {
				pos = o.getPos();
				arc.setArcByCenter(pos.x+scaleX, pos.y+scaleY, 10, 0, 360, Arc2D.OPEN);
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
