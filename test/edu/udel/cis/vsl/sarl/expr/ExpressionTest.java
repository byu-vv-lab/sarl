package edu.udel.cis.vsl.sarl.expr;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicConstant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class ExpressionTest {
	private SymbolicUniverse sUniverse;
	private static PrintStream out = System.out;
	
	//Kolby's Play Area
	private SymbolicTypeFactory stf;
	private CollectionFactory cf;
	StringObject string1;
	StringObject string2;
	StringObject string3;
	
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private SymbolicType realType, integerType;
	private NumericSymbolicConstant x; // real symbolic constant "X"
	private NumericSymbolicConstant y; // real symbolic constant "Y"
	private NumericExpression two; // real 2.0
	private NumericExpression three; // real 3.0

	

	
	SymbolicOperator addition;
	SymbolicType type1;
	
	//CommonSymbolicExpression expr1 = new CommonSymbolicExpression(addition,type1,null);
	
	SymbolicIntegerType intType;
	
	CommonSymbolicConstant c1 = new CommonSymbolicConstant(string1, intType);
	
	@Before
	public void setUp() throws Exception {
		sUniverse = Universes.newIdealUniverse();

		Xobj = sUniverse.stringObject("X");
		Yobj = sUniverse.stringObject("Y");
		realType = sUniverse.realType();
		integerType = sUniverse.integerType();
		x = (NumericSymbolicConstant) sUniverse.symbolicConstant(Xobj, realType);
		y = (NumericSymbolicConstant) sUniverse.symbolicConstant(Yobj, realType);
		two = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(2));
		three = (NumericExpression) sUniverse
				.cast(realType, sUniverse.integer(3));
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void toStringBufferLongTest() {
		
		//out.println(c1.toStringBufferLong());
		out.println("here");
		assertEquals(1,1);
	}
	
	@Test
	public void toStringPowerTest() {
		int exponent = 4;
		IntObject n = sUniverse.intObject(exponent);
		NumericExpression xpy = sUniverse.add(x, y);
		NumericExpression xpyp1 = sUniverse.power(xpy, n);
		
		NumericExpression xpyp2 = sUniverse.power(xpy, two);

		assertEquals(xpyp1.toString(), "X^4+4*(X^3)*Y+6*(X^2)*(Y^2)+4*X*(Y^3)+Y^4");
		assertEquals(xpyp2.toString(), "(X+Y)^2");
	}
	
	
	@Test
	public void newCnfFactoryTest() {
		//or here.
		assertEquals(5,5);
	}

}
