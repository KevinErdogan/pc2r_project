package model;

import java.util.ArrayList;

public class Game {
	public static final int MAP_HEIGHT = 600;
	public static final int MAP_WIDTH = 800;
	
	private ArrayList<Player> players;
	private Map map;
	private ArrayList<Obstacle> obstacles;
	private Objectif objectif;
	
	public Game(ArrayList<Player> players, ArrayList<Obstacle> obstacles, Objectif obj) {
		this.players = players;
		this.obstacles=obstacles;
		objectif=obj;
		map = new Map(Game.MAP_WIDTH, Game.MAP_HEIGHT);
	}
	
	
}
