package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;

/**
 * Implementation of symbolic types using Flyweight Pattern. Note that the enum
 * SymbolicTypeKind is defined in
 * edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF.
 */
public abstract class SymbolicType implements SymbolicTypeIF {

	private SymbolicTypeKind kind;

	private int id = -1;

	private boolean hashed = false;

	private int hashCode;

	/**
	 * Constructs new SymbolicType object with given kind and ID number -1.
	 */
	SymbolicType(SymbolicTypeKind kind) {
		assert kind != null;
		this.kind = kind;
	}

	protected abstract int computeHashCode();

	@Override
	public int id() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		if (!hashed) {
			hashCode = computeHashCode();
			hashed = true;
		}
		return hashCode;
	}

	/**
	 * Is the given symbolic type equal to this one---assuming the given
	 * symbolic type is of the same kind as this one? Must be defined in any
	 * concrete subclass.
	 * 
	 * @param that
	 *            a symbolic type of the same kind as this one
	 * @return true iff they define the same type
	 */
	protected abstract boolean intrinsicEquals(SymbolicType that);

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object instanceof SymbolicType) {
			SymbolicType that = (SymbolicType) object;

			if (id > 0 && that.id > 0)
				return id == that.id;
			if (hashed && that.hashed && hashCode != that.hashCode)
				return false;
			if (kind != that.kind)
				return false;
			return intrinsicEquals(that);
		}
		return false;
	}

	@Override
	public SymbolicTypeKind kind() {
		return kind;
	}

	@Override
	public boolean isInteger() {
		return kind == SymbolicTypeKind.INTEGER;
	}

	@Override
	public boolean isBoolean() {
		return kind == SymbolicTypeKind.BOOLEAN;
	}

	@Override
	public boolean isReal() {
		return kind == SymbolicTypeKind.REAL;
	}

	@Override
	public boolean isNumeric() {
		return kind == SymbolicTypeKind.INTEGER
				|| kind == SymbolicTypeKind.REAL;
	}

	/**
	 * Compares the given symbolic type to this one---assuming the given one is
	 * the same kind as this.
	 * 
	 * @param that
	 *            a symbolic type of the same kind as this
	 * @return a negative int if this one precedes that in some total order; 0
	 *         if equal, a positive int if that one precedes this
	 */
	protected abstract int intrinsicCompare(SymbolicType that);

	@Override
	public int compareTo(SymbolicTypeIF type) {
		SymbolicType that = (SymbolicType) type;
		int result = kind.compareTo(that.kind);

		if (result != 0)
			return result;
		return intrinsicCompare(that);
	}

}
