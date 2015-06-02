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
package edu.udel.cis.vsl.sarl.ideal.common;

import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.object.NumberObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.ideal.IF.Constant;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.Monic;
import edu.udel.cis.vsl.sarl.ideal.IF.Monomial;
import edu.udel.cis.vsl.sarl.ideal.IF.Polynomial;

/**
 * A constant which is not 1.
 * 
 * @author siegel
 * 
 */
public class NTConstant extends IdealExpression implements Constant {

	protected NTConstant(SymbolicType type, NumberObject value) {
		super(SymbolicOperator.CONCRETE, type, value);
		assert !value.isOne();
	}

	public IdealKind idealKind() {
		return IdealKind.Constant;
	}

	public NumberObject value() {
		return (NumberObject) argument(0);
	}

	public Number number() {
		return value().getNumber();
	}

	public boolean isZero() {
		return value().isZero();
	}

	public boolean isOne() {
		return false;
	}

	@Override
	public SymbolicMap<Monic, Monomial> termMap(IdealFactory factory) {
		return factory.singletonMap((Monic) factory.one(type()),
				(Monomial) this);
	}

	@Override
	public Constant monomialConstant(IdealFactory factory) {
		return this;
	}

	@Override
	public Monic monic(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monomial factorization(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial numerator(IdealFactory factory) {
		return this;
	}

	@Override
	public Polynomial denominator(IdealFactory factory) {
		return factory.one(type());
	}

	@Override
	public Monomial leadingTerm() {
		return this;
	}

	@Override
	public Polynomial expand(IdealFactory factory) {
		return this;
	}

	@Override
	public String toString() {
		return number().toString();
	}

	@Override
	public int degree() {
		return isZero() ? -1 : 0;
	}

	@Override
	public Constant constantTerm(IdealFactory factory) {
		return this;
	}

	/**
	 * If this constant is committed, returns a new {@link NTConstant} wrapping
	 * <code>value</code>, else changes the underlying value of this constant to
	 * <code>value</code>.
	 * 
	 * @param value
	 *            a non-<code>null</code> number object with same type as the
	 *            type of this constant
	 * @return a constant wrapping <code>value</code>: either this one or a new
	 *         one
	 */
	Constant setValue(NumberObject value) {
		if (isImmutable()) {
			return new NTConstant(this.type(), value);
		} else {
			this.setArgument(0, value);
			return this;
		}
	}

}
