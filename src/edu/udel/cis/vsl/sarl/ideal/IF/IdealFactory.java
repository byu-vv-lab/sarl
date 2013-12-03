/*******************************************************************************
 * Copyright (c) 2013 Stephen F. Siegel, University of Delaware.
 * 
 * This file is part of SARL.
 * 
 * SARL is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * SARL is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with SARL. If not, see <http://www.gnu.org/licenses/>.
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
	 * @return symbolic map consisting of one entry (key,value)
	 */
	<K extends NumericExpression, V extends SymbolicExpression> SymbolicMap<K, V> singletonMap(
			K key, V value);

	/**
	 * Create a value 1
	 * 
	 * @return
	 * 			a value 1 of type IntObject
	 */
	IntObject oneIntObject();

	/**
	 * Creates an integer constant
	 * 
	 * @param value - a normal integer value
	 * 
	 * @param type
	 * 				Integer
	 * 
	 * @return
	 * 			an integer of type Constant
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
	 * @param type - different data types of SymbolicType - real, Integer etc.
	 * 
	 * @param type
	 * 				SymbolicType
	 * 
	 * @return
	 * 			a value zero of the specified type
	 */
	Constant zero(SymbolicType type);

	/**
	 * Returns a constant
	 * 
	 * @param number - another form/representation of real number
	 * 
	 * @param type
	 * 				Number
	 * 
	 * @return
	 * 			a constant of type Constant
	 */
	Constant constant(Number number);

	/**
	 * a Monic ONE
	 * 
	* @param type - different data types of SymbolicType - real, Integer etc.
	 * 
	 * @param type
	 * 				SymbolicType
	 * 
	 * @return
	 * 			a value of 1 of type Monic
	 */
	One one(SymbolicType type);

	/**
	 * Creates a Monomial which is a Monic multiplied by a constant, integer or a real number.
	 * 
	 * @param constant - a concrete number. Wraps a NumberObject, which wraps a Number
	 * @param monic - product of powers of primitive expressions x_1^{i_1}*...*x_n^{i_n}, 
	 *                where the x_i are primitives and the i_j are positive concrete integers.
	 * 
	 * @param type
	 * 				Constant and Monic
	 * 
	 * @return
	 * 			a monomial by concatenating a constant of type Constant and a monic of type Monic
	 */
	Monomial monomial(Constant constant, Monic monic);

	/**
	 * Multiplies two polynomials by forming the factorization and by 
	 * factoring out the common factors that are produced from the two factorizations.
	 * 
	 * @param poly1 - Numeric Expression of type Polynomial
	 * @param poly2 - Numeric Expression of type Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			Multiplication of two polynomials of type Polynomial
	 */
	Polynomial multiply(Polynomial poly1, Polynomial poly2);

	/**
	 * The symbolic map from a Monic to a Monomial consisting of one or many entries given its factorization
	 * 
	 * @param termMap - a symbolic map between Monic and Monomial
	 * @param factorization - is of type Monomial
	 * 
	 * @return
	 * 			a polynomial consisting of symbolic map with entries of (monic, monomial) given factorization of type Monomial
	 */
	Polynomial polynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization);

	/**
	 * Adds two polynomials by forming the factorization and by factoring out 
	 * the common factors that are produced from the two factorizations.
	 * 
	 * @param p1 - Numeric Expression of type Polynomial
	 * @param p2 - Numeric Expression of type Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			Addition of two polynomials of type Polynomial
	 */
	
	Polynomial add(Polynomial p1, Polynomial p2);

	/**
	 *  Returns the expression by removing the constant term present in the 
	 * passed argument.
	 * 
	 * @param polynomial - Numeric Expression of type Polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			a polynomial of type Polynomial, without the constant term. If the constant term is passed as an argument, then returns zero
	 */
	Polynomial subtractConstantTerm(Polynomial polynomial);

	/**
	 * Divides each term in a polynomial with a constant of type Constant
	 * 
	 * @param polynomial - Numeric Expression of type Polynomial
	 * @param constant - a concrete number. Wraps a NumberObject, which wraps a Number
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			a polynomial with the common factors, which are common to the constant of type Constant, removed in each term
	 */
	Polynomial divide(Polynomial polynomial, Constant constant);

}
