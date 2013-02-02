package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;

public class MonomialSum extends CommonSymbolicExpression {

	protected MonomialSum(SymbolicTypeIF type, SymbolicMap termMap) {
		super(SymbolicOperator.ADD, type, termMap);
	}

}
