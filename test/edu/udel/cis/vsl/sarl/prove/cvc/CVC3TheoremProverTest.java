package edu.udel.cis.vsl.sarl.prove.cvc;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TheoremProverTest {

	PreUniverse universe;

	@SuppressWarnings("unused")
	private TheoremProverFactory proverFactory;

	@Before
	public void setUp() throws Exception {
		universe = PreUniverses.newPreUniverse(PreUniverses
				.newIdealFactorySystem());
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	

	@Test
	public void test() {
		//fail("Not yet implemented");
	}

}
