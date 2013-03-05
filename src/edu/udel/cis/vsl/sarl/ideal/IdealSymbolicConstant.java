package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.NumericSymbolicConstant;

public class IdealSymbolicConstant extends NumericPrimitive implements
		NumericSymbolicConstant {

	IdealSymbolicConstant(StringObject name, SymbolicType type) {
		super(SymbolicOperator.SYMBOLIC_CONSTANT, type, name);
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
