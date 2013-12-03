/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.expr.cnf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
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

/**
 * CnfFactoryTest is used to test the code mostly in CnfFactory. This includes
 * all of the various binary comparators and other methods for comparing and
 * determining expressions.
 * 
 */
public class CnfFactoryTest {

	private SymbolicUniverse sUniverse;
	private StringObject Xobj;

	private SymbolicTypeFactory stf;
	private CollectionFactory cf;
	private ObjectFactory of;
	StringObject string1;
	StringObject string2;
	StringObject string3;

	private NumericSymbolicConstant x; // real symbolic constant "X"
	private BooleanSymbolicConstant b;

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

	/**
	 * setUp() initializes many of the variables used in the following tests.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		sUniverse = Universes.newIdealUniverse();
		booleanType = sUniverse.booleanType();
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		
		Xobj = sUniverse.stringObject("X");

		stf = system.typeFactory();
		of = system.objectFactory();
		cf = system.collectionFactory();

	}

	/**
	 * tearDown()
	 * 
	 * @throws Exception
	 */
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

		assertEquals(e1, e2);
	}

	/**
	 * Testing for code in CnfFactoryNot() makes sure that the function not actually returns
	 * not(value)
	 */
	@Test
	public void notTest() {
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingfalse = sUniverse.bool(false);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(
				pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(
				qobject, booleanType);
		BooleanExpression foralltruechk = bef.exists(b, testingfalse);
		BooleanExpression EXISTS = bef.booleanExpression(
				SymbolicOperator.EXISTS, foralltruechk);
		CnfExpression cnf2 = (CnfExpression) EXISTS;
		BooleanExpression existschk = bef.forall(b, testingtrue);
		BooleanExpression FORALL = bef.booleanExpression(
				SymbolicOperator.FORALL, existschk);
		CnfExpression cnf3 = (CnfExpression) FORALL;
		BooleanExpression andtrue = bef.and(p, q);
		BooleanExpression ortrue = bef.or(p, q);
		BooleanExpression nottrue = bef.not(q);
		BooleanExpression foralltrue = bef.forall(b, testingtrue);
		BooleanExpression existstrue = bef.exists(b, testingfalse);

		// Use DeMorgan's rules logic to create same functions using ands or ors
		// to test
		assertEquals(bef.or(bef.not(p), bef.not(q)), bef.not(andtrue));
		assertEquals(bef.and(bef.not(p), bef.not(q)), bef.not(ortrue));
		assertEquals(q, bef.not(nottrue));

		// for not(forall) since it is not, check with reverse(i.e. exists)
		assertEquals(cnf2.argument(0), bef.not(foralltrue));

		// for not(exists)
		assertEquals(cnf3.argument(0), bef.not(existstrue));
	}

	/**
	 * Testing for code in CnfFactoryforall() checks to make sure the output of forall(values)
	 * is right. Testing done for forall(trues), forall(falses), and forall(mix true and false)
	 * 
	 */
	@Test
	public void forallTest() {
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);
		b = (BooleanSymbolicConstant) sUniverse.symbolicConstant(Xobj, booleanType);
		BooleanExpression foralltrue = bef.forall(b, testingtrue);
		BooleanExpression forallfalse = bef.forall(b, testingfalse);
		BooleanExpression foralltruetesting =bef.booleanExpression(SymbolicOperator.FORALL, b, testingtrue);
		BooleanExpression forallnottrue = bef.booleanExpression(SymbolicOperator.FORALL, b, testingtrue, testingfalse);
		BooleanExpression forallfalsetesting = bef.booleanExpression(SymbolicOperator.FORALL, b, testingfalse);

		assertEquals(foralltruetesting, foralltrue);
		assertNotEquals(forallnottrue, foralltrue);
		assertEquals(forallfalsetesting, forallfalse);
	}


	/**
	 * Testing for code in CnfFactoryNot(). Uses longer groups of values to ensure that even
	 * in long sets, not will still return not(value)
	 * 
	 */
	@Test
	public void cnFFactoryNotTest() {
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingfalse = sUniverse.bool(false);
		StringObject pobject = sUniverse.stringObject("a");
		StringObject qobject = sUniverse.stringObject("b");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(
				pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(
				qobject, booleanType);
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression andtrue = bef.and(p, q);
		BooleanExpression ortrue = bef.or(p, q);
		BooleanExpression nottrue = bef.not(q);
		BooleanExpression foralltrue = bef.forall(b, testingtrue);
		BooleanExpression existstrue = bef.exists(b, testingfalse);
		BooleanExpression foralltruechk = bef.exists(b, testingfalse);
		BooleanExpression EXISTS = bef.booleanExpression(
				SymbolicOperator.EXISTS, foralltruechk);
		CnfExpression cnf2 = (CnfExpression) EXISTS;
		BooleanExpression existschk = bef.forall(b, testingtrue);
		BooleanExpression FORALL = bef.booleanExpression(
				SymbolicOperator.FORALL, existschk);
		CnfExpression cnf3 = (CnfExpression) FORALL;
		// BooleanExpression FORALLTEST =
		// bef.booleanExpression(SymbolicOperator.FORALL, ANDset);

		assertEquals(bef.or(bef.not(p), bef.not(q)), bef.not(andtrue));
		assertEquals(bef.and(bef.not(p), bef.not(q)), bef.not(ortrue));
		assertEquals(q, bef.not(nottrue));

		assertEquals(cnf2.argument(0), bef.not(foralltrue));

		// for not(exists)
		assertEquals(cnf3.argument(0), bef.not(existstrue));
	}

	/**
	 * Testing for code in CnfFactoryOr() this includes combination and
	 * simplification of boolean variables
	 * 
	 */
	@Test
	public void orTest() {
		CnfFactory bef = (CnfFactory) Expressions.newCnfFactory(stf, of, cf);
		StringObject pobject = sUniverse.stringObject("p");
		StringObject qobject = sUniverse.stringObject("q");
		StringObject robject = sUniverse.stringObject("r");
		BooleanExpression p = (BooleanExpression) sUniverse.symbolicConstant(
				pobject, booleanType);
		BooleanExpression q = (BooleanExpression) sUniverse.symbolicConstant(
				qobject, booleanType);
		BooleanExpression r = (BooleanExpression) sUniverse.symbolicConstant(
				robject, booleanType);
		BooleanExpression falseExpr = bef.falseExpr();
		BooleanExpression trueExpr = bef.trueExpr();
		BooleanExpression qandtrue = bef.and(q, trueExpr);
		BooleanExpression pandfalse = bef.and(p, falseExpr);
		BooleanExpression qortrue = bef.or(q, bef.not(p));
		BooleanExpression porfalse = bef.or(p, r);
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);

		// testing for various combinations of true and false and and or results
		if(bef.getBooleanExpressionSimplification()){
		assertEquals(trueExpr, (bef.or(p, bef.or(q, bef.or(bef.not(p), r)))));
		assertEquals(trueExpr, bef.or(qortrue, porfalse));
		}
		// System.out.println(bef.and(p, bef.not(p)));

		
		assertEquals(falseExpr, bef.and(bef.not(p), p));
		assertEquals(falseExpr, bef.and(p, bef.not(p)));
		assertEquals(bef.or(bef.not(p), r), (bef.or(bef.not(p), bef.and(p, r))));
		assertEquals(testingtrue, bef.or(p, bef.not(p)));
		assertEquals(testingtrue, bef.or(bef.not(p), p));
		assertEquals(testingfalse, bef.not(bef.or(bef.not(p), p)));
		assertEquals(q, bef.or(qandtrue, pandfalse));
	}
	
	/**
	 * Tests CnfFactory and(bool boolExpr1, bool boolExpr2) method.
	 */
	@Test
	public void andTrueExprTest() {
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);

		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);

		BooleanExpression falseExpr = bef.falseExpr();
		BooleanExpression trueExpr = bef.trueExpr();
		
		
		assertEquals(testingtrue, bef.and(testingtrue, testingtrue));
		assertEquals(testingtrue, bef.and(trueExpr, testingtrue));
		assertEquals(testingfalse, bef.and(trueExpr, testingfalse));
		assertEquals(testingfalse, bef.and(testingfalse, trueExpr));
		assertEquals(testingfalse, bef.and(falseExpr, testingtrue));
		assertEquals(testingfalse, bef.and(testingtrue, falseExpr));
	
	}
	
	/**
	 * Tests CnfFactory or(bool boolExpr1, bool boolExpr2) method.
	 */
	@Test
	public void orTrueExprTest() {
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);

		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);

		BooleanExpression falseExpr = bef.falseExpr();
		BooleanExpression trueExpr = bef.trueExpr();
		
		assertEquals(testingtrue, bef.or(testingtrue, testingtrue));
		assertEquals(testingtrue, bef.or(trueExpr, testingtrue));
		assertEquals(testingtrue, bef.or(trueExpr, testingfalse));
		assertEquals(testingtrue, bef.or(falseExpr, testingtrue));
		assertEquals(testingfalse, bef.or(testingfalse, falseExpr));
		//System.out.println(bef.and(p, bef.or(q, bef.not(q))));
	}

	/**
	 * Testing for code in CnfFactoryEquiv()
	 * 
	 */
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
		Set<SymbolicObject> argSet = new HashSet<SymbolicObject>(
				Arrays.asList(arg));
		BooleanExpression b = test.booleanExpression(SymbolicOperator.AND, arg);
		BooleanExpression b2 = test.booleanExpression(SymbolicOperator.AND,
				argSet);

		assertEquals(false, b2.isTrue());
		assertEquals(false, b.isTrue());
		assertEquals(true, test.symbolic(bot).isTrue());
		assertEquals(false, test.symbolic(bof).isTrue());
		assertEquals(false, test.equiv(trueEx, falseEx).isTrue());
		assertEquals(true, test.equiv(trueEx, thisIsTrue).isTrue());
	}

	/**
	 * Testing for code in CnfFactoryNot()
	 * 
	 */
	@Test
	public void booleannotTest() {
		CnfFactory test = new CnfFactory(stf, of, cf);
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression falseEx = bef.falseExpr();

		assertEquals(false, test.not(falseEx).isFalse());
	}

	/**
	 * Testing for code in CnfFactoryforall()
	 * 
	 */
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

	/**
	 * Testing for code in CnfFactoryExists()
	 * 
	 */
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
		assertEquals(false, test.exists(x, trueEx).isFalse());
	}

	/**
	 * Testing for code in CnfSymbolicConstant()
	 * 
	 */
	@Test
	public void cnfSymbolicConstantTest() {
		StringObject name = sUniverse.stringObject("Hello");
		CnfFactory Test = new CnfFactory(stf, of, cf);
		CnfSymbolicConstant hellotest = (CnfSymbolicConstant) Test
				.booleanSymbolicConstant(name);
		StringObject hellomsg = sUniverse.stringObject("Hello");
		StringObject hellomsgfalse = sUniverse.stringObject("hello");

		assertEquals(hellomsg, hellotest.name());
		assertNotEquals(hellomsgfalse, hellotest.name());
		assertEquals("Hello", hellotest.toString());
		assertNotEquals("hello", hellotest.toString());
	}

	/**
	 * Testing for code in CnfFactoryExists()
	 * 
	 */
	@Test
	public void existsTest2() {
		BooleanExpressionFactory bef = Expressions.newCnfFactory(stf, of, cf);
		BooleanExpression testingtrue = sUniverse.bool(true);
		BooleanExpression testingfalse = sUniverse.bool(false);
		b = (BooleanSymbolicConstant) sUniverse.symbolicConstant(Xobj, booleanType);
		BooleanExpression existstrue = bef.exists(b, testingtrue);
		BooleanExpression existsfalse = bef.exists(b, testingfalse);
		BooleanExpression existstruetesting =bef.booleanExpression(SymbolicOperator.EXISTS, b, testingtrue);
		BooleanExpression existsnottrue = bef.booleanExpression(SymbolicOperator.EXISTS, b, testingtrue, testingfalse);
		BooleanExpression existsfalsetesting = bef.booleanExpression(SymbolicOperator.EXISTS, b, testingfalse);

		assertEquals(existstruetesting, existstrue);
		assertNotEquals(existsnottrue, existstrue);
		assertEquals(existsfalsetesting, existsfalse);

	}


}
