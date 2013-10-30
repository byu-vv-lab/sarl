package edu.udel.cis.vsl.sarl.object;

import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for Objects class
 * @author jtirrell
 *
 */
public class ObjectsTest {

	/**
	 * ObjectFactory used for testing
	 */
	private ObjectFactory objectFactory;

	/**
	 * Instantiates this.objectFactory
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.objectFactory = Objects.newObjectFactory(	Numbers.REAL_FACTORY );
	}

	/**
	 * Test for creating a new ObjectFactory
	 */
	@Test
	public void testNewObjectFactory() {
		assertTrue(objectFactory instanceof ObjectFactory);
	}
	
}
