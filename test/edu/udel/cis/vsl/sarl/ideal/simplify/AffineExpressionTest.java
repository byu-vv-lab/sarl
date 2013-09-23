package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class AffineExpressionTest {
	private Polynomial pseudo; /* maybe null */
	private static SymbolicUniverse universe;
	private static NumberFactory numberFactory;
	private static NumericSymbolicConstant x;

	private static NumericSymbolicConstant y;

	private static NumericExpression xpy;
	///////////////////////////////////////////////////////
	private static ObjectFactory objectFactory;
	private static SymbolicTypeFactory typeFactory;
	private static  CollectionFactory collectionFactory;
	private static NumericExpressionFactory numericExpressionFactory;

	private static BooleanExpressionFactory booleanFactory;
	private static RealNumberFactory realNumberFactory;
	private static CommonIdealFactory commonIdealFactory;

	private static IdealFactory idealFactory;
	private static IdealFactory polyconstant;

	private static SymbolicType realType;
	private static SymbolicType integerType;

	private static NumericExpression one, two;
	private static Constant c10;
	private static SymbolicType real;

	private static edu.udel.cis.vsl.sarl.IF.number.Number offset;
	private static edu.udel.cis.vsl.sarl.IF.number.Number coefficient;


	private static PrintStream out = System.out;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		universe = SARL.newStandardUniverse();
		realType = universe.realType();
		integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		xpy = universe.add(x, y);
		one = universe.rational(1); // 1.0
		two = universe.rational(2); // 2.0
		////////////
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		numberFactory = system.numberFactory();
		objectFactory = system.objectFactory();
		typeFactory = system.typeFactory();
		collectionFactory = system.collectionFactory();
		numericExpressionFactory = system.numericFactory();
		idealFactory = (IdealFactory) system.numericFactory();
		polyconstant = (IdealFactory) system.numericFactory();
		booleanFactory = system.booleanFactory();
		realNumberFactory = (RealNumberFactory) system.numberFactory();
		commonIdealFactory = new CommonIdealFactory(numberFactory,
				objectFactory, typeFactory,
				collectionFactory,
				booleanFactory); 
		//////////////////////////////////////////
		commonIdealFactory = new CommonIdealFactory(numberFactory,
				objectFactory, typeFactory,
				collectionFactory,
				booleanFactory); 
		c10 = idealFactory.intConstant(10);
		real = typeFactory.realType();
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test/*(expected=java.lang.AssertionError.class)*/
	public void tostringtest() {
		////////
		////
		offset = numberFactory.rational("3");
		coefficient = numberFactory.rational("3");
		SymbolicMap<Monic, Monomial> termMap = commonIdealFactory.emptyMap();
		
		Monic monic = (Monic) idealFactory.symbolicConstant(objectFactory.stringObject("Y"), real);
		
		Monomial factorization = idealFactory.monomial(c10, monic);
		termMap.put(monic, factorization);
		out.println(factorization.toString());
		Polynomial poly = commonIdealFactory.polynomial(termMap, factorization);
		Polynomial poly2 = commonIdealFactory.polynomial(termMap, factorization);
		poly = idealFactory.polynomial(termMap, factorization.factorization(idealFactory));
         SymbolicMap<Monic, Monomial> termMap0 = commonIdealFactory.emptyMap();
 		Monic monic0 = (Monic) idealFactory.symbolicConstant(objectFactory.stringObject("X"), real);
 		Monomial monomial0 = idealFactory.monomial(idealFactory.intConstant(10), monic0);
 		termMap0.put(monic0, monomial0);
 	//	out.println(monomial0.toString());
 		//out.println(monic0.isNumeric());

		//	coefficient =  numberFactory.rational("3");
		
		AffineExpression test2 = new AffineExpression(poly,numberFactory.rational("6"),numberFactory.rational("6"));
		AffineExpression test3 = new AffineExpression(poly,numberFactory.rational("6"),numberFactory.rational("6"));
		AffineExpression test4 = new AffineExpression(poly,numberFactory.rational("3"),numberFactory.rational("6"));
		AffineExpression test5 = new AffineExpression(poly,numberFactory.rational("6"),numberFactory.rational("15"));
		System.out.println(test2.toString());
		System.out.println(test2.hashCode());
		assertEquals(test2.toString(),test2.coefficient().toString()+"*"+test2.pseudo().toString()+"+"+test2.offset().toString());
		assertEquals(test2.equals(poly2),false);
		assertEquals(test2.equals(test3),true);
		assertEquals(test3.equals(test4),false);
		assertEquals(test3.equals(test5),false);
		boolean nullError = false;
		try{
		AffineExpression test = new AffineExpression(pseudo, offset,coefficient);
		assertEquals(test2.toString(),test2.coefficient().toString()+"*"+test2.pseudo().toString()+"+"+test2.offset().toString());	
		assertEquals(test.toString(), test.coefficient().toString());
			assertEquals(test2.equals(test3),true);
			assertEquals(test3.equals(test4),false);
			assertEquals(test3.equals(test5),false);
			assertEquals(test.equals(test),true);
		}catch(AssertionError e){
			System.out.println("Epic fail!:" + e.getMessage());
			nullError = true;
		}
		assertEquals(nullError, false);
	}


}
