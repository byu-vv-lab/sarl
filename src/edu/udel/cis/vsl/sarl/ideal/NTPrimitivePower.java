package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.IntObject;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

public class NTPrimitivePower extends CommonSymbolicExpression implements
		PrimitivePower {

	protected NTPrimitivePower(Primitive primitive, IntObject exponent) {
		super(SymbolicOperator.POWER, primitive.type(), primitive, exponent);
	}

	public Primitive primitive() {
		return (Primitive) argument(0);
	}

	public IntObject exponent() {
		return (IntObject) argument(1);
	}

}
