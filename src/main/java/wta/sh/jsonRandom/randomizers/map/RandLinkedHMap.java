package wta.sh.jsonRandom.randomizers.map;

import wta.sh.jsonRandom.randomizers.Random;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class RandLinkedHMap<T, Y> extends RandMap<T, Y, LinkedHashMap<T, Y>>{
	public RandLinkedHMap(LinkedHashMap<T, Y> object, Random random) {
		super(object, random);
	}

	@Override
	protected LinkedHashMap<T, Y> createMap() {
		return new LinkedHashMap<>();
	}

	public RandHashMap<T, Y> getUnlink(){
		return new RandHashMap<>(getUnlinkInner(), random);
	}

	public HashMap<T, Y> getUnlinkInner(){
		return new HashMap<>(inner);
	}
}
