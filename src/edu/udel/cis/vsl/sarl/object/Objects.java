package edu.udel.cis.vsl.sarl.object;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;

public class Objects {

	public static ObjectFactory newObjectFactory(NumberFactory numberFactory) {
		return new CommonObjectFactory(numberFactory);
	}
}
