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
package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class HerbrandTest {

	private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	//private SymbolicType realType, integerType;
	private boolean debug = false;

	@Before
	public void setUp() throws Exception {
		this.universe = Universes.newHerbrandUniverse();
		// this.realType = universe.realType();
		// this.integerType = universe.integerType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test12() {
		NumericExpression one = universe.rational(1);
		NumericExpression two = universe.rational(2);
		NumericExpression a = universe.add(one, two);
		NumericExpression b = universe.add(two, one);

		if (debug) {
			out.println("test12: a = " + a);
			out.println("test12: b = " + b);
		}
		assertFalse(a.equals(b));
	}

}
