package edu.udel.cis.vsl.sarl.symbolic.constant;

public class SymbolicConstantKey {
	SymbolicConstant constant;

	SymbolicConstantKey(SymbolicConstant constant) {
		this.constant = constant;
	}

	public boolean equals(Object object) {
		return object instanceof SymbolicConstantKey
				&& constant
						.intrinsicEquals(((SymbolicConstantKey) object).constant);
	}

	public int hashCode() {
		return constant.intrinsicHashCode();
	}

}
