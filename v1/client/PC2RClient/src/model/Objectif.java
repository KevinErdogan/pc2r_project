package model;

import utils.Point2D;

public class Objectif {
	private Point2D pos;
	private double radius = 35.0;
	private boolean pickedUp = false;
	
	public Objectif(Point2D objPt) {
		pos=objPt;
	}

	public void setPos(Point2D pos) {
		this.pos=pos;
	}
	
	public Point2D getPos() {
		return pos;
	}

	public double getRadius() {
		return radius;
	}

	public boolean isPickedUp() {
		return pickedUp;
	}

	public void setPickedUp(boolean pickedUp) {
		this.pickedUp = pickedUp;
	}
	


}
