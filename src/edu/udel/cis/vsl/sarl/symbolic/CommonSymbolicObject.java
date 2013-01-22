package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.SymbolicObject;

public abstract class CommonSymbolicObject implements SymbolicObject {

	private SymbolicObjectKind kind;

	private int hashCode;

	private boolean hashed = false;

	private long id = -1;

	CommonSymbolicObject(SymbolicObjectKind kind) {
		this.kind = kind;
	}

	/**
	 * Sets the id.
	 * 
	 * @param id
	 */
	void setId(long id) {
		this.id = id;
	}

	public long id() {
		return id;
	}

	protected abstract int compareLocal(SymbolicObject o);

	@Override
	public int compareTo(SymbolicObject o) {
		int result = kind.compareTo(o.symbolicObjectKind());

		if (result != 0)
			return result;
		return compareLocal(o);
	}

	@Override
	public SymbolicObjectKind symbolicObjectKind() {
		return kind;
	}

	protected abstract int computeHashCode();

	@Override
	public int hashCode() {
		if (!hashed) {
			hashCode = computeHashCode();
			hashed = true;
		}
		return hashCode;
	}

	protected abstract boolean intrinsicEquals(SymbolicObject o);

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o instanceof CommonSymbolicObject) {
			CommonSymbolicObject that = (CommonSymbolicObject) o;

			if (kind != that.kind)
				return false;
			if (id >= 0 && that.id >= 0) {
				return id == that.id;
			}
			if (hashed && that.hashed && hashCode != that.hashCode)
				return false;
			return intrinsicEquals(that);
		}
		return false;
	}
}
