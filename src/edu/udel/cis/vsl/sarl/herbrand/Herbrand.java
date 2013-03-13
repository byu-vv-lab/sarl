package edu.udel.cis.vsl.sarl.herbrand;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.herbrand.common.CommonHerbrandFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class Herbrand {

	public static NumericExpressionFactory newHerbrandFactory(
			NumberFactory numberFactory, ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory,
			CollectionFactory collectionFactory,
			BooleanExpressionFactory booleanFactory) {
		return new CommonHerbrandFactory(numberFactory, objectFactory,
				typeFactory, collectionFactory, booleanFactory);
	}

}
