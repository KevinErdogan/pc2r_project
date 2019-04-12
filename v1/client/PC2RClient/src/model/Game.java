package model;

import java.util.ArrayList;
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
	private List<Obstacle> obstacles;
	private boolean isObstaclesSet;
	private Objectif objectif;

	public Game(List<Pair<String, Point2D>> players, Point2D objPt) {
		this.players = new LinkedList<Pair<String, Point2D>>();
		this.obstacles = new ArrayList<Obstacle>();
		
		//test
		this.obstacles.add(new Obstacle(50.0, 30.0));
		//
		
		this.isObstaclesSet = false;
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
	
	public void addNewPlayer(Pair<String, Point2D> p) {//un nouveau joueur rejoint la partie
		this.players.add(p);
	}
	
	public void setObstacles(List<Point2D> obs) {//recuperation des obstacles
		if(this.isObstaclesSet == false) {
			this.isObstaclesSet = true;
			for(Point2D p : obs) {
				this.obstacles.add(new Obstacle(p));
			}
		}
	}
	
	public void playersCollide() {//collision player/player && player/obstacle
		
	}

	public Map getMap() {
		return map;
	}

	public List<Obstacle> getObstacles() {
		return obstacles;
	}
	
	
	
}
