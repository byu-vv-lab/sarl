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

	private static BooleanExpression trueExpr, falseExpr, xeq5, assumption, boolArg1, boolArg2;
	
	private static PrintStream out = System.out;
	
	private static boolean b;
	
	private static NumericSymbolicConstant x;
	
	private static SymbolicType realType;
	
	private static NumericExpression five;
	
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
		five = preUniv.rational(5); // 5.0
		
		xeq5 = preUniv.equals(x, five);
		trueExpr = preUniv.bool(true);
		//falseExpr = preUniv.bool(false);
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
		
		/*
		 * Creating a large complex equation, not finished
		 * 
		 * // x^2 * y
		xxy = getPreUniv().multiply(xy, x);
		symbExpr_xxy = xxy;
	
		
		boolArg1 = preUniv.equals(symbExpr_xxy, arg1);
		assumption = boolExprFact.and(boolArg1, boolArg2);
		IdealSimplifier simpEq1 =idealSimplifierFactory.newSimplifier(assumption);
		
		*/
	
	}
	@Test
	public void getReducedContext(){
		idealSimplifier = idealSimplifierFactory.newSimplifier(trueExpr);
		BooleanExpression boolTrue = idealSimplifier.getReducedContext();
		assertEquals(trueExpr,boolTrue);
	}
}
