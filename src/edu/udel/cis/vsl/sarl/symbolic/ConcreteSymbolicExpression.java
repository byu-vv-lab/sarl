package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.SymbolicTypeIF;

/**
 * Represents a concrete symbolic expression, i.e., a symbolic expression which
 * just wraps a concrete value. The operator is CONCRETE and has one argument.
 * 
 * @author siegel
 * 
 */
public class ConcreteSymbolicExpression extends CommonSymbolicExpression {

	ConcreteSymbolicExpression(SymbolicTypeIF type, SymbolicObject value) {
		super(SymbolicOperator.CONCRETE, type, value);
	}

	@Override
	public String atomString() {
		return toString();
	}

	@Override
	public String toString() {
		return argument(0).toString();
	}

}
