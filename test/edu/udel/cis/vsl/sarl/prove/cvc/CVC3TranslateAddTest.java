package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateAddTest {
	
	// Static fields: instantiated once and used for all tests...
		private static FactorySystem factorySystem = PreUniverses
				.newIdealFactorySystem();
		private static PreUniverse universe = PreUniverses
				.newPreUniverse(factorySystem);
		private static ExpressionFactory expressionFactory = factorySystem
				.expressionFactory();
		// types
		private static SymbolicRealType realType = universe.realType();
		//expressions
		private static NumericExpression five = universe.rational(5);
		private static NumericExpression two = universe.rational(2);
		private static NumericExpression one = universe.rational(1);
		private static BooleanExpression booleanExprTrue = universe
				.trueExpression();
		//SymbolicConstant
		private static SymbolicConstant e = universe.symbolicConstant(universe.stringObject("e"), realType);
		private static SymbolicConstant f = universe.symbolicConstant(universe.stringObject("f"), realType);
		private static SymbolicConstant g = universe.symbolicConstant(universe.stringObject("g"), realType);
		// Instance fields: instantiated before each test is run...
		private TheoremProverFactory proverFactory;
		private CVC3TheoremProver cvcProver;
		private ValidityChecker vc;
		
		/**
		 * Set up each test. This method is run before each test.
		 * 
		 * @throws Exception
		 */
		@Before
		public void setUp() throws Exception {
			
			proverFactory = Prove.newCVC3TheoremProverFactory(universe);
			cvcProver = (CVC3TheoremProver) proverFactory
					.newProver(booleanExprTrue);
			vc = cvcProver.validityChecker();
		}

		@After
		public void tearDown() throws Exception {
		}
		
	/**
	 * testTranslateAddOneArg creates exprs from translating rational numeric 
	 * expressions and adds them to an SymbolicCollection. The test then compares
	 * the translated symboliccollection list and the validity checker.
	 */

	
	@Test
	public void testTranslateAddOneArg(){
		
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);

		List<NumericExpression> s1 = new ArrayList<NumericExpression>();
		s1.add(one);
		s1.add(two);
		s1.add(five);
		
		SymbolicCollection<NumericExpression> addList = universe.basicCollection(s1);
		
		NumericExpression addExp2 = universe.add(addList);
		Expr addExpr2 = cvcProver.translate(addExp2);
		
		Expr addExpected2 = vc.plusExpr(oneExpr, twoExpr);
		Expr addExpected3 = vc.plusExpr(addExpected2, fiveExpr);
		Expr addExpected4 = vc.simplify(addExpected3);
		assertEquals(addExpected4, addExpr2);
	}
	
	/**
	 * testTranslateAddTwoArg compares the validity checker and a CVC3TheoremProver
	 * when taking two expr arguments.
	 */
	
	@Test
	public void testTranslateAddTwoArg(){
		
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);
		
		NumericExpression addExp1 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two);
		Expr addExpr1 = cvcProver.translate(addExp1);
		Expr addExpected1 = vc.plusExpr(oneExpr, twoExpr);
		assertEquals(addExpected1, addExpr1);
	}
	
	/**
	 * testTranslateAddTwoArgSymbolic compares the valditychecker and a CVC3ThoremProver
	 * when taking two symbolic constants.
	 */
	
	@Test
	public void testTranslateAddTwoArgSymbolic(){
		
		Expr eExpr = cvcProver.translate(e);
		Expr fExpr = cvcProver.translate(f);
		
		NumericExpression addExp1 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, e, f);
		Expr addExpr1 = cvcProver.translate(addExp1);
		Expr addExpected1 = vc.plusExpr(eExpr, fExpr);
		assertEquals(addExpected1, addExpr1);
	}
	
	/**
	 * testTranslateAddException adds and translates numeric expressions
	 */
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateAddException(){
		
		NumericExpression addExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two, five);
		cvcProver.translate(addExp3);
	}
	
	/**
	 * testTranslateAddExceptionSymbolic adds and translates symbolic constants
	 */
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateAddExceptionSymbolic(){
		
		NumericExpression addExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, e, f, g);
		cvcProver.translate(addExp3);
	}
}
