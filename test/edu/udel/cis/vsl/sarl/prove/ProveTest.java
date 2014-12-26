package edu.udel.cis.vsl.sarl.prove;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class ProveTest {

	PreUniverse universe;

	@SuppressWarnings("unused")
	private TheoremProverFactory proverFactory;

	@Before
	public void setUp() throws Exception {
		universe = PreUniverses.newPreUniverse(PreUniverses
				.newIdealFactorySystem());
		proverFactory = Prove.newMultiProverFactory(universe,
				Configurations.CONFIG);
	}

	@Test
	public void testValidityResult() {
		ResultType r = ValidityResult.ResultType.YES;
		ValidityResult v = Prove.validityResult(r);
		assertEquals(Prove.RESULT_YES, v);

		r = ValidityResult.ResultType.NO;
		v = Prove.validityResult(r);
		assertEquals(Prove.RESULT_NO, v);

		r = ValidityResult.ResultType.MAYBE;
		v = Prove.validityResult(r);
		assertEquals(Prove.RESULT_MAYBE, v);
	}
}
