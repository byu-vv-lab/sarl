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
import edu.udel.cis.vsl.sarl.simplify.Simplify;
import edu.udel.cis.vsl.sarl.simplify.IF.SimplifierFactory;
import edu.udel.cis.vsl.sarl.type.Types;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;
import edu.udel.cis.vsl.sarl.universe.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.universe.common.CommonFactorySystem;
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

		universe.setSimplifierFactory(simplifierFactory);
		return universe;
	}

	public static SymbolicUniverse newHerbrandUniverse() {
		FactorySystem system = newHerbrandFactorySystem();
		CommonSymbolicUniverse universe = new CommonSymbolicUniverse(system);
		SimplifierFactory simplifierFactory = Simplify
				.newIdentitySimplifierFactory(universe);

		universe.setSimplifierFactory(simplifierFactory);
		return universe;
	}
	
	public static SymbolicUniverse newStandardUniverse() {
		// TODO
		// need a simplifier for Herbrand: even if PLUS
		// is uninterpreted, if x=y the PLUS(x,z)=PLUS(y,z).
		
		// translate Herbrand operations as functions.
		// functions type is herbrandRealxherbrandReal->herbrandReal,
		// ditto for integer.
		// need to cast any other numeric expression to herbrand
		// before applying.
		// LTE(x,y), LT(x,y).  But  x==y means sub can take place?
		// not necessarily?
		// then use IdealSimplifier as usual
		return null;
	}

}
