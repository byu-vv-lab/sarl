package edu.udel.cis.vsl.sarl.symbolic.union;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.NumericConcreteExpressionIF;
import edu.udel.cis.vsl.sarl.IF.SymbolicUnionTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.IF.tree.TreeExpressionIF;

public class UnionFactory {

	private Map<SymbolicExpressionKey<UnionInjectExpression>, UnionInjectExpression> unionInjectMap = new HashMap<SymbolicExpressionKey<UnionInjectExpression>, UnionInjectExpression>();

	public UnionInjectExpression unionInjectExpression(
			SymbolicUnionTypeIF unionType, int memberIndex,
			TreeExpressionIF object) {
		return CommonSymbolicExpression.flyweight(unionInjectMap,
				new UnionInjectExpression(unionType, memberIndex, object));
	}

}
