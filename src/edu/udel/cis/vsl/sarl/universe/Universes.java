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
package edu.udel.cis.vsl.sarl.universe;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.collections.Collections;
import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.expr.Expressions;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.ideal.Ideal;
import edu.udel.cis.vsl.sarl.ideal.IF.IdealFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.Objects;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.common.CommonFactorySystem;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.reason.Reason;
import edu.udel.cis.vsl.sarl.reason.IF.ReasonerFactory;
import edu.udel.cis.vsl.sarl.simplify.Simplify;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.common.CommonSymbolicUniverse;

public class Universes {

	public static FactorySystem newFactorySystem(ObjectFactory objectFactory,
			SymbolicTypeFactory typeFactory,
			ExpressionFactory expressionFactory,
			CollectionFactory collectionFactory) {
		return new CommonFactorySystem(objectFactory, typeFactory,
				expressionFactory, collectionFactory);
	}

	public static FactorySystem newIdealFactorySystem() {
		NumberFactory numberFactory = Numbers.REAL_FACTORY;
		ObjectFactory objectFactory = Objects.newObjectFactory(numberFactory);
		SymbolicTypeFactory typeFactory = Types.newTypeFactory(objectFactory);
		CollectionFactory collectionFactory = Collections
				.newCollectionFactory(objectFactory);
		ExpressionFactory expressionFactory = Expressions
				.newIdealExpressionFactory(numberFactory, objectFactory,
						typeFactory, collectionFactory);

		return newFactorySystem(objectFactory, typeFactory, expressionFactory,
				collectionFactory);
	}

	public static FactorySystem newHerbrandFactorySystem() {
		NumberFactory numberFactory = Numbers.REAL_FACTORY;
		ObjectFactory objectFactory = Objects.newObjectFactory(numberFactory);
		SymbolicTypeFactory typeFactory = Types.newTypeFactory(objectFactory);
		CollectionFactory collectionFactory = Collections
				.newCollectionFactory(objectFactory);
		ExpressionFactory expressionFactory = Expressions
				.newHerbrandExpressionFactory(numberFactory, objectFactory,
						typeFactory, collectionFactory);

		return newFactorySystem(objectFactory, typeFactory, expressionFactory,
				collectionFactory);
	}

	public static SymbolicUniverse newIdealUniverse() {
		FactorySystem system = newIdealFactorySystem();
		CommonSymbolicUniverse universe = new CommonSymbolicUniverse(system);
		SimplifierFactory simplifierFactory = Ideal.newIdealSimplifierFactory(
				(IdealFactory) system.numericFactory(), universe);
		TheoremProverFactory proverFactory = Prove
				.newCVC3TheoremProverFactory(universe);
		ReasonerFactory reasonerFactory = Reason.newReasonerFactory(
				simplifierFactory, proverFactory);

		universe.setReasonerFactory(reasonerFactory);
		return universe;
	}

	public static SymbolicUniverse newHerbrandUniverse() {
		FactorySystem system = newHerbrandFactorySystem();
		CommonSymbolicUniverse universe = new CommonSymbolicUniverse(system);
		SimplifierFactory simplifierFactory = Simplify
				.newIdentitySimplifierFactory(universe);
		TheoremProverFactory proverFactory = Prove
				.newCVC3TheoremProverFactory(universe);
		ReasonerFactory reasonerFactory = Reason.newReasonerFactory(
				simplifierFactory, proverFactory);

		universe.setReasonerFactory(reasonerFactory);
		return universe;
	}

	public static SymbolicUniverse newStandardUniverse() {
		NumberFactory numberFactory = Numbers.REAL_FACTORY;
		ObjectFactory objectFactory = Objects.newObjectFactory(numberFactory);
		SymbolicTypeFactory typeFactory = Types.newTypeFactory(objectFactory);
		CollectionFactory collectionFactory = Collections
				.newCollectionFactory(objectFactory);
		ExpressionFactory expressionFactory = Expressions
				.newStandardExpressionFactory(numberFactory, objectFactory,
						typeFactory, collectionFactory);
		FactorySystem system = newFactorySystem(objectFactory, typeFactory,
				expressionFactory, collectionFactory);
		CommonSymbolicUniverse universe = new CommonSymbolicUniverse(system);
		SimplifierFactory simplifierFactory = Expressions
				.standardSimplifierFactory(expressionFactory, universe);
		TheoremProverFactory proverFactory = Prove
				.newCVC3TheoremProverFactory(universe);
		ReasonerFactory reasonerFactory = Reason.newReasonerFactory(
				simplifierFactory, proverFactory);

		universe.setReasonerFactory(reasonerFactory);
		return universe;
	}

}
