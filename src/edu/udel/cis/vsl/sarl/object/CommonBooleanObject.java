package edu.udel.cis.vsl.sarl.object;

import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public class CommonBooleanObject extends CommonSymbolicObject implements
		BooleanObject {

	private boolean value;

	CommonBooleanObject(boolean value) {
		super(SymbolicObjectKind.BOOLEAN);
		this.value = value;
	}

	@Override
	public boolean getBoolean() {
		return value;
	}

	/**
	 * Know that o has kind BOOLEAN and is not == to this.
	 */
	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value == ((BooleanObject) o).getBoolean();
	}

	@Override
	public int computeHashCode() {
		return symbolicObjectKind().hashCode() ^ new Boolean(value).hashCode();
	}

	@Override
	public String toString() {
		return new Boolean(value).toString();
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
	}

}
