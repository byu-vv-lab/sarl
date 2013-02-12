package edu.udel.cis.vsl.sarl.symbolic.cast;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeFactory;

public class CastFactory {

	private Map<SymbolicExpressionKey<RealCastExpression>, RealCastExpression> map = new HashMap<SymbolicExpressionKey<RealCastExpression>, RealCastExpression>();

	private CommonSymbolicTypeFactory typeFactory;

	private SymbolicTypeIF realType;

	public CastFactory(CommonSymbolicTypeFactory typeFactory) {
		this.typeFactory = typeFactory;
		realType = typeFactory.realType();
	}

	public CommonSymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public RealCastExpression realCast(NumericPrimitive integerExpression) {
		return CommonSymbolicExpression.flyweight(map, new RealCastExpression(
				realType, integerExpression));
	}

}
