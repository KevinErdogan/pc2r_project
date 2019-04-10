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
import utils.PairPoint2D;
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
			
			List<PairPoint2D<String>> players = game.getPlayers();
			int i =1;
			for(PairPoint2D<String> p : players) {
				// update playernameList in interface
				Point2D pos = p.getRight();
				arc.setArcByCenter(pos.getX(), pos.getY(), 10, 0, 360, Arc2D.OPEN);
				g2.draw(arc);
				String value = String.valueOf(i);
				g2.drawString(value, (float) (pos.getX()), (float) (pos.getY()));
			}
			
			Objectif obj = game.getObj();
			Point2D pos = obj.getPos();
			arc.setArcByCenter(pos.getX(), pos.getY(), 10, 0, 360, Arc2D.OPEN);
			g2.setColor(Color.red);
			g2.drawOval(((int)pos.getX())-8, ((int)pos.getY())-8, 8, 8);
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
}
