package edu.udel.cis.vsl.sarl.symbolic.ideal;

import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF.SymbolicTypeKind;

/**
 * The class used to represents all symbolic expressions in the ideal universe
 * that do not have either integer, real, or boolean type. This inclues arrays,
 * tuples, and functions. It simply wraps an instance of TreeExpressionIF of one
 * of these types.
 * 
 * @author siegel
 */
public class OtherIdealExpression extends IdealExpression {

	protected OtherIdealExpression(TreeExpressionIF expression) {
		super(expression);
		SymbolicTypeKind kind = type().kind();

		assert kind == SymbolicTypeKind.ARRAY || kind == SymbolicTypeKind.TUPLE
				|| kind == SymbolicTypeKind.FUNCTION;
	}

}
