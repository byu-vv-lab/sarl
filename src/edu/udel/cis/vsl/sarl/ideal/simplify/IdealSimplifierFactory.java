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

import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;

public class IdealSimplifierFactory implements SimplifierFactory {

	private SimplifierInfo info;

	public IdealSimplifierFactory(IdealFactory idealFactory,
			PreUniverse universe) {
		info = new SimplifierInfo();
		info.universe = universe;
		info.affineFactory = new AffineFactory(idealFactory);
		info.booleanFactory = idealFactory.booleanFactory();
		info.falseExpr = (BooleanExpression) universe.bool(false);
		info.trueExpr = (BooleanExpression) universe.bool(true);
		info.idealFactory = idealFactory;
		info.numberFactory = universe.numberFactory();
		info.out = System.out;
		info.verbose = false; // true;
	}

	@Override
	public IdealSimplifier newSimplifier(BooleanExpression assumption) {
		return new IdealSimplifier(info, assumption);
	}

}
