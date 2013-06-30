package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class CharTest {

	private PrintStream out = System.out;

	private SymbolicUniverse universe;

	private SymbolicType characterType;

	@Before
	public void setUp() throws Exception {
		universe = SARL.newStandardUniverse();
		characterType = universe.characterType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void ab() {
		SymbolicExpression a = universe.character('a');
		SymbolicExpression b = universe.character('b');
		BooleanExpression eq1, eq2;

		out.println("ab: a = " + a);
		out.println("ab: b = " + b);
		assertFalse(a.equals(b));
		assertEquals(a, a);
		assertEquals(b, b);
		eq1 = universe.equals(a, b);
		assertEquals(universe.falseExpression(), eq1);
		eq2 = universe.equals(a, a);
		assertEquals(universe.trueExpression(), eq2);
	}

	@Test
	public void symbolic() {
		SymbolicExpression x = universe.symbolicConstant(
				universe.stringObject("x"), characterType);
		SymbolicExpression y = universe.symbolicConstant(
				universe.stringObject("y"), characterType);
		BooleanExpression eq = universe.equals(x, y);

		out.println("symbolic: eq: " + eq);
		assertFalse(eq.isTrue());
		assertFalse(eq.isFalse());
	}

	@Test
	public void string() {
		SymbolicExpression s1 = universe.stringExpression("Hello");
		SymbolicExpression s2 = universe.stringExpression(" there.");

		out.println("string: s1 = " + s1);
		out.println("string: s2 = " + s2);
		assertFalse(s1.equals(s2));
		assertEquals(s1, s1);
		assertEquals(s2, s2);
	}

	@Test
	public void extract() {
		SymbolicExpression a = universe.character('a');
		SymbolicExpression x = universe.symbolicConstant(
				universe.stringObject("x"), characterType);
		Character a_char = universe.extractCharacter(a);
		Character x_char = universe.extractCharacter(x);

		assertEquals(new Character('a'), a_char);
		assertNull(x_char);
	}

}
