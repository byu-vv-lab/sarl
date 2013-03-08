package edu.udel.cis.vsl.sarl.ideal.IF;

import edu.udel.cis.vsl.sarl.IF.collections.SymbolicMap;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.common.One;

public interface IdealFactory extends NumericExpressionFactory {

	BooleanExpressionFactory booleanFactory();

	Constant intConstant(int value);

	@Override
	Constant zeroInt();

	@Override
	Constant zeroReal();

	Constant zero(SymbolicType type);

	One one(SymbolicType type);

	Polynomial multiply(Polynomial poly1, Polynomial poly2);

	Polynomial polynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization);

	<K extends NumericExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonMap(
			K key, V value);

	IntObject oneIntObject();

	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptyMap();

	Constant constant(Number number);

	Monomial monomial(Constant constant, Monic monic);

	Polynomial add(Polynomial p1, Polynomial p2);

	Polynomial subtractConstantTerm(Polynomial polynomial);

	Polynomial divide(Polynomial polynomial, Constant constant);

}
