package wta.sh.jsonRandom.randomizers.map;

import wta.sh.jsonRandom.randomizers.Random;

import java.util.HashMap;

public class RandHashMap<T, Y> extends RandMap<T, Y, HashMap<T, Y>>{
	public RandHashMap(HashMap<T, Y> object, Random random) {
		super(object, random);
	}

	@Override
	protected HashMap<T, Y> createMap() {
		return new HashMap<>();
	}
}
