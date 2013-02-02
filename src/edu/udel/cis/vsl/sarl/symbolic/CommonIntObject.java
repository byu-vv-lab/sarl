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

	@Override
	public IntObject minWith(IntObject that) {
		return value <= that.getInt() ? this : that;
	}

	@Override
	public IntObject maxWith(IntObject that) {
		return value >= that.getInt() ? this : that;
	}

	@Override
	public IntObject minus(IntObject that) {
		return new CommonIntObject(value - that.getInt());
	}

	@Override
	public IntObject plus(IntObject that) {
		return new CommonIntObject(value + that.getInt());
	}

	@Override
	public int signum() {
		if (value > 0)
			return 1;
		else if (value == 0)
			return 0;
		else
			return -1;
	}

	@Override
	public boolean isZero() {
		return value == 0;
	}

	@Override
	public boolean isPositive() {
		return value > 0;
	}

	@Override
	public boolean isNegative() {
		return value < 0;
	}

	@Override
	public boolean isOne() {
		return value == 1;
	}

}
