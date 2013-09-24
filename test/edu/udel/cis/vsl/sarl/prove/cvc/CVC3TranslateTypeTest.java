package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Type;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateTypeTest {
	
	// Static fields: instantiated once and used for all tests...
	private static PrintStream out = System.out;
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicRealType realType = universe.realType();
	private static SymbolicIntegerType intType = universe.integerType();
	private static SymbolicType boolType = universe.booleanType();
	private static SymbolicType intArrayType = universe.arrayType(intType);

	// Instance fields: instantiated before each test is run...
	private TheoremProverFactory proverFactory;
	private CVC3TheoremProver cvcProver;
	private ValidityChecker vc;
	
	/**
	 * Set up each test. This method is run before each test.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(universe.trueExpression());
		vc = cvcProver.validityChecker();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testTranslateTupleType() {
		// cvc3 int array (with int index type)
		Type intArrayDataType = vc.arrayType(vc.intType(), vc.intType());
		// give extent, along with array type in a tuple
		Type expected = vc.tupleType(vc.intType(), intArrayDataType);
		Type translateResult = cvcProver.translateType(intArrayType);
		
		// diagnostics
		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		out.println();
		assertEquals(expected, translateResult);
		
		// cvc3 tuple
		List<SymbolicType> typesList = new ArrayList<SymbolicType>();
		typesList.add(intType);
		typesList.add(intType);
		typesList.add(realType);
		SymbolicTypeSequence types = universe.typeSequence(typesList);
		StringObject name = universe.stringObject("twoIntRealTuple");
		SymbolicType twoIntRealTupleType = universe.tupleType(name, types);
		translateResult = cvcProver.translateType(twoIntRealTupleType);
		
		List<Type> cvc3Types = new ArrayList<Type>();
		cvc3Types.add(vc.intType());
		cvc3Types.add(vc.intType());
		cvc3Types.add(vc.realType());
		expected = vc.tupleType(cvc3Types);
		
		// diagnostics
		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		out.println();
		assertEquals(expected, translateResult);
	}
	
	@Test
	public void testTranslateFunctionType() {
		// int -> int 
		List<SymbolicType> typesList = new ArrayList<>();
		typesList.add(intType);
		SymbolicType intFunType = universe.functionType(typesList, intType);
		Type translateResult = cvcProver.translateType(intFunType);
		
		List<Type> cvc3Types = new ArrayList<>();
		cvc3Types.add(vc.intType());
		Type expected = vc.funType(cvc3Types, vc.intType());
		
		// diagnostics
		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		out.println();
		assertEquals(expected, translateResult);		
		
		// int, int -> bool
		typesList.add(intType);
		intFunType = universe.functionType(typesList, boolType);
		translateResult = cvcProver.translateType(intFunType);
		
		cvc3Types.add(vc.intType());
		expected = vc.funType(cvc3Types, vc.boolType());
		
		// diagnostics
		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		out.println();
		assertEquals(expected, translateResult);
	}
	
	@Test
	public void testTranslateUnionType() {
		List<SymbolicType> typesList = new ArrayList<>();
		typesList.add(intType);
		typesList.add(realType);
		SymbolicType intRealUnionType = universe
				.unionType(universe.stringObject("union"), typesList);
		Type translateResult = cvcProver.translateType(intRealUnionType);
		
		List<Type> cvc3Types = new ArrayList<>();
		cvc3Types.add(vc.intType());
		cvc3Types.add(vc.realType());
//		Type expected = vc... how do I make a union type?
		
		// diagnostics
//		out.println("expected: " + expected);
		out.println("translateResult: " + translateResult);
		out.println();
//		assertEquals(expected, translateResult);
	}
	
}
