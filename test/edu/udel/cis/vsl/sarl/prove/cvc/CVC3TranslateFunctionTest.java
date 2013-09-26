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
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateFunctionTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	private static SymbolicIntegerType intType = universe.integerType();
	// expressions
	private static NumericExpression oneInt = universe.integer(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	//constants
	private static SymbolicConstant e = universe
			.symbolicConstant(universe.stringObject("e"), intType);
	private static SymbolicConstant f = universe
			.symbolicConstant(universe.stringObject("f"), intType);
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
	public void testTranslateFunctionSymbolicConstant(){
	
		List <SymbolicType> types = new ArrayList<SymbolicType>();
		types.add(intType);
		
		SymbolicType type = universe.functionType(types, intType);
		SymbolicConstant symFunction = universe
				.symbolicConstant(universe.stringObject("SymbolicConstant"), type);
		
		List<SymbolicExpression> funList = new ArrayList<SymbolicExpression>();
		funList.add(oneInt); 
		SymbolicCollection<SymbolicExpression> collect = universe.basicCollection(funList);
		
		SymbolicExpression e = expressionFactory
				.expression(SymbolicOperator.APPLY, symFunction.type(), symFunction, collect);

		Expr expr = cvcProver.translate(e);
		Expr expr1 = expr.getOpExpr();
		Expr expr2 = expr.getChild(0);

		Expr oneIntExpr = cvcProver.translate(oneInt);
		
		Expr equationOne = vc.eqExpr(expr1, vc.exprFromString("SymbolicConstant"));
		Expr equationTwo = vc.eqExpr(expr2, oneIntExpr);

		assertEquals(QueryResult.VALID, vc.query(equationOne));
		assertEquals(QueryResult.VALID, vc.query(equationTwo));
	}
	
	@Test
	public void testTranslateFunctionLambda(){
		
		List <SymbolicType> types = new ArrayList<SymbolicType>();
		types.add(intType);
		
		SymbolicType type = universe.functionType(types, intType);
		List<SymbolicExpression> funList = new ArrayList<SymbolicExpression>();
		funList.add(oneInt); 
		SymbolicCollection<SymbolicExpression> collect = universe.basicCollection(funList);
		
		SymbolicExpression lamFunction = universe.lambda(e, f);
		SymbolicExpression e = expressionFactory
				.expression(SymbolicOperator.APPLY, type, lamFunction, collect);
		
		Expr expr = cvcProver.translate(e);
		Expr equationOne = vc.eqExpr(expr, vc.exprFromString("(LAMBDA (e: INT): f)(1)"));
		
		assertEquals(QueryResult.VALID, vc.query(equationOne));
	}
	
	@Test(expected = SARLInternalException.class)
	public void testTranslateFunctionUnexpected(){
	
		List <SymbolicType> types = new ArrayList<SymbolicType>();
		types.add(intType);
		
		SymbolicType type = universe.functionType(types, intType);
		SymbolicExpression symFunction = universe
				.symbolicConstant(universe.stringObject("SymbolicConstant"), type);
		
		List<SymbolicExpression> funList = new ArrayList<SymbolicExpression>();
		funList.add(symFunction);
		
		SymbolicExpression d = universe.array(symFunction.type(), funList);
		SymbolicExpression e = expressionFactory
				.expression(SymbolicOperator.APPLY, d.type(), d);
		
		cvcProver.translate(e);
	}

}
