package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.*;

import org.junit.Ignore;
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
	
	@Ignore
	@Test
	public void cvc4TheoremProverFactory() {
		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,
				universe.realType());
		
		BooleanExpression context = universe.equals(symConstYInt,
				universe.rational(0));
		CVC4TheoremProverFactory cvc4tpf = new CVC4TheoremProverFactory(universe);
		assertNotNull(cvc4tpf);
		cvc4tpf.newProver(context);
	}
}
