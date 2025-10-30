package wta.sh.jsonRandom.randomizers;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RandMap<T, Y> extends Randomizer<LinkedHashMap<T, Y>> {
	public RandMap(LinkedHashMap<T, Y> object, Random random) {
		super(object, random);
	}

	@Override
	public void randomize() {
		object=randomize(object, random);
	}

	public void forEachReplacerValue(BiFunction<T, Y, Y> eacher) {
		for (Map.Entry<T, Y> entry : object.entrySet()) {
			Y newValue = eacher.apply(entry.getKey(), entry.getValue());
			entry.setValue(newValue);
		}
	}

	public void forEachReplacer(BiFunction<T, Y, MapNode<T, Y>> eacher) {
		LinkedHashMap<T, Y> eachMap=new LinkedHashMap<>();
		for (Map.Entry<T, Y> entry : object.entrySet()) {
			MapNode<T, Y> node = eacher.apply(entry.getKey(), entry.getValue());
			eachMap.put(node.key, node.value);
		}
		object=eachMap;
	}

	public void forEach(BiConsumer<T, Y> eacher) {
		for (Map.Entry<T, Y> entry : object.entrySet()) {
			eacher.accept(entry.getKey(), entry.getValue());
		}
	}

	public void randomize(MapGrouper<T, Y> grouper) {
		// 1. Сгруппировать ключи по pos
		Map<Integer, ArrayList<MapNodePos<T, Y>>> groups = new HashMap<>();
		ArrayList<Integer> posesUnknot=new ArrayList<>();
		for (T key : object.keySet()) {
			Y value = object.get(key);
			MapNodePos<T, Y> node = grouper.group(key, value);
			int pos = node.pos();
			posesUnknot.add(pos);
			groups.computeIfAbsent(pos, ArrayList::new).add(node);
		}
		Map<Integer, Iterator<MapNodePos<T, Y>>> iterators = new HashMap<>();
		for (Map.Entry<Integer, ArrayList<MapNodePos<T, Y>>> entryI : groups.entrySet()){
			ArrayList<MapNodePos<T, Y>> valueI=entryI.getValue();
			shuffle(valueI, random);
			iterators.put(entryI.getKey(), valueI.iterator());
		}

		// 2. Возвращаем
		LinkedHashMap<T, Y> ret=new LinkedHashMap<>();
		for (Integer i : posesUnknot){
			MapNodePos<T, Y> nodeI=iterators.get(i).next();
			ret.put(nodeI.key(), nodeI.value());
		}
		object=ret;
	}

	public interface MapGrouper<T, Y>{
		MapNodePos<T, Y> group(T key, Y object);
	}

	public record MapNode<T, Y>(T key, Y value){}
	/**unimmutable! (copy, to use next)*/
	public static class MapNodePos<T, Y>{
		T key;
		Y value;
		int pos;

		public T key() {return key;}
		public Y value() {return value;}

		private void setKey(T key) {this.key = key;}
		private void setValue(Y value) {this.value = value;}

		public int pos() {return pos;}

		public MapNodePos(T key, Y value, int pos){
			this.key=key;
			this.value=value;
			this.pos=pos;
		}

		public MapNodePos<T, Y> copy(){
			return new MapNodePos<>(key, value, pos);
		}
	}

	protected static <T, Y> LinkedHashMap<T, Y> randomize(LinkedHashMap<T, Y> map, Random random) {
		List<Y> values = new ArrayList<>(map.values());
		shuffle(values, random);

		LinkedHashMap<T, Y> ret = new LinkedHashMap<>();
		Iterator<Y> iterator = values.iterator();
		for (T key : map.keySet()) {
			ret.put(key, iterator.next());
		}
		return ret;
	}

	protected static <T, Y> void shuffle(ArrayList<MapNodePos<T, Y>> nodes, Random random) {
		int n = nodes.size();
		if (n <= 1) return;

		for (int i = n - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			Y temp = nodes.get(i).value();
			nodes.get(i).setValue(nodes.get(j).value());
			nodes.get(j).setValue(temp);
		}
	}
}
