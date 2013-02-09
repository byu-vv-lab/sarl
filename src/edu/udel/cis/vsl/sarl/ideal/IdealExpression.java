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

	/**
	 * Compare this to an IdealExpression of the same type and IdealKind.
	 * 
	 * @param that
	 *            an IdealExpression of the same type and IdealKind as this
	 * @return negative, 0 or positive
	 */
	protected abstract int compareIdeal(IdealExpression that);

	public int compareNumeric(NumericExpression that) {
		if (type().isInteger()) {
			if (that.type().isReal())
				return -1;
		} else if (that.type().isInteger())
			return 1;
		{
			int result = idealKind().compareTo(
					((IdealExpression) that).idealKind());

			if (result != 0)
				return result;
			return compareIdeal((IdealExpression) that);
		}
	}

}
