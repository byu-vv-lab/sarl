package edu.udel.cis.vsl.sarl.collections.IF;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class ExpressionStub implements SymbolicExpression {

	private String name;

	private int id = -1;

	/**
	 * Creates new instance wrapping given name.
	 * 
	 * @param name
	 *            a non-null String
	 */
	public ExpressionStub(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof ExpressionStub)
			return name.equals(((ExpressionStub) object).name);
		return false;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public SymbolicObjectKind symbolicObjectKind() {
		return null;
	}

	@Override
	public int id() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public boolean isCanonic() {
		return id >= 0;
	}

	@Override
	public StringBuffer toStringBuffer(boolean atomize) {
		return new StringBuffer(name);
	}

	@Override
	public StringBuffer toStringBufferLong() {
		return new StringBuffer(name);
	}

	@Override
	public SymbolicObject argument(int index) {
		return null;
	}

	@Override
	public SymbolicObject[] arguments() {
		return null;
	}

	@Override
	public String atomString() {
		return name;
	}

	@Override
	public boolean isFalse() {
		return false;
	}

	@Override
	public boolean isNull() {
		return false;
	}

	@Override
	public boolean isNumeric() {
		return false;
	}

	@Override
	public boolean isOne() {
		return false;
	}

	@Override
	public boolean isTrue() {
		return false;
	}

	@Override
	public boolean isZero() {
		return false;
	}

	@Override
	public int numArguments() {
		return 0;
	}

	@Override
	public SymbolicOperator operator() {
		return null;
	}

	@Override
	public SymbolicType type() {
		return null;
	}

}
