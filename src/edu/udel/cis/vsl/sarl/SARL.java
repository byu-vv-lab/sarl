package edu.udel.cis.vsl.sarl;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicUniverse;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class SARL {

	public static SymbolicUniverseIF newIdealUniverse() {
		NumberFactoryIF numberFactory = Numbers.REAL_FACTORY;
		ObjectFactory objectFactory = new ObjectFactory(numberFactory);
		SymbolicTypeFactory typeFactory = new SymbolicTypeFactory(objectFactory);

		return new CommonSymbolicUniverse(objectFactory, typeFactory);
	}

}
