package model;

import utils.Pair;
import utils.Point2D;

public class Player {
	private String name;
	private int score;
	private Point2D pos;
	private final double thrustit = 0.10;//valeur a determiner pour que ce soit jouable (acceleration)
	private final double turnit = Math.PI/10;//changement de direction(virage) a determiner
	private final double radius = 10.0;//valeur arg0a determiner (hitbox en forme de cercle)
	private final double maxV = 1.0;
	private final double minV = -1.0;
	private Pair<Double, Integer> commands;//envoyer tous les serv_tickrate newcom et reset commands
	//faire correspondre les valeurs thrustit, turnit et radius avec le serveur
	
	
	
	public Player(String name, Point2D pos) {
		this.name = name;
		this.pos = pos;
		pos.vX = 0.0;//initialement a l'arret
		pos.vY = 0.0;
		pos.angle = 0.0;
		this.score = 0;
		this.commands = new Pair<Double, Integer>(0.0, 0);//angle, nombre de poussee
	}
	
	public Player(String name, double x, double y) {
		this.name = name;
		this.pos = new Point2D(x,y);
		pos.vX = 0.0;
		pos.vY = 0.0;
		pos.angle = 0.0;
		this.commands = new Pair<Double, Integer>(0.0, 0);
		this.score = 0;
	}
	
	//update avec donnees serveur (recu avec TICK)
	public void serveurUpdate(double x, double y, double vx, double vy, double angle) {
		this.pos = new Point2D(x,y);
		pos.vX = vx;
		pos.vY = vy;
		pos.angle = angle;
	}
	
	//deplacement
	public void move() {
		this.pos = new Point2D( (pos.x+pos.vX+Game.MAP_WIDTH)%Game.MAP_WIDTH,
								(pos.y+pos.vY+Game.MAP_HEIGHT )%Game.MAP_HEIGHT, 
								pos.vX, pos.vY, pos.angle);//arene thorique
		//System.out.println("new pos "+pos.x+" "+pos.y);
	}
	
	//controle du vehicule
	public void clock() {
		pos.angle = (pos.angle - this.turnit);
		this.commands.setLeft(this.commands.getLeft()-this.turnit);
		//System.out.println(pos.angle+" "+Math.cos(pos.angle)+" "+Math.sin(pos.angle));
	}
	
	public void anticlock() {
		pos.angle = (pos.angle + this.turnit);
		this.commands.setLeft(this.commands.getLeft()+this.turnit);
	}
	
	public void thrust() {
		double tmp_vx = pos.vX + this.thrustit*Math.cos(pos.angle);
		double tmp_vy = pos.vY + this.thrustit*Math.sin(pos.angle);
		if(tmp_vx < this.maxV && tmp_vx > this.minV)
			pos.vX = tmp_vx;
		if(tmp_vy < this.maxV && tmp_vy > this.minV)
			pos.vY = tmp_vy;
		this.commands.setRight(this.commands.getRight()+1);
		System.out.println(pos.vX+" "+pos.vY);
	}
	
	
	public boolean hasCollideWithObstacle(Obstacle obs) {
		return ((this.pos.x - obs.getPos().x) * (this.pos.x - obs.getPos().x) 
				+ (this.pos.y - obs.getPos().y) * (this.pos.y - obs.getPos().y))
				<= (this.radius * obs.getRadius());
	}
	
	public boolean hasCollideWithObjectif(Objectif obj) {
		return ((this.pos.x - obj.getPos().x) * (this.pos.x - obj.getPos().x) 
				+ (this.pos.y - obj.getPos().y) * (this.pos.y - obj.getPos().y))
				<= (this.radius * obj.getRadius());
	}
	
	public boolean hasCollideWithPlayer(Player pl) {
		return  ((this.pos.x - pl.getPos().x) * (this.pos.x - pl.getPos().x) 
				+ (this.pos.y - pl.getPos().y) * (this.pos.y - pl.getPos().y))
				<= (this.radius * pl.getRadius()); 
	}
	
	public void reactToCollision() {
		this.pos.vX = -1.0*this.pos.vX;
		this.pos.vY = -1.0*this.pos.vY;
	}
	
	public Pair<Double, Integer> getCommandsAndReset(){//a appeler pour recuper les donnee de NEWCOM
		Double angle = this.commands.getLeft();
		Integer nbThrust = this.commands.getRight();
		this.commands.setLeft(0.0);
		this.commands.setRight(0);
		return new Pair<Double, Integer>(angle, nbThrust);
	}
	
	public Point2D getPos() {
		return pos;
	}

	public void setPos(Point2D pos) {
		this.pos = pos;
	}

	public double getVx() {
		return pos.vX;
	}

	public void setVx(double vx) {
		pos.vX = vx;
	}

	public double getVy() {
		return pos.vY;
	}

	public void setVy(double vy) {
		pos.vY = vy;
	}

	public double getAngle() {
		return pos.angle;
	}

	public void setAngle(double angle) {
		this.pos.angle = angle;
	}

	public String getName() {
		return name;
	}

	public double getRadius() {
		return radius;
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}	
}
