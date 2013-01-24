package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.StringObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;

public class CommonSymbolicConstant extends CommonSymbolicExpression implements
		SymbolicConstantIF {

	CommonSymbolicConstant(StringObject name, SymbolicTypeIF type) {
		super(SymbolicOperator.SYMBOLIC_CONSTANT, type, name);
	}

	@Override
	public StringObject name() {
		return (StringObject) argument(0);
	}

}
