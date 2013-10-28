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
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.collections.common.PcollectionsSymbolicSequence;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateReadWriteTupleTest {
	
	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicIntegerType intType = universe.integerType();
	// expressions
	private static NumericExpression ten = universe.rational(10);
	private static NumericExpression oneInt = universe.integer(1);
	private static NumericExpression twoInt = universe.integer(2);
	private static NumericExpression fiveInt = universe.integer(5);
	private static NumericExpression tenInt = universe.integer(10);
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
	
	/**
	 * testTranslateTupleRead creates two ArrayLists one for numeric expressions and one for 
	 * symbolic integer type. The test then translates symbolic expressions that use the symbolic operator
	 * TUPLE_READ and compares it to the validity checker. 
	 */
	
	@Test
	public void testTranslateTupleRead() {
		
		Expr expr2 = cvcProver.translate(twoInt);
		
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>(); 
		tupleList.add(oneInt);
		tupleList.add(twoInt);
		tupleList.add(fiveInt);
		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(intType);
		tupleType.add(intType);
		tupleType.add(intType);

		SymbolicExpression s1 = universe.tuple(universe
				.tupleType(universe.stringObject("tuple"), tupleType), tupleList);
		SymbolicExpression s2 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s1.type(), 
						s1, universe.intObject(1));
		
		Expr expr1 = cvcProver.translate(s2);
		Expr expr3 = vc.eqExpr(expr1, expr2);
		
		assertEquals(QueryResult.VALID, vc.query(expr3));
	}
	
	/**
	 * testTranslateTupleWrite creates two ArrayLists one for numeric expressions and one for 
	 * symbolic integer type. The test then translates symbolic expressions that use the symbolic operator
	 * TUPLE_WRITE and compares it to the validity checker. 
	 */
	
	@Test
	public void testTranslateTupleWrite(){
		
		Expr expr2 = cvcProver.translate(ten);
		
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>(); 
		tupleList.add(oneInt);
		tupleList.add(twoInt);
		tupleList.add(fiveInt);
		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(intType);
		tupleType.add(intType);
		tupleType.add(intType);
		
		SymbolicExpression s1 = universe.tuple(universe
				.tupleType(universe.stringObject("tuple"), tupleType), tupleList);
		SymbolicExpression s2 = expressionFactory
				.expression(SymbolicOperator.TUPLE_WRITE, s1.type(), s1, universe.intObject(1), tenInt);
		SymbolicExpression s3 = universe.tupleRead(s2, universe.intObject(1));
		
		Expr expr1 = cvcProver.translate(s3);
		
		Expr expr3 = vc.eqExpr(expr2, expr1);
		
		assertEquals(QueryResult.VALID, vc.query(expr3));
	}
	
	/**
	 * testTranslateDenseTupleWrite translates symbolic expressions that
	 * use the symbolic operator DENSE_TUPLE_WRITE and TUPLE_READ. The test uses the validity
	 * checker and compares translations using numeric integer expressions with the 
	 * created translations using DENSE_TUPLE_WRITE and TUPLE_READ.
	 */
	
	@Test
	public void testTranslateDenseTupleWrite(){
		
		Expr oneIntExpr = cvcProver.translate(oneInt);
		Expr twoIntExpr = cvcProver.translate(twoInt);
		Expr fiveIntExpr = cvcProver.translate(fiveInt);
		
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>(); 
		tupleList.add(oneInt);
		tupleList.add(twoInt);
		tupleList.add(fiveInt);
		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(intType);
		tupleType.add(intType);
		tupleType.add(intType);
		
		SymbolicSequence<SymbolicExpression> tupleSequenceWrite = 
				new PcollectionsSymbolicSequence<SymbolicExpression>();
		tupleSequenceWrite = tupleSequenceWrite.add(fiveInt);
		tupleSequenceWrite = tupleSequenceWrite.add(oneInt);
		tupleSequenceWrite = tupleSequenceWrite.add(twoInt);
		
		SymbolicExpression s1 = universe.tuple(universe
				.tupleType(universe.stringObject("tuple"), tupleType), tupleList);
		SymbolicExpression s3 = expressionFactory
				.expression(SymbolicOperator.DENSE_TUPLE_WRITE, s1.type(), s1, tupleSequenceWrite);
		SymbolicExpression s4 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s3.type(), s3, universe.intObject(0));
		SymbolicExpression s5 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s3.type(), s3, universe.intObject(1));
		SymbolicExpression s6 = expressionFactory
				.expression(SymbolicOperator.TUPLE_READ, s3.type(), s3, universe.intObject(2));
		
		Expr tupleNumber1 = cvcProver.translate(s4);
		Expr tupleNumber2 = cvcProver.translate(s5);
		Expr tupleNumber3 = cvcProver.translate(s6);
		
		Expr equation1 = vc.eqExpr(tupleNumber1, fiveIntExpr);
		Expr equation2 = vc.eqExpr(tupleNumber2, oneIntExpr);
		Expr equation3 = vc.eqExpr(tupleNumber3, twoIntExpr);
	
		assertEquals(QueryResult.VALID, vc.query(equation1));
		assertEquals(QueryResult.VALID, vc.query(equation2));
		assertEquals(QueryResult.VALID, vc.query(equation3));
	}
}
