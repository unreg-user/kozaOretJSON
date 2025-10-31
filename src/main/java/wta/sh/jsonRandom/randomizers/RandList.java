package wta.sh.jsonRandom.randomizers;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RandList<T>  extends Randomizer<ArrayList<T>>{
	public RandList(ArrayList<T> object, Random random) {
		super(object, random);
	}

	@Override
	public void randomize() {
		shuffle(object, random);
	}

	public <U> void randomize(BiFunction<T, Integer, ListNodePos<T, U>> grouper) {
		// 1. Сгруппировать ключи по pos
		Map<U, ArrayList<T>> groups = new HashMap<>();
		ArrayList<U> posesUnknot=new ArrayList<>();
		int i = 0;
		for (T value : object) {
			ListNodePos<T, U> node = grouper.apply(value, i);
			U pos = node.pos;
			posesUnknot.add(pos);
			groups.computeIfAbsent(pos, k -> new ArrayList<>()).add(node.value);
			i++;
		}
		Map<U, Iterator<T>> iterators = new HashMap<>();
		for (Map.Entry<U, ArrayList<T>> entryI : groups.entrySet()){
			ArrayList<T> valueI=entryI.getValue();
			shuffle(valueI, random);
			iterators.put(entryI.getKey(), valueI.iterator());
		}

		// 2. Возвращаем
		ArrayList<T> ret=new ArrayList<>();
		for (U posI : posesUnknot){
			ret.add(iterators.get(posI).next());
		}
		object=ret;
	}

	public void forEach(BiConsumer<T, Integer> action) {
		int i = 0;
		for (T item : object) {
			action.accept(item, i++);
		}
	}
	public void replaceAll(BiFunction<T, Integer, T> operator) {
		ListIterator<T> it = object.listIterator();
		int i = 0;
		while (it.hasNext()) {
			it.set(operator.apply(it.next(), i++));
		}
	}

	public record ListNodePos<T, U>(T value, U pos){}
}
