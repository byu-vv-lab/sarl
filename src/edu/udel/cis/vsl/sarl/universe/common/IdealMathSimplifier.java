package edu.udel.cis.vsl.sarl.universe.common;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.Simplifier;

public class IdealMathSimplifier implements Simplifier {

	Simplifier idealSimplifier;
	Simplifier mathSimplifier;
	
	public IdealMathSimplifier(Simplifier idealSimplifier, Simplifier mathSimplifier){
		this.idealSimplifier = idealSimplifier;
		this.mathSimplifier = mathSimplifier;
	}
	@Override
	public SymbolicExpression apply(SymbolicExpression x) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PreUniverse universe() {
		// TODO Auto-generated method stub
		return idealSimplifier.universe();
	}

	@Override
	public Map<SymbolicConstant, SymbolicExpression> substitutionMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanExpression getReducedContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BooleanExpression getFullContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Interval assumptionAsInterval(SymbolicConstant symbolicConstant) {
		// TODO Auto-generated method stub
		return null;
	}

}
