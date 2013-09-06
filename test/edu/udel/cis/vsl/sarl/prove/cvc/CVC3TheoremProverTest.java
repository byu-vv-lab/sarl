package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import cvc3.Type;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TheoremProverTest {

	PreUniverse universe;

	@SuppressWarnings("unused")
	private TheoremProverFactory proverFactory;
	BooleanExpression booleanExprTrue;
	CVC3TheoremProverFactory theoremProver;
	CVC3TheoremProver cvcProver;
	private static PrintStream out;
	private static NumericExpression two;
	private static NumericExpression five;

	@Before
	public void setUp() throws Exception {
		universe = PreUniverses.newPreUniverse(PreUniverses
				.newIdealFactorySystem());
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
		booleanExprTrue = universe.bool(true);
		theoremProver= (CVC3TheoremProverFactory)proverFactory;
		cvcProver = (CVC3TheoremProver)theoremProver.newProver(booleanExprTrue);
		out = System.out;
		two = universe.rational(2);
		five = universe.rational(5);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	

	@Test
	public void testBoolean() {
		SymbolicType boolType = universe.booleanType();
		ValidityChecker v = ValidityChecker.create();
		Type t = cvcProver.translateType(boolType);
		assertEquals(t, v.boolType());
	}
	
	public void testQuotientRemainderPair() {

		SymbolicExpression numerator = universe.rational(1);
		SymbolicExpression denominator = universe.rational(2);
				
	//	cvcProver.getQuotientRemainderPair(numerator, denominator);
	}
	
	@Test
	public void testTranslate() {
		SymbolicExpression expr = universe.divide(
				universe.rational(1), universe.rational(2));
		out.println(cvcProver.translate(expr));
	}
	
	@Ignore
	@Test
	//Tests SARL vs. CVC on Multiply.  CVC gives 2*5 while SARL gives 10.
	public void translateMultiplyTest(){		
		NumericExpression mulExp = universe.multiply(two, five);
		Expr expr = cvcProver.translate(mulExp);
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);
		Expr expected = cvcProver.validityChecker().multExpr(twoExpr, fiveExpr); 
		assertEquals(expected, expr);
	}
}
