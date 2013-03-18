package edu.udel.cis.vsl.sarl.type.common;

import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class CommonSymbolicPrimitiveType extends CommonSymbolicType {

	private final static int classCode = CommonSymbolicPrimitiveType.class
			.hashCode();

	private StringBuffer name;

	CommonSymbolicPrimitiveType(SymbolicTypeKind kind) {
		super(kind);
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		if (name == null) {
			if (isBoolean())
				name = new StringBuffer("boolean");
			else
				name = new StringBuffer(typeKind().toString());
		}
		return name;
	}

	@Override
	protected int computeHashCode() {
		return classCode ^ typeKind().hashCode();
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
