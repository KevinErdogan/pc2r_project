package model;

import java.util.LinkedList;
import java.util.List;

import utils.Pair;
import utils.PairInteger;
import utils.PairPoint2D;
import utils.Point2D;

public class Game {
	public static final int MAP_HEIGHT = 600;
	public static final int MAP_WIDTH = 800;
	
	private List<PairInteger<String>> scores;
	private List<PairPoint2D<String>> players;
	private Map map;
	//private ArrayList<Obstacle> obstacles;
	private Objectif objectif;

	public Game(List<PairPoint2D<String>> players, Point2D objPt) {
		this.players = new LinkedList<PairPoint2D<String>>();
			for(PairPoint2D<String> p : players) {
				Point2D pos = (Point2D) p.getRight();
				this.players.add(new PairPoint2D<String>(p.getLeft(), pos));
			}
		
		objectif = new Objectif(objPt);
		map = new Map(Game.MAP_WIDTH, Game.MAP_HEIGHT); // ?
	}
		
	public Game(List<PairInteger> players, Point2D objPt) {
			this.players = new LinkedList<PairPoint2D<String>>();
			for(PairInteger<String> p : players) {
				Integer score = (Integer) p.getRight();
				this.players.add(new PairPoint2D<String>(p.getLeft(), new Point2D(-100,-100))); // not visible
				scores.add(new PairInteger<String>(p.getLeft(), score));
			}
			objectif = new Objectif(objPt);
			map = new Map(Game.MAP_WIDTH, Game.MAP_HEIGHT); // ?
	}
	
	public List<PairPoint2D<String>> getPlayers() {
		return new LinkedList<PairPoint2D<String>>(players);
	}

	public void setObjPos(Point2D pos) {
		objectif.setPos(pos);
	}

	public Objectif getObj() {
		return objectif;
	}
	
	public List<PairInteger<String>> getScores(){
		return scores;
	}
	
	
}
