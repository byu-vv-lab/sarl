package edu.udel.cis.vsl.sarl.object.common;

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
		return Boolean.toString(value);
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public int compareTo(BooleanObject o) {
		return value ? (o.getBoolean() ? 0 : 1) : (o.getBoolean() ? -1 : 0);
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		return new StringBuffer(Boolean.toString(value));
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return new StringBuffer(Boolean.toString(value));
	}

}
