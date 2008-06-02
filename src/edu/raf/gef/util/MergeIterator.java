package edu.raf.gef.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator that concatenates multiple iterators into one.
 * 
 * @param <T>
 */
public class MergeIterator<T> implements Iterator<T> {

	private Iterator<T>[] iterators;
	private int currentIteratorIndex;
	private boolean hasNext;

	@SuppressWarnings("unchecked")
	public MergeIterator(Iterator<T> a, Iterator<T> b) {
		this.iterators = new Iterator[] { a, b };
		findNext();
	}

	@SuppressWarnings("unchecked")
	public MergeIterator(Iterator<T> a, Iterator<T> b, Iterator<T> c) {
		this.iterators = new Iterator[] { a, b, c };
		findNext();
	}

	@SuppressWarnings("unchecked")
	public MergeIterator(Iterator<T>... s) {
		this.iterators = Arrays.copyOf(s, s.length);
		findNext();
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public T next() {
		if (!hasNext)
			throw new NoSuchElementException("No more elements!");
		T ret = iterators[currentIteratorIndex].next();
		findNext();
		return ret;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Read only iterator.");
	}

	private void findNext() {
		while (currentIteratorIndex < iterators.length
				&& !iterators[currentIteratorIndex].hasNext()) {
			currentIteratorIndex++;
		}
		hasNext = currentIteratorIndex < iterators.length;
	}
}
