package edu.udel.cis.vsl.sarl.universe.IF;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public interface FactorySystem {

	ObjectFactory objectFactory();

	ExpressionFactory expressionFactory();

	CollectionFactory collectionFactory();

	SymbolicTypeFactory typeFactory();

	NumberFactory numberFactory();

	BooleanExpressionFactory booleanFactory();

	NumericExpressionFactory numericFactory();

}
