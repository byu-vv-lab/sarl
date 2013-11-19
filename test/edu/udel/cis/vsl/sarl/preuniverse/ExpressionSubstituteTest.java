/**
 * Tests the method substitute(SymbolicExpression expression,
 * Map<SymbolicConstant, SymbolicExpression> Map) in class
 * ExpressionSubsitute.java
 * 
 * @author Gunjan Majmudar
 * 
 */

package edu.udel.cis.vsl.sarl.preuniverse;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonPreUniverse;
import edu.udel.cis.vsl.sarl.preuniverse.common.ExpressionSubstituter;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class ExpressionSubstituteTest {

	private static PreUniverse universe;

	private static CollectionFactory factory1;

	private static SymbolicTypeFactory typeFactory1;

	private static ExpressionSubstituter expr1;

	private static SymbolicExpression expression1, expression2, expression3,
			expression4, expression5, expression6, expression7, expression8;

	private static SymbolicType integerType, intArrayType, functionType,
			functionType1, realType, booleanType;

	private static SymbolicTupleType tupleType;

	private static SymbolicUnionType unionType;

	private static SymbolicTypeSequence sequence, sequence1;

	private static SymbolicCompleteArrayType completeArrayType;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);
		factory1 = test.collectionFactory();
		typeFactory1 = test.typeFactory();

		// SymbolicType
		integerType = universe.integerType();
		realType = universe.realType();
		booleanType = universe.booleanType();
		intArrayType = universe.arrayType(integerType);
		completeArrayType = universe
				.arrayType(integerType, universe.integer(2));
		tupleType = universe.tupleType(
				universe.stringObject("SequenceofInteger"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						integerType }));
		unionType = universe.unionType(
				universe.stringObject("union1"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						booleanType, intArrayType }));

		// SymbolicTypeSequence
		sequence = tupleType.sequence();
		sequence1 = universe.typeSequence(Arrays.asList(new SymbolicType[] {
				integerType, realType, booleanType, intArrayType }));
		functionType = universe.functionType(sequence, realType);
		functionType1 = universe.functionType(sequence1, realType);

		// SymbolicExpression
		expression1 = universe.nullExpression();
		expression2 = universe.symbolicConstant(
				universe.stringObject("intArrayTypeExpression"), intArrayType);
		expression3 = universe.symbolicConstant(
				universe.stringObject("TupleTypeExpression"), tupleType);
		expression4 = universe.symbolicConstant(
				universe.stringObject("UnionTypeExpression"), unionType);
		expression5 = universe.symbolicConstant(
				universe.stringObject("functionTypeExpression"), functionType);
		expression6 = universe.symbolicConstant(
				universe.stringObject("completeArrayTypeExpression"),
				completeArrayType);
		expression7 = universe.symbolicConstant(
				universe.stringObject("typesequenceExpression"), functionType1);
		expression8 = universe.emptyArray(integerType);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests the method substitute(SymbolicExpression expression,
	 * Map<SymbolicConstant, SymbolicExpression> Map) in class
	 * ExpressionSubsitute.java Cases covered were Null expression, arrayType,
	 * tupleType, unionType, functionType, completeArrayType type sequence and
	 * collection
	 * 
	 * @author Gunjan Majmudar
	 * 
	 */
	@SuppressWarnings("static-access")
	@Test
	public void expressionSubstituteTest() {
		expr1 = new ExpressionSubstituter(universe, factory1, typeFactory1);
		Map<SymbolicExpression, SymbolicExpression> newMap = new HashMap<SymbolicExpression, SymbolicExpression>();

		// constructor test
		assertEquals(this.factory1, factory1);
		assertEquals(this.universe, universe);
		assertEquals(this.typeFactory1, typeFactory1);

		// case Null expression
		assertEquals(expr1.substitute(expression1, newMap), expression1);

		// case arraytype
		assertEquals(expr1.substitute(expression2, newMap), expression2);

		// case tupletype
		assertEquals(expr1.substitute(expression3, newMap), expression3);

		// case unionType
		assertEquals(expr1.substitute(expression4, newMap), expression4);

		// case functionType
		assertEquals(expr1.substitute(expression5, newMap), expression5);

		// case completeArrayType
		assertEquals(expr1.substitute(expression6, newMap), expression6);

		// case typesequence
		assertEquals(expr1.substitute(expression7, newMap), expression7);

		// expression collection
		assertEquals(expr1.substitute(expression8, newMap), expression8);
	}
}
