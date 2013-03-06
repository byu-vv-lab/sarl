package edu.udel.cis.vsl.sarl.ideal;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.common.CommonIdealFactory;
import edu.udel.cis.vsl.sarl.ideal.simplify.IdealSimplifierFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class Ideal {

	public static IdealFactory newIdealFactory(NumberFactory numberFactory,
			ObjectFactory objectFactory, SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory) {
		return new CommonIdealFactory(numberFactory, objectFactory,
				typeFactory, collectionFactory);
	}

	public static SimplifierFactory newIdealSimplifierFactory(
			IdealFactory idealFactory, SymbolicUniverse universe) {
		return new IdealSimplifierFactory(idealFactory, universe);
	}

}
