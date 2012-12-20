package edu.udel.cis.vsl.sarl.symbolic.ideal;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;

/**
 * Root of the class hierarchy for symbolic expressions in the Ideal Symbolic
 * Universe. Every symbolic expression created by the ideal universe extends
 * this class.
 * 
 * An IdealExpression basically wraps a tree expression.
 * 
 * @author siegel
 */
public class IdealExpression extends SymbolicExpression {

	protected TreeExpressionIF expression;

	protected IdealExpression(TreeExpressionIF expression) {
		super(expression.type());
		this.expression = expression;
	}

	public TreeExpressionIF expression() {
		return expression;
	}

	protected boolean intrinsicEquals(SymbolicExpression that) {
		return expression.equals(that);
	}

	protected int intrinsicHashCode() {
		return expression.hashCode();
	}

	public String toString() {
		return expression.toString();
	}

	public String atomString() {
		return expression.atomString();
	}
}
