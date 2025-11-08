package wta.sh.jsonRandom.randomizers;

import java.util.*;

public abstract class Randomizer<T> {
	protected T inner;
	protected Random random;

	public Randomizer(T inner, Random random) {
		this.inner = inner;
		this.random = random;
	}

	public abstract void randomize();

	//getters & setters
	public T getInner() {
		return inner;
	}

	public void setInner(T inner) {
		this.inner = inner;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	public record NodePos<R>(R obj, int pos) {
	}

	protected static <T> void shuffle(List<T> list, Random random) {
		for (int i = list.size() - 1; i > 0; i--) {
			int j = random.nextInt(i + 1);
			T temp = list.get(i);
			list.set(i, list.get(j));
			list.set(j, temp);
		}
	}

	//TODO: fix
	/*/public static Object wrapRandomizable(Object obj, Random random) {
		if (obj instanceof ArrayList) {
			return wrapArrayList((ArrayList<?>) obj, random);
		} else if (obj instanceof LinkedHashMap) {
			return wrapLinkedHashMap((LinkedHashMap<?, ?>) obj, random);
		} else {
			return obj;
		}
	}/*/

	//TODO: fix
	/*/private static RandList<Object> wrapArrayList(ArrayList<?> list, Random random) {
		ArrayList<Object> wrapped = new ArrayList<>(list.size());
		for (Object item : list) {
			wrapped.add(wrapRandomizable(item, random));
		}
		return new RandList<>(wrapped, random);
	}/*/

	//TODO: fix
	/*/private static RandMap<Object, Object> wrapLinkedHashMap(LinkedHashMap<?, ?> map, Random random) {
		LinkedHashMap<Object, Object> wrapped = new LinkedHashMap<>(map.size());
		for (Map.Entry<?, ?> e : map.entrySet()) {
			wrapped.put(
				  wrapRandomizable(e.getKey(), random),
				  wrapRandomizable(e.getValue(), random)
			);
		}
		return new RandMap<>(wrapped, random);
	}/*/
}
