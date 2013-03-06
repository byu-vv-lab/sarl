package edu.udel.cis.vsl.sarl.ideal.common;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpression;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;

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

	protected IdealExpression(SymbolicOperator operator, SymbolicType type,
			SymbolicObject[] arguments) {
		super(operator, type, arguments);
	}

	protected IdealExpression(SymbolicOperator operator, SymbolicType type,
			Collection<SymbolicObject> arguments) {
		super(operator, type, arguments);
	}

	protected IdealExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0) {
		super(kind, type, arg0);
	}

	protected IdealExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1) {
		super(kind, type, arg0, arg1);
	}

	protected IdealExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		super(kind, type, arg0, arg1, arg2);
	}

	public abstract IdealKind idealKind();

}
