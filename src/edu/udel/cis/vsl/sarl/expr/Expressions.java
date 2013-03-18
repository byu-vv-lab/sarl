package edu.udel.cis.vsl.sarl.expr;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.cnf.CnfFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.common.CommonNumericExpressionFactory;
import edu.udel.cis.vsl.sarl.herbrand.Herbrand;
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class Expressions {

	public static ExpressionFactory newExpressionFactory(
			NumericExpressionFactory numericFactory) {
		return new CommonExpressionFactory(numericFactory);
	}

	public static BooleanExpressionFactory newCnfFactory(
			SymbolicTypeFactory typeFactory, ObjectFactory objectFactory,
			CollectionFactory collectionFactory) {
		return new CnfFactory(typeFactory, objectFactory, collectionFactory);
	}

	public static ExpressionFactory newIdealExpressionFactory(
			NumberFactory numberFactory, ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory, CollectionFactory collectionFactory) {
		BooleanExpressionFactory booleanFactory = new CnfFactory(typeFactory,
				objectFactory, collectionFactory);
		NumericExpressionFactory numericFactory = Ideal.newIdealFactory(
				numberFactory, objectFactory, typeFactory, collectionFactory,
				booleanFactory);

		return newExpressionFactory(numericFactory);
	}

	public static ExpressionFactory newHerbrandExpressionFactory(
			NumberFactory numberFactory, ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory, CollectionFactory collectionFactory) {
		BooleanExpressionFactory booleanFactory = new CnfFactory(typeFactory,
				objectFactory, collectionFactory);
		NumericExpressionFactory numericFactory = Herbrand.newHerbrandFactory(
				numberFactory, objectFactory, typeFactory, collectionFactory,
				booleanFactory);

		return newExpressionFactory(numericFactory);
	}

	public static ExpressionFactory newStandardExpressionFactory(
			NumberFactory numberFactory, ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory, CollectionFactory collectionFactory) {
		BooleanExpressionFactory booleanFactory = new CnfFactory(typeFactory,
				objectFactory, collectionFactory);
		NumericExpressionFactory idealFactory = Ideal.newIdealFactory(
				numberFactory, objectFactory, typeFactory, collectionFactory,
				booleanFactory);
		NumericExpressionFactory herbrandFactory = Herbrand.newHerbrandFactory(
				numberFactory, objectFactory, typeFactory, collectionFactory,
				booleanFactory);
		NumericExpressionFactory numericFactory = new CommonNumericExpressionFactory(
				idealFactory, herbrandFactory);

		return newExpressionFactory(numericFactory);
	}

	public static SimplifierFactory standardSimplifierFactory(
			ExpressionFactory standardExpressionFactory,
			SymbolicUniverse universe) {
		NumericExpressionFactory numericFactory = standardExpressionFactory
				.numericFactory();
		IdealFactory idealFactory;

		if (numericFactory instanceof IdealFactory)
			idealFactory = (IdealFactory) numericFactory;
		else if (numericFactory instanceof CommonNumericExpressionFactory)
			idealFactory = (IdealFactory) ((CommonNumericExpressionFactory) numericFactory)
					.idealFactory();
		else
			throw new SARLInternalException("Unknown expression factory kind.");

		return Ideal.newIdealSimplifierFactory(idealFactory, universe);
	}
}
