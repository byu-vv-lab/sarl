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
package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;
import edu.udel.cis.vsl.sarl.ideal.IF.RationalExpression;

/**
 * A nontrivial rational expression. It consists of a numerator and denominator,
 * both factored polynomials.
 * 
 * @author siegel
 * 
 */
public class NTRationalExpression extends IdealExpression implements
		RationalExpression {

	protected NTRationalExpression(Polynomial numerator, Polynomial denominator) {
		super(SymbolicOperator.DIVIDE, numerator.type(), numerator, denominator);
		assert !denominator.isOne();
		assert !denominator.isZero();
		assert !numerator.isZero();
		assert !numerator.equals(denominator);
	}

	public Polynomial numerator(IdealFactory factory) {
		return (Polynomial) argument(0);
	}

	public Polynomial numerator() {
		return (Polynomial) argument(0);
	}

	public Polynomial denominator(IdealFactory factory) {
		return (Polynomial) argument(1);
	}

	public Polynomial denominator() {
		return (Polynomial) argument(1);
	}

	@Override
	public IdealKind idealKind() {
		return IdealKind.NTRationalExpression;
	}

}
