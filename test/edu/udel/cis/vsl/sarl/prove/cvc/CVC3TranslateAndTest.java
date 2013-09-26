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
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateAndTest {

	// Static fields: instantiated once and used for all tests...
		private static FactorySystem factorySystem = PreUniverses
				.newIdealFactorySystem();
		private static PreUniverse universe = PreUniverses
				.newPreUniverse(factorySystem);
		private static ExpressionFactory expressionFactory = factorySystem
				.expressionFactory();
		private static SymbolicType boolType = universe.booleanType();
		// expressions
		private static BooleanExpression booleanExprTrue = universe
				.trueExpression();
		private static BooleanExpression booleanExprFalse = universe
				.falseExpression();
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
		
		@Test
		public void testTranslateAndOneArg(){
			Expr trueExpr = cvcProver.translate(booleanExprTrue);
			Expr falseExpr = cvcProver.translate(booleanExprFalse);

			List<BooleanExpression> s1 = new ArrayList<BooleanExpression>();
			s1.add(booleanExprFalse);
			s1.add(booleanExprTrue);
			
			SymbolicCollection<BooleanExpression> boolList = universe.basicCollection(s1);
			BooleanExpression andExp1 = (BooleanExpression) expressionFactory
					.expression(SymbolicOperator.AND, boolType, boolList);
			Expr expr1 = cvcProver.translate(andExp1);
			Expr expected1 = vc.andExpr(falseExpr, trueExpr);
			assertEquals(expected1, expr1);
			
		}
		
		@Test
		public void testTranslateAndTwoArg(){
			
			Expr trueExpr = cvcProver.translate(booleanExprTrue);
			Expr falseExpr = cvcProver.translate(booleanExprFalse);
			
			BooleanExpression andExp2 = (BooleanExpression) expressionFactory
					.expression(SymbolicOperator.AND, boolType, 
							booleanExprTrue, booleanExprTrue);
			Expr expr2 = cvcProver.translate(andExp2);
			Expr expected2 = vc.andExpr(trueExpr, trueExpr);
			assertEquals(expected2, expr2);
			
			BooleanExpression andExp3 = (BooleanExpression) expressionFactory
					.expression(SymbolicOperator.AND, boolType, 
							booleanExprTrue, booleanExprFalse);
			Expr expr3 = cvcProver.translate(andExp3);
			Expr expected3 = vc.andExpr(trueExpr, falseExpr);
			assertEquals(expected3, expr3);
		}
		
		@Test(expected = SARLInternalException.class)
		public void testTranslateAndException(){
			
			BooleanExpression andExp4 = (BooleanExpression) expressionFactory
					.expression(SymbolicOperator.AND, boolType, booleanExprTrue, 
							booleanExprFalse, booleanExprFalse);
			cvcProver.translate(andExp4);
		}
}
