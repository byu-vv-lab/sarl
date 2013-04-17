package edu.udel.cis.vsl.sarl.preuniverse;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.Expressions;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonFactorySystem;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class PreUniverses {
	public static FactorySystem newFactorySystem(ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory,
			ExpressionFactory expressionFactory,
			CollectionFactory collectionFactory) {
		return new CommonFactorySystem(objectFactory, typeFactory,
				expressionFactory, collectionFactory);
	}

	public static FactorySystem newIdealFactorySystem() {
		NumberFactory numberFactory = Numbers.REAL_FACTORY;
		ObjectFactory objectFactory = Objects.newObjectFactory(numberFactory);
		SymbolicTypeFactory typeFactory = Types.newTypeFactory(objectFactory);
		CollectionFactory collectionFactory = Collections
				.newCollectionFactory(objectFactory);
		ExpressionFactory expressionFactory = Expressions
				.newIdealExpressionFactory(numberFactory, objectFactory,
						typeFactory, collectionFactory);

		return newFactorySystem(objectFactory, typeFactory, expressionFactory,
				collectionFactory);
	}

	public static FactorySystem newHerbrandFactorySystem() {
		NumberFactory numberFactory = Numbers.REAL_FACTORY;
		ObjectFactory objectFactory = Objects.newObjectFactory(numberFactory);
		SymbolicTypeFactory typeFactory = Types.newTypeFactory(objectFactory);
		CollectionFactory collectionFactory = Collections
				.newCollectionFactory(objectFactory);
		ExpressionFactory expressionFactory = Expressions
				.newHerbrandExpressionFactory(numberFactory, objectFactory,
						typeFactory, collectionFactory);

		return newFactorySystem(objectFactory, typeFactory, expressionFactory,
				collectionFactory);
	}

}
