package edu.udel.cis.vsl.sarl.prove;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.prove.common.CommonValidityResult;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProver;
import edu.udel.cis.vsl.sarl.prove.cvc.CVC3TheoremProverFactory;

public class ProveTest {
	
	PreUniverse universe;

	@SuppressWarnings("unused")
	private TheoremProverFactory proverFactory;

	@Before
	public void setUp() throws Exception {
		universe = PreUniverses.newPreUniverse(PreUniverses
				.newIdealFactorySystem());
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
	}

	@Test
	public void testValidityResult() {
		BooleanExpression b = universe.bool(true);
		Prove p = new Prove();
		
		ResultType r = ValidityResult.ResultType.YES;
		ValidityResult v = p.validityResult(r);
		assertEquals(p.RESULT_YES,v);
		
		r = ValidityResult.ResultType.NO;
		v = p.validityResult(r);
		assertEquals(p.RESULT_NO,v);
		
		r = ValidityResult.ResultType.MAYBE;
		v = p.validityResult(r);
		assertEquals(p.RESULT_MAYBE,v);
	}
}
