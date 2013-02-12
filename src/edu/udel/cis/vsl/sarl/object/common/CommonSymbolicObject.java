package edu.udel.cis.vsl.sarl.object.common;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public abstract class CommonSymbolicObject implements SymbolicObject {

	private SymbolicObjectKind kind;

	private int hashCode;

	private boolean hashed = false;

	private long id = -1;

	protected CommonSymbolicObject(SymbolicObjectKind kind) {
		this.kind = kind;
	}

	public boolean isCanonic() {
		return id >= 0;
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

	/**
	 * Is the given symbolic object equal to this one---assuming the given
	 * symbolic object is of the same kind as this one? Must be defined in any
	 * concrete subclass.
	 * 
	 * @param that
	 *            a symbolic object of the same kind as this one
	 * @return true iff they define the same type
	 */
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

	public abstract void canonizeChildren(CommonObjectFactory factory);

}
