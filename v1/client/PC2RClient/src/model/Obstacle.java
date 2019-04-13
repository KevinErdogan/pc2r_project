package model;

import utils.Point2D;

public class Obstacle {

	private Point2D pos;
	private final double radius = 25.0;
	
	public Obstacle(double x, double y) {
		this.pos = new Point2D(x, y);
	}
	
	public Obstacle(Point2D pt) {
		this.pos = new Point2D(pt.x, pt.y);
	}
	
	public Point2D getPos() {
		return pos;
	}

	public double getRadius() {
		return radius;
	}
}
