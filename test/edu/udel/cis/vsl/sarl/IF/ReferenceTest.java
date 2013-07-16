package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.ArrayElementReference;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.OffsetReference;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.TupleComponentReference;
import edu.udel.cis.vsl.sarl.IF.expr.UnionMemberReference;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;

public class ReferenceTest {

	private static PrintStream out = System.out;

	private SymbolicUniverse universe = SARL.newStandardUniverse();

	// need: scalar, array, tuple, union, offset

	// array of array, array of tuple, array of union

	// tuple of array, tuple of tuple, tuple of union

	// for each: dereference, and assign

	private SymbolicIntegerType integerType = universe.integerType();

	private SymbolicRealType realType = universe.realType();

	private SymbolicType charType = universe.characterType();

	private IntObject zeroObj = universe.intObject(0);

	private IntObject oneObj = universe.intObject(1);

	private IntObject twoObj = universe.intObject(2);

	private NumericExpression zero = universe.integer(0);

	private NumericExpression one = universe.integer(1);

	private NumericExpression two = universe.integer(2);

	private NumericExpression zeroR = universe.rational(0.0);

	private NumericExpression oneR = universe.rational(1.0);

	private NumericExpression twoR = universe.rational(2.0);

	private NumericExpression threeR = universe.rational(3.0);

	private SymbolicExpression cChar = universe.character('c');

	private SymbolicExpression dChar = universe.character('d');

	private SymbolicArrayType arrayType = universe.arrayType(realType);

	private SymbolicArrayType array2dType = universe.arrayType(arrayType);

	private SymbolicTupleType tupleType = universe.tupleType(
			universe.stringObject("tuple"),
			Arrays.asList(new SymbolicType[] { charType, realType }));

