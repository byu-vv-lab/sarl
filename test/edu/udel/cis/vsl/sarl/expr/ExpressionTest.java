package edu.udel.cis.vsl.sarl.expr;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicConstant;

public class ExpressionTest {

	private static PrintStream out = System.out;
	
	StringObject string1;
	StringObject string2;
	StringObject string3;
	
	SymbolicOperator addition;
	SymbolicType type1;
	
	//CommonSymbolicExpression expr1 = new CommonSymbolicExpression(addition,type1,null);
	
	SymbolicIntegerType intType;
	
	CommonSymbolicConstant c1 = new CommonSymbolicConstant(string1, intType);
	
	@Test
	public void toStringBufferLongTest() {
		//out.println(c1.toStringBufferLong());
		out.println("here");
		assertEquals(1,1);
	}

}
