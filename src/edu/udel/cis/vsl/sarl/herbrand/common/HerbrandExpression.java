package edu.udel.cis.vsl.sarl.herbrand.common;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;

public class HerbrandExpression extends CommonSymbolicExpression implements
		NumericExpression {

	protected HerbrandExpression(SymbolicOperator operator, SymbolicType type,
			SymbolicObject[] arguments) {
		super(operator, type, arguments);
	}

	protected HerbrandExpression(SymbolicOperator operator, SymbolicType type,
			Collection<SymbolicObject> arguments) {
		super(operator, type, arguments);
	}

	protected HerbrandExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0) {
		super(kind, type, arg0);
	}

	protected HerbrandExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1) {
		super(kind, type, arg0, arg1);
	}

	protected HerbrandExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		super(kind, type, arg0, arg1, arg2);
	}


	

}
