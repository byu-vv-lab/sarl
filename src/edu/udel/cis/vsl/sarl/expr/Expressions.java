package edu.udel.cis.vsl.sarl.expr;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.ideal.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class Expressions {

	public static ExpressionFactory newExpressionFactory(
			NumericExpressionFactory numericFactory) {
		return new CommonExpressionFactory(numericFactory);
	}

	public static NumericExpressionFactory newIdealFactory(
			NumberFactoryIF numberFactory, ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory, CollectionFactory collectionFactory) {
		return new IdealFactory(numberFactory, objectFactory, typeFactory,
				collectionFactory);
	}

	public static ExpressionFactory newIdealExpressionFactory(
			NumberFactoryIF numberFactory, ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory, CollectionFactory collectionFactory) {
		return newExpressionFactory(newIdealFactory(numberFactory,
				objectFactory, typeFactory, collectionFactory));
	}

}
