package wta.sh.jsonRandom.randomizers.map;

import wta.sh.jsonRandom.randomizers.Random;
import wta.sh.jsonRandom.randomizers.Randomizer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public abstract class RandMap<T, Y, M extends Map<T, Y>> extends Randomizer<M> {
	public RandMap(M inner, wta.sh.jsonRandom.randomizers.Random random) {
		super(inner, random);
	}

	@Override
	public void randomize() {
		inner=randomize(inner, random);
	}

	public void forEachReplacerValue(BiFunction<T, Y, Y> eacher) {
		for (Map.Entry<T, Y> entry : inner.entrySet()) {
			Y newValue = eacher.apply(entry.getKey(), entry.getValue());
			entry.setValue(newValue);
		}
	}

	public void forEachReplacer(BiFunction<T, Y, MapNode<T, Y>> eacher) {
		M eachMap=createMap();
		for (Map.Entry<T, Y> entry : inner.entrySet()) {
			MapNode<T, Y> node = eacher.apply(entry.getKey(), entry.getValue());
			eachMap.put(node.key, node.value);
		}
		inner=eachMap;
	}

	public void forEach(BiConsumer<T, Y> eacher) {
		for (Map.Entry<T, Y> entry : inner.entrySet()) {
			eacher.accept(entry.getKey(), entry.getValue());
		}
	}

	public <U> void randomize(BiFunction<T, Y, MapNodePos<T, Y, U>> grouper) {
		// 1. Сгруппировать ключи по pos
		Map<U, ArrayList<MapNodePos<T, Y, U>>> groups = new HashMap<>();
		ArrayList<U> posesUnknot=new ArrayList<>();
		for (T keyI : inner.keySet()) {
			Y valueI = inner.get(keyI);
			MapNodePos<T, Y, U> nodeI = grouper.apply(keyI, valueI);
			U pos = nodeI.pos;
			posesUnknot.add(pos);
			groups.computeIfAbsent(pos, key -> new ArrayList<>()).add(nodeI);
		}
		Map<U, Iterator<MapNodePos<T, Y, U>>> iterators = new HashMap<>();
		for (Map.Entry<U, ArrayList<MapNodePos<T, Y, U>>> entryI : groups.entrySet()){
			ArrayList<MapNodePos<T, Y, U>> valueI=entryI.getValue();
			shuffle(valueI, random);
			iterators.put(entryI.getKey(), valueI.iterator());
		}

		// 2. Возвращаем
		M ret=createMap();
		for (U i : posesUnknot){
			MapNodePos<T, Y, U> nodeI=iterators.get(i).next();
			ret.put(nodeI.key, nodeI.value);
		}
		inner=ret;
	}

	public record MapNode<T, Y>(T key, Y value){}
	/**unimmutable! (copy, to use next)*/
	public static class MapNodePos<T, Y, U>{
		T key;
		Y value;
		U pos;

		public T key() {return key;}
		public Y value() {return value;}

		private void setKey(T key) {this.key = key;}
		private void setValue(Y value) {this.value = value;}

		public U pos() {return pos;}

		public MapNodePos(T key, Y value, U pos){
			this.key=key;
			this.value=value;
			this.pos=pos;
		}

		public MapNodePos<T, Y, U> copy(){
			return new MapNodePos<>(key, value, pos);
		}
	}

	protected M randomize(M map, wta.sh.jsonRandom.randomizers.Random random) {
		List<Y> values = new ArrayList<>(map.values());
		shuffle(values, random);

		M ret = createMap();
		Iterator<Y> iterator = values.iterator();
		for (T key : map.keySet()) {
			ret.put(key, iterator.next());
		}
		return ret;
	}

	protected static <T, Y> void shuffle(ArrayList<MapNodePos<T, Y, ?>> nodes, Random random) {
		int n = nodes.size();
		if (n <= 1) return;

		for (int i = n - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			Y temp = nodes.get(i).value;
			nodes.get(i).setValue(nodes.get(j).value);
			nodes.get(j).setValue(temp);
		}
	}

	protected abstract M createMap();
}
