/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with SARL. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package edu.udel.cis.vsl.sarl.ideal.IF;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.common.One;

/**
 * <p>
 * An {@link IdealFactory} provides a few services beyond those guaranteed by an
 * arbitrary {@link NumericExpressionFactory}.
 * </p>
 * 
 * The ideal factory produces and manipulates the following kinds of numeric
 * expressions:
 * 
 * <p>
 * A {@link Constant} represents a concrete value. Each constant has either
 * integer or real type.
 * </p>
 * 
 * <p>
 * A {@link Primitive} expression is one which is not concrete and cannot be
 * expressed as a sum or product or quotient of other expressions. Examples:
 * symbolic constants, array read expressions of integer or real type, tuple
 * read expressions of integer or real types, function applications for
 * functions returning integer or real.
 * </p>
 * 
 * <p>
 * Any value which is the result of raising a primitive expression to a concrete
 * positive integer power is an instance of {@link PrimitivePower}. Any
 * {@link Primitive} is also a {@link PrimitivePower} by taking the exponent to
 * be 1.
 * </p>
 * 
 * <p>
 * A {@link Monic} is the product of {@link PrimitivePower}s. Any
 * {@link PrimitivePower} is also a {@link Monic}: it is the product of a single
 * primitive-power. The {@link Constant} 1 (integer or real) is also a
 * {@link Monic}: it is the empty product. The integer and real 1s are the only
 * constants which are also {@link Monic}s.
 * </p>
 * 
 * <p>
 * A {@link Monomial} is the product of a {@link Constant} and a {@link Monic}.
 * Any {@link Constant} is also a {@link Monomial} by taking 1 for the monic.
 * Any {@link Monic} is also a {@link Monomial} by taking 1 for the constant.
 * </p>
 * 
 * <p>
 * A {@link Polynomial} is the sum of {@link Monomial}s. Any {@link Monomial} is
 * a {@link Polynomial} (a sum with one term).
 * </p>
 * 
 * <p>
 * A {@link RationalExpression} is the quotient of two {@link Polynomial}s. Any
 * {@link Polynomial} is also a {@link RationalExpression} by taking the
 * denominator to be the (polynomial) 1. Any {@link RationalExpression} of
 * integer type is also a {@link Polynomial}. (The result of integer division of
 * two integer polynomials may be a {@link Primitive} expression with operator
 * {@link SymbolicOperator#INT_DIVIDE}.)
 * </p>
 * 
 * A relational numeric expression will always be in one of the following forms:
 * 
 * <ul>
 * <li><code>0&lt;p</code></li>
 * <li><code>0&le;p</code></li>
 * <li><code>0=p</code></li>
 * <li><code>0&ne;p</code></li>
 * </ul>
 * 
 * where <code>p</code> is a {@link Polynomial}.
 * 
 * @author siegel
 * 
 */
public interface IdealFactory extends NumericExpressionFactory {

	/**
	 * The empty map from K to V, i.e., the map containing no entries.
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
	 * @return symbolic map consisting of one entry (key,value)
	 */
	<K extends NumericExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonMap(
			K key, V value);

	/**
	 * Returns an int-object wrapping the int 1.
	 * 
	 * @return a value 1 of type IntObject
	 */
	IntObject oneIntObject();

	/**
	 * Creates an integer constant
	 * 
	 * @param value
	 *            - a normal integer value
	 * 
	 * @return an integer of type Constant
	 */
	Constant intConstant(int value);

	@Override
	/**
	 * Creates a zero integer constant
	 * 
	 * @return
	 * 			a zero integer of type Constant
	 */
	Constant zeroInt();

	@Override
	/**
	 * Creates a real zero constant
	 * 
	 * @return
	 * 			a real zero of type Constant
	 */
	Constant zeroReal();

	/**
	 * Creates a value zero for the type that is passed as an argument
	 * 
	 * @param type
	 *            - different data types of SymbolicType - real, Integer etc.
	 * 
	 * @return a value zero of the specified type
	 */
	Constant zero(SymbolicType type);

	/**
	 * Returns a constant
	 * 
	 * @param number
	 *            - another form/representation of real number
	 * 
	 * @return a constant of type Constant
	 */
	Constant constant(Number number);

	/**
	 * a Monic ONE
	 * 
	 * @param type
	 *            - different data types of SymbolicType - real, Integer etc.
	 * 
	 * @return a value of 1 of type Monic
	 */
	One one(SymbolicType type);

	/**
	 * Creates a Monomial which is a Monic multiplied by a constant, integer or
	 * a real number.
	 * 
	 * @param constant
	 *            - a concrete number. Wraps a NumberObject, which wraps a
	 *            Number
	 * @param monic
	 *            - product of powers of primitive expressions
	 *            x_1^{i_1}*...*x_n^{i_n}, where the x_i are primitives and the
	 *            i_j are positive concrete integers.
	 * 
	 * @return a monomial by concatenating a constant of type Constant and a
	 *         monic of type Monic
	 */
	Monomial monomial(Constant constant, Monic monic);

	/**
	 * Multiplies two polynomials by forming the factorization and by factoring
	 * out the common factors that are produced from the two factorizations.
	 * 
	 * @param poly1
	 *            - Numeric Expression of type Polynomial
	 * @param poly2
	 *            - Numeric Expression of type Polynomial
	 * 
	 * @return Multiplication of two polynomials of type Polynomial
	 */
	Polynomial multiply(Polynomial poly1, Polynomial poly2);

	/**
	 * The symbolic map from a Monic to a Monomial consisting of one or many
	 * entries given its factorization
	 * 
	 * @param termMap
	 *            - a symbolic map between Monic and Monomial
	 * @param factorization
	 *            - is of type Monomial
	 * 
	 * @return a polynomial consisting of symbolic map with entries of (monic,
	 *         monomial) given factorization of type Monomial
	 */
	Polynomial polynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization);

	/**
	 * Adds two polynomials by forming the factorization and by factoring out
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1
	 *            - Numeric Expression of type Polynomial
	 * @param p2
	 *            - Numeric Expression of type Polynomial
	 * 
	 * @return Addition of two polynomials of type Polynomial
	 */
	Polynomial add(Polynomial p1, Polynomial p2);

	/**
	 * Given a polynomial p with constant term c, returns p-c.
	 * 
	 * @param polynomial
	 *            a non-<code>null</code> instance of {@link Polynomial}
	 * 
	 * @return result of subtracting the constant term from the polynomial
	 */
	Polynomial subtractConstantTerm(Polynomial polynomial);

	/**
	 * Divides each term in a polynomial with by a constant. The polynomial and
	 * constant must have the same type. If the type is integer, this will
	 * perform integer division on each term; this is only equivalent to integer
	 * division of the polynomial by the constant if the constant divides each
	 * term.
	 * 
	 * 
	 * @param polynomial
	 *            a non-<code>null</code> instance of {@link Polynomial}
	 * @param constant
	 *            a concrete number of the same type as the
	 *            <code>polynomial</code>
	 * 
	 * @return the polynomial that results from dividing the given polynomial by
	 *         the given constant
	 */
	Polynomial divide(Polynomial polynomial, Constant constant);

	/**
	 * Given a numeric expression e of integer or real type, returns a (possibly
	 * simpler) polynomial p of the same type which has the property that e=0 if
	 * and only if p=0. Example: e=x^5, p=x.
	 * 
	 * @param expr
	 *            a non-<code>null</code> numeric expression
	 * @return a polynomial of same type which is zero iff <code>expr</code> is
	 */
	Polynomial zeroEssence(NumericExpression expr);

}
