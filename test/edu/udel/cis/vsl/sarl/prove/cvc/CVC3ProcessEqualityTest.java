package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
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

public class CVC3ProcessEqualityTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicRealType realType = universe.realType();
	private static SymbolicType boolType = universe.booleanType();
	// expressions
	private static NumericExpression five = universe.rational(5);
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// Instance fields: instantiated before each test is run...
	private TheoremProverFactory proverFactory;
	private CVC3TheoremProver cvcProver;

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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProcessEqualityArrayCompleteEqual() {

		List<SymbolicExpression> a1 = new ArrayList<SymbolicExpression>();
		a1.add(0, two);
		a1.add(1, one);
		a1.add(2, five);
		
		List<SymbolicExpression> a2 = new ArrayList<SymbolicExpression>();
		a2.add(0, two);
		a2.add(1, one);
		a2.add(2, five);

		SymbolicExpression s1 = universe.array(realType, a1);
		SymbolicExpression s2 = universe.array(realType, a2);

		BooleanExpression f = (BooleanExpression) expressionFactory.expression(
				SymbolicOperator.EQUALS, boolType, s1, s2);

		assertEquals(ResultType.YES, cvcProver.valid(f).getResultType());
	}
	
	@Test
	public void testProcessEqualityArrayCompleteNotEqual() {

		List<SymbolicExpression> a1 = new ArrayList<SymbolicExpression>();
		a1.add(0, two);
		a1.add(1, one);
		
		List<SymbolicExpression> a2 = new ArrayList<SymbolicExpression>();
		a2.add(0, two);
		a2.add(1, one);
		a2.add(2, five);

		SymbolicExpression s1 = universe.array(realType, a1);
		SymbolicExpression s2 = universe.array(realType, a2);

		BooleanExpression f = (BooleanExpression) expressionFactory.expression(
				SymbolicOperator.EQUALS, boolType, s1, s2);

		assertEquals(ResultType.NO, cvcProver.valid(f).getResultType());
	}
	
	@Test
	public void testProcessEqualityArrayIncompleteEqual(){

		SymbolicType incompleteArrayType = universe.arrayType(realType);
		
		SymbolicExpression a1 = universe.symbolicConstant(universe
				.stringObject("a"), incompleteArrayType);
		SymbolicExpression a2 = universe.symbolicConstant(universe
				.stringObject("a"), incompleteArrayType);

		BooleanExpression f = (BooleanExpression) expressionFactory.expression(
				SymbolicOperator.EQUALS, boolType, a1, a2);

		assertEquals(ResultType.YES, cvcProver.valid(f).getResultType());
	}
	
	@Test
	public void testProcessEqualityArrayIncompleteNotEqual(){

		SymbolicType incompleteArrayType = universe.arrayType(realType);
		
		SymbolicExpression a1 = universe.symbolicConstant(universe
				.stringObject("a"), incompleteArrayType);
		SymbolicExpression a2 = universe.symbolicConstant(universe
				.stringObject("b"), incompleteArrayType);

		BooleanExpression f = (BooleanExpression) expressionFactory.expression(
				SymbolicOperator.EQUALS, boolType, a1, a2);

		assertEquals(ResultType.NO, cvcProver.valid(f).getResultType());
	}
}
