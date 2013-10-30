/* Copyright 2013 Stephen F. Siegel, University of Delaware
 */
package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;

import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * Converted (failed) benchmark to allow analysis and submission for TRAC ticket on issues
 * Intended to perform performance analysis on polynomial-term expansion with "divides"
 * @author danfried
 *
 */
public class DividesTest {
	
	static long start, time;
	
	private static FactorySystem system;

	private static PreUniverse preUniv;
	
	private static PrintStream out;
	
	private static BooleanExpression assumption, trueExpr, claim;
	
	private static IdealFactory idealFactory;
	
	private static IdealSimplifierFactory idealSimplifierFactory;
	
	private static IdealSimplifier idealSimplifier;
	
	//static NumericSymbolicConstant x, c, div;
	
	private static SymbolicType integerType;
	
	private static NumericExpression x, y, z, c, div, int3;

	private static Polynomial divPoly;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		system = PreUniverses.newIdealFactorySystem();

		preUniv = PreUniverses.newPreUniverse(system);

		out = System.out;

		idealFactory = (IdealFactory) system.expressionFactory()
				.numericFactory();

		idealSimplifierFactory = (IdealSimplifierFactory) Ideal
				.newIdealSimplifierFactory(idealFactory, preUniv);

		integerType = preUniv.integerType();

		x = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("x"), integerType);

		y = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("y"), integerType);

		z = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("z"), integerType);

		c = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("c"), integerType);

		div = (NumericSymbolicConstant) preUniv.symbolicConstant(
				preUniv.stringObject("div"), integerType);

		int3 = preUniv.integer(3);

		divPoly = (Polynomial) x;

		trueExpr = preUniv.bool(true);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test1() {
		
		/*
		out.println("here:  " + preUniv.divides(div, x) + "  " 
		+ preUniv.divides(div, x).type()); // + "  true? " + preUniv.divides(div, x)==trueExpr);
		*/
		assumption = preUniv.equals(z, preUniv.divide(x, y));
		//assumption = preUniv.equals(preUniv.divide(x, y), z);
		//assumption = preUniv.equals(preUniv.divides(div, x), preUniv.trueExpression()); //div == x * n
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		
		ArrayList<Polynomial> divCol = new ArrayList<Polynomial>();
		
		divCol.add(divPoly);
		
		//out.println(idealSimplifier.apply(divPoly));
		//out.println(preUniv.divides(div, divPoly));
		//out.println("true? : " + idealSimplifier.simplifyExpression(divPoly));
		
		//out.println("assumption: " + assumption);
		//out.println("works???? " + idealSimplifier.apply(preUniv.divides(y, x)));
		assertEquals(z, idealSimplifier.apply(preUniv.divides(y, x)));
		//out.println(z == idealSimplifier.apply(preUniv.divides(y,x)));
		//out.println(idealSimplifier.apply(preUniv.divide(x, y)));
		
		int size = 3;
		
		for(int i = 0; i < size; i++){
			//divPoly = divPoly * (x + c)
			//divPoly =  (Polynomial) preUniv.multiply(divPoly, x);
			divPoly =  (Polynomial) preUniv.multiply(divPoly, preUniv.add(x, int3));
			divCol.add((Polynomial)divPoly);
		}
		
		for(Polynomial i : divCol){
			out.println(i + "  :  " + i.type() + "  :  " + i.degree());
			//out.println(preUniv.divides(x, i));
			out.println("apply:  " + idealSimplifier.apply((Polynomial)i));
			out.println(preUniv.divide(i, x));
			out.println("true? : " + idealSimplifier.simplifyExpression(i));
			out.println();
		}
		
		
		/*for(NumericExpression i : divCol){
			start = System.currentTimeMillis();
			
			out.print(i + " :  ");
			idealSimplifier.apply((Polynomial)i);
			
			time = System.currentTimeMillis() - start;
			out.println("  time: " + time);
		}*/
		
		
		
		//claim = preUniv.
	}
	
	/**
	 * Test on IdealSimplifier with increasing terms
	 * of an integer-based polynomial when applying and evaluating 
	 * "divides"
	 * 
	 * @see IdealSimplifier
	 */
	@Test
	public void dividBenchTest(){
		/*
		out.println("here:  " + preUniv.divides(div, x) + "  " 
		+ preUniv.divides(div, x).type()); // + "  true? " + preUniv.divides(div, x)==trueExpr);
		*/
		assumption = preUniv.equals(z, preUniv.divide(x, y));
		//assumption = preUniv.equals(preUniv.divide(x, y), z);
		//assumption = preUniv.equals(preUniv.divides(div, x), preUniv.trueExpression()); //div == x * n
		idealSimplifier = idealSimplifierFactory.newSimplifier(assumption);
		
		ArrayList<Polynomial> divCol = new ArrayList<Polynomial>();
		
		divCol.add(divPoly);
		
		//out.println(idealSimplifier.apply(divPoly));
		//out.println(preUniv.divides(div, divPoly));
		//out.println("true? : " + idealSimplifier.simplifyExpression(divPoly));
		
		//out.println("assumption: " + assumption);
		//out.println("works???? " + idealSimplifier.apply(preUniv.divides(y, x)));
		//assertEquals(z, idealSimplifier.apply(preUniv.divides(y, x)));
		//out.println(z == idealSimplifier.apply(preUniv.divides(y,x)));
		//out.println(idealSimplifier.apply(preUniv.divide(x, y)));
		
		int size = 3;
		
		for(int i = 0; i < size; i++){
			//divPoly = divPoly * (x + c)
			//divPoly =  (Polynomial) preUniv.multiply(divPoly, x);
			divPoly =  (Polynomial) preUniv.multiply(divPoly, preUniv.add(x, int3));
			divCol.add((Polynomial)divPoly);
		}
		
		for(Polynomial i : divCol){
			out.println(i + "  :  " + i.type() + "  :  " + i.degree());
			//assertEquals(CommonObjects.onePxPxSqdP3x4th.type(), i.type());
			//out.println(preUniv.divides(x, i));
			out.println("apply:  " + idealSimplifier.apply((Polynomial)i));
			assertEquals(preUniv.trueExpression(), preUniv.divides(i, y));
			out.println(preUniv.divide(i, x));
			out.println("true? : " + idealSimplifier.simplifyExpression(i));
			out.println();
		}
		
		
		/*for(NumericExpression i : divCol){
			start = System.currentTimeMillis();
			
			out.print(i + " :  ");
			idealSimplifier.apply((Polynomial)i);
			
			time = System.currentTimeMillis() - start;
			out.println("  time: " + time);
		}*/
		
		
		
		//claim = preUniv.
	}

}
