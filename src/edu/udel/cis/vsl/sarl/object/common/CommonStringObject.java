package edu.udel.cis.vsl.sarl.object.common;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

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

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public int compareTo(StringObject o) {
		return value.compareTo(o.getString());
	}
}
