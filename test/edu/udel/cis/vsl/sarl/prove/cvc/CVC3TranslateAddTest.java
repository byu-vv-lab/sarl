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
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
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
		private static SymbolicIntegerType intType = universe.integerType();
		//expressions
		private static NumericExpression five = universe.rational(5);
		private static NumericExpression two = universe.rational(2);
		private static NumericExpression one = universe.rational(1);
		private static BooleanExpression booleanExprTrue = universe
				.trueExpression();
		//SymbolicConstant
		private static SymbolicConstant x = universe.symbolicConstant(universe.stringObject("x"), realType);
		private static SymbolicConstant y = universe.symbolicConstant(universe.stringObject("y"), realType);
		private static SymbolicConstant z = universe.symbolicConstant(universe.stringObject("z"), intType);
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
		
		NumericExpression addExp = universe.add(addList);
		Expr addExpr = cvcProver.translate(addExp);
		
		Expr addExpected1 = vc.plusExpr(oneExpr, twoExpr);
		Expr addExpected2 = vc.plusExpr(addExpected1, fiveExpr);
		Expr addExpected3 = vc.simplify(addExpected2);
		assertEquals(addExpected3, addExpr);
	}
	
	/**
	 * testTranslateAddTwoArg compares the validity checker and a CVC3TheoremProver
	 * when taking two numericExpression arguments.
	 */
	
	@Test
	public void testTranslateAddTwoArg(){
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);
		
		NumericExpression addExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two);
		Expr addExpr = cvcProver.translate(addExp);
		Expr addExpected = vc.plusExpr(oneExpr, twoExpr);
		assertEquals(addExpected, addExpr);
	}
	
	/**
	 * testTranslateAddTwoArgSymbolic compares the valditychecker and a CVC3ThoremProver
	 * when taking two symbolic constants.
	 */
	
	@Test
	public void testTranslateAddTwoArgSymbolic(){
		
		Expr xExpr = cvcProver.translate(x);
		Expr yExpr = cvcProver.translate(y);
		
		NumericExpression addExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, x, y);
		Expr addExpr = cvcProver.translate(addExp);
		Expr addExpected = vc.plusExpr(xExpr, yExpr);
		assertEquals(addExpected, addExpr);
	}
	
	/**
	 * testTranslateAddException adds and translates numeric expressions
	 */
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateAddException(){
		
		NumericExpression addExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, one, two, five);
		cvcProver.translate(addExp);
	}
	
	/**
	 * testTranslateAddExceptionSymbolic adds and translates symbolic constants
	 */
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateAddExceptionSymbolic(){
		
		NumericExpression addExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, x, y, z);
		cvcProver.translate(addExp);
	}
	
	/**
	 * testTranslateAddIntAndRealToInt takes a SymbolicConstant of RealType and
	 * a SymbolicConstant of IntType and adds them together as a Real.  This is
	 * compared for equality to CVC3.  
	 */
	@Test
	public void testTranslateAddIntAndRealToInt(){	
		Expr yExpr = cvcProver.translate(y);
		Expr zExpr = cvcProver.translate(z);
		
		NumericExpression addExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, intType, y, z);
		Expr addExpr = cvcProver.translate(addExp);
		Expr addExpected = vc.plusExpr(yExpr, zExpr);
		assertEquals(addExpected, addExpr);
	}

	/**
	 * testTranslateAddIntAndRealToInt takes a SymbolicConstant of RealType and
	 * a SymbolicConstant of IntType and adds them together as an Int.  This is
	 * compared for equality to CVC3.  
	 */
	@Test
	public void testTranslateAddIntAndRealToReal(){	
		Expr yExpr = cvcProver.translate(y);
		Expr zExpr = cvcProver.translate(z);
		
		NumericExpression addExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.ADD, realType, y, z);
		Expr addExpr = cvcProver.translate(addExp);
		Expr addExpected = vc.plusExpr(yExpr, zExpr);
		assertEquals(addExpected, addExpr);
	}
}
