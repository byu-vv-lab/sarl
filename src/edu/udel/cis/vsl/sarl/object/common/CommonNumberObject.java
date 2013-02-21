package edu.udel.cis.vsl.sarl.object.common;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumber;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;

public class CommonNumberObject extends CommonSymbolicObject implements
		NumberObject {

	private Number value;

	CommonNumberObject(Number value) {
		super(SymbolicObjectKind.NUMBER);
		this.value = value;
	}

	@Override
	public Number getNumber() {
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
		return value instanceof IntegerNumber;
	}

	@Override
	public boolean isReal() {
		return value instanceof RationalNumber;
	}

	@Override
	public void canonizeChildren(CommonObjectFactory factory) {
	}

	@Override
	public int compareTo(NumberObject o) {
		return value.compareTo(o.getNumber());
	}
}
