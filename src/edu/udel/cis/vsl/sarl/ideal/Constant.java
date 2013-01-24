package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.NumberObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

public class Constant extends CommonSymbolicExpression implements Monomial {

	protected Constant(SymbolicTypeIF type, NumberObject value) {
		super(SymbolicOperator.CONCRETE, type, value);
	}

	public NumberObject value() {
		return (NumberObject) argument(0);
	}

}
