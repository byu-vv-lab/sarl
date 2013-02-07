package edu.udel.cis.vsl.sarl.ideal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.number.IntegerNumberIF;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactoryIF;
import edu.udel.cis.vsl.sarl.collections.CollectionFactory;
import edu.udel.cis.vsl.sarl.collections.CommonCollectionFactory;
import edu.udel.cis.vsl.sarl.number.Numbers;
import edu.udel.cis.vsl.sarl.object.ObjectFactory;
import edu.udel.cis.vsl.sarl.type.SymbolicTypeFactory;

public class IdealTest {

	private NumberFactoryIF numberFactory;
	private ObjectFactory objectFactory;
	private SymbolicTypeFactory typeFactory;
	private CollectionFactory collectionFactory;
	private IdealFactory idealFactory;

	@Before
	public void setUp() throws Exception {
		numberFactory = Numbers.REAL_FACTORY;
		objectFactory = new ObjectFactory(numberFactory);
		typeFactory = new SymbolicTypeFactory(objectFactory);
		collectionFactory = new CommonCollectionFactory(objectFactory);
		idealFactory = new IdealFactory(numberFactory, objectFactory,
				typeFactory, collectionFactory);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void concreteCreation() {
		Constant c10 = idealFactory.intConstant(10);

		assertEquals(10, ((IntegerNumberIF) c10.number()).intValue());
	}

}
