package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;
import static edu.udel.cis.vsl.sarl.ideal.simplify.CommonObjects.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.common.CommonCollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class IdealSimplifierTest {

	private static Map<SymbolicConstant, SymbolicExpression> substitutionMap = null;
	
	private static Map<Polynomial, BoundsObject> boundMap = new HashMap<Polynomial, BoundsObject>();
	
	private static Map<BooleanExpression, Boolean> booleanMap = new HashMap<BooleanExpression, Boolean>();
	
	private static Map<Polynomial, Number> constantMap = new HashMap<Polynomial, Number>();
	
	private static boolean intervalComputed = false;	
	
	private static SimplifierInfo simplifierInfo;
	
	private static PreUniverse preUniv;
	
	private static FactorySystem system;
	
	static private IdealFactory idealFactory;
		
	private static NumberFactory numFact;
	
	private static BooleanExpressionFactory boolExprFact;
	
	private static AffineFactory affineFact;
	
	static private IdealSimplifierFactory simplifierFactory;
	
	private static ObjectFactory objFact; 
	
	private static SymbolicTypeFactory symTypeFact;
	
	private static ExpressionFactory commonExprFact;
	
	private static CollectionFactory collectionFactory;

	private static BooleanExpression trueExpr, falseExpr, xeq5, two_xx, assumption, boolArg1, boolArg2;
	
	private static PrintStream out = System.out;
	
	private static boolean b;
	
	private static NumericSymbolicConstant x,y;
	
	private static SymbolicType realType;
	
	private static NumericExpression zero,two,five, twenty_five, neg_twenty_five,eq;
	
	private static IdealSimplifier idealSimplifier;
	
	private static SymbolicConstant symConstant;
	
	
	//private static SymbolicType _booleanType;
	
	//private static CnfFactory cnfFact;
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		system = PreUniverses.newIdealFactorySystem();
		preUniv = PreUniverses.newPreUniverse(system);
		idealFactory = (IdealFactory) system.expressionFactory().numericFactory();
		numFact = preUniv.numberFactory();
		objFact = PreUniverses.newIdealFactorySystem().objectFactory();
		symTypeFact = PreUniverses.newIdealFactorySystem().typeFactory();
		boolExprFact = PreUniverses.newIdealFactorySystem().booleanFactory();
		commonExprFact = PreUniverses.newIdealFactorySystem().expressionFactory();
		//affineFact = (AffineFactory)system.expressionFactory().numericFactory();
		realType = preUniv.realType();
		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
			 preUniv.stringObject("x"), realType);
		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
				 preUniv.stringObject("y"), realType);
		zero = preUniv.rational(0);
		two = preUniv.rational(2); //2.0
		five = preUniv.rational(5); // 5.0
		twenty_five = preUniv.rational(25); //25.0
		neg_twenty_five = preUniv.rational(-25);
		xeq5 = preUniv.equals(x, five);
		eq = preUniv.multiply(two, preUniv.multiply(x, x));
		trueExpr = preUniv.bool(true);
		falseExpr = preUniv.bool(false);
		//simplifierFactory = PreUniverses
		idealSimplifierFactory = (IdealSimplifierFactory) Ideal
				.newIdealSimplifierFactory(idealFactory, preUniv);
		
		//idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		
		

	}
	
	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void getFullContextText(){
		
		idealSimplifier = idealSimplifierFactory.newSimplifier(xeq5);
		BooleanExpression boolXEq5 = idealSimplifier.getFullContext();
		assertEquals(xeq5,boolXEq5);
		 
		boolArg1 = preUniv.lessThanEquals(twenty_five, preUniv.multiply(x, x));
		IdealSimplifier simpEq1 =idealSimplifierFactory.newSimplifier(boolArg1);
		BooleanExpression boolSimpEq1 = simpEq1.getFullContext();
		assertEquals(preUniv.lessThanEquals(zero, preUniv.add(neg_twenty_five, preUniv.multiply(x, x))), boolSimpEq1);
		
		/*NumericExpression xSqr = preUniv.multiply(xInt, xInt);
		NumericExpression two_xSqr = preUniv.multiply(xSqr, int2);*/
		boolArg2 = preUniv.lessThanEquals(two, preUniv.multiply(x,x));
		IdealSimplifier simpEq2 =idealSimplifierFactory.newSimplifier(boolArg2);
		BooleanExpression boolSimpEq2 = simpEq2.getFullContext();
		assertEquals(boolArg2, boolSimpEq2);
		 // x^2 * y

		
		/*//SymbolicExpression three_xy = preUniv.multiply(int3,xy);
		
		//boolArg2 = preUniv.equals(three_xy, symbExpr_xpy);
		//assumption = boolExprFact.and(boolArg1, boolArg2);
		*/
		
		
	
	}
	@Test
	public void getReducedContext(){
		idealSimplifier = idealSimplifierFactory.newSimplifier(trueExpr);
		BooleanExpression boolTrue = idealSimplifier.getReducedContext();
		assertEquals(trueExpr,boolTrue);
		
		boolArg2 = preUniv.lessThanEquals(two, preUniv.multiply(x,x));
		IdealSimplifier simpEq2 =idealSimplifierFactory.newSimplifier(boolArg2);
		BooleanExpression boolSimpEq2 = simpEq2.getReducedContext();
		assertEquals(boolArg2, boolSimpEq2);
		
		
	}
}
