package edu.udel.cis.vsl.sarl.object;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class Objects {

	public static ObjectFactory newObjectFactory(NumberFactoryIF numberFactory) {
		return new CommonObjectFactory(numberFactory);
	}
}
