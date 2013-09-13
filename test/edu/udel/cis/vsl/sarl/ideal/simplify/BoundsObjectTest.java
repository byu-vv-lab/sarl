package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class BoundsObjectTest {
/*
	@Test
	public void test() {
		fail("Not yet implemented");
	}
*/
	private static FactorySystem system;
	
	private static SymbolicTypeFactory symbFactory;
	
	private static SymbolicExpression symbExpr;
	
	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;
	
	private static NumericExpressionFactory numExprFact;
	
	private static SymbolicType realType;

	private static SymbolicType integerType;
	
	private static PreUniverse preUniv;
	
	private static NumericExpression xpy;
	
	private static BoundsObject boundobj;
	
	private static NumberFactory numFact;
	
	private static Number numBound;
	
	private static NumericExpression one, two, five;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		preUniv = PreUniverses.newPreUniverse(system);
		one = preUniv.rational(1); // 1.0
		two = preUniv.rational(2); // 2.0
		five = preUniv.rational(5); // 5.0
		system = PreUniverses.newIdealFactorySystem();
		symbFactory = system.expressionFactory().typeFactory();
		realType = preUniv.realType();
		integerType = preUniv.integerType();
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), realType);
		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), realType);
		xpy = preUniv.add(x, y);
		symbExpr = xpy;
//		xpy.
//		numbFact = preUniv.  //numExprFact.numberFactory();
//		numbBound = preUniv.  //numbFact.rational("three");
//		
//		boundobj = BoundsObject.newTightBound(symbExpr, bound)
		numBound = (Number) numFact.rational("3");
	}
	
	@Ignore
	@Test
	public void equals(){
		BoundsObject boundObjEquals = BoundsObject.newTightBound(symbExpr, (edu.udel.cis.vsl.sarl.IF.number.Number)numBound);
	}
}
