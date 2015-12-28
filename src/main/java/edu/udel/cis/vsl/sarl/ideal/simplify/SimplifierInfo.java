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
package edu.udel.cis.vsl.sarl.ideal.simplify;

import java.io.PrintStream;

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * An object that gathers together a variety of objects as fields needed to
 * perform simplification.
 * 
 * @author siegel
 * 
 */
public class SimplifierInfo {

	/**
	 * Treat every polynomial as a linear combination of monomials, so Gaussian
	 * elimination is performed on all equalities, and not just degree 1
	 * equalities.
	 */
	public boolean linearizePolynomials = true;

	/**
	 * Returns the symbolic universe.
	 */
	public PreUniverse universe;

	public IdealFactory idealFactory;

	public NumberFactory numberFactory;

	public BooleanExpressionFactory booleanFactory;

	public AffineFactory affineFactory;

	public PrintStream out;

	public boolean verbose;

	public BooleanExpression trueExpr, falseExpr;
}
