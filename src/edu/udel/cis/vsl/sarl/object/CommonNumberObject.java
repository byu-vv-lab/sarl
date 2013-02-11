package edu.udel.cis.vsl.sarl.object;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

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

	@Override
	public boolean isInteger() {
		return value instanceof IntegerNumberIF;
	}

	@Override
	public boolean isReal() {
		return value instanceof RationalNumberIF;
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
	}
}
