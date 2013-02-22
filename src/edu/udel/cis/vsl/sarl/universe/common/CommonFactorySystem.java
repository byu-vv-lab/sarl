package edu.udel.cis.vsl.sarl.universe.common;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.IF.FactorySystem;

public class CommonFactorySystem implements FactorySystem {

	private ObjectFactory objectFactory;

	private ExpressionFactory expressionFactory;

	private CollectionFactory collectionFactory;

	private SymbolicTypeFactory typeFactory;

	public CommonFactorySystem(ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory,
			ExpressionFactory expressionFactory,
			CollectionFactory collectionFactory) {
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.expressionFactory = expressionFactory;
		this.collectionFactory = collectionFactory;

		objectFactory.setCollectionComparator(collectionFactory.comparator());
		objectFactory.setExpressionComparator(expressionFactory.comparator());
		objectFactory.setTypeComparator(typeFactory.typeComparator());
		objectFactory.setTypeSequenceComparator(typeFactory
				.typeSequenceComparator());
		typeFactory.setExpressionComparator(expressionFactory.comparator());
		expressionFactory.setObjectComparator(objectFactory.comparator());
		collectionFactory.setElementComparator(expressionFactory.comparator());
		objectFactory.init();
		typeFactory.init();
		expressionFactory.init();
		collectionFactory.init();
	}

	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	@Override
	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	@Override
	public ExpressionFactory expressionFactory() {
		return expressionFactory;
	}

	@Override
	public CollectionFactory collectionFactory() {
		return collectionFactory;
	}

	@Override
	public NumberFactory numberFactory() {
		return objectFactory.numberFactory();
	}

	@Override
	public NumericExpressionFactory numericFactory() {
		return expressionFactory.numericFactory();
	}

}
