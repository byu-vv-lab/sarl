package edu.udel.cis.vsl.sarl.symbolic.type;

import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;

/**
 * Implementation of symbolic types using Flyweight Pattern. Note that the enum
 * SymbolicTypeKind is defined in
 * edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF.
 */
public abstract class SymbolicType implements SymbolicTypeIF {

	private SymbolicTypeKind kind;

	private int id = -1;

	/**
	 * Constructs new SymbolicType object with given kind and ID number -1. The
	 * ID number is only used for canonical representatives of the equivalence
	 * class, which is determined only after the object is instantiated and
	 * looked up in a set to see if an equivalent one already exists. This is
	 * the Flyweight pattern.
	 */
	SymbolicType(SymbolicTypeKind kind) {
		assert kind != null;
		this.kind = kind;
	}

	boolean intrinsicEquals(SymbolicType that) {
		return kind == that.kind;
	}

	int intrinsicHashCode() {
		return kind.hashCode();
	}

	public int id() {
		return id;
	}

	void setId(int id) {
		this.id = id;
	}

	public SymbolicTypeKind kind() {
		return kind;
	}

	public boolean isInteger() {
		return kind == SymbolicTypeKind.INTEGER;
	}

	public boolean isBoolean() {
		return kind == SymbolicTypeKind.BOOLEAN;
	}

	public boolean isReal() {
		return kind == SymbolicTypeKind.REAL;
	}

	public boolean isNumeric() {
		return kind == SymbolicTypeKind.INTEGER
				|| kind == SymbolicTypeKind.REAL;
	}

}
