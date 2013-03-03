package edu.udel.cis.vsl.sarl.expr.cnf;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanSymbolicConstant;

public class CnfSymbolicConstant extends CnfBooleanExpression implements
		BooleanSymbolicConstant {

	CnfSymbolicConstant(StringObject name, SymbolicType type) {
		super(SymbolicOperator.SYMBOLIC_CONSTANT, type, name);
		assert type.isBoolean();
	}

	@Override
	public StringObject name() {
		return (StringObject) argument(0);
	}

	@Override
	public String toString() {
		return name().toString();
	}

}
