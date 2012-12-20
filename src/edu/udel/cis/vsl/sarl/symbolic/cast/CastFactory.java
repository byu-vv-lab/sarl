package edu.udel.cis.vsl.sarl.symbolic.cast;

import java.util.HashMap;
import java.util.Map;

import edu.udel.cis.vsl.sarl.symbolic.NumericPrimitive;
import edu.udel.cis.vsl.sarl.symbolic.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpression;
import edu.udel.cis.vsl.sarl.symbolic.expression.SymbolicExpressionKey;
import edu.udel.cis.vsl.sarl.symbolic.type.SymbolicTypeFactory;

public class CastFactory {

	private Map<SymbolicExpressionKey<RealCastExpression>, RealCastExpression> map = new HashMap<SymbolicExpressionKey<RealCastExpression>, RealCastExpression>();

	private SymbolicTypeFactory typeFactory;

	private SymbolicTypeIF realType;

	public CastFactory(SymbolicTypeFactory typeFactory) {
		this.typeFactory = typeFactory;
		realType = typeFactory.realType();
	}

	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	public RealCastExpression realCast(NumericPrimitive integerExpression) {
		return SymbolicExpression.flyweight(map, new RealCastExpression(
				realType, integerExpression));
	}

}
