package edu.udel.cis.vsl.sarl.universe.common;

import java.util.Map;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Interval;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.common.CommonSimplifier;

public class MathSimplifier extends CommonSimplifier{

	private SymbolicExpression sinFunction;
	private SymbolicExpression cosFunction;
	private MathUniverse mathUniverse;
	
	public MathSimplifier (MathUniverse mathUniverse) {
		super(mathUniverse);
		this.mathUniverse = mathUniverse;
		this.sinFunction = sinFunction;//making method for sin 
		this.cosFunction = cosFunction;
		// TODO Auto-generated constructor stub
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

	@Override
	protected SymbolicExpression simplifyExpression(
			SymbolicExpression expression) {
		
		SymbolicExpression result = this.simplifyGenericExpression(expression);
		SymbolicOperator operator = result.operator();
		if (operator == SymbolicOperator.POWER){
			NumericExpression base = (NumericExpression) result.argument(0);
			SymbolicObject arg1 = result.argument(1);
			if(arg1.symbolicObjectKind() == SymbolicObjectKind.INT){
				int exp = ((IntObject) arg1).getInt();
			}
		SymbolicExpression function = (SymbolicExpression) base.argument(0);	
			
		}
				
				
		return result;
	}

}
