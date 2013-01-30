package edu.udel.cis.vsl.sarl.symbolic;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.symbolic.ideal.IdealUniverse;
import edu.udel.cis.vsl.sarl.symbolic.standard.StandardUniverse;
import edu.udel.cis.vsl.sarl.symbolic.type.SymbolicTypeFactory;

public class Symbolics {
	public static SymbolicUniverseIF newRealUniverse(
			NumberFactoryIF numberFactory) {
		return new IdealUniverse(numberFactory);
	}

	public static SymbolicUniverseIF newStandardUniverse(
			NumberFactoryIF numberFactory) {
		return new StandardUniverse(numberFactory);
	}

	/**
	 * Generates new standard universe that shared the type factory with the
	 * given universe.
	 */
	public static SymbolicUniverseIF newStandardUniverse(
			NumberFactoryIF numberFactory, SymbolicUniverseIF universe) {
		SymbolicTypeFactory typeFactory;

		if (universe instanceof IdealUniverse) {
			typeFactory = ((IdealUniverse) universe).typeFactory();
		} else if (universe instanceof StandardUniverse) {
			typeFactory = ((StandardUniverse) universe).typeFactory();
		} else {
			throw new RuntimeException("Unknown kind of universe: "
					+ universe.getClass());
		}
		return new StandardUniverse(numberFactory, typeFactory);
	}

}
