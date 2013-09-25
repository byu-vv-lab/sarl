package edu.udel.cis.vsl.sarl.expr.cnf;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.Expressions;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CnfFactoryTest {

	static PrintStream out = System.out;

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

}
