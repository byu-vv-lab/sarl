package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;

/**
 * This is a demo for SARL features for simplifications of symbolic expressions.
 * 
 * @author mohammedalali
 *
 */
public class SimplificationDemo {
	
	//PreUniverse myUniverse;
	
	SymbolicUniverse myUniverse;
	
	SymbolicExpression symExpr1;
	
	BooleanExpression boolExpr1, boolExpr2;
	
	BooleanSymbolicConstant xBoolean, yBoolean;
	
	StringObject xObject, yObject;

	SymbolicType booleanType, integerType;
	
	NumericSymbolicConstant xNumeric, yNumeric;
	
	Reasoner reasoner;
	
	/**
	 * concrete integers constants
	 */
	NumericExpression zero, one, two;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		myUniverse = Universes.newIdealUniverse();
		xObject = myUniverse.stringObject("X");
		yObject = myUniverse.stringObject("Y");
		booleanType = myUniverse.booleanType();
		integerType = myUniverse.integerType();
		xBoolean = (BooleanSymbolicConstant)myUniverse.symbolicConstant(xObject, booleanType);
		yBoolean = (BooleanSymbolicConstant)myUniverse.symbolicConstant(yObject, booleanType);
		xNumeric = (NumericSymbolicConstant)myUniverse.symbolicConstant(xObject, integerType);
		yNumeric = (NumericSymbolicConstant)myUniverse.symbolicConstant(yObject, integerType);
		zero = myUniverse.integer(0);
		one = myUniverse.integer(1);
		two = myUniverse.integer(2);
		
		boolExpr1 = myUniverse.equals(xNumeric, two); // X=2
		boolExpr2 = myUniverse.lessThanEquals(xNumeric, two); // X<=2
		symExpr1 = myUniverse.add(xNumeric, yNumeric); // X+Y
		//the simplifier might return Y+2
			}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSimplify() {
		reasoner = myUniverse.reasoner(boolExpr1);
		BooleanExpression andExpression = myUniverse.and(boolExpr1, boolExpr2);
		
		assertTrue(andExpression.type().isBoolean());
		
		System.out.println("Simplifying: (X=2 && X<=2): " + andExpression);
		System.out.println(reasoner.simplify(myUniverse.and(boolExpr1, boolExpr2)));
		System.out.println(reasoner.getFullContext());
		
	}

}
