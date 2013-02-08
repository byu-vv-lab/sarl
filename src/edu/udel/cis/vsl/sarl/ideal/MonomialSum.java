package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

public class MonomialSum extends CommonSymbolicExpression {

	protected MonomialSum(SymbolicTypeIF type, SymbolicMap termMap) {
		super(SymbolicOperator.ADD, type, termMap);
	}

	public SymbolicMap termMap() {
		return (SymbolicMap) argument(0);
	}

	public int numTerms() {
		return ((SymbolicMap) argument(0)).size();
	}

	public StringBuffer toStringBuffer() {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;

		for (SymbolicExpressionIF expr : termMap()) {
			if (first)
				first = false;
			else
				buffer.append("+");
			buffer.append(expr.toString());
		}
		return buffer;
	}

	@Override
	public String toString() {
		return toStringBuffer().toString();
	}

}
