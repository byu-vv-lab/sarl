package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class CVC4TheoremProverFactoryTest {
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	@Test
	public void cvc4TheoremProverFactory() {
		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,
				universe.realType());

		CVC4TheoremProverFactory cvc4tpf = new CVC4TheoremProverFactory(
				universe);
		assertNotNull(cvc4tpf);
		// TODO: Don't commit this until it is working as it causes other
		// things to crash
		// cvc4tpf.newProver(context);
	}
}
