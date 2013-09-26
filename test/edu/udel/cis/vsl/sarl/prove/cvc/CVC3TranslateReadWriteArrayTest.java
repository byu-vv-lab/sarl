package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.QueryResult;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateReadWriteArrayTest {

	// Static fields: instantiated once and used for all tests...
		private static FactorySystem factorySystem = PreUniverses
				.newIdealFactorySystem();
		private static PreUniverse universe = PreUniverses
				.newPreUniverse(factorySystem);
		private static ExpressionFactory expressionFactory = factorySystem
				.expressionFactory();
		// types
		private static SymbolicRealType realType = universe.realType();
		// expressions
		private static NumericExpression ten = universe.rational(10);
		private static NumericExpression five = universe.rational(5);
		private static NumericExpression two = universe.rational(2);
		private static NumericExpression one = universe.rational(1);
		private static NumericExpression zero = universe.zeroReal();
		private static NumericExpression zeroInt = universe.zeroInt();
		private static NumericExpression oneInt = universe.integer(1);
		private static NumericExpression twoInt = universe.integer(2);
		private static BooleanExpression booleanExprTrue = universe
				.trueExpression();
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
	public void testTranslateArrayWriteComplete(){

		Expr zeroModifier = cvcProver.translate(zero);
		
		List<SymbolicExpression> array = new ArrayList<SymbolicExpression>(3);
		array.add(0, two);
		array.add(1, five);
		array.add(2, ten);
		
		SymbolicExpression newArray = universe.array(realType, array);
		SymbolicExpression translateArray = expressionFactory
				.expression(SymbolicOperator.ARRAY_WRITE, newArray.type(), 
						newArray, universe.integer(0), zero);
		
		Expr expr = cvcProver.translate(translateArray);
		Expr expr2 = cvcProver.translate(newArray);

		Expr expected = vc.writeExpr(expr2, zeroModifier, zeroModifier);
		
		assertEquals(expected, expr);
		
	}
	
	@Test
	public void testTranslateArrayWriteIncomplete(){
		
		Expr expr2 = cvcProver.translate(five);
		
		SymbolicType incompleteArrayType = universe.arrayType(realType);
		SymbolicExpression a = universe.symbolicConstant(universe
				.stringObject("a"), incompleteArrayType);

		SymbolicExpression s1 = expressionFactory
				.expression(SymbolicOperator.ARRAY_WRITE, a.type(), 
						a, twoInt, five);
		SymbolicExpression s2 = expressionFactory
				.expression(SymbolicOperator.ARRAY_READ, a.type(), 
						s1, twoInt);
		
		Expr expr1 = cvcProver.translate(s2);
		
		Expr equation1 = vc.eqExpr(expr1, expr2);
		
		assertEquals(QueryResult.VALID, vc.query(equation1));
	}
	
	@Test
	public void testTranslateDenseArrayWriteComplete(){
		
		Expr zeroExpr = cvcProver.translate(zero);
		Expr oneExpr = cvcProver.translate(one);
		Expr fiveExpr = cvcProver.translate(five);
		Expr tenExpr = cvcProver.translate(ten);
		
		List<SymbolicExpression> array = new ArrayList<SymbolicExpression>(3);
		array.add(0, two);
		array.add(1, five);
		array.add(2, ten);
		
		List<SymbolicExpression> newArray = new ArrayList<SymbolicExpression>(2);
		newArray.add(0, five);
		newArray.add(1, ten);
		
		SymbolicExpression regularArray = universe.array(realType, array);
		SymbolicExpression s1 = universe.denseArrayWrite(regularArray, newArray);
		
		Expr cvcArray = cvcProver.translate(s1);
		Expr cvcArray2 = cvcProver.translate(regularArray);
		
		Expr cvcArray3 = vc.writeExpr(cvcArray2, zeroExpr, fiveExpr);
		Expr expected = vc.writeExpr(cvcArray3, oneExpr, tenExpr);
		
		assertEquals(expected, cvcArray);
	}
	
	@Test
	public void translateDenseArrayWriteIncomplete(){
		
		Expr expr4 = cvcProver.translate(five);
		Expr expr5 = cvcProver.translate(ten);
		Expr expr6 = cvcProver.translate(two);
		
		SymbolicType incompleteArrayType = universe.arrayType(realType);
		SymbolicExpression a = universe.symbolicConstant(universe
				.stringObject("a"), incompleteArrayType);
		
		List<SymbolicExpression> newArray = new ArrayList<SymbolicExpression>();
		newArray.add(0, five);
		newArray.add(1, ten);
		newArray.add(2, two);

		SymbolicExpression s1 = universe.denseArrayWrite(a, newArray);
		SymbolicExpression s2 = expressionFactory.expression(SymbolicOperator.ARRAY_READ, a.type(), s1, zeroInt);
		SymbolicExpression s3 = expressionFactory.expression(SymbolicOperator.ARRAY_READ, a.type(), s1, oneInt);
		SymbolicExpression s4 =	expressionFactory.expression(SymbolicOperator.ARRAY_READ, a.type(), s1, twoInt);
		
		Expr expr1 = cvcProver.translate(s2);
		Expr expr2 = cvcProver.translate(s3);
		Expr expr3 = cvcProver.translate(s4);
		
		Expr equation1 = vc.eqExpr(expr1, expr4);
		Expr equation2 = vc.eqExpr(expr2, expr5);
		Expr equation3 = vc.eqExpr(expr3, expr6);
		
		assertEquals(QueryResult.VALID, vc.query(equation1));
		assertEquals(QueryResult.VALID, vc.query(equation2));
		assertEquals(QueryResult.VALID, vc.query(equation3));
	}
}
