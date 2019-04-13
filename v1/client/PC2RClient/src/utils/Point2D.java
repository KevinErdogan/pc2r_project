package utils;

public class Point2D {
	public double x;
	public double y;
	public double vX;
	public double vY;
	public double angle;
	
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2D(double x, double y, double vX, double vY, double angle) {
		super();
		this.x = x;
		this.y = y;
		this.vX = vX;
		this.vY = vY;
		this.angle = angle;
	}

}
