package edu.udel.cis.vsl.sarl.preuniverse.IF;

import java.util.Collection;

import edu.udel.cis.vsl.sarl.IF.CoreUniverse;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.CharObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;

/**
 * A {@link PreUniverse} provides most of the services of a
 * {@link SymbolicUniverse}, but not those that require reasoning, specifically
 * theorem proving and simplification.
 * 
 * The functionality of a symbolic universe is partitioned in this way to
 * provide a hierarchical design to SARL. The simplification/proving modules
 * require the basic symbolic algebra services provided by a {@link PreUniverse}
 * . A full {@link SymbolicUniverse} requires the simplification and proving
 * modules. A hierarchy in the USES relation is achieved
 * 
 * @author siegel
 *
 */
public interface PreUniverse extends CoreUniverse {

	// ************************************************************************
	// * Methods NOT also in SymbolicUniverse
	// ************************************************************************

	/**
	 * Returns the charObject wrapping the given char value. These are SARL
	 * char, not Java char.
	 * 
	 * @param value
	 *            a SARL char
	 * @return the CharObject wrapping that char
	 */
	CharObject charObject(char value);

	void incrementValidCount();

	void incrementProverValidCount();

	/**
	 * Given an iterable collection of SymbolicTypes, returns a
	 * SymbolicTypeSequence conatining those SymbolicTypes
	 * 
	 * @param SymbolicType
	 *            - types
	 * @return SymbolicTypeSequence of SymbolicType - types
	 */
	SymbolicTypeSequence typeSequence(Iterable<? extends SymbolicType> types);

	<T extends SymbolicExpression> SymbolicCollection<T> basicCollection(
			Collection<T> javaCollection);

	/**
	 * Changes the names of the bound variables in the expression so that every
	 * bound variable has a unique name. The names will be unique among ALL
	 * bound variables ever encountered by this method in this preuniverse.
	 * 
	 * @param expr
	 *            a symbolic expressions
	 * @return a symbolic expression equivalent to expr but with the names of
	 *         the bound variables possibly changed to be unique
	 */
	SymbolicExpression cleanBoundVariables(SymbolicExpression expr);

}
