package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class BoundCleanerTest {
	
	private static PrintStream out = System.out;

	private PreUniverse universe;

	private SymbolicType integerType;

	private SymbolicType realType;

	private NumericExpression zeroR;

	private NumericExpression oneR;

	private SymbolicArrayType arrayIntegerType;

	private SymbolicArrayType arrayRealType;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();

		universe = PreUniverses.newPreUniverse(system);
		integerType = universe.integerType();
		realType = universe.realType();
		zeroR = universe.zeroReal();
		oneR = universe.oneReal();
		arrayIntegerType = universe.arrayType(integerType);
		arrayRealType = universe.arrayType(realType);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	/**
	 * This will clean:
	 * <pre>
	 * 1. forall x . x<1
	 * 2. forall x . x>0
	 * 3. (1) && (2)
	 * </pre>
	 * 
	 */
	public void clean1() {
		NumericSymbolicConstant x = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("x"), realType);
		BooleanExpression xlt1 = universe.lessThan(x, oneR);
		BooleanExpression all1 = universe.forall(x, xlt1);
		BooleanExpression xgt0 = universe.lessThan(zeroR, x);
		BooleanExpression all2 = universe.forall(x, xgt0);
		BooleanExpression all12 = universe.and(all1,all2);
		SymbolicExpression result1, result2, result12;

		result1 = universe.cleanBoundVariables(all1);
		out.println(result1);
		assertEquals(all1, result1);
		// TODO figure out how to make the following real tests...
		result2 = universe.cleanBoundVariables(all2);
		out.println(result2);
		result12 = universe.cleanBoundVariables(all12);
		out.println(result12);
	}
	
	/**
	 * This will clean: forall x . (x<1 && (exists x . x>0))
	 */
	@Test public void cleanNest() {
		NumericSymbolicConstant x = (NumericSymbolicConstant) universe
				.symbolicConstant(universe.stringObject("x"), realType);
		BooleanExpression xlt1 = universe.lessThan(x, oneR);
		BooleanExpression xgt0 = universe.lessThan(zeroR, x);
		BooleanExpression exists1 = universe.exists(x, xgt0);
		BooleanExpression and1 = universe.and(xlt1, exists1);
		BooleanExpression all1 = universe.forall(x, and1);
		SymbolicExpression result = universe.cleanBoundVariables(all1);
		SymbolicExpression result2 = universe.cleanBoundVariables(all1);
		
		out.println(result);
		assertEquals(result, result2);
	}

}
