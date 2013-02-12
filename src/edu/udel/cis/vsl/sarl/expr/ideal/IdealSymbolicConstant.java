package edu.udel.cis.vsl.sarl.expr.ideal;

import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.expr.IF.NumericSymbolicConstant;

public class IdealSymbolicConstant extends NumericPrimitive implements
		NumericSymbolicConstant {

	IdealSymbolicConstant(StringObject name, SymbolicTypeIF type) {
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
