package model;

import utils.Point2D;

public class Obstacle {

	private Point2D pos;
	private final double radius = 10.0;
	
	public Obstacle(double x, double y) {
		this.pos = new Point2D(x, y);
	}
	
	public Obstacle(Point2D pt) {
		this.pos = new Point2D(pt.getX(), pt.getY());
	}
	
	public Point2D getPos() {
		return pos;
	}

	public double getRadius() {
		return radius;
	}
}
