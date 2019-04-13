package model;

import java.util.ArrayList;
import java.util.List;

import utils.Pair;
import utils.Point2D;

public class Game {
	public static final int MAP_HEIGHT = 600;
	public static final int MAP_WIDTH = 800;
	
	private List<Pair<String, Integer>> scores;
	private List<Player> players;
	private List<Obstacle> obstacles;
	private boolean isObstaclesSet;
	private Objectif objectif;
	private Player myPlayer;

	public Game(List<Pair<String, Point2D>> players, Point2D objPt, String myPlayerName) {
		this.players = new ArrayList<Player>();
		this.obstacles = new ArrayList<Obstacle>();
		this.scores = new ArrayList<Pair<String,Integer>>();
		
		//test
		this.obstacles.add(new Obstacle(50.0, 30.0));
		//
		this.isObstaclesSet = false;
		if (players != null) {
			for(Pair<String, Point2D> p : players) {
				String name = p.getLeft();
				scores.add(new Pair<String, Integer>(name, 0));
				Point2D pos = p.getRight();
				Player pl = new Player(name, pos);
				this.players.add(pl);
				if(name.contentEquals(myPlayerName))
					myPlayer=pl;
			}
		}
		
		objectif = new Objectif(objPt);
	}
	
	public void update(List<Pair<String, Point2D>> tick) {
		for(Pair<String, Point2D> p : tick) {
			for(Player pl : players) {
				if(pl.getName().equals(p.getLeft())) {
					pl.setPos(p.getRight());
					break;
				}
			}
		}
	}
	
	public List<Player> getPlayers() {
		return new ArrayList<Player>(players);
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
	
	public void addNewPlayer(Player p) {//un nouveau joueur rejoint la partie
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


	public List<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public void moveAllPlayers() {
		for(Player p : players) {
			p.move();
		}
	}

	public void setScores(List<Pair<String, Integer>> scores) {
		this.scores=scores;
	}

	public Player getMyPlayer() {
		return myPlayer;
	}
	
}
