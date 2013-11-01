package edu.udel.cis.vsl.sarl.collections;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionComparatorStub;
import edu.udel.cis.vsl.sarl.collections.IF.ExpressionStub;
import edu.udel.cis.vsl.sarl.collections.common.CljSortedSymbolicMap;
import edu.udel.cis.vsl.sarl.collections.common.PcollectionsSymbolicMap;
public class MapBenchmark {
	
	
	public static void main(String args[])
	{
		int[] size = new int[2];
		size[0] = 500;
		size[1] = 600;
		/*size[2] = 300;
		size[3] = 400;
		size[4] = 500;
		size[5] = 600;*/
		/*size[6] = 100000;
		size[7] = 250000;
		size[8] = 500000;
		size[9] = 750000;
		size[10] = 1000000;*/
		for(int s = 0; s < size.length; s++)
		{	SymbolicExpression[] keyVals = new SymbolicExpression[size[s]];
			for(int b = 0; b < size[s]; b++)
			{
				keyVals[b] = new ExpressionStub(""+b+"");
			}
			
			Comparator<SymbolicExpression> elementComparator = new ExpressionComparatorStub();
			CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression> cljMap = new CljSortedSymbolicMap<SymbolicExpression,SymbolicExpression>(elementComparator);
			PMap<SymbolicExpression,SymbolicExpression> pmap = HashTreePMap.empty();
			PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression> pMap = new PcollectionsSymbolicMap<SymbolicExpression,SymbolicExpression>(pmap);
			//set up for removing elemnts from the array
			ArrayList<Integer> removeList = new ArrayList<Integer>(size[s]);
			for(int l = 0; l < size[s]; l++)
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
			for(int t = 0; t < 5; t++)
			{
				//start add to clj
				long cljStartTime = System.nanoTime();
				for(int k = 0;k < size[s]; k++)
				{
					if(k+1 == size[s])
					{
						cljMap = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) cljMap.put(keyVals[k], keyVals[0]);
					}
					else
					{
						cljMap = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) cljMap.put(keyVals[k], keyVals[k+1]);
					}
				}
				long cljEndTime = System.nanoTime();
				long cljDuration = cljEndTime - cljStartTime;
				System.out.println("Adding "+size[s]+" to CljMap took "+cljDuration/1000+" microseconds");
				System.out.println("Clj size = "+cljMap.size());
				//end clj add
				
				//start add pmap
				long pmapStartTime = System.nanoTime();
				for(int j = 0;j < size[s]; j++)
				{
					if(j+1 == size[s])
					{
						pMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) pMap.put(keyVals[j], keyVals[0]);
					}
					else
					{
						pMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) pMap.put(keyVals[j], keyVals[j+1]);
					}
				}
				long pmapEndTime = System.nanoTime();
				long pmapDuration = pmapEndTime - pmapStartTime;
				System.out.println("Adding "+size[s]+" to PcollectionMap took "+pmapDuration/1000+" microseconds");
				System.out.println("Pmap size = "+pMap.size());
				//end add pmap
				
				//start remove from clj
				cljStartTime = System.nanoTime();
				for(int l = 0;l < size[s]; l++)
				{
					cljMap = (CljSortedSymbolicMap<SymbolicExpression, SymbolicExpression>) cljMap.remove(keyVals[removeElements[l]]);
				}
				cljEndTime = System.nanoTime();
				cljDuration = cljEndTime - cljStartTime;
				System.out.println("Removing "+size[s]+" elements from CljMap took "+cljDuration/1000+" microseconds");
				System.out.println("Clj size = "+cljMap.size());
				//end remove clj
				
				//start remove from pmap
				pmapStartTime = System.nanoTime();
				for(int m = 0;m < size[s]; m++)
				{
					pMap = (PcollectionsSymbolicMap<SymbolicExpression, SymbolicExpression>) pMap.remove(keyVals[removeElements[m]]);
				}
				pmapEndTime = System.nanoTime();
				pmapDuration = pmapEndTime - pmapStartTime;
				System.out.println("Removing "+size[s]+" elements from PcollectionMap took "+pmapDuration/1000+" microseconds");
				//end remove pmap
				System.out.println("pMap size = "+cljMap.size());
				System.out.println('\n');
			}
			
		}
	
	}
	
	
}
