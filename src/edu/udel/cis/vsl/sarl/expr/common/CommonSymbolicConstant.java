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
package edu.udel.cis.vsl.sarl.expr.common;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

/**
 * A "symbolic constant" is a symbol used in symbolic execution to represent an
 * input value. It is "constant" in the sense that its value does not change in
 * the course of an execution of the program. A symbolic constant is determined
 * by two things: a name (which is a String), and a type (instance of
 * SymbolicTypeIF). Two distinct symbolic constants may have the same name, but
 * different types. Two symbolic constants are considered equal iff their names
 * are equal and their types are equal. Symbolic constants are symbolic
 * expressions and are therefore immutable.
 */
public class CommonSymbolicConstant extends CommonSymbolicExpression implements
		SymbolicConstant {

	/**
	 * Constructs new {@link CommonSymbolicConstant} with given
	 * <code>name</code> and <code>type</code>.
	 * 
	 * @param name
	 *            a non-<code>null</code> string object
	 * @param type
	 *            a non-<code>null</code> type
	 */
	public CommonSymbolicConstant(StringObject name, SymbolicType type) {
		super(SymbolicOperator.SYMBOLIC_CONSTANT, type, name);
	}

	@Override
	public StringObject name() {
		return (StringObject) argument(0);
	}

}
