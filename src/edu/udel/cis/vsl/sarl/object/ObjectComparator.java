package edu.udel.cis.vsl.sarl.object;

import java.util.Comparator;

import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.collections.SymbolicCollection;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpressionIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberIF;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject.SymbolicObjectKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeIF;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequenceIF;
import edu.udel.cis.vsl.sarl.collections.CollectionComparator;
import edu.udel.cis.vsl.sarl.symbolic.ExpressionComparator;
import edu.udel.cis.vsl.sarl.type.TypeComparator;
import edu.udel.cis.vsl.sarl.type.TypeSequenceComparator;

public class ObjectComparator implements Comparator<SymbolicObject> {

	private ExpressionComparator expressionComparator;

	private CollectionComparator collectionComparator;

	private TypeComparator typeComparator;

	private TypeSequenceComparator typeSequenceComparator;

	public ObjectComparator(ExpressionComparator expressionComparator,
			CollectionComparator collectionComparator,
			TypeComparator typeComparator,
			TypeSequenceComparator typeSequenceComparator) {
		this.expressionComparator = expressionComparator;
		this.collectionComparator = collectionComparator;
		this.typeComparator = typeComparator;
		this.typeSequenceComparator = typeSequenceComparator;
		// expressionComparator.setNumericComparator(c)
		expressionComparator.setObjectComparator(this);
		expressionComparator.setTypeComparator(typeComparator);
		collectionComparator.setExpressionComparator(expressionComparator);
		typeComparator.setExpressionComparator(expressionComparator);
		typeComparator.setObjectComparator(this);
		typeComparator.setTypeSequenceComparator(typeSequenceComparator);
		typeSequenceComparator.setTypeComparator(typeComparator);
	}

	public Comparator<SymbolicExpressionIF> expressionComparator() {
		return expressionComparator;
	}

	public Comparator<SymbolicCollection> collectionComparator() {
		return collectionComparator;
	}

	public Comparator<SymbolicTypeIF> typeComparator() {
		return typeComparator;
	}

	public Comparator<SymbolicTypeSequenceIF> typeSequenceComparator() {
		return typeSequenceComparator;
	}

	@Override
	public int compare(SymbolicObject o1, SymbolicObject o2) {
		SymbolicObjectKind kind = o1.symbolicObjectKind();
		int result = kind.compareTo(o2.symbolicObjectKind());

		if (result != 0)
			return result;

		switch (kind) {
		case EXPRESSION:
			return expressionComparator.compare((SymbolicExpressionIF) o1,
					(SymbolicExpressionIF) o2);
		case EXPRESSION_COLLECTION:
			return collectionComparator.compare((SymbolicCollection) o1,
					(SymbolicCollection) o2);
		case TYPE:
			return typeComparator.compare((SymbolicTypeIF) o1,
					(SymbolicTypeIF) o2);
		case TYPE_SEQUENCE:
			return typeSequenceComparator.compare((SymbolicTypeSequenceIF) o1,
					(SymbolicTypeSequenceIF) o2);
		case BOOLEAN:
			return ((BooleanObject) o1).getBoolean() ? (((BooleanObject) o2)
					.getBoolean() ? 0 : 1)
					: (((BooleanObject) o2).getBoolean() ? -1 : 0);
		case INT:
			return ((IntObject) o1).getInt() - ((IntObject) o2).getInt();
		case NUMBER:
			return ((NumberIF) o1).compareTo((NumberIF) o2);
		case STRING:
			return ((StringObject) o1).getString().compareTo(
					((StringObject) o2).getString());
		default:
			throw new SARLInternalException("unreachable");
		}
	}
}
