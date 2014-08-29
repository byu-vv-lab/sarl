package edu.udel.cis.vsl.sarl.type.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;

public class DomainTypeTest {

	public final static PrintStream out = System.out;

	private SymbolicUniverse universe;

	private SymbolicIntegerType intType;

	private SymbolicArrayType intArrayType;

	private NumericExpression zero, one, two;

	private IntObject litIdx;

	private IntObject recIdx;

	private SymbolicUnionType domainType;

	private SymbolicType rangeType;

	private SymbolicType rectangularDomainType;

	private SymbolicType literalDomainType;

	@Before
	public void setUp() {
		universe = SARL.newStandardUniverse();
		intType = universe.integerType();
		intArrayType = universe.arrayType(intType);
		zero = universe.zeroInt();
		one = universe.oneInt();
		two = universe.integer(2);
		recIdx = universe.intObject(0);
		litIdx = universe.intObject(1);
		rangeType = universe.tupleType(universe.stringObject("range"),
				Arrays.asList(intType, intType, intType));
		rectangularDomainType = universe.arrayType(rangeType);
		literalDomainType = universe.arrayType(intArrayType);
		domainType = universe.unionType(universe.stringObject("domain"),
				Arrays.asList(rectangularDomainType, literalDomainType));
	}

	@Test
	public void injext1() {
		SymbolicExpression tuple1 = universe.array(intType,
				Arrays.asList(one, two, zero));
		SymbolicExpression tuple2 = universe.array(intType,
				Arrays.asList(two, two, two));
		SymbolicExpression literal = universe.array(intArrayType,
				Arrays.asList(tuple1, tuple2));
		SymbolicExpression domain = universe.unionInject(domainType, litIdx,
				literal);

		out.println("domain = " + domain);

		assertTrue(universe.unionTest(litIdx, domain).isTrue());
		assertTrue(universe.unionTest(recIdx, domain).isFalse());

		SymbolicExpression literal2 = universe.unionExtract(litIdx, domain);

		out.println("literal2 = " + literal2);
		assertEquals(literal, literal2);
	}
}
