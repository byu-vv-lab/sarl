package edu.udel.cis.vsl.sarl.symbolic.type;

public class SymbolicPrimitiveType extends SymbolicType {

	SymbolicPrimitiveType(SymbolicTypeKind kind) {
		super(kind);
	}

	@Override
	public String toString() {
		return kind().toString();
	}

	@Override
	protected int computeHashCode() {
		return kind().hashCode();
	}

	/**
	 * Returns true since we are assuming that has the same kind as this, and
	 * there is only one primitive type of this kind.
	 */
	@Override
	protected boolean intrinsicEquals(SymbolicType that) {
		return true;
	}

	/**
	 * Returns 0 since we are assuming that has the same kind as this, and there
	 * is only one primitive type of this kind.
	 */
	@Override
	protected int intrinsicCompare(SymbolicType that) {
		return 0;
	}

}
