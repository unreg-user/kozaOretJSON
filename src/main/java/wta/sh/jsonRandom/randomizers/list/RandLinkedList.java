package wta.sh.jsonRandom.randomizers.list;

import wta.sh.jsonRandom.randomizers.Random;

import java.util.LinkedList;

public class RandLinkedList<T> extends RandList<T, LinkedList<T>>{
	public RandLinkedList(LinkedList<T> inner, Random random) {
		super(inner, random);
	}

	@Override
	protected LinkedList<T> createList() {
		return new LinkedList<>();
	}
}
