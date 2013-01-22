package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicObject;

public class CommonIntObject extends CommonSymbolicObject implements IntObject {

	private int value;

	CommonIntObject(int value) {
		super(SymbolicObjectKind.INTEGER);
		this.value = value;
	}

	@Override
	public int getInt() {
		return value;
	}

	@Override
	protected int compareLocal(SymbolicObject o) {
		return new Integer(value).compareTo(((IntObject) o).getInt());
	}

	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value == ((IntObject) o).getInt();
	}

	@Override
	public int computeHashCode() {
		return symbolicObjectKind().hashCode() ^ new Integer(value).hashCode();
	}

	@Override
	public String toString() {
		return new Integer(value).toString();
	}

}
