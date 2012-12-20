package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.tass.config.RunConfiguration;
import edu.udel.cis.vsl.sarl.number.IF.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.symbolic.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.symbolic.ideal.IdealUniverse;
import edu.udel.cis.vsl.sarl.symbolic.standard.StandardUniverse;
import edu.udel.cis.vsl.sarl.symbolic.type.SymbolicTypeFactory;

public class Symbolics {
	public static SymbolicUniverseIF newRealUniverse(
			RunConfiguration configuration, NumberFactoryIF numberFactory) {
		return new IdealUniverse(configuration, numberFactory);
	}

	public static SymbolicUniverseIF newStandardUniverse(
			RunConfiguration configuration, NumberFactoryIF numberFactory) {
		return new StandardUniverse(configuration, numberFactory);
	}

	/**
	 * Generates new standard universe that shared the type factory with the
	 * given universe.
	 */
	public static SymbolicUniverseIF newStandardUniverse(
			RunConfiguration configuration, NumberFactoryIF numberFactory,
			SymbolicUniverseIF universe) {
		SymbolicTypeFactory typeFactory;

		if (universe instanceof IdealUniverse) {
			typeFactory = ((IdealUniverse) universe).typeFactory();
		} else if (universe instanceof StandardUniverse) {
			typeFactory = ((StandardUniverse) universe).typeFactory();
		} else {
			throw new RuntimeException("Unknown kind of universe: "
					+ universe.getClass());
		}
		return new StandardUniverse(configuration, numberFactory, typeFactory);
	}

}
