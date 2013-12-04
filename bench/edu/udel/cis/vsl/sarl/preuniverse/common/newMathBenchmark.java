package edu.udel.cis.vsl.sarl.preuniverse.common;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.util.SingletonMap;
import edu.udel.cis.vsl.sarl.universe.common.MathUniverse; 
import edu.udel.cis.vsl.sarl.universe.common.MathSimplifier;

public class newMathBenchmark { 
	
	
	
	private static MathUniverse mathUniverse; 
	private static MathSimplifier trigIdentity;

	
	public static void main(String [] args){  
		MathUniverse mathUniverse; 
		 MathSimplifier trigIdentity; 
		
		

		SymbolicExpression cos = mathUniverse.cosFunction; 
		SymbolicExpression cos2 = mathUniverse.multiply((NumericExpression) cos, (NumericExpression) cos);  
		long x = System.nanoTime();
		trigIdentity.simplifyExpression(cos2); 
		long y = System.nanoTime(); 
		
		System.out.println(y-x);
	}
}