	private NumericSymbolicConstant iVar = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("i"), integerType);

	private NumericSymbolicConstant x = (NumericSymbolicConstant) universe
			.symbolicConstant(universe.stringObject("x"), realType);

	private SymbolicExpression a1 = universe.array(realType,
			Arrays.asList(new SymbolicExpression[] { oneR, twoR, threeR }));

	private SymbolicExpression a2 = universe.symbolicConstant(
			universe.stringObject("a2"), arrayType);

	private SymbolicExpression t1 = universe.tuple(tupleType,
			Arrays.asList(new SymbolicExpression[] { cChar, threeR }));

	private SymbolicExpression t2 = universe.symbolicConstant(
			universe.stringObject("t2"), tupleType);

	private ReferenceExpression nullReference = universe.nullReference();

	private ReferenceExpression identityReference = universe
			.identityReference();

	private ArrayElementReference ar0 = universe.arrayElementReference(
			identityReference, zero);

	private ArrayElementReference ar1 = universe.arrayElementReference(
			identityReference, one);

	private ArrayElementReference ar2 = universe.arrayElementReference(
			identityReference, two);

	private ArrayElementReference ari = universe.arrayElementReference(
			identityReference, iVar);

	private TupleComponentReference tr0 = universe.tupleComponentReference(
			identityReference, zeroObj);

	private TupleComponentReference tr1 = universe.tupleComponentReference(
			identityReference, oneObj);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void identity() {
		out.println("identity: identityReference = " + identityReference);
		out.println("identity: a1 = " + a1);
		out.println("identity: a2 = " + a2);
		out.println("identity: t1 = " + t1);
		out.println("identity: t2 = " + t2);
		assertEquals(a1, universe.dereference(a1, identityReference));
		assertEquals(a2, universe.dereference(a2, identityReference));
		assertEquals(t1, universe.dereference(t1, identityReference));
		assertEquals(t2, universe.dereference(t2, identityReference));
		assertEquals(a2, universe.assign(a1, identityReference, a2));
		assertEquals(t2, universe.assign(t1, identityReference, t2));
	}

	@Test
	public void array() {
		SymbolicExpression expected = universe.array(realType, Arrays
				.asList(new SymbolicExpression[] { threeR, twoR, threeR }));

		out.println("arrayElement: ar0 = " + ar0);
		out.println("arrayElement: ar1 = " + ar1);
		out.println("arrayElement: ar2 = " + ar2);
		out.println("arrayElement: ari = " + ari);
		assertEquals(oneR, universe.dereference(a1, ar0));
		assertEquals(expected, universe.assign(a1, ar0, threeR));
		assertEquals(twoR, universe.dereference(a1, ar1));
		assertEquals(
				universe.array(
						realType,
						Arrays.asList(new SymbolicExpression[] { oneR, threeR,
								threeR })), universe.assign(a1, ar1, threeR));
		assertEquals(universe.arrayRead(a2, zero),
				universe.dereference(a2, ar0));
		assertEquals(universe.arrayRead(a2, two), universe.dereference(a2, ar2));
		assertEquals(universe.arrayRead(a1, iVar),
				universe.dereference(a1, ari));
		assertEquals(universe.arrayRead(a2, iVar),
				universe.dereference(a2, ari));
		assertEquals(universe.arrayWrite(a1, iVar, threeR),
				universe.assign(a1, ari, threeR));
	}

	@Test(expected = SARLException.class)
	public void arrayOutOfBounds() {
		ArrayElementReference ar3 = universe.arrayElementReference(
				identityReference, universe.integer(3));

		universe.dereference(a1, ar3);
	}

	@Test(expected = SARLException.class)
	public void wrongReferenceKind() {
		universe.dereference(a1, tr0);
	}

	@Test
	public void array2d() {
		SymbolicExpression b0 = universe.array(realType,
				Arrays.asList(new SymbolicExpression[] { zeroR, oneR }));
		SymbolicExpression b1 = universe.array(realType,
				Arrays.asList(new SymbolicExpression[] { twoR, threeR }));
		SymbolicExpression b = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { b0, b1 }));
		ArrayElementReference ar00 = universe.arrayElementReference(ar0, zero);
		ArrayElementReference ar11 = universe.arrayElementReference(ar1, one);
		SymbolicExpression c0 = b0;
		SymbolicExpression c1 = universe.arrayWrite(b1, one, zeroR);
		SymbolicExpression c = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { c0, c1 }));

		assertEquals(b0, universe.dereference(b, ar0));
		assertEquals(b1, universe.dereference(b, ar1));
		assertEquals(zeroR, universe.dereference(b, ar00));
		assertEquals(threeR, universe.dereference(b, ar11));
		assertEquals(c, universe.assign(b, ar11, zeroR));
		assertEquals(c, universe.assign(b, ar1, c1));
	}

	@Test
	public void tuple() {
		// t1=<'c',3.0>
		assertEquals(cChar, universe.dereference(t1, tr0));
		assertEquals(threeR, universe.dereference(t1, tr1));
		assertEquals(universe.tuple(tupleType,
				Arrays.asList(new SymbolicExpression[] { dChar, threeR })),
				universe.assign(t1, tr0, dChar));

	}

	@Test
	public void union() {
		SymbolicUnionType unionType = universe.unionType(
				universe.stringObject("union"),
				Arrays.asList(new SymbolicType[] { integerType, charType }));
		UnionMemberReference ur0 = universe.unionMemberReference(
				identityReference, zeroObj);
		UnionMemberReference ur1 = universe.unionMemberReference(
				identityReference, oneObj);
		SymbolicExpression u1 = universe.unionInject(unionType, zeroObj, one);
		SymbolicExpression u2 = universe.unionInject(unionType, oneObj, cChar);

		assertEquals(one, universe.dereference(u1, ur0));
		assertEquals(cChar, universe.dereference(u2, ur1));
		assertEquals(universe.unionInject(unionType, zeroObj, two),
				universe.assign(u1, ur0, two));
		assertEquals(universe.unionInject(unionType, oneObj, dChar),
				universe.assign(u2, ur1, dChar));
	}

	@Test
	public void offset() {
		OffsetReference or0 = universe.offsetReference(identityReference, zero);
		OffsetReference or1 = universe.offsetReference(identityReference, one);

		assertEquals(x, universe.dereference(x, or0));
	}

	@Test(expected = SARLException.class)
	public void offsetBad() {
		OffsetReference or1 = universe.offsetReference(identityReference, one);

		universe.dereference(x, or1);
	}

	@Test(expected = SARLException.class)
	public void nullReference() {
		universe.dereference(x, nullReference);
	}
}
