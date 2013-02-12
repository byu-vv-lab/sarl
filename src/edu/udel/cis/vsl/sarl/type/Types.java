package edu.udel.cis.vsl.sarl.type;

import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.type.common.CommonSymbolicTypeFactory;

public class Types {

	public static SymbolicTypeFactory newTypeFactory(ObjectFactory objectFactory) {
		return new CommonSymbolicTypeFactory(objectFactory);
	}
}
