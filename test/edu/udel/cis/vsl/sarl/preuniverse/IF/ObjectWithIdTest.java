/* @author Gunjan Majmudar */

package edu.udel.cis.vsl.sarl.preuniverse.IF;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;

public class ObjectWithIdTest {

	private static PreUniverse universe;

	@SuppressWarnings("unused")
	// stringObject is being used in test
	private static SymbolicObject stringObject, objectWithId;

	private static Collection<SymbolicObject> test;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(test);
		stringObject = universe.stringObject("Hello");
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

	// TODO: this test is wrong. Why should the Hello
	// have ID 40?????
	@Ignore
	@Test
	public void objectWithIdTest() {

		test = universe.objects();
		System.out.print(test);
		objectWithId = universe.objectWithId(40);

		assertEquals(universe.stringObject("Hello"), objectWithId);

	}
}
