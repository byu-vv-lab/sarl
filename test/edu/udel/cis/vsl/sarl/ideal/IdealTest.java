package edu.udel.cis.vsl.sarl.ideal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumberIF;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.collections.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.CommonCollectionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;
import edu.udel.cis.vsl.sarl.symbolic.NumericExpression;
import edu.udel.cis.vsl.sarl.symbolic.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class IdealTest {

	private static PrintStream out = System.out;
	private NumberFactoryIF numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;

	private RationalNumberIF n1; // 3/2
	private RationalNumberIF n2; // -1/4
	private RationalNumberIF n3; // 5/4
	private Constant c1; // real constant 3/2
	private Constant c2; // real constant -1/4
	private Constant c10; // int constant 10
	StringObject Xobj; // "X"
	NumericSymbolicConstant x; // int symbolic constant "X"
	NumericSymbolicConstant y; // int symbolic constant "Y"

	@Before
	public void setUp() throws Exception {
		numberFactory = Numbers.REAL_FACTORY;
		objectFactory = new ObjectFactory(numberFactory);
		typeFactory = new SymbolicTypeFactory(objectFactory);
		collectionFactory = new CommonCollectionFactory(objectFactory);
		idealFactory = new IdealFactory(numberFactory, objectFactory,
				typeFactory, collectionFactory);
		n1 = numberFactory.rational("1.5");
		n2 = numberFactory.rational("-.25");
		n3 = numberFactory.rational("1.25");
		c1 = idealFactory.constant(n1);
		c2 = idealFactory.constant(n2);
		c10 = idealFactory.intConstant(10);
		Xobj = objectFactory.stringObject("X");
		x = idealFactory.newNumericSymbolicConstant(Xobj,
				typeFactory.integerType());
		y = idealFactory.newNumericSymbolicConstant(
				objectFactory.stringObject("Y"), typeFactory.integerType());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constantCreation() {
		out.println("constantCreation: " + c10);
		assertEquals(10, ((IntegerNumberIF) c10.number()).intValue());
	}

	@Test
	public void constantAdd() {
		Constant c3 = (Constant) idealFactory.add(c1, c2);

		out.println("constantAdd: " + c1 + " + " + c2 + " = " + c3);
		assertEquals(n3, c3.number());
	}

	@Test
	public void constantMultiply() {
		Constant result = (Constant) idealFactory.multiply(c1, c2);
		RationalNumberIF expected = numberFactory.rational("-.375");

		out.println("constantMultiply: " + c1 + " * " + c2 + " = " + result);
		assertEquals(expected, result.number());
	}

	@Test
	public void symbolicConstantCreate() {
		out.println("symbolicConstantCreate: " + x);
		assertEquals("X", x.name().getString());
		assertEquals(typeFactory.integerType(), x.type());
	}

	@Test
	public void symbolicConstantEquality() {
		SymbolicConstantIF x2 = idealFactory.newNumericSymbolicConstant(
				objectFactory.stringObject("X"), typeFactory.integerType());

		assertEquals(x, x2);
	}

	@Test
	public void symbolicConstantInequality1() {
		assertFalse(x.equals(y));
	}

	@Test
	public void symbolicConstantInequality2() {
		SymbolicConstantIF x2 = idealFactory.newNumericSymbolicConstant(
				objectFactory.stringObject("X"), typeFactory.realType());

		assertFalse(x.equals(x2));
	}

	@Test
	public void commutativity1() {
		SymbolicExpressionIF xpy = idealFactory.add(x, y);
		SymbolicExpressionIF ypx = idealFactory.add(y, x);

		out.println("commutativity1: " + xpy + " vs. " + ypx);
		assertEquals(xpy, ypx);
	}

	@Test
	public void xplus1squared() {
		NumericExpression xp1 = idealFactory
				.add(x, idealFactory.intConstant(1));
		SymbolicExpressionIF xp1squared = idealFactory.multiply(xp1, xp1);
		SymbolicExpressionIF x2p2xp1 = idealFactory.add(idealFactory.multiply(
				x, x), idealFactory.add(
				idealFactory.multiply(idealFactory.intConstant(2), x),
				idealFactory.intConstant(1)));

		out.println("xplus1squared: " + xp1squared + " vs. " + x2p2xp1);
		assertEquals(xp1squared, x2p2xp1);
	}

}
