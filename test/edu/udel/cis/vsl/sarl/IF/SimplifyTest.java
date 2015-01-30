package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class SimplifyTest {

	static PrintStream out = System.out;

	static SymbolicUniverse universe = Universes.newIdealUniverse();

	static SymbolicIntegerType intType = universe.integerType();

	static NumericExpression zero = universe.integer(0);

	static NumericExpression one = universe.integer(1);

	static NumericExpression two = universe.integer(2);

	static NumericExpression three = universe.integer(3);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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

	@Test
	public void test() {
		NumericExpression x = (NumericExpression) universe.symbolicConstant(
				universe.stringObject("x"), intType);
		SymbolicCompleteArrayType arrayType = universe.arrayType(intType, x);
		NumericSymbolicConstant index = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("i"), intType);
		SymbolicExpression arrayLambda = universe.arrayLambda(arrayType,
				universe.lambda(index, index));

		out.println(arrayLambda);

		BooleanExpression context = universe.equals(x, three);
		Reasoner reasoner = universe.reasoner(context);
		SymbolicExpression simplifiedArrayLambda = reasoner
				.simplify(arrayLambda);
		SymbolicExpression concreteArray = universe.array(intType,
				Arrays.asList(zero, one, two));

		out.println(simplifiedArrayLambda);
		out.flush();

		assertEquals(concreteArray, simplifiedArrayLambda);
	}

}
