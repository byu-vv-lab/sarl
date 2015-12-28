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
package edu.udel.cis.vsl.sarl.number.real;

import edu.udel.cis.vsl.sarl.IF.number.Number;

public abstract class RealNumber implements Number {

	/**
	 * Order is: all real integers, followed by all real rationals, followed by
	 * everything else.
	 */
	@Override
	public int compareTo(Number o) {
		if (this instanceof RealInteger) {
			if (o instanceof RealInteger) {
				return ((RealInteger) this).value().compareTo(
						((RealInteger) o).value());
			}
			return -1;
		} else if (this instanceof RealRational) {
			if (o instanceof RealRational) {
				RealRational me = (RealRational) this;
				RealRational you = (RealRational) o;
				int result = me.numerator().compareTo(you.numerator());

				if (result != 0)
					return result;
				result = me.denominator().compareTo(you.denominator());
				return result;
			} else if (o instanceof RealInteger)
				return 1;
			return -1;
		} else {
			throw new RuntimeException("Should not happen");
		}
	}
}
