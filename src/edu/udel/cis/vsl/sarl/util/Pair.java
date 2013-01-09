package edu.udel.cis.vsl.sarl.util;

public class Pair<S, T> {

	public S left;

	public T right;

	public Pair(S left, T right) {
		assert left != null;
		assert right != null;
		this.left = left;
		this.right = right;
	}

	public int hashCode() {
		return left.hashCode() + right.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof Pair<?, ?>) {
			return left.equals(((Pair<?, ?>) object).left)
					&& right.equals(((Pair<?, ?>) object).right);
		} else {
			return false;
		}
	}

}
