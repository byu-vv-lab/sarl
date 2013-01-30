package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.StringObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;

public class NumericSymbolicConstant extends NumericPrimitive implements
		SymbolicConstantIF {

	NumericSymbolicConstant(StringObject name, SymbolicTypeIF type) {
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
