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
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.common.One;

/**
 * An {@link IdealFactory} provides a few services beyond those guaranteed by an
 * arbitrary {@link NumericExpressionFactory}.
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
	 * @param type
	 *            Integer
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
	 * @param type
	 *            SymbolicType
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
	 * @param type
	 *            Number
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
	 * @param type
	 *            SymbolicType
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
	 * @param type
	 *            Constant and Monic
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
	 * @param type
	 *            Polynomial
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
	 * @param type
	 *            Polynomial
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
