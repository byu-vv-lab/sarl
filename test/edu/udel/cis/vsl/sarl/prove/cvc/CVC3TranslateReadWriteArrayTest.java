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
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
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
	private static SymbolicIntegerType intType = universe.integerType();
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
	// SymbolicConstants
	private static SymbolicConstant x = universe.symbolicConstant(
			universe.stringObject("x"), intType);
	// private static SymbolicConstant y = universe.symbolicConstant(
	// universe.stringObject("y"), intType);
	// private static SymbolicConstant z = universe.symbolicConstant(
	// universe.stringObject("z"), intType);
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
		proverFactory = Prove.newProverFactory(
				universe,
				Configurations.getDefaultConfiguration().getProverWithKind(
						ProverKind.CVC3_API));
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * testTranslateArrayWriteComplete creates a Array using translated numeric
	 * expressions. The test compares the array, using the symbolic operator,
	 * with the validity checker using writeExpr.
	 */

	@Test
	public void testTranslateArrayWriteComplete() {

		Expr zeroModifier = cvcProver.translate(zero);

		List<SymbolicExpression> array = new ArrayList<SymbolicExpression>(3);
		array.add(0, two);
		array.add(1, five);
		array.add(2, ten);

		SymbolicExpression newArray = universe.array(realType, array);
		SymbolicExpression translateArray = expressionFactory.expression(
				SymbolicOperator.ARRAY_WRITE, newArray.type(), newArray,
				universe.integer(0), zero);

		Expr expr = cvcProver.translate(translateArray);
		Expr expr2 = cvcProver.translate(newArray);

		Expr expected = vc.writeExpr(expr2, zeroModifier, zeroModifier);

		assertEquals(expected, expr);

	}

	/**
	 * testTranslateArrayWriteIncomplete uses ARRAY_WRITE and ARRAY_Read to
	 * create symbolic expressions that will be used in translation for a Expr
	 * that will be compared to a translation that uses a numeric expression.
	 */

	@Test
	public void testTranslateArrayWriteIncomplete() {

		Expr expr = cvcProver.translate(five);

		SymbolicType incompleteArrayType = universe.arrayType(realType);
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), incompleteArrayType);

		SymbolicExpression s1 = expressionFactory.expression(
				SymbolicOperator.ARRAY_WRITE, a.type(), a, x, five);
		SymbolicExpression s2 = expressionFactory.expression(
				SymbolicOperator.ARRAY_READ, a.type(), s1, x);

		Expr expr2 = cvcProver.translate(s2);

		Expr equation = vc.eqExpr(expr2, expr);

		assertEquals(QueryResult.VALID, vc.query(equation));
	}

	/**
	 * testTranslateDenseArrayWriteComplete creates a symbolic expression using
	 * dense array write taking two array lists created within the test. The
	 * test then translates the dense array write symbolic expression and
	 * compares it to the validity checker using the operation writeExpr.
	 * 
	 */

	@Test
	public void testTranslateDenseArrayWriteComplete() {

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
		SymbolicExpression s1 = universe
				.denseArrayWrite(regularArray, newArray);

		Expr cvcArray = cvcProver.translate(s1);
		Expr cvcArray2 = cvcProver.translate(regularArray);

		Expr cvcArray3 = vc.writeExpr(cvcArray2, zeroExpr, fiveExpr);
		Expr expected = vc.writeExpr(cvcArray3, oneExpr, tenExpr);

		assertEquals(expected, cvcArray);
	}

	/**
	 * testTranslateDenseArrayWriteIncomplete uses universe.denseArrayWrite and
	 * ARRAY_READ to create symbolic expressions for translations that will be
	 * used to compare with the validity checker using eqExpr.
	 */

	@Test
	public void testTranslateDenseArrayWriteIncomplete() {

		Expr expr = cvcProver.translate(five);
		Expr expr2 = cvcProver.translate(ten);
		Expr expr3 = cvcProver.translate(two);

		SymbolicType incompleteArrayType = universe.arrayType(realType);
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), incompleteArrayType);

		List<SymbolicExpression> newArray = new ArrayList<SymbolicExpression>();
		newArray.add(0, five);
		newArray.add(1, ten);
		newArray.add(2, two);

		SymbolicExpression s1 = universe.denseArrayWrite(a, newArray);
		SymbolicExpression s2 = expressionFactory.expression(
				SymbolicOperator.ARRAY_READ, a.type(), s1, zeroInt);
		SymbolicExpression s3 = expressionFactory.expression(
				SymbolicOperator.ARRAY_READ, a.type(), s1, oneInt);
		SymbolicExpression s4 = expressionFactory.expression(
				SymbolicOperator.ARRAY_READ, a.type(), s1, twoInt);

		Expr expr4 = cvcProver.translate(s2);
		Expr expr5 = cvcProver.translate(s3);
		Expr expr6 = cvcProver.translate(s4);

		Expr equation1 = vc.eqExpr(expr4, expr);
		Expr equation2 = vc.eqExpr(expr5, expr2);
		Expr equation3 = vc.eqExpr(expr6, expr3);

		assertEquals(QueryResult.VALID, vc.query(equation1));
		assertEquals(QueryResult.VALID, vc.query(equation2));
		assertEquals(QueryResult.VALID, vc.query(equation3));
	}
}
