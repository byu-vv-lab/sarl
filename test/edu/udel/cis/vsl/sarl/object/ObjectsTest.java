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

	private ObjectFactory objectFactory;

	@Before
	public void setUp() throws Exception {
		Objects obj = new Objects();
		this.objectFactory = Objects.newObjectFactory(
				Numbers.REAL_FACTORY
			);
	}

	@Test
	public void testNewObjectFactory() {
		assertTrue(objectFactory instanceof ObjectFactory);
	}
	
}
