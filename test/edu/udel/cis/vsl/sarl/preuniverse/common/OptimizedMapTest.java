/*@author Gunjan Majmudar */


package edu.udel.cis.vsl.sarl.preuniverse.common;


import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant; 
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.preuniverse.common.OptimizedMap;

public class OptimizedMapTest {
	
	private static PreUniverse universe;

	private static SymbolicExpression hello, key;
	
	private static OptimizedMap nMap;
	
	private static boolean temp;
	
	private static int value;
	
	private static char c;
	
	private static Object nvalue, nkey;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem test = PreUniverses.newIdealFactorySystem();
		universe = new CommonPreUniverse(test);
		hello = universe.stringExpression("Hello");
		key = universe.character(c);
		Map<SymbolicConstant, SymbolicExpression> newMap = new HashMap<SymbolicConstant,SymbolicExpression>();
		nMap = new OptimizedMap(newMap);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	
	
	
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void optimizedMapPutTest(){
		
		nMap.put(key, hello);
	
		
	}
	
	@Test
	public void optimizedMapTest(){
		temp = nMap.isEmpty();
		
		value = nMap.size();
		System.out.print(value);
		
		assertEquals(temp, true);
		assertEquals(nMap.containsKey(key),false);
		assertEquals(nMap.containsValue(nvalue), false);
		
	}
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void optimizedMapRemoveTest(){
		
		nMap.remove(key);
	
		
	}
	
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void optimizedMapEntrySetTest(){
		
		nMap.entrySet();
	
		
	}
	
	@Test
	public void optimizedMapValuesTest(){
		
		nMap.values();
	
		
	}
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void optimizedMapKeysetTest(){
	
		nMap.keySet();
		
	}
	
	@Test
	(expected = UnsupportedOperationException.class)
	public void optimizedMapClearTest(){
		
		nMap.clear();
		
	}

}
