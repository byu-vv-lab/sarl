package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;

public class IdealSimplifierSimpExprTest {
	
	private static IdealSimplifier idealSimp;
	
	private static BooleanExpression assumption;
	
	private static NumericSymbolicConstant x,y;
	
	private NumericExpression numExpr;
	
	private SymbolicExpression symExpr;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CommonObjects.setUp();
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				 preUniv.stringObject("x"), realType);
		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
					 preUniv.stringObject("y"), realType);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void simplifyExpressionTrivial() {
		
		numExpr = preUniv.multiply(preUniv.divide(onePxPxSqdP3x4th, x), x);
		
		symExpr = numExpr;

		assumption = preUniv.equals(preUniv.multiply(rat5,x), preUniv.multiply(y, y));
		
		idealSimp = idealSimplifierFactory.newSimplifier(assumption);
		
		assertEquals(onePxPxSqdP3x4th, idealSimp.simplifyExpression(symExpr));
	}

	@Test
	public void simplifyExpressionDivide() {
		NumericExpression num = preUniv.add(preUniv.multiply(rat6, x), preUniv.multiply(preUniv.multiply(rat2,  x), preUniv.power(y, 2)));
		
		NumericExpression denom = preUniv.multiply(rat2, x);
		
		numExpr = preUniv.divide(num, denom);
		
		symExpr = numExpr;

		assumption = preUniv.equals(preUniv.multiply(rat5,x), preUniv.multiply(y, y));
		
		idealSimp = idealSimplifierFactory.newSimplifier(assumption);
		
		NumericExpression numExpect = preUniv.add(preUniv.power(y, 2), rat3);
		
		SymbolicExpression expected = numExpect;
		
		assertEquals(expected, idealSimp.simplifyExpression(symExpr));
		
	}
	
	@Test
	public void simplifyExpressionPoly() {
		NumericExpression num = 
				preUniv.add(
							preUniv.add(
									preUniv.multiply(rat4,
												preUniv.multiply(preUniv.power(x, 3),preUniv.power(y, 2))),
									preUniv.multiply(rat2, preUniv.multiply(xy, x))), 
						preUniv.multiply(rat3, xy));

		NumericExpression denom = preUniv.multiply(y, x);
		
		numExpr = preUniv.divide(num, denom);
		
		symExpr = numExpr;

		assumption = preUniv.equals(preUniv.multiply(rat5,x), preUniv.multiply(y, y));
		
		idealSimp = idealSimplifierFactory.newSimplifier(assumption);
		
		NumericExpression numExpect = preUniv.add(preUniv.multiply(preUniv.multiply(rat4,preUniv.power(x, 2)), y), 
											preUniv.add(preUniv.multiply(rat2,x),
												rat3));
		
		SymbolicExpression expected = numExpect;
		
		assertEquals(expected, idealSimp.simplifyExpression(symExpr));
	}
}
