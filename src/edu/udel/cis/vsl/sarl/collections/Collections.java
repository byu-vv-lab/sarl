package edu.udel.cis.vsl.sarl.collections;

import edu.udel.cis.vsl.sarl.collections.IF.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.common.CommonCollectionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class Collections {
	public static CollectionFactory newCollectionFactory(
			ObjectFactory objectFactory) {
		return new CommonCollectionFactory(objectFactory);
	}
}
