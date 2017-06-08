package com.cqfc.util;

public class Pair<P, V> {
	private P first;
	private V second;

	public Pair() {
	}

	public Pair(P p, V v) {
		this.first = p;
		this.second = v;
	}

	public P first() {
		return first;
	}

	public void first(P p) {
		this.first = p;
	}

	public V second() {
		return second;
	}

	public void second(V v) {
		this.second = v;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{first=").append(first).append(", second=").append(second).append("}");
		return sb.toString();
	}
}
