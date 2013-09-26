package edu.udel.cis.vsl.sarl.expr.cnf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.Expressions;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class CnfFactoryTest {
	
	private SymbolicUniverse sUniverse;
	private static PrintStream out = System.out;
	
	private SymbolicTypeFactory stf;
	private CollectionFactory cf;
	private ObjectFactory of;
	private NumberFactory nf;
	private SymbolicType herbrandType;
	StringObject string1;
	StringObject string2;
	StringObject string3;
	
	private SymbolicConstant zz;
	
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private SymbolicType realType, integerType, arrayType, tupleType;
	private NumericSymbolicConstant x; // real symbolic constant "X"
	private NumericSymbolicConstant xInt; // Int symbolic constant "X"
	private NumericSymbolicConstant y; // real symbolic constant "Y"
	private BooleanSymbolicConstant b;
	private SymbolicConstant t;
	private NumericExpression two; // real 2.0
	private NumericExpression three; // real 3.0
	private NumericExpression twoInt; // int 2.0
	private NumericExpression threeInt; // int 3.0
	private BooleanObject trueBoolObj; // True
	private BooleanObject falseBoolObj; // False
	private IntObject fiveIntObj; // 5
	private IntObject zeroIntObj; // 0
	private IntObject negIntObj; // -10
	private NumericExpression xpy;
	private NumericExpression xty;

	static NumberFactory numberFactory = Numbers.REAL_FACTORY;

	static ObjectFactory objectFactory = Objects
			.newObjectFactory(numberFactory);

	static SymbolicTypeFactory typeFactory = Types
			.newTypeFactory(objectFactory);

	static CollectionFactory collectionFactory = Collections
			.newCollectionFactory(objectFactory);

	static BooleanExpressionFactory factory = Expressions.newCnfFactory(
			typeFactory, objectFactory, collectionFactory);

	static SymbolicType booleanType = typeFactory.booleanType();

	static BooleanSymbolicConstant p = factory
			.booleanSymbolicConstant(objectFactory.stringObject("p"));

	static BooleanSymbolicConstant q = factory
			.booleanSymbolicConstant(objectFactory.stringObject("q"));

	static BooleanSymbolicConstant r = factory
			.booleanSymbolicConstant(objectFactory.stringObject("r"));

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		sUniverse = Universes.newIdealUniverse();

		
		
		Xobj = sUniverse.stringObject("X");
		Yobj = sUniverse.stringObject("Y");
		trueBoolObj = sUniverse.booleanObject(true);
		falseBoolObj = sUniverse.booleanObject(false);
		fiveIntObj = sUniverse.intObject(5);
		zeroIntObj = sUniverse.intObject(0);
		negIntObj = sUniverse.intObject(-10);
		realType = sUniverse.realType();
		booleanType = sUniverse.booleanType();
		integerType = sUniverse.integerType();
		arrayType = sUniverse.arrayType(integerType);
		List<SymbolicType> fieldType1 = new ArrayList<SymbolicType>();
		fieldType1.add(integerType);
		
		
		tupleType = sUniverse.tupleType(sUniverse.stringObject("typ1"), fieldType1);
		
	
		
		zz =  sUniverse.symbolicConstant(Xobj, arrayType);
		t = sUniverse.symbolicConstant(Xobj, tupleType);
		b = (BooleanSymbolicConstant) sUniverse.symbolicConstant(Xobj,booleanType );
		x = (NumericSymbolicConstant)  sUniverse.symbolicConstant(Xobj, realType);
		xInt = (NumericSymbolicConstant) sUniverse.symbolicConstant(Xobj, integerType);
		y = (NumericSymbolicConstant) sUniverse.symbolicConstant(Yobj, realType);
		two = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(2));
		three = (NumericExpression) sUniverse.cast(realType, sUniverse.integer(3));
		twoInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(2));
		threeInt = (NumericExpression) sUniverse.cast(integerType, sUniverse.integer(3));
		xpy = sUniverse.add(x,y);
		xty = sUniverse.multiply(x,y);
		
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		
		stf = system.typeFactory();
		of = system.objectFactory();
		cf = system.collectionFactory();
		nf = system.numberFactory();
		
		herbrandType = stf.herbrandRealType();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Check !(p&&q) equals (!p)||(!q).
	 */
	@Test
	public void notAnd() {
		BooleanExpression e1 = factory.not(factory.and(p, q));
		BooleanExpression e2 = factory.or(factory.not(p), factory.not(q));

		out.println("!(p&&q) = " + e1);
		assertEquals(e1, e2);
	}
	

	@Test
	public void notTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingfalse = sUniverse.bool(false);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(qobject, booleanType);
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression andtrue =  bef.and(p, q);
		BooleanExpression ortrue = bef.or(p, q);
		BooleanExpression nottrue = bef.not(q);
		BooleanExpression foralltrue = bef.forall(b, testingtrue);
		BooleanExpression existstrue = bef.exists(b, testingfalse);

		
		//Use DeMorgan's rules logic to create same functions using ands or ors to test
		assertEquals(bef.or(bef.not(p), bef.not(q)), bef.not(andtrue));
		assertEquals(bef.and(bef.not(p), bef.not(q)), bef.not(ortrue));
		assertEquals(q, bef.not(nottrue));
		
		//Created as setup for forall and exists
		BooleanExpression foralltruechk = bef.exists(b, testingfalse);
		BooleanExpression EXISTS = bef.booleanExpression(SymbolicOperator.EXISTS, foralltruechk);
		CnfExpression cnf2 = (CnfExpression) EXISTS;
		
		BooleanExpression existschk = bef.forall(b, testingtrue);
		BooleanExpression FORALL = bef.booleanExpression(SymbolicOperator.FORALL, existschk);
		CnfExpression cnf3 = (CnfExpression) FORALL;
		
		//for not(forall) since it is not, check with reverse(i.e. exists)
		assertEquals(cnf2.argument(0), bef.not(foralltrue));
		
		//for not(exists)
		assertEquals(cnf3.argument(0), bef.not(existstrue));
	}
	
	@Test
	public void forallTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(qobject, booleanType);
		BooleanExpression andtrue = bef.and(p, q);
		BooleanExpression foralltrue = bef.forall(b, q);
		BooleanExpression foralltrue2 = bef.forall(b, p);
		BooleanExpression FORALL = bef.booleanExpression(SymbolicOperator.FORALL, foralltrue);
		BooleanExpression FORALL2 = bef.booleanExpression(SymbolicOperator.FORALL, foralltrue2);
		CnfExpression cnf = (CnfExpression) FORALL;
		CnfExpression cnf2 = (CnfExpression) FORALL2;
		assertEquals(bef.and((BooleanExpression) cnf2.argument(0), (BooleanExpression) cnf.argument(0)), bef.forall(b, andtrue));
	}
	
	@Test
	public void orTest(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(qobject, booleanType);
		BooleanExpression falseExpr= bef.falseExpr();
		BooleanExpression trueExpr= bef.trueExpr();
		
		//testing for various combinations of true and falso and and or results
		assertEquals(testingtrue, bef.and(testingtrue, testingtrue));
		assertEquals(testingtrue, bef.and(trueExpr, testingtrue));
		assertEquals(testingfalse, bef.and(trueExpr, testingfalse));
		assertEquals(testingfalse, bef.and(testingfalse, trueExpr));
		assertEquals(testingfalse, bef.and(falseExpr, testingtrue));
		assertEquals(testingfalse, bef.and(testingtrue, falseExpr));
		assertEquals(testingtrue, bef.or(testingtrue, testingtrue));
		assertEquals(testingtrue, bef.or(trueExpr, testingtrue));
		assertEquals(testingtrue, bef.or(trueExpr, testingfalse));
		assertEquals(testingtrue, bef.or(falseExpr, testingtrue));
		assertEquals(testingfalse, bef.or(testingfalse, falseExpr));
		
		BooleanExpression qandtrue = bef.and(q, trueExpr);
		BooleanExpression pandfalse = bef.and(p, falseExpr);

		BooleanExpression AND = bef.booleanExpression(SymbolicOperator.AND, qandtrue);
		assertEquals(q, bef.or(AND, pandfalse));
	}
	
	
	@Test
	public void equivTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		BooleanExpression trueEx = bef.trueExpr();
		BooleanExpression thisIsTrue = bef.trueExpr();
		BooleanObject bot = sUniverse.booleanObject(true);
		BooleanObject bof = sUniverse.booleanObject(false);
		
		SymbolicObject[] arg = sUniverse.and(trueEx, trueEx).arguments();
		Set<SymbolicObject> argSet = new HashSet<SymbolicObject>(Arrays.asList(arg));
		BooleanExpression b = test.booleanExpression(SymbolicOperator.AND, arg);
		BooleanExpression b2 = test.booleanExpression(SymbolicOperator.AND, argSet);
		
		assertEquals(false, b2.isTrue());
		assertEquals(false, b.isTrue());
		assertEquals(true, test.symbolic(bot).isTrue());
		assertEquals(false, test.symbolic(bof).isTrue());
		assertEquals(false, test.equiv(trueEx, falseEx).isTrue());
		assertEquals(true, test.equiv(trueEx, thisIsTrue).isTrue());
	}
	
	@Test
	public void booleannotTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		assertEquals(false, test.not(falseEx).isFalse());
	}
	
	
	@Test
	public void forAllTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		BooleanExpression trueEx = bef.trueExpr();

		assertEquals(true, test.forall(x, falseEx).isFalse());
		assertEquals(false, test.forall(x, falseEx).isTrue());
		assertEquals(true, test.forall(x, trueEx).isTrue());
		assertEquals(false, test.forall(x, trueEx).isFalse());
	}
	
	@Test
	public void existsTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();
		BooleanExpression trueEx = bef.trueExpr();
		BooleanExpression b = bef.or(trueEx, falseEx);

		assertEquals(true, test.exists(x, b).isTrue());
		assertEquals(true, test.exists(x, falseEx).isFalse());
		assertEquals(false, test.exists(x, falseEx).isTrue());
		assertEquals(true, test.exists(x, trueEx).isTrue());
		assertEquals(false,test.exists(x, trueEx).isFalse());
	}
	
	@Test
	public void cnfSymbolicConstantTest() {
		StringObject name = sUniverse.stringObject("Hello");
		CnfFactory Test = new CnfFactory(stf, of, cf);
		CnfSymbolicConstant hellotest = (CnfSymbolicConstant) Test.booleanSymbolicConstant(name);
		StringObject hellomsg = sUniverse.stringObject("Hello");
		StringObject hellomsgfalse = sUniverse.stringObject("hello");
		assertEquals(hellomsg, hellotest.name());
		assertNotEquals(hellomsgfalse, hellotest.name());
		assertEquals("Hello", hellotest.toString());
		assertNotEquals("hello",hellotest.toString());
	}
	
	@Test
	public void existsTest2(){
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(qobject, booleanType);
		BooleanExpression ortrue = bef.or(p, q);
		BooleanExpression existstrue = bef.exists(b, q);
		BooleanExpression existstrue2 = bef.exists(b, p);
		BooleanExpression EXISTS = bef.booleanExpression(SymbolicOperator.EXISTS, existstrue);
		BooleanExpression EXISTS2 = bef.booleanExpression(SymbolicOperator.EXISTS, existstrue2);
		CnfExpression cnf = (CnfExpression) EXISTS;
		CnfExpression cnf2 = (CnfExpression) EXISTS2;
		System.out.println(cnf.argument(0));
		assertEquals(bef.or((BooleanExpression) cnf2.argument(0), (BooleanExpression) cnf.argument(0)), bef.exists(b, ortrue));
		}
	

}
