package edu.udel.cis.vsl.sarl.type;

import edu.udel.cis.vsl.sarl.object.ObjectFactory;

public class SymbolicPrimitiveType extends SymbolicType {

	SymbolicPrimitiveType(SymbolicTypeKind kind) {
		super(kind);
	}

	@Override
	public String toString() {
		return typeKind().toString();
	}

	@Override
	protected int computeHashCode() {
		return typeKind().hashCode();
	}

	/**
	 * Returns true since we are assuming that has the same kind as this, and
	 * there is only one primitive type of this kind.
	 */
	@Override
	protected boolean typeEquals(SymbolicType that) {
		return true;
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
	}

}
