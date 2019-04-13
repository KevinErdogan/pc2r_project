package model;

import utils.Point2D;

public class Player {
	private String name;
	private Point2D pos;
	private final double thrustit = 0.10;//valeur a determiner pour que ce soit jouable (acceleration)
	private final double turnit = 10.0;//changement de direction(virage) a determiner
	private final double radius = 10.0;//valeur arg0a determiner (hitbox en forme de cercle)
	private final double maxV = 1.0;
	private final double minV = -1.0;
	//faire correspondre les valeurs thrustit, turnit et radius avec le serveur
	
	public Player(String name, Point2D pos) {
		this.name = name;
		this.pos = pos;
		
		pos.vX = 0.0;//initialement a l'arret
		pos.vY = 0.0;
		pos.angle = 0.0;
	}
	
	public Player(String name, double x, double y) {
		this.name = name;
		this.pos = new Point2D(x,y);
		pos.vX = 0.0;
		pos.vY = 0.0;
		pos.angle = 0.0;
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
								(pos.y+pos.vY+Game.MAP_HEIGHT )%Game.MAP_HEIGHT );//arene thorique
	}
	
	//controle du vehicule
	public void clock() {
		pos.angle = (pos.angle - this.turnit);
		System.out.println(pos.angle+" "+Math.cos(pos.angle)+" "+Math.sin(pos.angle));
	}
	
	public void anticlock() {
		pos.angle = (pos.angle + this.turnit);
	}
	
	public void thrust() {System.out.println("accel");
		double tmp_vx = pos.vX + this.thrustit*Math.cos(pos.angle);
		double tmp_vy = pos.vY + this.thrustit*Math.sin(pos.angle);
		if(tmp_vx < this.maxV && tmp_vx > this.minV)
			pos.vX = tmp_vx;
		if(tmp_vy < this.maxV && tmp_vy > this.minV)
			pos.vY = tmp_vy;
		System.out.println(pos.vX+" "+pos.vY);
	}
	
	public void decelerate() {
		double tmp_vx = pos.vX - this.thrustit*Math.cos(pos.angle);
		double tmp_vy = pos.vY - this.thrustit*Math.sin(pos.angle);
		if(tmp_vx < this.maxV && tmp_vx > this.minV)
			pos.vX = tmp_vx;
		if(tmp_vy < this.maxV && tmp_vy > this.minV)
			pos.vY = tmp_vy;
	}
	
	public boolean hasCollideWithObstacle(Obstacle obs) {//reste a gerer les collisions "thorique"
		return ((this.pos.x - obs.getPos().x) * (this.pos.x - obs.getPos().x) 
				+ (this.pos.y - obs.getPos().y) * (this.pos.y - obs.getPos().y))
				<= (this.radius * obs.getRadius());
	}
	
	public boolean hasCollideWithPlayer(Player pl) {//reste a gerer les collisions "thorique"
		return  ((this.pos.x - pl.getPos().x) * (this.pos.x - pl.getPos().x) 
				+ (this.pos.y - pl.getPos().y) * (this.pos.y - pl.getPos().y))
				<= (this.radius * pl.getRadius()); 
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
	
}
