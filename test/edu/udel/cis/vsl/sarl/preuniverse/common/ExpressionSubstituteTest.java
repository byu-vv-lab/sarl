/* @author Gunjan Majmudar */

package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class ExpressionSubstituteTest {

	private static PreUniverse universe;

	private static SymbolicType realType, integerType, booleanType;

	private static CollectionFactory factory1;

	private static SymbolicTypeFactory typeFactory1;

	private static ExpressionSubstituter expr1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);
		factory1 = test.collectionFactory();
		typeFactory1 = test.typeFactory();

		// initializing symbolic types
		integerType = universe.integerType();
		booleanType = universe.booleanType();
		realType = universe.realType();

		expr1 = new ExpressionSubstituter(universe, factory1, typeFactory1);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void expressionSubstituteTest1() {
		

	}

}
