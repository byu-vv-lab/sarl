package edu.udel.cis.vsl.sarl.expr;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicConstant;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class ExpressionTest {

	private static PrintStream out = System.out;
	
	//Kolby's Play Area
	private SymbolicTypeFactory stf;
	private ObjectFactory of;
	private CollectionFactory cf;
	
	StringObject string1;
	StringObject string2;
	StringObject string3;
	
	SymbolicOperator addition;
	SymbolicType type1;
	
	//CommonSymbolicExpression expr1 = new CommonSymbolicExpression(addition,type1,null);
	
	SymbolicIntegerType intType;
	
	CommonSymbolicConstant c1 = new CommonSymbolicConstant(string1, intType);
	
	@Before
	public void setUp() throws Exception {
		//Not sure what to put here.
	}
	
	@Test
	public void toStringBufferLongTest() {
		//out.println(c1.toStringBufferLong());
		out.println("here");
		assertEquals(1,1);
	}
	
	@Test
	public void newCnfFactoryTest() {
		//or here.
		assertEquals(5,5);
	}

}
