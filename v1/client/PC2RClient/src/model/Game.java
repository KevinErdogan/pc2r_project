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
	private Player myPlayer = null;

	public Game(List<Pair<String, Point2D>> players, Point2D objPt, List<Point2D> obs, String myPlayerName) {
		this.players = new ArrayList<Player>();
		this.obstacles = new ArrayList<Obstacle>();
		this.scores = new ArrayList<Pair<String,Integer>>();
		
		for(Point2D p : obs) {
			this.obstacles.add(new Obstacle(p));
		}
		System.out.println("Map has "+this.obstacles.size()+" obstacle(s) !");
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
		ArrayList<Player> toRemove = new ArrayList<>(players);
		boolean newPlayer;
		for(Pair<String, Point2D> p : tick) {
			newPlayer = true;
			for(Player pl : players) {
				if(pl.getName().equals(p.getLeft())) {
					pl.setPos(p.getRight());
					newPlayer = false;
					toRemove.remove(pl);
					break;
				}
			}
			if(newPlayer) {
				players.add(new Player(p.getLeft(), p.getRight()));//on retire les joueurs deco
			}
		}
		players.removeAll(toRemove);
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
	

	public List<Obstacle> getObstacles() {
		return obstacles;
	}
	
	public void moveAllPlayersAndCollide() {
		for(Player p : players) {
			p.move();
		}
		for(int i = 0; i < players.size(); i++) {
			for(int j = i+1; j < players.size(); j++) {
				if(players.get(i).hasCollideWithPlayer(players.get(j))) {
					players.get(i).reactToCollision();
					players.get(j).reactToCollision();
				}
			}
			for(Obstacle o : obstacles) {
				if(players.get(i).hasCollideWithObstacle(o)) {
					players.get(i).reactToCollision();
				}
			}
		}
		if(this.objectif.isPickedUp() == false) {
			for(Player p : players) {
				if(p.hasCollideWithObjectif(this.objectif)) {
					p.setScore(p.getScore()+1);
					this.objectif.setPickedUp(true);
					break;
				}
			}
		}
	}
	
	public void updateScores(List<Pair<String, Integer>> scores) {
		for(Pair<String, Integer> p : scores) {
			for(Player pl : players) {
				if(pl.getName().equals(p.getLeft())) {
					pl.setScore(p.getRight());
					break;
				}
			}
		}
	}

	public void setScores(List<Pair<String, Integer>> scores) {
		this.scores=scores;
	}

	public Player getMyPlayer() {
		return myPlayer;
	}

	public void setObjectif(Objectif objectif) {
		this.objectif = objectif;
	}

}
