package edu.udel.cis.vsl.sarl.type;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.object.CommonSymbolicObject;

/**
 * Implementation of symbolic types using Flyweight Pattern. Note that the enum
 * SymbolicTypeKind is defined in
 * edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF.
 */
public abstract class SymbolicType extends CommonSymbolicObject implements
		SymbolicTypeIF {

	private SymbolicTypeKind kind;

	/**
	 * Constructs new SymbolicType object with given kind and ID number -1.
	 */
	SymbolicType(SymbolicTypeKind kind) {
		super(SymbolicObjectKind.TYPE);
		assert kind != null;
		this.kind = kind;
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
	protected abstract boolean typeEquals(SymbolicType that);

	@Override
	public boolean intrinsicEquals(SymbolicObject object) {
		if (this == object)
			return true;
		if (object instanceof SymbolicType) {
			SymbolicType that = (SymbolicType) object;

			if (kind != that.kind)
				return false;
			return typeEquals(that);
		}
		return false;
	}

	@Override
	public SymbolicTypeKind typeKind() {
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
	public int compareLocal(SymbolicObject type) {
		SymbolicType that = (SymbolicType) type;
		int result = kind.compareTo(that.kind);

		if (result != 0)
			return result;
		return intrinsicCompare(that);
	}

}
