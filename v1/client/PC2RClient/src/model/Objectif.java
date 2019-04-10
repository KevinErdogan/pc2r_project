package model;

import utils.Point2D;

public class Objectif {
	private Point2D pos;
	
	public Objectif(Point2D objPt) {
		pos=objPt;
	}

	public void setPos(Point2D pos) {
		this.pos=pos;
	}
	
	public Point2D getPos() {
		return pos;
	}

}
