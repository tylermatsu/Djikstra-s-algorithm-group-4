package grp;


public interface Hasher<E> {
	public int hash(E elem);
}

// Note that that Comparator<E> interface used below is in the Java library in java.util,
//    so import java.util.*; when you use the Comparator
// ALSO, the Iterator<E> interface you'll use is in java.util

