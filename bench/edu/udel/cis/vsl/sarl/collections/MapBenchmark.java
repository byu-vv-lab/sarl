package edu.udel.cis.vsl.sarl.collections;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicMap;
import edu.udel.cis.vsl.sarl.collections.common.CljSortedSymbolicMap;
import edu.udel.cis.vsl.sarl.collections.common.PcollectionsSymbolicMap;
import edu.udel.cis.vsl.sarl.number.real.RealNumberFactory;
import edu.udel.cis.vsl.sarl.object.common.CommonObjectFactory;
public class MapBenchmark {
	//SymbolicExpression[] keyVals;
	//int size = 100;
	private static CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> cljMap;
	private static PMap<SymbolicExpression,SymbolicExpression> pmap;
	private static Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();
	private static PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> pMap;
	
	public static void main(String args[]){
	int size = 1000000;
	SymbolicExpression[] keyVals = new SymbolicExpression[size];
	
	cljMap = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
	pmap = HashTreePMap.empty();
	pMap = new PcollectionsSymbolicMap<SymbolicExpression,SymbolicExpression>(pmap);
	
		for(int i = 0; i < size; i++)
		{
			keyVals[i] = new ExpressionStub(""+i+"");
		}
		
		long cljStartTime = System.nanoTime();
		for(int k = 0;k < size; k++)
		{
			if(k+1 == size)
			{
				cljMap.put(keyVals[k], keyVals[0]);
			}
			else
			{
				cljMap.put(keyVals[k], keyVals[k+1]);
			}
		}
		long cljEndTime = System.nanoTime();
		long cljDuration = cljEndTime - cljStartTime;
		
		System.out.println(cljDuration);
		
		long pmapStartTime = System.nanoTime();
		for(int k = 0;k < size; k++)
		{
			if(k+1 == size)
			{
				pMap.put(keyVals[k], keyVals[0]);
			}
			else
			{
				pMap.put(keyVals[k], keyVals[k+1]);
			}
		}
		long pmapEndTime = System.nanoTime();
		long pmapDuration = pmapEndTime - pmapStartTime;
		
		System.out.println(pmapDuration);

		//set up for removing elemnts from the array
		ArrayList removeList = new ArrayList(size);
		for(int l = 0; l < size; l++)
		{
			removeList.add(l);
		}
		Collections.shuffle(removeList);
		int[] removeElements = new int[removeList.size()];
		for(int m = 0; m < removeList.size();m++)
		{
			removeElements[m] = (int) removeList.get(m);
		}
		//end set up
		
		cljStartTime = System.nanoTime();
		for(int k = 0;k < size; k++)
		{
			cljMap.remove(keyVals[removeElements[k]]);
		}
		cljEndTime = System.nanoTime();
		cljDuration = cljEndTime - cljStartTime;
		System.out.println(cljDuration);
		
		pmapStartTime = System.nanoTime();
		for(int k = 0;k < size; k++)
		{
			pMap.remove(keyVals[removeElements[k]]);
		}
		pmapEndTime = System.nanoTime();
		pmapDuration = pmapEndTime - pmapStartTime;
		
		System.out.println(pmapDuration);
	}
	
	
}
