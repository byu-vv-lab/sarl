package edu.udel.cis.vsl.sarl.object;

import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.number.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ObjectsTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		ObjectFactory objectFactory = Objects.newObjectFactory(
				Numbers.REAL_FACTORY
			);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testNewObjectFactory() {
		//this test is incomplete
		//System.out.println(objectFactory.getClass());
		//assertTrue(objectFactory instanceof ObjectFactory);
	}
	
}
