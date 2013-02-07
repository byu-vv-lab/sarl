package edu.udel.cis.vsl.sarl;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverseIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.collections.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.CommonCollectionFactory;
import edu.udel.cis.vsl.sarl.ideal.IdealFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;
import edu.udel.cis.vsl.sarl.symbolic.CommonSymbolicUniverse;
import edu.udel.cis.vsl.sarl.symbolic.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class SARL {

	public static SymbolicUniverseIF newIdealUniverse() {
		NumberFactoryIF numberFactory = Numbers.REAL_FACTORY;
		ObjectFactory objectFactory = new ObjectFactory(numberFactory);
		SymbolicTypeFactory typeFactory = new SymbolicTypeFactory(objectFactory);
		CollectionFactory collectionFactory = new CommonCollectionFactory(
				objectFactory);
		NumericExpressionFactory numericExpressionFactory = new IdealFactory(
				numberFactory, objectFactory, typeFactory, collectionFactory);

		return new CommonSymbolicUniverse(numericExpressionFactory);
	}

}
