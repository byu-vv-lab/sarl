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

	IntObject oneIntObject();

	Constant intConstant(int value);

	@Override
	Constant zeroInt();

	@Override
	Constant zeroReal();

	Constant zero(SymbolicType type);

	/**
	 * Returns a constant
	 * 
	 * @param number
	 * 
	 * @param type
	 * 				Number
	 * 
	 * @return
	 * 			a constant of type Constant
	 */
	Constant constant(Number number);

	One one(SymbolicType type);

	/**
	 * Creates a Monomial
	 * 
	 * @param constant
	 * @param monic
	 * 
	 * @param type
	 * 				Constant and Monic
	 * 
	 * @return
	 * 			a monomial by concatenating a constant of type Constant and a monic of type Monic
	 */
	Monomial monomial(Constant constant, Monic monic);

	/**
	 * Multiplies two polynomials
	 * 
	 * @param poly1
	 * @param poly2
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			Multiplication of two polynomials of type Polynomial
	 */
	Polynomial multiply(Polynomial poly1, Polynomial poly2);

	Polynomial polynomial(SymbolicMap<Monic, Monomial> termMap,
			Monomial factorization);

	/**
	 * Adds two polynomials
	 * 
	 * @param p1
	 * @param p2
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			Addition of two polynomials of type Polynomial
	 */
	
	Polynomial add(Polynomial p1, Polynomial p2);

	/**
	 * Subtracts the constant term for the given polynomial
	 * 
	 * @param polynomial
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			a polynomial of type Polynomial, without the constant term. If the constant term is passed as an argument, then returns zero
	 */
	Polynomial subtractConstantTerm(Polynomial polynomial);

	/**
	 * Divides the polynomial with a constant of type Constant
	 * 
	 * @param polynomial
	 * @param constant
	 * 
	 * @param type
	 * 				Polynomial
	 * 
	 * Returns
	 * 			a polynomial with the common factors, which are common to the constant of type Constant, removed
	 */
	Polynomial divide(Polynomial polynomial, Constant constant);

}
