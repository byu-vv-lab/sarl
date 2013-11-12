package edu.udel.cis.vsl.sarl.collections.IF;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public class ExpressionComparatorStub implements Comparator<SymbolicExpression> {

	@Override
	public int compare(SymbolicExpression o1, SymbolicExpression o2) {
		String name1 = ((SymbolicExpression) o1).toString();
		String name2 = ((SymbolicExpression) o2).toString();

		return name1.compareTo(name2);
	}

}
