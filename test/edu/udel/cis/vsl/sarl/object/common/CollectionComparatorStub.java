package edu.udel.cis.vsl.sarl.object.common;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.common.BasicCollection;

public class CollectionComparatorStub implements Comparator<BasicCollection> {

	@Override
	public int compare(BasicCollection, BasicCollection) {
		String name1 = ((ExpressionStub) o1).toString();
		String name2 = ((ExpressionStub) o2).toString();

		return name1.compareTo(name2);
	}

}
