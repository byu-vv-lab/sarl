package edu.udel.cis.vsl.sarl.IF;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;

public class ManchunsTest {

	@Test
	public void getReducedContextTest() {
		/*
		 * (0 == x || 0 == y) => true
		 * 
		 * (0 < -1*x || 0 < -1*y) => 0 < -1*x
		 * 
		 * (0 != x || 0 != y) => (0 != x) && (0 != x || 0 != y)
		 */
		SymbolicUniverse universe = SARL.newStandardUniverse();
		NumericExpression x = (NumericExpression) universe.symbolicConstant(
				universe.stringObject("x"), universe.realType());
		NumericExpression y = (NumericExpression) universe.symbolicConstant(
				universe.stringObject("y"), universe.realType());
		BooleanExpression claim = universe.neq(x, universe.zeroReal());
		Reasoner reasoner;

		claim = universe.or(claim, universe.neq(y, universe.zeroReal()));
		System.out.println(claim);
		reasoner = universe.reasoner(claim);
		claim = reasoner.getReducedContext();
		System.out.println(claim);
	}

}