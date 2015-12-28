package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class FloatToIntTest {
	SymbolicUniverse universe = SARL.newStandardUniverse();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		SymbolicType realType = universe.realType(), integerType = universe
				.integerType();
		StringObject xname = universe.stringObject("x");
		// $real floatX;  (a real number)
		SymbolicExpression floatX = universe.symbolicConstant(xname, realType);
		// intY = (int) x;
		SymbolicExpression intY = universe.cast(integerType, floatX);
		// floatY = ($real) intY;
		SymbolicExpression floatY = universe.cast(realType, intY);
		BooleanExpression xEqualsY = universe.equals(floatX, floatY);
		Reasoner reasoner = universe.reasoner(universe.trueExpression());
		// checks if floatX equals floatY?
		ValidityResult result = reasoner.valid(xEqualsY);

		// the result should NOT be YES
		assertFalse(result.getResultType() == ResultType.YES);
	}
}
