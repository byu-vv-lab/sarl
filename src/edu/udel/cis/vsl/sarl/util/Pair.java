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
package edu.udel.cis.vsl.sarl.util;

public class Pair<S, T> {

	public S left;

	public T right;

	public Pair(S left, T right) {
		assert left != null;
		assert right != null;
		this.left = left;
		this.right = right;
	}

	public int hashCode() {
		return left.hashCode() + right.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof Pair<?, ?>) {
			return left.equals(((Pair<?, ?>) object).left)
					&& right.equals(((Pair<?, ?>) object).right);
		} else {
			return false;
		}
	}

}
