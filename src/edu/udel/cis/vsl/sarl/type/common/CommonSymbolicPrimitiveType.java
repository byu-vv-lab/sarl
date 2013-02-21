package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicPrimitiveType extends CommonSymbolicType {

	CommonSymbolicPrimitiveType(SymbolicTypeKind kind) {
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
	protected boolean typeEquals(CommonSymbolicType that) {
		return true;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

}
