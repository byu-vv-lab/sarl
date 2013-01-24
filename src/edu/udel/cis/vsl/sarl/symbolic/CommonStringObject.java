package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.StringObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicObject;

public class CommonStringObject extends CommonSymbolicObject implements
		StringObject {

	private String value;

	CommonStringObject(String value) {
		super(SymbolicObjectKind.STRING);
		this.value = value;
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	protected int compareLocal(SymbolicObject o) {
		return value.compareTo(((StringObject) o).getString());
	}

	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value.equals(((StringObject) o).getString());
	}

	@Override
	public int computeHashCode() {
		return symbolicObjectKind().hashCode() ^ value.hashCode();
	}

	@Override
	public String toString() {
		return value.toString();
	}
}
