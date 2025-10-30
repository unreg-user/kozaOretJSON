package wta.sh.jsonRandom.randomizers;

import java.util.List;

public abstract class Randomizer<T> {
	protected T object;
	protected Random random;

	public Randomizer(T object, Random random){
		this.object=object;
		this.random=random;
	}

	public abstract void randomize();

	//getters & setters
	public T getObject() {return object;}
	public void setObject(T object) {this.object = object;}
	public Random getRandom() {return random;}
	public void setRandom(Random random) {this.random = random;}

	public record NodePos<R>(R obj, int pos){}

	protected static <T> void shuffle(List<T> list, Random random) {
		for (int i = list.size()-1; i>0; i--) {
			int j = random.nextInt(i+1);
			T temp = list.get(i);
			list.set(i, list.get(j));
			list.set(j, temp);
		}
	}
}
