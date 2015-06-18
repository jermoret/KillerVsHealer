package actors;

import java.util.Random;
import java.util.stream.IntStream;

public class Victim {
	public static enum Area {LEG, ARM, BODY, HEAD};
	
	private int _dmg[] = new int[4];
	private int lifeLeft;
	
	public Victim() {
		lifeLeft = new Random().nextInt(7) + 12;
	}
	
	public boolean isDead() {
		return lifeLeft <= 0;
	}
	
	public boolean isHealed() {
		return (IntStream.of(_dmg).sum()) <= 4;
	}
	
	public void incTime() {
		lifeLeft -= Math.max(1,(IntStream.of(_dmg).filter(i -> i > 6).count()));
	}
	
	public void damage(int dmg, int a) {
		if (dmg > 0) _dmg[a] += dmg;
	}
	
	public void damage(int dmg, Area a) {
		damage(dmg, a.ordinal());
	}
	
	public void heal(int dmg, int a) {
		if (_dmg[a] > 0) _dmg[a] -= dmg;
	}
	
	public void heal(int dmg, Area a) {
		heal(dmg, a.ordinal());
	}
	
	public void addTime(int time) {
		lifeLeft += time;
	}
	
	public int getDamage(int a) {
		return _dmg[a];
	}
	
	public int getDamage(Area a) {
		return _dmg[a.ordinal()];
	}
}
