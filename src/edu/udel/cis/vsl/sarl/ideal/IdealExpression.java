package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.NumericExpression;

public abstract class IdealExpression extends CommonSymbolicExpression
		implements NumericExpression {

	public enum IdealKind {
		NTRationalExpression,
		NTPolynomial,
		NTMonomial,
		NTMonic,
		NTPrimitivePower,
		NumericPrimitive,
		Constant
	}

	protected IdealExpression(SymbolicOperator operator, SymbolicTypeIF type,
			SymbolicObject[] arguments) {
		super(operator, type, arguments);
	}

	protected IdealExpression(SymbolicOperator kind, SymbolicTypeIF type,
			SymbolicObject arg0) {
		super(kind, type, arg0);
	}

	protected IdealExpression(SymbolicOperator kind, SymbolicTypeIF type,
			SymbolicObject arg0, SymbolicObject arg1) {
		super(kind, type, arg0, arg1);
	}

	protected IdealExpression(SymbolicOperator kind, SymbolicTypeIF type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		super(kind, type, arg0, arg1, arg2);
	}

	public abstract IdealKind idealKind();

}
