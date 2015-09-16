package edu.udel.cis.vsl.sarl.collections.common;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.collections.IF.SortedSymbolicMap;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;

public class SimpleSortedMap<K extends SymbolicExpression, V extends SymbolicExpression>
		extends CommonSortedMap<K, V> implements SortedSymbolicMap<K, V> {

	// Types...

	protected class EntryComparator implements Comparator<Entry<K, V>> {
		@Override
		public int compare(Entry<K, V> o1, Entry<K, V> o2) {
			return comparator.compare(o1.getKey(), o2.getKey());
		}
	}

	// Static fields...

	/**
	 * An empty array used to form new empty instances of SimpleSortedMap. They
	 * can all shared the same array since an empty array can never be modified.
	 */
	private static SimpleEntry[] emptyArray = new SimpleEntry[0];

	/**
	 * The comparator used to compare keys, which defines the total order on the
	 * entries in this map.
	 */
	private Comparator<? super K> comparator;

	/**
	 * The current number of entries in this map. Note that this is in general
	 * less or equal to the length of the array {@link #entries}.
	 */
	private int size;

	/**
	 * The array containing the entries in this map. Only the first
	 * {@link #size} elements of the array contain useful data. The remaining
	 * elements are just extra space that will be used in case the map grows.
	 * When this object is committed (made immutable), this array will be
	 * "trimmed" so that its length is exactly equal to {@link #size}: no more
	 * extra space is needed since the once the map is committed it can never
	 * grow.
	 */
	private Entry<K, V>[] entries;

	// Constructors...

	protected SimpleSortedMap(Comparator<? super K> comparator, int size,
			Entry<K, V>[] entries) {
		super();
		this.comparator = comparator;
		this.size = size;
		this.entries = entries;
		for (int i = 0; i < size; i++) {
			Entry<K, V> entry = entries[i];

			entry.getKey().addReferenceFrom(this);
			entry.getValue().addReferenceFrom(this);
		}
	}

	@SuppressWarnings("unchecked")
	protected SimpleSortedMap(Comparator<? super K> comparator) {
		this(comparator, 0, (Entry<K, V>[]) emptyArray);
	}

	/**
	 * Constructs new instance from the given Java map. The new instances is
	 * backed by the Java map, i.e., if you modify the Java map you could mess
	 * up this new instance.
	 * 
	 * @param javaMap
	 *            a Java map from K to V, not necessarily ordered
	 * @param comparator
	 *            the comparator on keys
	 */
	protected SimpleSortedMap(Map<K, V> javaMap,
			Comparator<? super K> comparator) {
		super();
		size = javaMap.size();
		this.comparator = comparator;
		// safety: Java won't let you instantiate a generic array,
		// but I know that all the keys will belong to K and values
		// will belong to V because they come from javaMap:
		@SuppressWarnings("unchecked")
		Entry<K, V>[] entries2 = (Entry<K, V>[]) new Entry<?, ?>[size];

		entries = entries2;
		javaMap.entrySet().toArray(entries);
		Arrays.sort(entries, new EntryComparator());
		for (Entry<K, V> entry : entries) {
			entry.getKey().addReferenceFrom(this);
			entry.getValue().addReferenceFrom(this);
		}
	}

	// Helper methods...

	/**
	 * Copies Entries from one array to another. New instances of {@link Entry}
	 * are created because they cannot be safely shared.
	 * 
	 * @param source
	 *            source array
	 * @param sourceStart
	 *            index at which to start copying from source
	 * @param dest
	 *            destination array
	 * @param destStart
	 *            index at which to start writing to dest
	 * @param num
	 *            number of entries to copy
	 */
	private void copyEntries(Entry<K, V>[] source, int sourceStart,
			Entry<K, V>[] dest, int destStart, int num) {
		for (int i = 0; i < num; i++) {
			Entry<K, V> entry1 = source[sourceStart + i];
			@SuppressWarnings("unchecked")
			Entry<K, V> entry2 = (Entry<K, V>) new SimpleEntry(entry1.getKey(),
					entry1.getValue());

			dest[destStart + i] = entry2;
		}
	}

	/**
	 * Finds the index of the entry in the array {@link #entries} whose key
	 * equals the given key, if there is one. Else returns -1.
	 * 
	 * @param key
	 *            the key to look for; any member of K
	 * @return index of the entry with that key, or -1 if there is none
	 */
	private int find(K key) {
		int lo = 0, hi = size - 1;

		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			int compare = comparator.compare(entries[mid].getKey(), key);

			if (compare == 0) {
				return mid;
			} else if (compare < 0) { // x<element
				lo = mid + 1;
			} else {
				hi = mid - 1;
			}
		}
		return -1;
	}

	@Override
	public Comparator<? super K> comparator() {
		return comparator;
	}

	@Override
	public SortedSymbolicMap<K, V> put(K key, V value) {
		int lo = 0, hi = size - 1;

		// loop invariant: hi-lo >= -1.
		// hi>=lo -> hi-((lo+hi)/2 + 1) >= -1.
		// hi>=lo -> ((lo+hi)/2 -1) - lo >= -1.
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			Entry<K, V> entry = entries[mid];
			int keyComparison = comparator.compare(entry.getKey(), key);

			if (keyComparison == 0) {
				if (value.equals(entry.getValue()))
					return this;

				@SuppressWarnings("unchecked")
				Entry<K, V> newEntry = (Entry<K, V>) new SimpleEntry(key, value);
				// new version replacing old entry with new one at mid
				@SuppressWarnings("unchecked")
				Entry<K, V>[] newEntries = (Entry<K, V>[]) new SimpleEntry[entries.length];

				copyEntries(entries, 0, newEntries, 0, mid); // 0..mid-1
				newEntries[mid] = newEntry;
				copyEntries(entries, mid + 1, newEntries, mid + 1, size - mid
						- 1); // mid+1..size-1
				return new SimpleSortedMap<K, V>(comparator, size, newEntries);
			} else if (keyComparison < 0) { // x<element
				lo = mid + 1;
			} else { // x>element
				hi = mid - 1;
			}
		}
		assert hi - lo == -1;
		// Example: hi=-1, lo=0
		// Example: hi=length-1, lo=length
		// lo is where element should be inserted
		// new version inserting new entry at position lo

		@SuppressWarnings("unchecked")
		Entry<K, V> newEntry = (Entry<K, V>) new SimpleEntry(key, value);
		@SuppressWarnings("unchecked")
		Entry<K, V>[] newEntries = (Entry<K, V>[]) new SimpleEntry[newLength(size)];

		copyEntries(entries, 0, newEntries, 0, lo);
		newEntries[lo] = newEntry;
		copyEntries(entries, lo, newEntries, lo + 1, size - lo);
		return new SimpleSortedMap<K, V>(comparator, size + 1, newEntries);
	}

	@Override
	public SortedSymbolicMap<K, V> putMut(K key, V value) {
		if (isImmutable())
			return put(key, value);

		int lo = 0, hi = size - 1;

		// loop invariant: hi-lo >= -1.
		// hi>=lo -> hi-((lo+hi)/2 + 1) >= -1.
		// hi>=lo -> ((lo+hi)/2 -1) - lo >= -1.
		while (lo <= hi) {
			int mid = (lo + hi) / 2;
			Entry<K, V> entry = entries[mid];
			int keyComparison = comparator.compare(entry.getKey(), key);

			if (keyComparison == 0) {
				entry.getValue().removeReferenceFrom(this);
				value.addReferenceFrom(this);
				entry.setValue(value);
				return this;
			} else if (keyComparison < 0) { // x<element
				lo = mid + 1;
			} else { // x>element
				hi = mid - 1;
			}
		}
		assert hi - lo == -1;
		// Example: hi=-1, lo=0
		// Example: hi=length-1, lo=length
		// lo is where element should be inserted
		// new version inserting new entry at position lo

		@SuppressWarnings("unchecked")
		Entry<K, V> newEntry = (Entry<K, V>) new SimpleEntry(key, value);

		key.addReferenceFrom(this);
		value.addReferenceFrom(this);
		if (size < entries.length) {
			// shift up the subsequence starting from lo...
			System.arraycopy(entries, lo, entries, lo + 1, size - lo);
			entries[lo] = newEntry;
		} else {
			@SuppressWarnings("unchecked")
			Entry<K, V>[] newEntries = (Entry<K, V>[]) new SimpleEntry[newLength(size)];

			System.arraycopy(entries, 0, newEntries, 0, lo);
			newEntries[lo] = newEntry;
			System.arraycopy(entries, lo, newEntries, lo + 1, size - lo);
			entries = newEntries;
		}
		size++;
		return this;
	}

	@Override
	public SortedSymbolicMap<K, V> remove(K key) {
		int index = find(key);

		if (index < 0) {
			return this;
		} else {
			@SuppressWarnings("unchecked")
			Entry<K, V>[] newEntries = (Entry<K, V>[]) new SimpleEntry[size - 1];

			copyEntries(entries, 0, newEntries, 0, index);
			copyEntries(entries, index + 1, newEntries, index, size - index - 1);
			return new SimpleSortedMap<K, V>(comparator, size - 1, newEntries);
		}
	}

	@Override
	public SortedSymbolicMap<K, V> removeMut(K key) {
		if (isImmutable())
			return remove(key);

		int index = find(key);

		if (index >= 0) {
			entries[index].getKey().removeReferenceFrom(this);
			entries[index].getValue().removeReferenceFrom(this);
			System.arraycopy(entries, index + 1, entries, index, size - index
					- 1);
			size--;
		}
		return this;
	}

	@Override
	public V get(K key) {
		int index = find(key);

		if (index < 0) {
			return null;
		} else {
			return entries[index].getValue();
		}
	}

	class KeyIterable implements Iterable<K> {
		class KeyIterator implements Iterator<K> {
			int nextIndex = 0;

			@Override
			public boolean hasNext() {
				return nextIndex < size;
			}

			@Override
			public K next() {
				K result = entries[nextIndex].getKey();

				nextIndex++;
				return result;
			}

			@Override
			public void remove() {
				throw new SARLException("unimplemented");
			}
		}

		@Override
		public Iterator<K> iterator() {
			return new KeyIterator();
		}
	}

	@Override
	public Iterable<K> keys() {
		return new KeyIterable();
	}

	class ValueIterable implements Iterable<V> {
		class ValueIterator implements Iterator<V> {
			int nextIndex = 0;

			@Override
			public boolean hasNext() {
				return nextIndex < size;
			}

			@Override
			public V next() {
				V result = entries[nextIndex].getValue();

				nextIndex++;
				return result;
			}

			@Override
			public void remove() {
				throw new SARLException("unimplemented");
			}
		}

		@Override
		public Iterator<V> iterator() {
			return new ValueIterator();
		}
	}

	@Override
	public Iterable<V> values() {
		return new ValueIterable();
	}

	class EntriesIterable implements Iterable<Entry<K, V>> {
		class EntriesIterator implements Iterator<Entry<K, V>> {
			int nextIndex = 0;

			@Override
			public boolean hasNext() {
				return nextIndex < size;
			}

			@Override
			public Entry<K, V> next() {
				Entry<K, V> result = entries[nextIndex];

				nextIndex++;
				return result;
			}

			@Override
			public void remove() {
				throw new SARLException("unimplemented");
			}
		}

		@Override
		public Iterator<Entry<K, V>> iterator() {
			return new EntriesIterator();
		}
	}

	@Override
	public Iterable<Entry<K, V>> entries() {
		return new EntriesIterable();
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public Iterator<V> iterator() {
		return (new ValueIterable()).iterator();
	}

	@Override
	public void canonizeChildren(ObjectFactory factory) {
		if (size != entries.length) {
			@SuppressWarnings("unchecked")
			Entry<K, V>[] newEntries = (Entry<K, V>[]) new Entry<?, ?>[size];

			System.arraycopy(entries, 0, newEntries, 0, size);
			entries = newEntries;
		}
		for (int i = 0; i < size; i++) {
			Entry<K, V> entry = entries[i];
			K oldKey = entry.getKey(), newKey = factory.canonic(oldKey);

			if (oldKey != newKey) {
				@SuppressWarnings("unchecked")
				Entry<K, V> newEntry = (Entry<K, V>) new SimpleEntry(newKey,
						factory.canonic(entry.getValue()));

				entries[i] = newEntry;
			} else {
				V oldValue = entry.getValue(), newValue = factory
						.canonic(oldValue);

				if (oldValue != newValue)
					entry.setValue(newValue);
			}
		}
	}

	/**
	 * Iterable over keys and values.
	 * 
	 * @author siegel
	 */
	class ChildIterable implements Iterable<SymbolicExpression> {
		class ChildIterator implements Iterator<SymbolicExpression> {
			int nextIndex = 0;
			boolean didKey = false;

			@Override
			public boolean hasNext() {
				return nextIndex < size;
			}

			@Override
			public SymbolicExpression next() {
				SymbolicExpression result;

				if (didKey) {
					result = entries[nextIndex].getValue();
					didKey = false;
					nextIndex++;
				} else {
					result = entries[nextIndex].getKey();
					didKey = true;
				}
				return result;
			}

			@Override
			public void remove() {
				throw new SARLException("unimplemented");
			}
		}

		@Override
		public Iterator<SymbolicExpression> iterator() {
			return new ChildIterator();
		}
	}

	@Override
	protected Iterable<? extends SymbolicObject> getChildren() {
		return new ChildIterable();
	}

	@Override
	protected void nullifyFields() {
		entries = null;
	}
}
