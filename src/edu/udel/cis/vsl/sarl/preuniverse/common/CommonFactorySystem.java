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
package edu.udel.cis.vsl.sarl.preuniverse.common;

import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CommonFactorySystem implements FactorySystem {

	private ObjectFactory objectFactory;

	private ExpressionFactory expressionFactory;

	private CollectionFactory collectionFactory;

	private SymbolicTypeFactory typeFactory;

	public CommonFactorySystem(ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory,
			ExpressionFactory expressionFactory,
			CollectionFactory collectionFactory) {
		this.objectFactory = objectFactory;
		this.typeFactory = typeFactory;
		this.expressionFactory = expressionFactory;
		this.collectionFactory = collectionFactory;

		objectFactory.init();
		typeFactory.init();
		expressionFactory.init();
		collectionFactory.init();
	}

	@Override
	public ObjectFactory objectFactory() {
		return objectFactory;
	}

	@Override
	public SymbolicTypeFactory typeFactory() {
		return typeFactory;
	}

	@Override
	public ExpressionFactory expressionFactory() {
		return expressionFactory;
	}

	@Override
	public CollectionFactory collectionFactory() {
		return collectionFactory;
	}

	@Override
	public NumberFactory numberFactory() {
		return objectFactory.numberFactory();
	}

	@Override
	public NumericExpressionFactory numericFactory() {
		return expressionFactory.numericFactory();
	}

	@Override
	public BooleanExpressionFactory booleanFactory() {
		return expressionFactory.booleanFactory();
	}

}
