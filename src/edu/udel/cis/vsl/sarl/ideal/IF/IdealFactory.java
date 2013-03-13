package edu.udel.cis.vsl.sarl.ideal.IF;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.common.One;

/**
 * An IdealFactory has to provide a few services beyond those guaranteed by an
 * arbitrary NumericExpressionFactory.
 * 
 * @author siegel
 * 
 */
public interface IdealFactory extends NumericExpressionFactory {

	/**
	 * The empty map from K to V.
	 * 
	 * @return the empty map
	 */
	<K extends SymbolicExpression, V extends SymbolicExpression> SymbolicMap<K, V> emptyMap();

	/**
	 * The singleton map from K to V consisting of one entry (key,value).
	 * 
	 * @param key
	 *            an element of K
	 * @param value
	 *            an element of V
	 * @return symbolic map consiting of one entry (key,value)
	 */
	<K extends NumericExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonMap(
			K key, V value);

	IntObject oneIntObject();

	Constant intConstant(int value);

	@Override
	Constant zeroInt();

	@Override
	Constant zeroReal();

	Constant zero(SymbolicType type);

	Constant constant(Number number);

	One one(SymbolicType type);

	Monomial monomial(Constant constant, Monic monic);

	Polynomial multiply(Polynomial poly1, Polynomial poly2);

	Polynomial polynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization);

	Polynomial add(Polynomial p1, Polynomial p2);

	Polynomial subtractConstantTerm(Polynomial polynomial);

	Polynomial divide(Polynomial polynomial, Constant constant);

}
