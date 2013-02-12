package edu.udel.cis.vsl.sarl.expr.IF;

import java.util.Collection;
import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstantIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public interface ExpressionFactory {

	void setObjectComparator(Comparator<SymbolicObject> c);

	void init();

	NumericExpressionFactory numericFactory();

	ObjectFactory objectFactory();

	SymbolicExpressionIF canonic(SymbolicExpressionIF expression);

	Comparator<SymbolicExpressionIF> comparator();

	SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject[] arguments);

	SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0);

	SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1);

	SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, SymbolicObject arg0, SymbolicObject arg1,
			SymbolicObject arg2);

	SymbolicExpressionIF expression(SymbolicOperator operator,
			SymbolicTypeIF type, Collection<SymbolicObject> args);

	SymbolicConstantIF symbolicConstant(StringObject name, SymbolicTypeIF type);
}
