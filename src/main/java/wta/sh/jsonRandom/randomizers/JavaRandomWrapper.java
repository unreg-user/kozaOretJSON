package wta.sh.jsonRandom.randomizers;

import java.util.Random;

public class JavaRandomWrapper implements wta.sh.jsonRandom.randomizers.Random {
	private final Random random;

	public JavaRandomWrapper(Random random){
		this.random=random;
	}
	public JavaRandomWrapper(){
		random=new Random();
	}

	@Override
	public int nextInt(int bound) {
		return random.nextInt(bound);
	}
}
