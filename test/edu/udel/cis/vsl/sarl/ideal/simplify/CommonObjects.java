package edu.udel.cis.vsl.sarl.ideal.simplify;

import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class CommonObjects {

	static FactorySystem system;

	static PreUniverse universe;

	static void setUp() {
		system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
	}

}
