package ua.tifoha;

import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Vitaliy Sereda on 17.05.17.
 */
public class ThreadTesting {
	public static void main(String[] args) throws InterruptedException {
//		final Thread thread = new Thread(() -> {
//			while (!Thread.currentThread().isInterrupted()) {
//				try {
//					System.out.println(Thread.currentThread() + "Sleep");
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					System.err.println(Thread.currentThread() + "Interruption");
//				}
//			}
//		});
//		thread.setName("Infinity Thread");
//		thread.start();
//
//		System.out.println(Thread.currentThread() + "Sleep");
//		TimeUnit.MILLISECONDS.sleep(400);
//
//		System.out.println(Thread.currentThread() + "End.");
		Set<N> set = new TreeSet<>();
		set.add(new N(2));
		set.add(new N(2));
		System.out.println(set);
	}

	public static class N implements Comparable<N>{
		private final int value;

		public N(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		@Override
		public int compareTo(N n) {
			return Integer.compare(value, n.value);
		}

		@Override
		public String toString() {
			return Objects.toString(value);
		}
	}
}
