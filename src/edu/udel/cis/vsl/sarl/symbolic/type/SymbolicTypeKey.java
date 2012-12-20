package edu.udel.cis.vsl.sarl.symbolic.type;

/**
 * Wrapper class (wrapping a SymbolicType object) used by Flyweight Pattern to
 * basically redefined the equals/hashCode methods. The re-defined methods will
 * invoke the methods instrinsicEquals/intrinsicHashCode in the SymbolicType
 * object. These are used for find the canonical representative of an
 * equivalence class. For the user outside of this module, we want the
 * equals/hashCode methods to be == and Object.hashCode (or something
 * equivalently fast)---which is the main advantage of flyweighting.
 */
public class SymbolicTypeKey {

	private SymbolicType type;

	public SymbolicTypeKey(SymbolicType type) {
		assert type != null;
		this.type = type;
	}

	public boolean equals(Object object) {
		return object instanceof SymbolicTypeKey
				&& ((SymbolicTypeKey) object).type.intrinsicEquals(type);
	}

	public int hashCode() {
		return type.intrinsicHashCode();
	}

}
