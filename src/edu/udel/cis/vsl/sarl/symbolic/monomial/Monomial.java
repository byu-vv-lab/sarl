package edu.udel.cis.vsl.sarl.symbolic.monomial;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.monic.MonicMonomial;

/**
 * A monomial is the product of a constant (i.e., a numeric concrete value) and
 * a monic monomial.
 */
public class Monomial extends CommonSymbolicExpression implements TreeExpressionIF {

	private static NumberFactoryIF numberFactory = Numbers.REAL_FACTORY;

	private static IntegerNumberIF NEG_ONE = numberFactory.integer(-1);

	private NumericConcreteExpressionIF coefficient;

	private MonicMonomial monic;

	Monomial(NumericConcreteExpressionIF coefficient, MonicMonomial monic) {
		super(coefficient.type());
		assert monic != null;
		assert coefficient.type().equals(monic.type());
		this.coefficient = coefficient;
		this.monic = monic;
	}

	public NumericConcreteExpressionIF coefficient() {
		return coefficient;
	}

	public MonicMonomial monicMonomial() {
		return monic;
	}

	protected int intrinsicHashCode() {
		return Monomial.class.hashCode() + coefficient.hashCode()
				+ monic.hashCode();
	}

	protected boolean intrinsicEquals(CommonSymbolicExpression expression) {
		if (expression instanceof Monomial) {
			Monomial that = (Monomial) expression;

			return coefficient.equals(that.coefficient)
					&& monic.equals(that.monic);
		}
		return false;
	}

	public String toString() {
		if (monic.isOne()) {
			return coefficient.toString();
		} else {
			if (coefficient.isOne()) {
				return monic.toString();
			} else {
				return coefficient + "*" + monic;
			}
		}
	}

	public String atomString() {
		if (monic.isOne()) {
			return coefficient.atomString();
		} else {
			if (coefficient.isOne()) {
				return monic.atomString();
			} else {
				return "(" + coefficient + "*" + monic + ")";
			}
		}
	}

	public boolean isZero() {
		return coefficient.isZero();
	}

	public boolean isOne() {
		return coefficient.isOne() && monic.isOne();
	}

	/**
	 * The degree of 0 is -1. For a non-zero monomial, the degree is the degree
	 * of the monic monomial.
	 */
	public IntegerNumberIF degree() {
		if (isZero()) {
			return NEG_ONE;
		}
		return monic.degree();
	}

	public TreeExpressionIF argument(int index) {
		if (coefficient.isOne()) {
			// this is the monic
			return monic.argument(index);
		} else {
			// general case...
			switch (index) {
			case 0:
				return coefficient;
			case 1:
				return monic;
			default:
				throw new RuntimeException("numArguments = " + 2 + ", index = "
						+ index);
			}
		}
	}

	public SymbolicOperator operator() {
		if (coefficient.isOne()) {
			// this is the monic
			return monic.operator();
		} else {
			// general case...
			return SymbolicOperator.MULTIPLY;
		}
	}

	public int numArguments() {
		if (coefficient.isOne()) {
			// this is the monic
			return monic.numArguments();
		} else {
			// general case...
			return 2;
		}
	}
}
