package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.NumberIF;
import edu.udel.cis.vsl.sarl.IF.NumberObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicObject;

public class CommonNumberObject extends CommonSymbolicObject implements
		NumberObject {

	private NumberIF value;

	CommonNumberObject(NumberIF value) {
		super(SymbolicObjectKind.NUMBER);
		this.value = value;
	}

	@Override
	public NumberIF getNumber() {
		return value;
	}

	@Override
	protected int compareLocal(SymbolicObject o) {
		return value.compareTo(((NumberObject) o).getNumber());
	}

	@Override
	public boolean intrinsicEquals(SymbolicObject o) {
		return value.equals(((NumberObject) o).getNumber());
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
	public int signum() {
		return value.signum();
	}

	@Override
	public boolean isZero() {
		return value.isZero();
	}

	@Override
	public boolean isOne() {
		return value.isOne();
	}
}
