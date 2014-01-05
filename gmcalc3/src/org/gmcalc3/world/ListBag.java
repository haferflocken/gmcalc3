// An ordered bag is a collection that keeps track of the number of each element in it as a number.
// Inspired by the Bag interface in the Apache Commons Collections framework.

package org.gmcalc3.world;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ListBag<E> implements List<E> {

	// The iterator.
	private class ListBagIterator implements Iterator<E> {
		protected int index, lastIndex;
		
		private ListBagIterator() {
			index = 0;
			lastIndex = -1;
		}

		public boolean hasNext() {
			return index < ListBag.this.size();
		}

		public E next() {
			if (!hasNext())
				throw new NoSuchElementException();
			E out = ListBag.this.get(index);
			lastIndex = index;
			index++;
			return out;
		}

		public void remove() {
			if (lastIndex == -1)
				throw new IllegalStateException();
			ListBag.this.remove(lastIndex);
			lastIndex = -1;
		}
		
	}
	
	// The list iterator.
	private class ListBagListIterator extends ListBagIterator implements ListIterator<E> {
		
		private ListBagListIterator() {
			super();
		}
		
		private ListBagListIterator(int startIndex) {
			this();
			index = startIndex;
		}

		public boolean hasPrevious() {
			return index > 0;
		}

		public E previous() {
			if (!hasPrevious())
				throw new NoSuchElementException();
			E out = ListBag.this.get(index - 1);
			lastIndex = index - 1;
			index--;
			return out;
		}

		public int nextIndex() {
			return index;
		}

		public int previousIndex() {
			return index - 1;
		}

		public void set(E e) {
			if (lastIndex == -1)
				throw new IllegalStateException();
			ListBag.this.set(lastIndex, e);
		}

		public void add(E e) {
			ListBag.this.add(index, e);
			index++;
			lastIndex = -1;
		}
		
	}
	
	// The contents of the bag.
	private ArrayList<E> items; 
	private ArrayList<Integer> counts;
	
	// Constructors.
	public ListBag() {
		items = new ArrayList<E>();
		counts = new ArrayList<Integer>();
	}
	
	private ListBag(List<E> items, List<Integer> counts) {
		if (items instanceof ArrayList)
			this.items = (ArrayList<E>)items;
		else
			this.items = new ArrayList<E>(items);
		if (counts instanceof ArrayList)
			this.counts = (ArrayList<Integer>)counts;
		else
			this.counts = new ArrayList<Integer>(counts);
	}
	
	// MODIFIES: this
	// EFFECTS:	 Adds e to this, increasing its count by one. If e isn't already in this it is added to the end. Returns true.
	public boolean add(E e) {
		return add(e, 1);
	}
	
	// MODIFIES: this
	// EFFECTS:  Adds e to this, increasing its count by amount. If e isn't already in this it is added to the end. Returns true;
	public boolean add(E e, int amount) {
		int i = indexOf(e);
		if (i == -1) {
			items.add(e);
			counts.add(amount);
		}
		else {
			int newCount = counts.get(i) + amount;
			counts.set(i, newCount);
		}
		return true;
	}
	
	// MODIFIES: this
	// EFFECTS:  Increases the count of e in this by one and ensures the position of e is the given index.
	public void add(int index, E element) {
		add(index, element, 1);
	}
	
	// MODIFIES: this
	// EFFECTS:  Increases the count of e in this by amount and ensures the position of e is the given index.
	public void add(int index, E e, int amount) {
		int i = indexOf(e);
		if (i == -1) {
			items.add(index, e);
			counts.add(index, amount);
		}
		else if (i == index) {
			counts.set(index, counts.get(index) + amount);
		}
		else {
			int newCount = counts.get(i) + amount;
			items.add(index, e);
			counts.add(index, newCount);
			items.remove(i);
			counts.remove(i);
		}
	}
	
	// MODIFIES: this
	// EFFECTS:  Adds all elements of c to this, increasing the count of each by one. Returns true.
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			add(e);
		}
		return true;
	}
	
	// MODIFIES: this
	// EFFECTS:  Increases the count in this of the elements in the given collection,
	//			 ensuring they are at the specified position in the list.
	public boolean addAll(int index, Collection<? extends E> c) {
		// TODO
		throw new UnsupportedOperationException();
	}
	
	// MODIFIES: this
	// EFFECTS:  Removes all elements from this.
	public void clear() {
		items.clear();
		counts.clear();
	}
	
	// EFFECTS:  Returns true if this contains at least one of the given element.
	public boolean contains(Object o) {
		return items.contains(o);
	}
	
	// EFFECTS:  Returns true if this contains at least one of every element in the collection.
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o))
				return false;
		}
		return true;
	}
	
	// EFFECTS:  Returns true if the given list contains the same elements in the same order.
	public boolean equals(Object o) {
		if (o instanceof ListBag) {
			return items.equals(((ListBag<?>)o).items);
		}
		if (o instanceof List) {
			return items.equals((List<?>)o);
		}
		return false;
	}
	
	// EFFECTS:  Returns the element at the specified position in this list.
	public E get(int index) {
		return items.get(index);
	}
	
	// EFFECTS:  Returns the count of the element at the specified position in this list.
	public int getCount(int index) {
		return counts.get(index);
	}
	
	// EFFECTS:  Returns the count of the value in this list, returning 0 if none are in this.
	public int getCount(Object o) {
		int index = indexOf(o);
		if (index == -1)
			return 0;
		return counts.get(index);
	}
	
	// EFFECTS:  Returns the hash code value for this list. See java.util.List.hashCode() for details.
	public int hashCode() {
		int hashCode = 1;
		for (E e : items)
			hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
		return hashCode;
	}
	
	// EFFECTS:  Returns the index of the first occurrence of the specified element in this list
	//			 or -1 if this does not contain the element.
	public int indexOf(Object o) {
		return items.indexOf(o);
	}
	
	// EFFECTS:  Returns true if this contains no elements.
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	// EFFECTS:  Returns an iterator over the elements in this list in proper sequence.
	public Iterator<E> iterator() {
		return new ListBagIterator();
	}
	
	// EFFECTS:  Returns the index of the last occurrence of the specified element in this list
	//			 or -1 if this does not contain the element.
	public int lastIndexOf(Object o) {
		return items.lastIndexOf(o);
	}
	
	// EFFECTS:  Returns a list iterator over the elements in this list in proper sequence.
	public ListIterator<E> listIterator() {
		return new ListBagListIterator();
	}
	
	// EFFECTS:  Returns a list iterator over the elements in this list in proper sequence
	//			 starting at the specified position in the list.
	public ListIterator<E> listIterator(int index) {
		if (index < 0 || index > items.size())
			throw new IndexOutOfBoundsException();
		return new ListBagListIterator(index);
	}
	
	// MODIFIES: this
	// EFFECTS:  Removes the element at the given index and sets its count to 0.
	public E remove(int index) {
		counts.remove(index);
		return items.remove(index);
	}
	
	// MODIFIES: this
	// EFFECTS:  Subtracts one from the count of the given element, removing it entirely if its count hits 0.
	public boolean remove(Object o) {
		return remove(o, 1);
	}
	
	// MODIFIES: this
	// EFFECTS:  Subtracts amount from the count of the given element, removing it entirely if its count hits 0.
	public boolean remove(Object o, int amount) {
		int index = indexOf(o);
		if (index == -1)
			return false;
		int newCount = counts.get(index) - amount;
		if (newCount < 1)
			remove(index);
		else
			counts.set(index, newCount);
		return true;
	}
	
	// MODIFIES: this
	// EFFECTS:  Remove all occurrences from this of every element that is in the given collection.
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			boolean removed = remove(o);
			if (removed == true)
				changed = true;
		}
		return changed;
	}
	
	// MODIFIES: this
	// EFFECTS:  Remove from this list all elements that aren't in the given collection.
	public boolean retainAll(Collection<?> c) {
		boolean changed = false;
		ArrayList<E> newItems = new ArrayList<E>();
		ArrayList<Integer> newCounts = new ArrayList<Integer>();
		int size = items.size();
		for (int i = 0; i < size; i++) {
			E item = items.get(i);
			if (c.contains(item)) {
				newItems.add(item);
				newCounts.add(counts.get(i));
			}
			else
				changed = true;
		}
		items.clear();
		counts.clear();
		items = newItems;
		counts = newCounts;
		return changed;
	}
	
	// MODIFIES: this
	// EFFECTS:  If the element isn't already in the list, replaces the element at the specified 
	//			 position in this list with one of the specified element.
	//			 If the element is already at another position in this, removes the element at the given
	//			 position and moves the given element to the given position.
	//			 Returns the element previously at the position.
	public E set(int index, E newItem) {
		E oldItem = items.get(index);
		int newItemIndex = indexOf(newItem);
		// If the new item isn't already in the list, replace the element at index with the given one.
		if (newItemIndex == -1) {
			items.set(index, newItem);
			counts.set(index, 1);
		}
		// If the element is in the list, move it to the index and remove the old thing that was there.
		else {
			items.set(index, newItem);
			counts.set(index, counts.get(newItemIndex));
			items.remove(newItemIndex);
			counts.remove(newItemIndex);
		}
		return oldItem;
	}
	
	// MODIFIES: this
	// EFFECTS:  Sets the count at the specified index.
	public void setCount(int index, int count) {
		counts.set(index, count);
	}
	
	// MODIFIES: this
	// EFFECTS:  Sets the count of the given element.
	public void setCount(E element, int count) {
		int index = indexOf(element);
		if (index != -1)
			setCount(index, count);
	}
	
	// EFFECTS:  Returns the number of elements in this.
	public int size() {
		return items.size();
	}
	
	// EFFECTS:  Returns a view of the portion of this list of the range [fromIndex, toIndex)
	public List<E> subList(int fromIndex, int toIndex) {
		return new ListBag<E>(items.subList(fromIndex, toIndex), counts.subList(fromIndex, toIndex));
	}
	
	// EFFECTS:  Returns an array containing all the items in this list in proper sequence.
	public Object[] toArray() {
		return items.toArray();
	}
	
	// EFFECTS:  Returns an array containing all of the items in this list in proper sequence.
	//			 The runtime type of the returned array is that of the specified array.
	public <T> T[] toArray(T[] a) {
		return items.toArray(a);
	}
}
