package utils;

public class PairPoint2D<T> {
	private T left;
	private Point2D right;
	
	public PairPoint2D(T left, Point2D right) {
		this.left = left;
		this.right = right;
	}
	
	public T getLeft() {
		return left;
	}
	public Point2D getRight() {
		return right;
	}
}
