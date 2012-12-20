package edu.udel.cis.vsl.sarl.symbolic.constant;

import edu.udel.cis.vsl.sarl.symbolic.BooleanPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.SymbolicConstantExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

/**
 * Represents an expression consisting of just a symbolic constant. Essentially
 * wraps a symbolic constant into a SymbolicExpression.
 * 
 * Implements the NumericPrimitive interface because a symbolic constant
 * expression of numeric type is primitive: it cannot be decomposed further into
 * numeric expressions. Ditto for BooleanPrimitive. (These interfaces are used
 * in the canonical form algorithms.)
 * 
 * A SymbolicConstantExpressionIF is a TreeExpressionIF, so must implement
 * kind() and argument(index). There are 0 arguments, and the kind is
 * SYMBOLIC_CONSTANT.
 */
public class SymbolicConstantExpression extends SymbolicExpression implements
		SymbolicConstantExpressionIF, NumericPrimitive, BooleanPrimitive {

	private SymbolicConstantIF symbolicConstant;

	SymbolicConstantExpression(SymbolicConstantIF symbolicConstant) {
		super(symbolicConstant.type());
		this.symbolicConstant = symbolicConstant;
	}

	@Override
	protected boolean intrinsicEquals(SymbolicExpression that) {
		return that instanceof SymbolicConstantExpression
				&& symbolicConstant
						.equals(((SymbolicConstantExpression) that).symbolicConstant);
	}

	@Override
	protected int intrinsicHashCode() {
		return SymbolicConstantExpression.class.hashCode()
				+ symbolicConstant.hashCode();
	}

	@Override
	public SymbolicConstantIF symbolicConstant() {
		return symbolicConstant;
	}

	/**
	 * String representation suitable for incorporating into another formula, in
	 * this case, just same as toString, i.e., the name of the symbolic
	 * constant.
	 */
	@Override
	public String atomString() {
		return symbolicConstant.toString();
	}

	@Override
	public String toString() {
		return symbolicConstant.toString();
	}

	@Override
	public TreeExpressionIF argument(int index) {
		throw new IllegalArgumentException(
				"No arguments to a symbolic constant expression");
	}

	@Override
	public SymbolicKind kind() {
		return SymbolicKind.SYMBOLIC_CONSTANT;
	}

	@Override
	public int numArguments() {
		return 0;
	}

	@Override
	public NumericPrimitiveKind numericPrimitiveKind() {
		assert type().isNumeric();
		return NumericPrimitiveKind.SYMBOLIC_CONSTANT;
	}

	@Override
	public BooleanPrimitiveKind booleanPrimitiveKind() {
		assert type().isBoolean();
		return BooleanPrimitiveKind.SYMBOLIC_CONSTANT;
	}

}
