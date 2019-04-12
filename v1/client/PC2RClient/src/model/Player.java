package model;

import utils.Point2D;

public class Player {
	private Map m;
	
	private String name;
	private Point2D pos;
	private double vx; //vitesse
	private double vy; //vitesse
	private double angle;
	private final double thrustit = 0.10;//valeur a determiner pour que ce soit jouable (acceleration)
	private final double turnit = 10.0;//changement de direction(virage) a determiner
	private final double radius = 10.0;//valeur a determiner (hitbox en forme de cercle)
	private final double maxV = 3.0;
	//faire correspondre les valeurs thrustit, turnit et radius avec le serveur
	
	public Player(String name, Point2D pos, Map m) {
		this.name = name;
		this.pos = pos;
		
		this.vx = 0.0;//initialement a l'arret
		this.vy = 0.0;
		this.angle = 0.0;
		this.m = m;
	}
	
	public Player(String name, double x, double y, Map m) {
		this.name = name;
		this.pos = new Point2D(x,y);
		this.vx = 0.0;
		this.vy = 0.0;
		this.angle = 0.0;
		this.m = m;
	}
	
	//update avec donnees serveur (recu avec TICK)
	public void serveurUpdate(double x, double y, double vx, double vy, double angle) {
		this.pos = new Point2D(x,y);
		this.vx = vx;
		this.vy = vy;
		this.angle = angle;
	}
	
	//deplacement
	public void move() {
		this.pos = new Point2D( (pos.getX()+this.vx+m.getWidth())%m.getWidth(),
								(pos.getY()+this.vy+m.getHeight())%m.getHeight() );//arene thorique
	}
	
	//controle du vehicule
	public void clock() {
		this.angle = (this.angle - this.turnit + 360.0)%360.0;
	}
	
	public void anticlock() {
		this.angle = (this.angle + this.turnit + 360.0)%360.0;
	}
	
	public void thrust() {
		double tmp_vx = this.vx + this.thrustit*Math.cos(this.angle);
		double tmp_vy = this.vy + this.thrustit*Math.sin(this.angle);
		if(tmp_vx < this.maxV)
			this.vx = tmp_vx;
		if(tmp_vy < this.maxV)
			this.vy = tmp_vy;
	}
	
	public boolean hasCollideWithObstacle(Obstacle obs) {//reste a gerer les collisions "thorique"
		return ((this.pos.getX() - obs.getPos().getX()) * (this.pos.getX() - obs.getPos().getX()) 
				+ (this.pos.getY() - obs.getPos().getY()) * (this.pos.getY() - obs.getPos().getY()))
				<= (this.radius * obs.getRadius());
	}
	
	public boolean hasCollideWithPlayer(Player pl) {//reste a gerer les collisions "thorique"
		return  ((this.pos.getX() - pl.getPos().getX()) * (this.pos.getX() - pl.getPos().getX()) 
				+ (this.pos.getY() - pl.getPos().getY()) * (this.pos.getY() - pl.getPos().getY()))
				<= (this.radius * pl.getRadius()); 
	}
	

	public Point2D getPos() {
		return pos;
	}

	public void setPos(Point2D pos) {
		this.pos = pos;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double angle) {
		this.angle = angle;
	}

	public String getName() {
		return name;
	}

	public double getRadius() {
		return radius;
	}
	
	
	
	
}
