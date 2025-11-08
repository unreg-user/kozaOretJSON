package wta.sh.jsonRandom.randomizers.list;

import wta.sh.jsonRandom.randomizers.Random;
import wta.sh.jsonRandom.randomizers.Randomizer;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public abstract class RandList<T, M extends List<T>>  extends Randomizer<M> {
	public RandList(M inner, Random random) {
		super(inner, random);
	}

	@Override
	public void randomize() {
		shuffle(inner, random);
	}

	public <U> void randomize(BiFunction<T, Integer, ListNodePos<T, U>> grouper) {
		// 1. Сгруппировать ключи по pos
		Map<U, M> groups = new HashMap<>();
		ArrayList<U> posesUnknot = new ArrayList<>();
		int i = 0;
		for (T value : inner) {
			ListNodePos<T, U> node = grouper.apply(value, i);
			U pos = node.pos;
			posesUnknot.add(pos);
			groups.computeIfAbsent(pos, k -> createList()).add(node.value);
			i++;
		}
		Map<U, Iterator<T>> iterators = new HashMap<>();
		for (Map.Entry<U, M> entryI : groups.entrySet()){
			M valueI=entryI.getValue();
			shuffle(valueI, random);
			iterators.put(entryI.getKey(), valueI.iterator());
		}

		// 2. Возвращаем
		M ret=createList();
		for (U posI : posesUnknot){
			ret.add(iterators.get(posI).next());
		}
		inner=ret;
	}

	public void forEach(BiConsumer<T, Integer> action) {
		int i = 0;
		for (T item : inner) {
			action.accept(item, i++);
		}
	}
	public void replaceAll(BiFunction<T, Integer, T> operator) {
		ListIterator<T> it = inner.listIterator();
		int i = 0;
		while (it.hasNext()) {
			it.set(operator.apply(it.next(), i++));
		}
	}

	protected abstract M createList();

	public record ListNodePos<T, U>(T value, U pos){}
}
