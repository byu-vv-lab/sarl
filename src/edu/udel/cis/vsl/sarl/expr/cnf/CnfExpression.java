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
package edu.udel.cis.vsl.sarl.expr.cnf;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSet;
import edu.udel.cis.vsl.sarl.expr.common.CommonSymbolicExpression;

public class CnfExpression extends CommonSymbolicExpression implements
		BooleanExpression {

	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			Collection<SymbolicObject> args) {
		super(kind, type, args);
		assert type.isBoolean();
	}

	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject[] args) {
		super(kind, type, args);
		assert type.isBoolean();
	}

	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg) {
		super(kind, type, arg);
		assert type.isBoolean();
	}

	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1) {
		super(kind, type, arg0, arg1);
		assert type.isBoolean();
	}

	protected CnfExpression(SymbolicOperator kind, SymbolicType type,
			SymbolicObject arg0, SymbolicObject arg1, SymbolicObject arg2) {
		super(kind, type, arg0, arg1, arg2);
		assert type.isBoolean();
	}

	@Override
	public BooleanExpression booleanArg(int i) {
		return (BooleanExpression) argument(i);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SymbolicCollection<BooleanExpression> booleanCollectionArg(int i) {
		return (SymbolicCollection<BooleanExpression>) argument(i);
	}

	/**
	 * Returns the i-th argument of this expression in the case where the i-th
	 * argument should be an instance of SymbolicSet<BooleanExpression>. A
	 * SARLException is thrown if that argument is not an instance of that type,
	 * or if i is out of range.
	 * 
	 * @param i
	 *            integer in range [0,numArgs-1]
	 * @return the i-th argument of this expression
	 */
	@SuppressWarnings("unchecked")
	public SymbolicSet<BooleanExpression> booleanSetArg(int i) {
		return (SymbolicSet<BooleanExpression>) argument(i);
	}

}
