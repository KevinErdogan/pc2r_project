package utils;

public class PairInteger<T> {
	private T left;
	private Integer right;
	
	public PairInteger(T left, Integer right) {
		this.left = left;
		this.right = right;
	}
	
	public T getLeft() {
		return left;
	}
	public Integer getRight() {
		return right;
	}
}
