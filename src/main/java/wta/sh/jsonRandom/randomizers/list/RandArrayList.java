package wta.sh.jsonRandom.randomizers.list;

import wta.sh.jsonRandom.randomizers.Random;

import java.util.ArrayList;

public class RandArrayList<T> extends RandList<T, ArrayList<T>>{
	public RandArrayList(ArrayList<T> object, Random random) {
		super(object, random);
	}

	@Override
	protected ArrayList<T> createList() {
		return new ArrayList<>();
	}
}
