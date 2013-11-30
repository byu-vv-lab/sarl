package edu.udel.cis.vsl.sarl.universe.common;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;
import edu.udel.cis.vsl.sarl.simplify.common.CommonSimplifier;

public class MathSimplifier extends CommonSimplifier {

	private SymbolicExpression sinFunction;
	private SymbolicExpression cosFunction;
	private MathUniverse mathUniverse;
	private NumericExpression one;

	public MathSimplifier(MathUniverse mathUniverse,
			SymbolicExpression sinFunction, SymbolicExpression cosFunction) {
		super(mathUniverse);
		this.mathUniverse = mathUniverse;
		this.sinFunction = sinFunction;// making method for sin
		this.cosFunction = cosFunction;
		this.one = mathUniverse.oneReal();

	}

	@Override
	public Map<SymbolicConstant, SymbolicExpression> substitutionMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanExpression getReducedContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanExpression getFullContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected SymbolicExpression simplifyExpression(
			SymbolicExpression expression) {
		SymbolicExpression result = this.simplifyGenericExpression(expression);
		SymbolicOperator operator = result.operator();

		if (operator == SymbolicOperator.POWER) {
			NumericExpression base = (NumericExpression) result.argument(0);
			SymbolicObject arg1 = result.argument(1);

			if (base.operator() == SymbolicOperator.APPLY) {
				SymbolicExpression function = (SymbolicExpression) base
						.argument(0);

				if (function == cosFunction) {
					if (arg1.symbolicObjectKind() == SymbolicObjectKind.INT) {
						int exp = ((IntObject) arg1).getInt();

						if (exp > 1) {
							SymbolicSequence<?> sequence = (SymbolicSequence<?>) base
									.argument(1);
							NumericExpression theta = (NumericExpression) sequence
									.get(0);
							NumericExpression sinTheta = mathUniverse
									.sin(theta);
							NumericExpression sinTheta2 = mathUniverse
									.multiply(sinTheta, sinTheta);
							NumericExpression difference = mathUniverse
									.subtract(one, sinTheta2);
							NumericExpression factor = mathUniverse.power(
									difference, exp / 2);

							result = exp % 2 == 0 ? factor : mathUniverse
									.multiply(mathUniverse.cos(theta), factor);
						}
					}
				}
			}
		}
		return result;
	}
}
