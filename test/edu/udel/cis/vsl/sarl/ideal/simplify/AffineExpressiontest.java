package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;

public class AffineExpressiontest {
	private Polynomial pseudo; /* maybe null */
	private static SymbolicUniverse universe;
	private NumberFactory numberFactory;
	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;

	private static NumericExpression xpy;

	private static SymbolicType realType;

	private static SymbolicType integerType;

	private static NumericExpression one, two;

	private Number coefficient; /* not null */

	private Number offset; /* not null */


	private static PrintStream out = System.out;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe = SARL.newStandardUniverse();
		realType = universe.realType();
		integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		xpy = universe.add(x, y);
		one = universe.rational(1); // 1.0
		two = universe.rational(2); // 2.0
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void tostringtest() {
		
		//offset = numberFactory.rational("2");
	//	coefficient =  numberFactory.rational("3");
		AffineExpression test = new AffineExpression(pseudo, offset,coefficient);
	    assertEquals(test.toString(),test.toString());
	}

}
