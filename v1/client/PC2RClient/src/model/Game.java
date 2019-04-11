package model;

import java.util.LinkedList;
import java.util.List;

import utils.Pair;
import utils.Point2D;

public class Game {
	public static final int MAP_HEIGHT = 600;
	public static final int MAP_WIDTH = 800;
	
	private List<Pair<String, Integer>> scores;
	private List<Pair<String, Point2D>> players;
	private Map map;
	//private ArrayList<Obstacle> obstacles;
	private Objectif objectif;

	public Game(List<Pair<String, Point2D>> players, Point2D objPt) {
		this.players = new LinkedList<Pair<String, Point2D>>();
			for(Pair<String, Point2D> p : players) {
				Point2D pos = (Point2D) p.getRight();
				this.players.add(new Pair<String, Point2D>(p.getLeft(), pos));
			}
		
		objectif = new Objectif(objPt);
		map = new Map(Game.MAP_WIDTH, Game.MAP_HEIGHT); // ?
	}
	
	public List<Pair<String, Point2D>> getPlayers() {
		return new LinkedList<Pair<String, Point2D>>(players);
	}

	public void setObjPos(Point2D pos) {
		objectif.setPos(pos);
	}

	public Objectif getObj() {
		return objectif;
	}
	
	public List<Pair<String, Integer>> getScores(){
		return scores;
	}
	
	
}
