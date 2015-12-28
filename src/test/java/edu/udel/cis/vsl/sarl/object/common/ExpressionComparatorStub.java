package edu.udel.cis.vsl.sarl.object.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public class ExpressionComparatorStub implements Comparator<SymbolicExpression> {

	@Override
	public int compare(SymbolicExpression o1, SymbolicExpression o2) {
		String name1 = ((ExpressionStub) o1).toString();
		String name2 = ((ExpressionStub) o2).toString();

		return name1.compareTo(name2);
	}

}
